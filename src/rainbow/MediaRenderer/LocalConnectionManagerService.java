package rainbow.MediaRenderer;

import java.beans.PropertyChangeSupport;

import org.teleal.cling.model.action.ActionException;
import org.teleal.cling.support.connectionmanager.ConnectionManagerService;
import org.teleal.cling.support.model.ConnectionInfo;
import org.teleal.cling.support.model.ProtocolInfo;
import org.teleal.cling.support.model.ProtocolInfos;
import org.teleal.common.util.MimeType;
import rainbow.Common.Common;
import android.util.Log;

public class LocalConnectionManagerService extends ConnectionManagerService {
 
	
	public final String VedioMimeTypes[] = {
			"video/mp4",
			"video/x-ms-wmv",
			"video/x-pn-realvideo",
			"video/vnd.rn-realvideo",
			"video/x-matroska",
			"video/x-flv",
			"video/MP2T",
			
	};
	
	public final String AudioMimeTypes[] = {
			"audio/mp3",
			"audio/mpeg", 
			"audio/x-mpeg-3",
			"audio/aac",

	};
	
	public final String PhotoMimeTypes[] = {
			"image/jpeg",
			"image/png",
	};
	
	
	public LocalConnectionManagerService(ProtocolInfos sourceProtocolInfo, ProtocolInfos sinkProtocolInfo, ConnectionInfo... activeConnections){
		  Log.i(Common.TAG, "construct 1  ");
		//  super.
	
	} 

	public  LocalConnectionManagerService(ProtocolInfos sourceProtocolInfo, ProtocolInfos sinkProtocolInfo) {
		  Log.i(Common.TAG, "construct 2 ");
	}
	
	public  LocalConnectionManagerService(ConnectionInfo... activeConnections) {
		  Log.i(Common.TAG, "construct 3 ");
	}
	
	public  LocalConnectionManagerService(PropertyChangeSupport propertyChangeSupport, ProtocolInfos sourceProtocolInfo, ProtocolInfos sinkProtocolInfo, ConnectionInfo... activeConnections) {
		  Log.i(Common.TAG, "construct 4 ");
	}

      public LocalConnectionManagerService() {
/*          List<PluginFeature> types = Registry.getDefault().getPluginFeatureListByPlugin("typefindfunctions");
          for (PluginFeature type : types) {
              try {
                  MimeType mt = MimeType.valueOf(type.getName());
                  log.fine("Supported MIME type: " + mt);
                  sinkProtocolInfo.add(new ProtocolInfo(mt));
              } catch (IllegalArgumentException ex) {
                  log.finer("Ignoring invalid MIME type: " + type.getName());
              }
          }
          MimeType.
          Log.i("Supported MIME types: " + sinkProtocolInfo.size());
      }*/
    	  new MimeType("", null);
    	  for(int i = 0 ; i < VedioMimeTypes.length;i++){
    		  sinkProtocolInfo.add(new ProtocolInfo(MimeType.valueOf(VedioMimeTypes[i])));
    		  Log.i(Common.TAG, "Support Vedio Mimetype :  "+VedioMimeTypes[i]);
    	  }
    	  for(int i = 0 ; i < AudioMimeTypes.length;i++){
    		  sinkProtocolInfo.add(new ProtocolInfo(MimeType.valueOf(AudioMimeTypes[i])));
    		  Log.i(Common.TAG, "Support Audio Mimetype :  "+AudioMimeTypes[i]);
    	  }
    	  
    	  for(int i = 0 ; i < PhotoMimeTypes.length;i++){
    		  sinkProtocolInfo.add(new ProtocolInfo(MimeType.valueOf(PhotoMimeTypes[i])));
    		  Log.i(Common.TAG, "Support Audio Mimetype :  "+PhotoMimeTypes[i]);
    	  }
    	          
  }
      
      
      @Override
	public PropertyChangeSupport	getPropertyChangeSupport() {
    	  Log.i(Common.TAG, "getPropertyChangeSupport "+propertyChangeSupport.toString()); 
		return propertyChangeSupport;
    	  
      }
      
      
      @Override
	public void getProtocolInfo() {
    	  Log.i(Common.TAG, "getProtocolInfo "); 
      }
      
      @Override
	public ProtocolInfos	getSinkProtocolInfo() {
    	  Log.i(Common.TAG, "getSinkProtocolInfo "+sinkProtocolInfo.toString()); 
		return sinkProtocolInfo;
    	  
      }
      
      @Override
	public ProtocolInfos	getSourceProtocolInfo() {
    	  Log.i(Common.TAG, "getSourceProtocolInfo "+sinkProtocolInfo.toString());
		return sinkProtocolInfo; 
      }
      
  
      
      @Override
	public ConnectionInfo	getCurrentConnectionInfo(int connectionId) {
    	  ConnectionInfo info = null;
		try {
			 info = super.getCurrentConnectionInfo(connectionId);;
		} catch (ActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionInfo  inf = new ConnectionInfo();

		info.setConnectionStatus(org.teleal.cling.support.model.ConnectionInfo.Status.OK);
		
  	  Log.i(Common.TAG, "getCurrentConnectionInfo info: "+info.toString());

		return info;
    	  
      }
      
      @Override
	public org.teleal.cling.model.types.csv.CSV<org.teleal.cling.model.types.UnsignedIntegerFourBytes>	getCurrentConnectionIDs() {
    	  Log.i(Common.TAG, "getCurrentConnectionId id: "+super.getCurrentConnectionIDs());
    	  return super.getCurrentConnectionIDs();
      }
}
