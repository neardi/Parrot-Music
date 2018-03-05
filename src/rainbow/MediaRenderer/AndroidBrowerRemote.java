package rainbow.MediaRenderer;

import java.net.URI;

import org.teleal.cling.model.resource.IconResource;

import paulo.brower.IRainbowBrowerService;
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

public class AndroidBrowerRemote {

	private final String				BROWER_ACTION = "Rainbow.brower";
	
	private Context	 					mContext;
	private static AndroidBrowerRemote 	mInstance =null;
	private	IRainbowBrowerService 		imageViewControl = null; 
	private ServiceConnection 			BrowerServiceConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			imageViewControl = (IRainbowBrowerService.Stub.asInterface(service)) ;
			Log.i(Common.TAG, "connected to AndroidImageViewControl service ");

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			imageViewControl = null;
		}
		
	};

	
	public AndroidBrowerRemote(Context context){
		mContext = context;
	}
	
	
	public static AndroidBrowerRemote getInstance(Context context){
		if(mInstance == null)
			mInstance = new AndroidBrowerRemote(context);
		return mInstance;
	}
	
	
	public void ConnectToService(){
	//	new ComponentName("rainbow.Imageview", "rainbow.Imageview.");
/*		Log.i(Common.TAG, "connecting to AndroidImageViewControl service ");
		Intent serviceIntent=new Intent(mContext,rainbow.MediaRenderer.AndroidImageViewService.class);
		mContext.bindService(serviceIntent, PlayerServiceConnection,
								Context.BIND_AUTO_CREATE);*/
		Intent serviceIntent=new Intent();
		serviceIntent.setClassName("paulo.brower", "paulo.brower.RainbowBrowerService");
		mContext.bindService(serviceIntent, BrowerServiceConnection,
								Context.BIND_AUTO_CREATE);
	}
	
	
	public void SetUrl(String url){
		try {
			imageViewControl.onSetUrl(url);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void GetUrl(){
		
	}
	
	public void Stop(){
		try {
			imageViewControl.onClose();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
}
