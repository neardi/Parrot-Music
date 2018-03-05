package rainbow.MediaRenderer;

import java.net.URI;

import paulo.onlineImage.IAndroidOnlineImageViewService;

import rainbow.Common.Common;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AndroidImageViewManager {
	private Context mContext;
	static AndroidImageViewManager mInstance =null;
	private	IAndroidOnlineImageViewService imageViewControl = null; 
	
	private ServiceConnection PlayerServiceConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			imageViewControl = (IAndroidOnlineImageViewService.Stub.asInterface(service)) ;
			Log.i(Common.TAG, "connected to AndroidImageViewControl service ");

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			imageViewControl = null;
		}
		
	};

	
	public AndroidImageViewManager(Context context){
		mContext = context;
	}
	
	
	public static AndroidImageViewManager getInstance(Context context){
		if(mInstance == null)
			mInstance = new AndroidImageViewManager(context);
		return mInstance;
	}
	
	public void startImageView(URI uri){
		mContext.startActivity(new Intent(mContext,rainbow.MediaRenderer.AndroidImageView.class)
		.setData(Uri.parse(uri.toString()))
		.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

	}
	
	public void connectToImageViewControl(Context context ){
	//	new ComponentName("paulo.onlineImage", "paulo.onlineImage.AndroidImageViewService");
		Log.i(Common.TAG, "connecting to AndroidImageViewControl service ");
	//	Intent serviceIntent=new Intent(mContext,rainbow.MediaRenderer.AndroidImageViewService.class);
		Intent serviceIntent=new Intent();
		serviceIntent.setClassName("paulo.onlineImage", "paulo.onlineImage.AndroidImageViewService");
		context.bindService(serviceIntent, PlayerServiceConnection,
								Context.BIND_AUTO_CREATE);
	}
	
	public void setUrl(String url){
		try {
			imageViewControl.onSetUrl(url);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			imageViewControl.onClose();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
