package rainbow.MediaRenderer;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.SyncFailedException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import rainbow.MediaRenderer.IAndroidMusicPlaybackService.Stub;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;





public class AndroidMusicPlaybackService extends Service{
    private final String TAG = "onlinemusic";

    public static final String SERVICECMD = "com.android.music.musicservicecommand";  
    public static final String CMDNAME = "command";  
    public static final String CMDTOGGLEPAUSE = "togglepause";  
    public static final String CMDSTOP = "stop";  
    public static final String CMDPAUSE = "pause";  
    public static final String CMDPREVIOUS = "previous";  
    public static final String CMDNEXT = "next";  
    public static final String TOGGLEPAUSE_ACTION = "com.android.music.musicservicecommand.togglepause";  
    public static final String PAUSE_ACTION = "com.android.music.musicservicecommand.pause";  
    public static final String PREVIOUS_ACTION = "com.android.music.musicservicecommand.previous";  
    public static final String NEXT_ACTION = "com.android.music.musicservicecommand.next";  
    public static final String ACTION_FROM = "from"; 
    public static final String ACTION_FROM_DATA= "Rainbow";

	MultiPlayer mPlayer = null;
	String mPlayingPath = "";
	public String mAlbumName = "";
	public String mArtistName = "";
	public String mCoverPath = "";
	public String mTrackName = "";
	Context mContext  = null;
	AndroidMusicInfoPanelControl mPlayingInfoPanel;
    private AudioManager mAudioManager;
	AndroidDownloader mDownloader;
	FileDescriptor mBufferaFd;
	
    private static final int FOCUSCHANGE = 4;
	boolean isFillingCache = false;
	int mCurrentReconnectTimes = 0;
	ArrayList<Uri> mQueen;
	int mCuurentPlayingId = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
        mPlayer = new MultiPlayer();
        mQueen = new ArrayList<Uri>();
        mQueen.add(Uri.parse("http://file16.top100.cn/201206290935/BE85C8C88C96DBD044717038C18C6B65/Special_322611/Love%20the%20Way%20You%20Lie.mp3"));
        mQueen.add(Uri.parse("http://file12.top100.cn/201206290935/C9EB5B81D52AF55F9A948BB8E2EDDA67/Special_246280/%E6%B2%A1%E9%82%A3%E4%B9%88%E7%AE%80%E5%8D%95.mp3"));
        mQueen.add(Uri.parse("http://file17.top100.cn/201206290934/C5E935EB78FEA48A9E866A9D76041152/Special_350121/%E5%9B%A0%E4%B8%BA%E7%88%B1%E6%83%85.mp3"));
        mQueen.add(Uri.parse("http://file19.top100.cn/201206290934/7F313DC483448DE615E318DA31D17524/Special_388365/%E9%82%A3%E4%BA%9B%E5%B9%B4.mp3"));
        mQueen.add(Uri.parse("http://file16.top100.cn/201206290937/5A805471E7F06167002E0EE7533BB91B/Special_329225/%E4%BD%A0%E4%B8%8D%E7%9F%A5%E9%81%93%E7%9A%84%E4%BA%8B.mp3"));
       
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mPlayingInfoPanel = new AndroidMusicInfoPanelControl(this);
        
        //add android default action for control music 
        IntentFilter commandFilter = new IntentFilter();  
       // commandFilter.addAction(SERVICECMD);  
        commandFilter.addAction(TOGGLEPAUSE_ACTION);  
        commandFilter.addAction(PAUSE_ACTION);  
        //commandFilter.addAction(NEXT_ACTION);  
       // commandFilter.addAction(PREVIOUS_ACTION);  
        registerReceiver(mIntentReceiver, commandFilter);  
        
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mIntentReceiver);
		super.onDestroy();
	}

	public void open(String path) {
		if(mPlayer != null){
			mPlayer.release();
		}
		mPlayer = new MultiPlayer();
        mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        
		mPlayingPath = path;
		mPlayer.SetUrl(path);
	}
	
	public void setInfo(String title,String artist ,String CoverPath){
		mTrackName = title;
		mArtistName = artist;
		mCoverPath = CoverPath ;
		
		Log.d(TAG,"name: "+title);
		Log.d(TAG,"Artist: "+artist);
		Log.d(TAG,"CoverPath: "+CoverPath);

	}

	public long seek(long pos) {
		return mPlayer.seek(pos*1000);
	}


	public long duration() {
		return mPlayer.duration()/1000;
	}


	public long position() {
		return mPlayer.position()/1000;
	}


	public String getPath() {
		return mPlayingPath;
	}


	public long getArtistId() {
		return 0;
	}


	public String getArtistName() {
		return mArtistName;
	}


	public long getAlbumId() {
		return 0;
	}


	public String getAlbumName() {
		return mAlbumName;
	}


	public String getTrackName() {
		return mTrackName;
	}


	public void next(boolean b) {
		// TODO Auto-generated method stub
		
	}


	public void prev() {
		// TODO Auto-generated method stub
		
	}


	public void play() {
		// TODO Auto-generated method stub
		mPlayer.start();
	}


	public void pause() {
		// TODO Auto-generated method stub
		mPlayer.pause();
	}


	public void stop() {
		// TODO Auto-generated method stub
			mPlayer.stop();
		//	mPlayer.release();
		 mAudioManager.abandonAudioFocus(mAudioFocusListener);
		    if(mDownloader != null){
        		mDownloader.Destroy();
        		mDownloader = null;
        	}
	}


	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return mPlayer.isPlaying();
	}

	public Uri getNextPlay(){
		
		if(mQueen.isEmpty())
			return Uri.parse("");
		if(mCuurentPlayingId >= (mQueen.size() -1))
			mCuurentPlayingId = 0;
		else 
			mCuurentPlayingId ++;
		Log.i(TAG, "now Playing : "+mQueen.get(mCuurentPlayingId).toString() );
		return mQueen.get(mCuurentPlayingId);
	}
	private OnAudioFocusChangeListener mAudioFocusListener = new OnAudioFocusChangeListener() {
        @Override
		public void onAudioFocusChange(int focusChange) {
            mMediaplayerHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };
    
    private Handler mMediaplayerHandler = new Handler() {
        float mCurrentVolume = 1.0f;
        @Override
        public void handleMessage(Message msg) {
        switch (msg.what) {
        case FOCUSCHANGE:
            // This code is here so we can better synchronize it with the code that
            // handles fade-in
            switch (msg.arg1) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.v(TAG, "AudioFocus: received AUDIOFOCUS_LOSS");
          
                    pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.v(TAG, "AudioFocus: received AUDIOFOCUS_LOSS_TRANSIENT");
                 
                    pause();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                  
                        play(); // also queues a fade-in
                   
                    break;
                default:
                    Log.e(TAG, "Unknown audio focus change code");
            }
            break;
        }
        }
    };
    
	 private class MultiPlayer {
	        private MediaPlayer mMediaPlayer = new MediaPlayer();
	        private Handler mHandler;
	        private boolean mIsInitialized = false;
	        private boolean isValid = false;
	        String tmpFile = "";
	        
	        public MultiPlayer() {
	           // mMediaPlayer.setWakeMode(AndroidMusicPlaybackService.this, PowerManager.PARTIAL_WAKE_LOCK);
	        }

	        public void SetUrl(String path){
            	
	        	if(mDownloader != null){
            		mDownloader.Destroy();
            		mDownloader = null;
            	}
/*	        	
	        	mDownloader = new AndroidDownloader(path,10){
					@Override
					public void Downloadfinished() {
						isFillingCache = false;
						setDataSource("//"+tmpFile);	
					}	
            	};
        		if(!mDownloader.initConnection())
        			return;
        		if(!mDownloader.createTmpFile())
        			return ;
        		
            	if(!mDownloader.startDownload())
            		return;
            	isFillingCache = true;
            	tmpFile = mDownloader.getCacheFile();*/
	        	pauseAndroidMusic();
	        	setDataSource(path);
			

	        }
	        
	        public void setDataSource(String path) {
	            try {
	            	Log.i(TAG,"Music Playback path : "+path);
	            	Log.i(TAG,"Music Playback reconnect times : "+mCurrentReconnectTimes);

	            	isValid = true;
	            	if(mMediaPlayer.isPlaying())
	            		mMediaPlayer.stop();
	                mMediaPlayer.reset();
	                mMediaPlayer.release();
	                
	                mMediaPlayer = new MediaPlayer();
	        /*       FileInputStream fi1 = new FileInputStream(path); 
	                mBufferaFd = fi1.getFD();
	                mMediaPlayer.setDataSource(mBufferaFd);*/
	              
	                mMediaPlayer.setDataSource(path);
	                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		            mMediaPlayer.setOnCompletionListener(listener);
		            mMediaPlayer.setOnErrorListener(errorListener);
		            mMediaPlayer.setOnPreparedListener(prepareListener);
	                mMediaPlayer.prepare();
	            } catch (IOException ex) {
	            	ex.printStackTrace();
	                mIsInitialized = false;
	                return;
	            } catch (IllegalArgumentException ex) {
	              
	                mIsInitialized = false;
	                
	                return;
	            } catch (IllegalStateException e) {
	            	e.printStackTrace();
	            	
	            	  try {
							Thread.sleep(300);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
		                if(++mCurrentReconnectTimes > 3)
		                	return;
		                
		                setDataSource(path);
		            	return;
				}

/*	            Intent i = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
	            i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
	            i.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
	            sendBroadcast(i);
*/	            mIsInitialized = true;
				mCurrentReconnectTimes = 0;
	        }
	        
	        public boolean isInitialized() {
	            return mIsInitialized;
	        }

	        public void start() {
	        	if(isFillingCache){
	        		 Log.d(TAG,"Music playback Service: buffing");
	        		return;
	        	}
	            Log.d(TAG,"Music playback Service: start");
	            mMediaPlayer.start();
	        }

	        public void stop() {
	            mMediaPlayer.reset();
	            mIsInitialized = false;
	        }

	        /**
	         * You CANNOT use this player anymore after calling release()
	         */
	        public void release() {
	            stop();
	            mMediaPlayer.release();
	        }
	        
	        public void pause() {
	            mMediaPlayer.pause();
	        }
	        
	 
	        
	        public void setHandler(Handler handler) {
	            mHandler = handler;
	        }
	        
	        public boolean isPlaying(){
	        	return mMediaPlayer.isPlaying();
	        }
	        
	        MediaPlayer.OnPreparedListener prepareListener = new MediaPlayer.OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Log.i(TAG,"Music Playback prepared ");
					mPlayingInfoPanel.setInfo(mTrackName, mArtistName);
					mp.start();
				}
			};
	        
	        MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
	            @Override
				public void onCompletion(MediaPlayer mp) {
	                // Acquire a temporary wakelock, since when we return from
	                // this callback the MediaPlayer will release its wakelock
	                // and allow the device to go to sleep.
	                // This temporary wakelock is released when the RELEASE_WAKELOCK
	                // message is processed, but just in case, put a timeout on it.
	   //             mWakeLock.acquire(30000);
	             //   mHandler.sendEmptyMessage(TRACK_ENDED);
	             //   mHandler.sendEmptyMessage(RELEASE_WAKELOCK);
	            	Log.i(TAG,"Music Playback Completed ");
	            	if(mDownloader != null){
	            		mDownloader.Destroy();
	            		mDownloader = null;
	            	}
	            	//setDataSource("");
	            }
	        };

	        MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
	            @Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
	            	//setDataSource("");

	                switch (what) {
	                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
	                    mIsInitialized = false;
	                    mMediaPlayer.release();
	                    
	                    // Creating a new MediaPlayer and settings its wakemode does not
	                    // require the media service, so it's OK to do this now, while the
	                    // service is still being restarted
	                 //   mMediaPlayer = new MediaPlayer(); 
	                 //   mMediaPlayer.setWakeMode(AndroidMusicPlaybackService.this, PowerManager.PARTIAL_WAKE_LOCK);
	              //      mHandler.sendMessageDelayed(mHandler.obtainMessage(SERVER_DIED), 2000);
	                    if(mDownloader != null){
		            		mDownloader.Destroy();
		            		mDownloader = null;
		            	}
	                    return true;
	                default:
	                    Log.d("MultiPlayer", "Error: " + what + "," + extra);
	                    break;
	                }
	                return false;
	           }
	        };

	        public long duration() {
	        	if(!mIsInitialized)
	        		return 0;
	            return mMediaPlayer.getDuration();
	        }

	        public long position() {
	        	if(!mIsInitialized)
	        		return 0;
	            return mMediaPlayer.getCurrentPosition();
	        }

	        public long seek(long whereto) {
	            mMediaPlayer.seekTo((int) whereto);
	            return whereto;
	        }

	        public void setVolume(float vol) {
	            mMediaPlayer.setVolume(vol, vol);
	        }

	        public void setAudioSessionId(int sessionId) {
	            mMediaPlayer.setAudioSessionId(sessionId);
	        }

	        public int getAudioSessionId() {
	            return mMediaPlayer.getAudioSessionId();
	        }
	    }
	 
	 private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {  
	     @Override  
	     public void onReceive(Context context, Intent intent) {  
	         String action = intent.getAction();  
	         String cmd = intent.getStringExtra("command"); 
	         String msgFrom = intent.getStringExtra(ACTION_FROM);  
	         Log.d(TAG, "Receive Action: "+action+" Command: "+cmd);
	         if(msgFrom.equals(ACTION_FROM_DATA))
	        	 return;
	         
	         if (CMDNEXT.equals(cmd) || NEXT_ACTION.equals(action)) {  
	           //  next(true);  
	         } else if (CMDPREVIOUS.equals(cmd) || PREVIOUS_ACTION.equals(action)) {  
	            // prev();  
	         } else if (CMDTOGGLEPAUSE.equals(cmd) || TOGGLEPAUSE_ACTION.equals(action)) {  
	             if (isPlaying()) {  
	                 pause();  
	             } else {  
	                 play();  
	             }  
	         } else if (CMDPAUSE.equals(cmd) || PAUSE_ACTION.equals(action)) {  
	        	 if (isPlaying()) 
	        		 stop();
	         } else if (CMDSTOP.equals(cmd)) {  
	             pause();  
	             seek(0);  
	         }
	     }  
	 };  
	 
	 private void pauseAndroidMusic() {  
		    Intent freshIntent = new Intent();  
		    freshIntent.setAction("com.android.music.musicservicecommand.pause");  
		    freshIntent.putExtra("command", "pause");
		    freshIntent.putExtra(ACTION_FROM, ACTION_FROM_DATA);  
		    sendBroadcast(freshIntent);  
		} 
	 
	 
	 static class ServiceStub extends Stub {
	        WeakReference<AndroidMusicPlaybackService> mService;
	        
	        ServiceStub(AndroidMusicPlaybackService service) {
	            mService = new WeakReference<AndroidMusicPlaybackService>(service);
	        }

	        @Override
			public void openFile(String path)
	        {
	            mService.get().open(path);
	        }


	        @Override
			public boolean isPlaying() {
	            return mService.get().isPlaying();
	        }
	        @Override
			public void stop() {
	            mService.get().stop();
	        }
	        @Override
			public void pause() {
	            mService.get().pause();
	        }
	        @Override
			public void play() {
	            mService.get().play();
	        }
	        @Override
			public void prev() {
	            mService.get().prev();
	        }
	        @Override
			public void next() {
	            mService.get().next(true);
	        }
	        @Override
			public String getTrackName() {
	            return mService.get().getTrackName();
	        }
	        @Override
			public String getAlbumName() {
	            return mService.get().getAlbumName();
	        }
	        
	        @Override
			public long getAlbumId() {
	            return mService.get().getAlbumId();
	        }
	        @Override
			public String getArtistName() {
	            return mService.get().getArtistName();
	        }
	        @Override
			public long getArtistId() {
	            return mService.get().getArtistId();
	        }

	        
	        public void setArtistName(String name) {
	        	 mService.get().mArtistName = name;
	        }
	        public void setAlbumName(String name) {
	        	 mService.get().mAlbumName = name;
	        }
	        public void setTrackName(String name) {
	        	 mService.get().mTrackName = name;
	        }
	        
	        @Override
			public String getPath() {
	            return mService.get().getPath();
	        }

	        @Override
			public long position() {
	            return mService.get().position();
	        }
	        @Override
			public long duration() {
	            return mService.get().duration();
	        }
	        @Override
			public long seek(long pos) {
	            return mService.get().seek(pos);
	        }

			@Override
			public void setInfo(String title, String artist, String coverPath){
				 mService.get().setInfo(title, artist, coverPath);
			}

	    }
	 

	    private final IBinder mBinder = new ServiceStub(this);

	    public class DownloadFinishedImp implements AndroidDownloader.DownloadFinished{

			@Override
			public void Downloadfinished() {
				// TODO Auto-generated method stub
				
			}
	    	
	    }

}
