package rainbow.MediaRenderer;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;

import rainbow.Common.Common;
import rainbow.service.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class AndroidImageView extends Activity{
	private ImageView mImageView = null;
	private ProgressBar mProgressbar = null;
	private String mImageUri = "";
	private DownloadThread mDownloadThread = new DownloadThread();
	Bitmap mImageMap = null;
	public static EventHandler mEventHandler = null ;
	Timer mCoolingTimer ;
	public final static int SHOW_IMAGE = 0;
	public final static int FINISH_VIEW = 1;
	public final static int SET_URL = 2;
	public final static int THREAD_BUSY = 3;
	


	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshow);
		 
		mImageView = (ImageView)findViewById(R.id.imageView);
		mProgressbar = (ProgressBar) findViewById(R.id.progressBar);

		mEventHandler = new EventHandler(Looper.getMainLooper());
		startService(new Intent(this,rainbow.MediaRenderer.AndroidImageViewService.class));
		
		setUrl(getIntent().getData().toString());
	}
	
	private void setUrl(String url){
		Bundle data = new Bundle();
		data.putString("url", url);
		Message msg = new Message();
		msg.setData(data);
		msg.what = AndroidImageView.SET_URL;
		AndroidImageView.mEventHandler.sendMessage(msg);
	}
	
	
	 private Bitmap getBitmapFromUrl(String imgUrl) {
         URL url;
         Bitmap bitmap = null;
         try {
                 url = new URL(imgUrl);
            	 InputStream mInputStream = null;	
            	 HttpURLConnection  mhttpConnection = null;
                 mhttpConnection = (HttpURLConnection )url.openConnection();
                 mhttpConnection.setConnectTimeout(5000);
                 mhttpConnection.setReadTimeout(15000);
                 mInputStream = mhttpConnection.getInputStream();
                // url.openConnection().
                 
                 BitmapFactory.Options options=new BitmapFactory.Options();
                 options.inJustDecodeBounds = true;
                 BufferedInputStream bis = new BufferedInputStream(mInputStream);
                 BitmapFactory.decodeStream(bis, null, options);
                 options.inSampleSize = computeInitialSampleSize(options,720, 1280*720);
                 if(options.outWidth == -1)
                	 return null;
                 
                 Log.i(Common.TAG, String.format("image size : %d*%d samplesize: %d" , options.outWidth,options.outHeight,options.inSampleSize));
                 options.inJustDecodeBounds = false;
                 
                // BufferedInputStream is = new BufferedInputStream(mInputStream);
                 
                 HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
                 InputStream is = connection.getInputStream();
                 bitmap = BitmapFactory.decodeStream(is, null, options);
                 int x = 0 ,y = 0 ;
                 //bitmap.getPixel(x, y);
                 Log.i(Common.TAG, String.format("image size : %d*%d getPixel: %d*%d" , bitmap.getWidth(), bitmap.getHeight(),x,y));

                 
                 bis.close();
                 is.close();
                 mhttpConnection.disconnect();
         } catch (MalformedURLException e) {
                 e.printStackTrace();
         } catch (IOException e) {
                 e.printStackTrace();
         }
         return bitmap;
	}
	
	 public void Close(){
		 Log.i(Common.TAG, "===> image view finished <===");
		 recycle();
		 finish();
		// this.finishActivity(0);
	 }
	 
	 private void recycle(){
	  	   if(mImageMap != null && !mImageMap.isRecycled() ){
	    		 mImageMap.recycle();
	    		 System.gc();
	    		 Log.i(Common.TAG, "mImageMap recycle ");
	  	   }
	  	   
	  	//   if(mImageView.get().)
	 }
	 
	private class DownloadThread extends Thread{
		
		private boolean closed = false;
		@Override
		public void run(){
			mImageMap = getBitmapFromUrl(mImageUri);
			if(!closed)
				mEventHandler.sendEmptyMessage(SHOW_IMAGE);
		}
		
		
		public void Close(){
			synchronized (this) {
				closed =true;
			}
			
		}
	 }
	 
	 public class EventHandler extends Handler {
		 
	        public EventHandler(Looper looper) {
	           super(looper);
	       }
	 
	        public EventHandler() {
	           super();
	       }
	 
	        @Override
	        public void handleMessage(Message msg) {
	           // TODO Auto-generated method stub
	           switch (msg.what) {
	           case SHOW_IMAGE:	       
	      		 mImageView.setImageBitmap(mImageMap);
	    		 mImageView.invalidate();
	    		 mProgressbar.setVisibility(View.GONE);
	        	   break;     
	               
	           case FINISH_VIEW:
	        	   Close();
	        	   break;
	        	   
	           case SET_URL:

	        	   if(mEventHandler.hasMessages(THREAD_BUSY)){
	        		   Log.w(Common.TAG,"thread is busy,lose image : "+mImageUri);
	        		   return;
	        	   }
	        	   String url = msg.getData().getString("url");
	        	   if(url.equals(mImageUri)){
	        	   		return;
	        	   }
	        	   mEventHandler.sendEmptyMessageDelayed(THREAD_BUSY, 500);
	        	   recycle();

	        	   mImageUri =url; 
	        	  // if(mDownloadThread.isAlive()){
		        	   mDownloadThread.Close();
		        	   mDownloadThread.stop();
		        	   mDownloadThread = new DownloadThread();
		        	   Log.i(Common.TAG, "new image download thread was created");
		          // }
	        	   
	        	   mDownloadThread.start();
	        	   break;
	           }
	           super.handleMessage(msg);
	       }
	 
	   }
	 
	 
	 private static int computeInitialSampleSize(BitmapFactory.Options options,
			 int minSideLength, int maxNumOfPixels) {
			 double w = options.outWidth;
			 double h = options.outHeight;

			 int lowerBound = (maxNumOfPixels == -1) ? 1 :
			 (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
			 int upperBound = (minSideLength == -1) ? 128 :
			 (int) Math.min(Math.floor(w / minSideLength),
			 Math.floor(h / minSideLength));

			 if (upperBound < lowerBound) 
			 // return the larger one when there is no overlapping zone.
				 return lowerBound;
			 
			if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
				return 1;
			} else if (minSideLength == -1) {
				return lowerBound;
			} else {
				return upperBound;
			}
			
	 } 
	
}