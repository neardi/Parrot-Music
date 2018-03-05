package rainbow.MediaRenderer;
import rainbow.Common.Common;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;


public class AndroidMusicRemote {

	final int START_PLAY = 0;
    private final String TAG = Common.TAG;

	private IAndroidMusicPlaybackService mService = null;
	private static AndroidMusicRemote mInstance= null;
	String path= "";
	
	public class MediaPlayerServiceConnection implements ServiceConnection {
	    //  Tag for debugging.
	    private final String TAG = Common.TAG;

	    //  The service.

	    
	    public void onServiceConnected(ComponentName name, IBinder service) {
	        Log.i(TAG, "MediaPlaybackService is connected!  Name: " + name.getClassName());
	        mService = IAndroidMusicPlaybackService.Stub.asInterface(service);
       
	    }

	    public void onServiceDisconnected(ComponentName name) {
	        Log.i(TAG, "MediaPlaybackService disconnected!");
	        mService = null;
	    }
	}
	
	Context  mContext = null;
	
	AndroidMusicRemote (Context context){
		mContext = context;

	}
	public static AndroidMusicRemote getInstance(Context context) {
		
		if(mInstance == null)
			mInstance = new AndroidMusicRemote(context);
		// TODO Auto-generated method stub
		return mInstance;
	}
	
	public void ConnectToService(Context context){
		Intent intent = new Intent(mContext,rainbow.MediaRenderer.AndroidMusicPlaybackService.class);
		MediaPlayerServiceConnection conn = new MediaPlayerServiceConnection();
		context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
		

/*		Intent serviceIntent=new Intent(mContext,rainbow.MediaRenderer.AndroidImageViewService.class);
		mContext.bindService(serviceIntent, MediaPlayerServiceConnection,
								Context.BIND_AUTO_CREATE);*/
		
		
	}
	

	public int getDuration(){
		if(mService != null)
			try {
				return (int) mService.duration();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return 0;
	}
	
	public int getPosition(){
		if(mService != null)
			try {

				return (int) mService.position();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return 0;
	
	}
	
	public void pause(){
		if(mService != null)
			try {
				mService.pause();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void play(){
		Log.i(TAG, "play music");
		if(mService != null)
			try {
				mService.play();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void stop(){ 
		Log.i(Common.TAG, "stop ");
		if(mService == null)
			return;
		
		try {
			 mService.stop();
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void seek(int position){ 
		if(mService == null)
			return;
		
		try {
			 mService.seek(position);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setInfo(String title,String artist ,String coverUrl){
		if(mService == null)
			return;
		
		try {
			 mService.setInfo(title, artist, coverUrl);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setUrl(String url){
		Log.i(Common.TAG, "try to set url  "+url);
		if(mService == null){
			path = url;
			mHandler.sendEmptyMessageDelayed(START_PLAY,500);
			return;
		}
		
		Log.i(Common.TAG, "set url : "+url);
		path = url;
		try {
			 mService.openFile(url);
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String getCurrentPlayingUrl() {
		// TODO Auto-generated method stub
		return path;
	}
	
	Handler mHandler  =new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case START_PLAY:
				setUrl(path);
			
			}
			
		}
	};
	
}
