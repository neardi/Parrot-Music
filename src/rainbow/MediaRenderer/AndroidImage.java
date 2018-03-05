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

public class AndroidImage {
	private Context mContext;
	static AndroidImage mInstance =null;
	private	IAndroidOnlineImageViewService imageViewControl = null; 
	
	private ServiceConnection PlayerServiceConnection = new ServiceConnection(){

		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			imageViewControl = (IAndroidOnlineImageViewService) (IAndroidOnlineImageViewService.Stub.asInterface(service)) ;
			Log.i(Common.TAG, "connected to AndroidImageViewControl service ");

		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			imageViewControl = null;
		}
		
	};

	
	public AndroidImage(Context context){
		mContext = context;
	}
	
	
	public static AndroidImage getInstance(Context context){
		if(mInstance == null)
			mInstance = new AndroidImage(context);
		return mInstance;
	}
	
	public void startImageView(URI uri){
		mContext.startActivity(new Intent(mContext,rainbow.MediaRenderer.AndroidImageView.class)
		.setData(Uri.parse(uri.toString()))
		.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

	}
	
	public void connectToImageViewControl(){
	//	new ComponentName("rainbow.Imageview", "rainbow.Imageview.");
/*		Log.i(Common.TAG, "connecting to AndroidImageViewControl service ");
		Intent serviceIntent=new Intent(mContext,rainbow.MediaRenderer.AndroidImageViewService.class);
		mContext.bindService(serviceIntent, PlayerServiceConnection,
								Context.BIND_AUTO_CREATE);*/
		Intent serviceIntent=new Intent();
		serviceIntent.setClassName("paulo.onlineImage", "paulo.onlineImage.AndroidImageViewService");
		mContext.bindService(serviceIntent, PlayerServiceConnection,
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
