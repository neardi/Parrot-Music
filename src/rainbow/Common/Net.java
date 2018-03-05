package rainbow.Common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import android.content.Context;


import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Net{
	
	Context mContext;
	
	public void Net (Context context ){
		
		mContext = context;
	}
	
	
	private InetAddress getLocalIpAddress() throws UnknownHostException {
		WifiManager wifiManager = (WifiManager) mContext.getSystemService("wifi");
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return InetAddress.getByName(String.format("%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
				(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
	}
	

}
