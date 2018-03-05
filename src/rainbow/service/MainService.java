package rainbow.service;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.teleal.cling.android.AndroidUpnpService;

import rainbow.Common.Common;
import rainbow.MediaRenderer.ClingLogger;
import rainbow.MediaRenderer.LocalMediaRenderer;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;


public class MainService extends Service {
	
	
	private static final String TAG = Common.TAG;
	private static final Logger log = Logger.getLogger(TAG);
	private String mSerialNum = "";
	private AndroidUpnpService mUpnpService;
	private LocalMediaRenderer mMediaRenderer = null;
	
	private final int mControlConnectionPort = 1910;
	
	static private Context mContext;
	
	private ServiceConnection serviceConnection = new ServiceConnection(){
		
		
		public void onServiceConnected(ComponentName className, IBinder service) {
			mUpnpService = (AndroidUpnpService) service;
		
			if (mMediaRenderer == null) {
				try {
					mMediaRenderer = new LocalMediaRenderer(mContext);
					mUpnpService.getRegistry().addDevice(mMediaRenderer.getDevice());
					
				} catch (Exception ex) {
				
					Log.e(TAG, "Creating upnp service failed : "+ex);
					return;
				}
			} 
			//mUpnpService.getRegistry().addListener(mDeviceListRegistryListener);
			//mUpnpService.getControlPoint().execute(mMediaRenderer.getSubscriptionCallback());
			//	mUpnpService.getControlPoint().search();
			Log.i(TAG, "Upnp service Start");			
		}
		
		public void onServiceDisconnected(ComponentName className) {
			mUpnpService = null;
		}
		
	};

	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mContext = this;
		log.log(Level.INFO,"====> Rainbow Start <==== ");
		
		new ClingLogger().SetLogLevel(Level.SEVERE);
		InitAllService();
	}
	
	private void InitAllService(){
		
		//media 
		getApplicationContext().bindService(
				new Intent(this, rainbow.Upnp.UpnpService.class), serviceConnection,
				Context.BIND_AUTO_CREATE);

	}
	
	public static Context getContext(){
		return MainService.mContext;
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		getApplicationContext().unbindService(serviceConnection);

	}
	
	
}