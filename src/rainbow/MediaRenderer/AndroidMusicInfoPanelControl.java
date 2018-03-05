package rainbow.MediaRenderer;

import rainbow.service.R;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

public class AndroidMusicInfoPanelControl {
	Context mContext;
	ViewGroup mPanel;

	WindowManager wm;
	
	AndroidMusicPlayerInfoPanel mInfoPannel;
	final int SHOW_TIME = 10000;
	boolean isShown = false;
	
	Runnable mHidePanel = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//mPanel.setVisibility(View.GONE );
			wm.removeView(mInfoPannel);
			isShown = false;
		}
	};
	
	Handler mHandler = new Handler();
	
	AndroidMusicInfoPanelControl(Context context){
		mContext =  context;
		mInfoPannel  = new AndroidMusicPlayerInfoPanel(context);		
		init();
	
	}

	void init(){
		wm = (WindowManager)mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);  
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
                WindowManager.LayoutParams.ANIMATION_CHANGED|WindowManager.LayoutParams.ALPHA_CHANGED,
                PixelFormat.TRANSLUCENT);

	}
	
	public void setInfo(String title ,String artist){

		Log.i("musicplayinfo", "title: "+title+artist);
		if(title.isEmpty())
			title = mContext.getString(R.string.unkown_title);
		if(artist.isEmpty())
			artist = mContext.getString(R.string.unkown_artist);;
		
		mInfoPannel.setInfo(title, artist, null);
		
		showInfoPanel();
	}
	
	void showInfoPanel(){
		if(isShown){
		//	if(mHandler.)
			mHandler.removeCallbacks(mHidePanel);
			wm.removeView(mInfoPannel);
		}
	       WindowManager.LayoutParams params = new WindowManager.LayoutParams(
	                LayoutParams.MATCH_PARENT,
	                LayoutParams.WRAP_CONTENT,
	                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
	                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
	                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
	                PixelFormat.TRANSLUCENT);

		wm.addView(mInfoPannel, params);
		isShown = true;
		mHandler.postDelayed(mHidePanel, SHOW_TIME);
	}
	
}