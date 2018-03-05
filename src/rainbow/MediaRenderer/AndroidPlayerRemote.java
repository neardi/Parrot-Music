package rainbow.MediaRenderer;

import java.net.URI;

import rainbow.Common.Common;

import com.aidl.testService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class AndroidPlayerRemote {
	
	final private String AndroidPlayerPackageName = "com.player";
	final private String AndroidPlayerClassName = "com.player.RealColaPlayerActivity";
	
	Context mContext;
	testService PlayerService = null;
	String mPlayingUrl = "";
	static AndroidPlayerRemote mInstance = null;
	
	
	private ServiceConnection PlayerServiceConnection = new ServiceConnection()
	{
		
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			PlayerService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			PlayerService = testService.Stub.asInterface(service);
			Toast.makeText(mContext, "Service Connected.", Toast.LENGTH_LONG)
					.show();
			Log.i(Common.TAG, "Connet to Service success");

		}

	};
	
	
	AndroidPlayerRemote(Context context){
		mContext = context;
	}

	public static AndroidPlayerRemote getInstance(Context context){
		if(mInstance == null)
			mInstance = new AndroidPlayerRemote(context);
		return mInstance;
	}
	
	public void StartPlayer(URI uri){
		Log.i(Common.TAG, "start colaPlayer");
		mContext.startActivity(
				new Intent().setComponent(new ComponentName(AndroidPlayerPackageName, AndroidPlayerClassName))
							.setData(Uri.parse(uri.toString()))
							.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)      
				);
		
		
	}
	

	
	public boolean ConnectToService(){		
		Log.i(Common.TAG, "connecting to player service ");
		Intent serviceIntent=new Intent("android.intent.action.SERV");
		mContext.bindService(serviceIntent, PlayerServiceConnection,
								Context.BIND_AUTO_CREATE);
		return true;

	}
	
	public void DisconnectToService(){
		mContext.unbindService(PlayerServiceConnection);
	}
	
	public void Pause(){
		if(PlayerService == null)
			return;
		try {
			PlayerService.UpnpPAUSE();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.i(Common.TAG,"Player puase failed: "+e);
		}
	}
	
	public void Stop(){
		if(PlayerService == null)
			return;
		Log.i(Common.TAG,"android Player Stop  ");
		try {
			PlayerService.UpnpSTOP();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.i(Common.TAG,"Player Stop failed: "+e);
		}
	
	}
	
	public void Play(){
		if(PlayerService == null)
			return;
		
		try {
			PlayerService.UpnpPLAY();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.i(Common.TAG,"Player Play failed: "+e);
		}
		
	}
	
	public void SetPlayUrl(String Url){
		if(PlayerService == null)
			return;
		mPlayingUrl = Url;
		try {
			PlayerService.UpnpSETURI(Url);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.i(Common.TAG,"Player UpnpSETURI failed: "+Url+e);
		}
	}
	
	public void Seek(int RealTime){
		if(PlayerService == null)
			return;
		
		try {
			PlayerService.UpnpSEEK(RealTime);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.i(Common.TAG,"Player Seek failed: "+RealTime+e);
		}
		
	}
	
	public int getCurrentDuration(){
		if(PlayerService == null)
			return 0;
		
		try {
			return PlayerService.UpnpGETDURATION();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.i(Common.TAG,"Player getCurrentDuration failed: "+e);
			return 0;
		}
		
	}
	
	public int getDuration(){
		if(PlayerService == null)
			return 0;
		
		try {
			return 	PlayerService.UpnpGETDURATIONSECONDS();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.i(Common.TAG,"Player getDuration failed: "+e);
			return 0;
		}
			
	}
		
	public String getCurrentPlayingUrl(){
		return mPlayingUrl;
	}
		
}

