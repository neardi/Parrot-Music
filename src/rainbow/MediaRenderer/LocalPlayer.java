package rainbow.MediaRenderer;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.teleal.cling.model.ModelUtil;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.teleal.cling.support.avtransport.lastchange.AVTransportVariable.AVTransportURIMetaData;
import org.teleal.cling.support.lastchange.LastChange;
import org.teleal.cling.support.model.MediaInfo;
import org.teleal.cling.support.model.PositionInfo;
import org.teleal.cling.support.model.StorageMedium;
import org.teleal.cling.support.model.TransportAction;
import org.teleal.cling.support.model.TransportInfo;
import org.teleal.cling.support.model.TransportState;
import org.teleal.common.xhtml.Meta;
import org.xmlpull.v1.XmlPullParserException;

import rainbow.Common.Common;
import rainbow.Common.XmlParser;
import rainbow.Common.XmlParser.MediaMetaData;
import rainbow.service.MainService;
import rainbow.service.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;

public class LocalPlayer {
	

	private UnsignedIntegerFourBytes  	mInstanceId;
	private Context 					mContext;
	
	private AudioManager 				mAudiomanage;
	private AndroidVideoPlayerControl 	mAndroidPlayer;
	private AndroidImageViewRemote 		mAndroidImageViewManager = null;
	private AndroidMusicRemote 			mMusicPlayer = null;
	private AndroidBrowerRemote			mAndroidBrowerRemote = null;
	private volatile TransportInfo 		mCurrentTransportInfo = new TransportInfo();
	private TransportState 				mTransportState;
	
	private final int 					VEDIO_PLAYBACK = 0;
	private final int 					IMAGE_VIEW = 1;
	private final int 					MUSIC_PLAYBACK = 2;
	private final int 					NOT_RUNNING = 4;
	private int 						mCurrentRunningState = NOT_RUNNING;
	
	private PositionInfo 				mCurrentPositionInfo = new PositionInfo();
	private MediaMetaData 				mMetaData = new MediaMetaData();
	private String 						mURIMetaData;
	private String 						mUrl = "";
	private boolean 					mMusicIsPlaying = false;
	
	private final String				UPNP_CLASS_VIDEO = "object.item.videoItem.video";
	private final String				UPNP_CLASS_HTML = "object.item.textItem.html";

	
	public LocalPlayer(Context context){
		mContext = context;
			
	}
	
	public LocalPlayer(UnsignedIntegerFourBytes unsignedIntegerFourBytes,
			LastChange avTransportLastChange,
			LastChange renderingControlLastChange,
			Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mInstanceId = unsignedIntegerFourBytes;
		mTransportState = TransportState.STOPPED ;
	}

	public void play(){
		mTransportState = TransportState.PLAYING ;
		Log.i(Common.TAG, "LocalPlayer play : "+mTransportState);

		if(mCurrentRunningState == MUSIC_PLAYBACK){
			if(mMusicPlayer != null)
				mMusicPlayer.play();
			return;
		}
		
		mAndroidPlayer.Play();
	}
	
	public void pause(){
		Log.i(Common.TAG, "LocalPlayer pause : ");
		mTransportState = TransportState.PAUSED_PLAYBACK ;
		
		if(mCurrentRunningState == MUSIC_PLAYBACK){
			if(mMusicPlayer != null)
				mMusicPlayer.pause();
			return;
		}
		
		mAndroidPlayer.Pause();
		
	}
	
	public void stop(){
		Log.i(Common.TAG, String.format("LocalPlayer stop ,state: %d ",mCurrentRunningState));

		mTransportState = TransportState.STOPPED ;
	

		if(mCurrentRunningState == MUSIC_PLAYBACK){
			if(mMusicPlayer != null)
				mMusicPlayer.stop();
			mMusicIsPlaying = false;
			return;
		}else if(mCurrentRunningState == VEDIO_PLAYBACK){

			if(mAndroidPlayer ==null)
				return;
			mAndroidPlayer.Stop();
			mAndroidPlayer.DisconnectToService();
			
		}else if(mCurrentRunningState == IMAGE_VIEW){
			if(mAndroidImageViewManager ==null)
				return;
			mAndroidImageViewManager.close();
		}
		mCurrentRunningState = NOT_RUNNING;	
		if(mMusicIsPlaying)
			mCurrentRunningState = MUSIC_PLAYBACK;
	}
	
	public void next(){
		Log.i(Common.TAG, "LocalPlayer next : ");
	
	}

	public void setURI(URI uri,String currentURIMetaData) {
		mUrl = uri.toString();
		Log.d(Common.TAG, "setURI : "+ uri);
		Log.d(Common.TAG, "setURI currentURIMetaData: "+currentURIMetaData);
		if(uri == null)
			return;
		if(currentURIMetaData.equals("video")){
			mMetaData.Class = "object.item.videoItem.video"; 
		}else if(currentURIMetaData.equals("photo")){
			mMetaData.Class = "object.item.imageItem.photo";
		}else if(currentURIMetaData.equals("music")){
			mMetaData.Class = "object.item.audioItem.musicTrack";
		}else{
			mMetaData = XmlParser.getMetaData(currentURIMetaData);
		}
		
		Log.i(Common.TAG, "upnp class: "+mMetaData.Class);
		if(mMetaData.Class.indexOf("imageItem")>0){
			if(mAndroidImageViewManager == null){
				mAndroidImageViewManager= AndroidImageViewRemote.getInstance(mContext);
				mAndroidImageViewManager.connectToImageViewControl();
				/*mHandler.postDelayed(mImageViewSetUrl, 200);
				return;*/
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			mAndroidImageViewManager.setUrl(uri.toString());
			mCurrentRunningState = IMAGE_VIEW;
		}else if(mMetaData.Class.indexOf("audioItem") >0){
			
			if(mCurrentRunningState == VEDIO_PLAYBACK){
				mAndroidPlayer.Stop();
			}
			Log.i(Common.TAG, String.format("start music: title : %s, artist : %s ,url : %s",mMetaData.Title,  mMetaData.Artist, mMetaData.CoverUrl));
			if(mMusicPlayer == null){
				mMusicPlayer= AndroidMusicRemote.getInstance(mContext);

			}
			mMusicPlayer.ConnectToService(mContext);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mMusicPlayer.setInfo(mMetaData.Title, mMetaData.Artist, mMetaData.CoverUrl);
			mMusicPlayer.setUrl(uri.toString());
			mCurrentRunningState = MUSIC_PLAYBACK;
			mMusicIsPlaying = true;
		}else if(mMetaData.Class.indexOf(UPNP_CLASS_HTML) >-1){
			if(mAndroidBrowerRemote == null){
				mAndroidBrowerRemote= AndroidBrowerRemote.getInstance(mContext);
				mAndroidBrowerRemote.ConnectToService();
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Log.d(Common.TAG, " metaData: "+mMetaData.CoverUrl);
			
			mAndroidBrowerRemote.SetUrl(uri.toString());
			
		}else{
			
			if(mCurrentRunningState == MUSIC_PLAYBACK && mMusicIsPlaying){
				mMusicPlayer.stop();
			}
			
			mAndroidPlayer =  AndroidVideoPlayerControl.getInstance(mContext);
			mAndroidPlayer.StartPlayer(uri);
			mAndroidPlayer.ConnectToService();
			mAndroidPlayer.SetPlayUrl(uri.toString());
			mCurrentPositionInfo = new PositionInfo(1,currentURIMetaData, uri.toString());
			mURIMetaData = currentURIMetaData;
			mCurrentRunningState = VEDIO_PLAYBACK;
		}
		
		mTransportState = TransportState.PLAYING;
	}

	
	public MediaInfo getCurrentMediaInfo() {
	
		Log.i(Common.TAG, "LocalPlayer getCurrentMediaInfo : ");
	
		return new MediaInfo(mUrl,mURIMetaData,new UnsignedIntegerFourBytes(1),getCurrentPositionInfo().getTrackDuration(), StorageMedium.VIDEO8);
		
		
	}

	public TransportInfo getCurrentTransportInfo() {
		// TODO Auto-generated method stub
		Log.i(Common.TAG, "LocalPlayer getCurrentTransportInfo : "+mTransportState);
		return new TransportInfo(mTransportState);
	}

	public PositionInfo getCurrentPositionInfo() {
		// TODO Auto-generated method stub
		int Duration = 0;
		int CurrentDuration = 0;
		String url = "";
		if(mCurrentRunningState == MUSIC_PLAYBACK){

			Duration = mMusicPlayer.getDuration();
			url = mMusicPlayer.getCurrentPlayingUrl();
			CurrentDuration = mMusicPlayer.getPosition();
		}else if (mCurrentRunningState == VEDIO_PLAYBACK){
			Duration = mAndroidPlayer.getDuration();
			url = mAndroidPlayer.getCurrentPlayingUrl();
			CurrentDuration = mAndroidPlayer.getCurrentDuration();
		}
		
		Log.i(Common.TAG, "LocalPlayer getCurrentPositionInfo: " +Duration +" current: "+CurrentDuration);
		
        synchronized (LocalPlayer.this) {
                 mCurrentPositionInfo =
                         new PositionInfo(
                                 1,
                                 ModelUtil.toTimeString(Duration),
                                 mURIMetaData,
                                 url,
                                 ModelUtil.toTimeString(CurrentDuration),
                                 "NOT_IMPLEMENTED",
                                 -1,
                                 -1
                                 
                         );
          }
       
		//Log.i(Common.TAG, "LocalPlayer getCurrentPositionInfo : " +mCurrentPositionInfo.toString());
		return mCurrentPositionInfo;
	}

	public Object[] getCurrentTransportActions() {
		// TODO Auto-generated method stub
		Log.i(Common.TAG, "LocalPlayer getCurrentTransportActions : ");
		TransportAction[] actions;
		
		switch (mTransportState) {
		case STOPPED:
			actions = new TransportAction[]{
					TransportAction.Play
			};
			break;
		case PLAYING:
			actions = new TransportAction[]{
					TransportAction.Stop,
					TransportAction.Pause,
					TransportAction.Seek
			};
			break;
		case PAUSED_PLAYBACK:
			actions = new TransportAction[]{
					TransportAction.Stop,
					TransportAction.Pause,
					TransportAction.Seek,
					TransportAction.Play
			};
			break;
		default:
			actions = null;
		}
		return actions;
	}

	public void seek(long fromTimeString) {
		// TODO Auto-generated method stub
		Log.i(Common.TAG, "LocalPlayer seek : " + fromTimeString);
		if(mCurrentRunningState == MUSIC_PLAYBACK){
			if(mMusicPlayer != null)
				mMusicPlayer.seek((int)fromTimeString);
		}else
			mAndroidPlayer.Seek((int)fromTimeString);
	}

	public UnsignedIntegerFourBytes getInstanceId() {
		// TODO Auto-generated method stub

		
		return mInstanceId;
	}
	
	
/*	Runnable mImageViewSetUrl = new Runnable() {	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mAndroidImageViewManager.setUrl(mUrl.toString());
			mCurrentRunningState = IMAGE_VIEW;
		}
	};*/
	Handler mHandler = new Handler();
}
