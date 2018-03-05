package rainbow.Upnp;

import org.teleal.cling.UpnpServiceConfiguration;
import org.teleal.cling.android.AndroidUpnpServiceConfiguration;
import org.teleal.cling.android.AndroidUpnpServiceImpl;
import org.teleal.cling.android.AndroidWifiSwitchableRouter;
import org.teleal.cling.model.types.ServiceType;
import org.teleal.cling.model.types.UDAServiceType;
import org.teleal.cling.protocol.ProtocolFactory;

import android.content.BroadcastReceiver;
import android.net.wifi.WifiManager;

public class UpnpService extends AndroidUpnpServiceImpl {
	

	protected AndroidUpnpServiceConfiguration createConfiguration(
			WifiManager wifiManager) {
		return new AndroidUpnpServiceConfiguration(wifiManager) {

			
			 /* The only purpose of this class is to show you how you'd configure
			 * the AndroidUpnpServiceImpl in your application:
			 * 
			 */ @Override 
			 public int getRegistryMaintenanceIntervalMillis() {
			  return 500; }
			  
		/*	  @Override public ServiceType[] getExclusiveServiceTypes() {
			  return new ServiceType[] { new UDAServiceType("SwitchPower") }; 
			  }*/
			 

		};
	}
	
	
	
	@Override
	protected AndroidWifiSwitchableRouter createRouter(UpnpServiceConfiguration configuration,
            ProtocolFactory protocolFactory,
            android.net.wifi.WifiManager wifiManager,
            android.net.ConnectivityManager connectivityManager){
		
		return new AndroidWifiSwitchableRouter(configuration, protocolFactory, wifiManager, connectivityManager){
		};
			
	}
}