package rainbow.MediaRenderer;

import rainbow.Common.Common;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;


public class AndroidImageViewService extends Service implements IlocalImageViewControl{

	//ServiceBinder binder = null;
	@Override
	public void onCreate() {
	//	binder = new ServiceBinder();
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(Common.TAG,"bind ControlService");
		return mBinder;
		
	}

	private final IAndroidImageViewService.Stub mBinder = new IAndroidImageViewService.Stub() {  
   
		@Override
		public void onSetUrl(String url) throws RemoteException {
			// TODO Auto-generated method stub
			Bundle data = new Bundle();
			data.putString("url", url);
			Message msg = new Message();
			msg.setData(data);
			msg.what = AndroidImageView.SET_URL;
			AndroidImageView.mEventHandler.sendMessage(msg);
			
			Log.i(Common.TAG,"ControlService onSetUrl"+url);
		}
		@Override
		public void onClose() throws RemoteException {
			// TODO Auto-generated method stub
			AndroidImageView.mEventHandler.sendEmptyMessage(AndroidImageView.FINISH_VIEW);
			Log.i(Common.TAG,"ControlService onClose");
		}  
          
    };  
    
	 public class ServiceBinder extends Binder implements IlocalImageViewControl{
		 
		 @Override
		public void onSetUrl(String url) {
				// TODO Auto-generated method stub
				Bundle data = new Bundle();
				data.putString("url", url);
				Message msg = new Message();
				msg.setData(data);
				msg.what = AndroidImageView.SET_URL;
				AndroidImageView.mEventHandler.sendMessage(msg);
				
				Log.i(Common.TAG,"ControlService onSetUrl"+url);
			}

			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				AndroidImageView.mEventHandler.sendEmptyMessage(AndroidImageView.FINISH_VIEW);
				Log.i(Common.TAG,"ControlService onClose");
			}

	    }


	 @Override
	public void onSetUrl(String url) {
			// TODO Auto-generated method stub
			Bundle data = new Bundle();
			data.putString("url", url);
			Message msg = new Message();
			msg.setData(data);
			msg.what = AndroidImageView.SET_URL;
			AndroidImageView.mEventHandler.removeMessages(AndroidImageView.SET_URL);
			AndroidImageView.mEventHandler.sendMessage(msg);
			
			Log.i(Common.TAG,"ControlService onSetUrl"+url);
		}

		@Override
		public void onClose() {
			// TODO Auto-generated method stub
			AndroidImageView.mEventHandler.sendEmptyMessage(AndroidImageView.FINISH_VIEW);
			Log.i(Common.TAG,"ControlService onClose");
		}
	
	
	 
 }