package rainbow.MediaRenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.ServiceManager;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.LocalService;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.meta.ModelDetails;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.model.types.UDN;
import org.teleal.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.teleal.cling.support.lastchange.LastChange;
import org.teleal.cling.support.renderingcontrol.lastchange.RenderingControlLastChangeParser;

import rainbow.Common.Common;

import android.content.Context;
import android.util.Log;

public class LocalMediaRenderer {

	
	private LocalDevice mDevice;
	private AnnotationLocalServiceBinder mBinder = new AnnotationLocalServiceBinder();
	private LocalService<LocalConnectionManagerService> mConnectionManagerService ;
	private LocalService<LocalAVTransport> mAVTransportService ;                                     // DOC:INC1
	private LocalService<LocalAudioRenderingControl> renderingControlService;
	
	final protected ServiceManager<LocalConnectionManagerService> mConnectionManager;
	final protected ServiceManager<LocalAVTransport> mAVTransportManager;
	final protected ServiceManager<LocalAudioRenderingControl> mRenderingControlManager;
	
	final protected LastChange mAvTransportLastChange = new LastChange(new AVTransportLastChangeParser());
	final protected LastChange mRenderingControlLastChange = new LastChange(new RenderingControlLastChangeParser());
	
	public static final long LAST_CHANGE_FIRING_INTERVAL_MILLISECONDS = 200;
	
	private LocalPlayers mPlayers;
	
	String mSerialNum ="";
	
	public LocalMediaRenderer(final Context context){
		
		
		
		mPlayers  = new LocalPlayers(2,mAvTransportLastChange,mRenderingControlLastChange,context);
		getSerialNum();
		
		mConnectionManagerService = mBinder.read(LocalConnectionManagerService.class);
		mConnectionManager =  new DefaultServiceManager<LocalConnectionManagerService>(mConnectionManagerService){
			 @Override
			 protected LocalConnectionManagerService createServiceInstance() throws Exception {
				 return new LocalConnectionManagerService();
			 }	
		};
		mConnectionManagerService.setManager(mConnectionManager);
		
		mAVTransportService = mBinder.read(LocalAVTransport.class);
		mAVTransportManager = new DefaultServiceManager<LocalAVTransport> (mAVTransportService){
            @Override
            protected LocalAVTransport createServiceInstance() throws Exception {
                return new LocalAVTransport(mAvTransportLastChange,mPlayers);
            }
	    };
	    
		mAVTransportService.setManager(mAVTransportManager);
		
		renderingControlService = mBinder.read(LocalAudioRenderingControl.class);
		mRenderingControlManager = new DefaultServiceManager<LocalAudioRenderingControl>(renderingControlService){
			
			@Override
			protected LocalAudioRenderingControl createServiceInstance()  throws Exception {
				return new LocalAudioRenderingControl(mRenderingControlLastChange,context);
				
			}
			
		};
		renderingControlService.setManager(mRenderingControlManager);
		
		try{
			Log.w("Rainbow","new LocalDevice");
			mDevice = new LocalDevice(
				new DeviceIdentity(UDN.uniqueSystemIdentifier(mSerialNum)),
				new UDADeviceType("MediaRenderer", 1),
				new DeviceDetails(String.format("BestBox (%s)", mSerialNum),
				new ManufacturerDetails("SVA", "http://www.sva.com.cn"),
				new ModelDetails("BestBox Media Player","BestBox Media Player Renderer", "1", "http://www.sva.com.cn")),
				new LocalService[]{
					mAVTransportService,
					renderingControlService,
					mConnectionManagerService
					
				}
			);
			
		}catch(ValidationException ex){
			  throw new RuntimeException(ex);
		}
		
		runLastChangePushThread();
	}
	
	protected void runLastChangePushThread() {
		Log.w("Rainbow","runLastChangePushThread");
	// TODO: We should only run this if we actually have event subscribers
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						// These operations will NOT block and wait for network responses
						
						mAVTransportManager.getImplementation().fireLastChange();	
						mRenderingControlManager.getImplementation().fireLastChange();
						Thread.sleep(LAST_CHANGE_FIRING_INTERVAL_MILLISECONDS);
					}
				} catch (Exception ex) {
					//throw new RuntimeException(ex);
					Log.w("Rainbow","runLastChangePushThread"+ex);
				}
			}
		}.start();
	}
	
	public LocalDevice getDevice() {
		return mDevice;
	}
	
	

	String getSerialNum(){
        /*Runtime runtime = Runtime.getRuntime();
	    try{
	        Process process = runtime.exec("cat /system/etc/sys_serial.dat ");
	       
	        try {
				process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        InputStream input = process.getInputStream();
	        InputStream stderr = process.getErrorStream();  
	        BufferedReader br = new BufferedReader(new InputStreamReader(input));
	        BufferedReader berr = new BufferedReader(new InputStreamReader(stderr));

	        String strLine;
	        while(null != (strLine = br.readLine())){
	        	 Log.i(Common.TAG, strLine);
	        	  Pattern p = Pattern.compile("([a-zA-Z0-9]{8})$");   
        	        Matcher m = p.matcher(strLine); 
        	        if(m.find()){	     
        	        	x = m.group(1);
        	        }
	        	//mSerialNum = strLine;	
	        	Log.i(Common.TAG, "serial num:" +mSerialNum);
	        }     
	    } catch (IOException e) {
	            e.printStackTrace();	            
	    }*/
		mSerialNum = "001";
		return mSerialNum;
		
	}
}
