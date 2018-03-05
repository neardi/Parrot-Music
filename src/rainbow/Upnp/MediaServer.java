package rainbow.Upnp;


import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.controlpoint.SubscriptionCallback;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.model.gena.CancelReason;
import org.teleal.cling.model.gena.GENASubscription;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.LocalService;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.meta.ModelDetails;
import org.teleal.cling.model.types.DeviceType;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.model.types.UDN;
import org.teleal.cling.support.avtransport.impl.AVTransportService;
import org.teleal.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.teleal.cling.support.connectionmanager.ConnectionManagerService;
import org.teleal.cling.support.lastchange.LastChange;
import org.teleal.cling.support.renderingcontrol.lastchange.RenderingControlLastChangeParser;

import rainbow.Common.Common;
import rainbow.MediaRenderer.LocalAVTransport;
import rainbow.MediaRenderer.LocalAudioRenderingControl;
//import rainbow.MediaRenderer.RendererNoMediaPresent;
//import rainbow.MediaRenderer.RendererStateMachine;
import rainbow.MediaShare.HttpServer;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

public class MediaServer  {

	private UDN udn = UDN.uniqueSystemIdentifier("GNaP-MediaServer");
	private LocalDevice localDevice;

	//private final static String deviceType = "MediaServer";AVTTransport
	private final static String deviceType = "MediaRenderer";
	private final static int version = 1;
	private final static String LOGTAG = "GNaP-MediaServer";
	private final static int port = 8192;
	private static InetAddress localAddress;
	private static String mManufacturer = "SVA";
	private static String mModel = "BestBox";
	private SubscriptionCallback mCallback;
	
	
	private AnnotationLocalServiceBinder mBinder = new AnnotationLocalServiceBinder();
	private LocalService<ConnectionManagerService> mConnectionManagerService ;
	private LocalService<AVTransportService> mAVTransportService ;                                     // DOC:INC1
	private LocalService<LocalAudioRenderingControl> renderingControlService;
	
	final protected LastChange avTransportLastChange = new LastChange(new AVTransportLastChangeParser());
	final protected LastChange renderingControlLastChange = new LastChange(new RenderingControlLastChangeParser());
	 
	
	public MediaServer(InetAddress localAddress) throws ValidationException {
	
		if(mManufacturer == "")
			mManufacturer = android.os.Build.MANUFACTURER;
 	

		
		
		initRendererService();
		 
		//start http server
		this.localAddress = localAddress;
		try {
			new HttpServer(port);
		}
		catch (IOException ioe )
		{
			System.err.println( "Couldn't start server:\n" + ioe );
			System.exit( -1 );
		}
		
		Log.v(Common.TAG, "Started Http Server on port " + port);
	}
	
	void initRendererService(){
		mConnectionManagerService = mBinder.read(ConnectionManagerService.class);
		mConnectionManagerService.setManager(
		        new DefaultServiceManager<ConnectionManagerService>(
		        		mConnectionManagerService,
		                ConnectionManagerService.class
		        ));
		
		/*mAVTransportService = mBinder.read(AVTransportService.class);
		mAVTransportService.setManager(
				new DefaultServiceManager<AVTransportService> (mAVTransportService,null)
	            {
	                    @Override
	                    protected AVTransportService createServiceInstance() throws Exception {
	               //         return new AVTransportService(
	                  //      	RendererStateMachine.class,   // All states
	                   //     	RendererNoMediaPresent.class  // Initial state
	                        //);
	                    }
	             });
		*/
		renderingControlService = mBinder.read(LocalAudioRenderingControl.class);
/*		renderingControlService.setManager(
				new DefaultServiceManager<LocalAudioRenderingControl>(renderingControlService){
					
					@Override
					protected LocalAudioRenderingControl createServiceInstance()  throws Exception {
					//	return new LocalAudioRenderingControl(renderingControlLastChange);
						
					}
				}
		);*/
		
		LocalService localService[] = null;
		localService[0] = mConnectionManagerService;
		localService[1] = mAVTransportService;
		
		DeviceType type = new UDADeviceType(deviceType, version);
		DeviceDetails details = new DeviceDetails(mModel,
				new ManufacturerDetails(mManufacturer),
				new ModelDetails("Bestbox Media Player Sharing", "GNaP MediaServer for Android", "1.0"));

		LocalService service = new AnnotationLocalServiceBinder()
		.read(AVTransportService.class);
		
		try {
			localDevice = new LocalDevice(new DeviceIdentity(udn), type, details,
					localService);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	 
	
	public LocalDevice getDevice() {
		return localDevice;
	}

	public String getAddress() {
		return localAddress.getHostAddress() + ":" + port;
	}
	
	public SubscriptionCallback getSubscriptionCallback() {
		return mCallback;
	}
}
