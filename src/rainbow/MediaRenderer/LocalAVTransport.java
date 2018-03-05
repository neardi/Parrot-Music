package rainbow.MediaRenderer;

import java.net.URI;
import java.util.Map;

import org.teleal.cling.model.ModelUtil;
import org.teleal.cling.model.types.ErrorCode;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.teleal.cling.support.avtransport.AVTransportErrorCode;
import org.teleal.cling.support.avtransport.AVTransportException;
import org.teleal.cling.support.avtransport.AbstractAVTransportService;
import org.teleal.cling.support.lastchange.LastChange;
import org.teleal.cling.support.model.DeviceCapabilities;
import org.teleal.cling.support.model.MediaInfo;
import org.teleal.cling.support.model.PlayMode;
import org.teleal.cling.support.model.PositionInfo;
import org.teleal.cling.support.model.StorageMedium;
import org.teleal.cling.support.model.TransportInfo;
import org.teleal.cling.support.model.TransportSettings;
import org.teleal.common.http.HttpFetch;
import org.teleal.common.util.URIUtil;

import android.util.Log;

import rainbow.Common.Common;



public class LocalAVTransport extends AbstractAVTransportService{
	private static String 			TAG = Common.TAG;
     
    final private Map<UnsignedIntegerFourBytes, LocalPlayer> players;
    
   // final ProtocolInfos mSourceProtocols = new ProtocolInfos(new ProtocolInfo());
    
  
   
    
    public LocalAVTransport(LastChange lastChange, Map<UnsignedIntegerFourBytes, LocalPlayer> players){
    	super(lastChange);
    	this.players = players;
    
    }

    protected Map<UnsignedIntegerFourBytes, LocalPlayer> getPlayers() {
    	 return players;
    }
    
    
    protected LocalPlayer getInstance(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
    	LocalPlayer player = getPlayers().get(instanceId);
    	if (player == null) {
    		throw new AVTransportException(AVTransportErrorCode.INVALID_INSTANCE_ID);
    	}
    	return player;
    }

    
    @Override
	public void setAVTransportURI(UnsignedIntegerFourBytes instanceId,
								  String currentURI,
								  String currentURIMetaData) throws AVTransportException {
    		URI uri;
    	try {
    		uri = new URI(currentURI);
    	} catch (Exception ex) {
    		Log.w(TAG, "setAVTransportURI : "+"CurrentURI can not be null or malformed");
    		throw new AVTransportException(
    				ErrorCode.INVALID_ARGS, "CurrentURI can not be null or malformed"
    				);
    	}
    	 /* 
   	if (currentURI.startsWith("http:")) {
    		try {
    			HttpFetch.validate(URIUtil.toURL(uri));
    		} catch (Exception ex) {
    			throw new AVTransportException(
    					AVTransportErrorCode.RESOURCE_NOT_FOUND, ex.getMessage()
    					);
    		}
    	} else if (!currentURI.startsWith("file:")) {
    		throw new AVTransportException(
			ErrorCode.INVALID_ARGS, "Only HTTP and file: resource identifiers are supported");
    	}
*/
	// TODO: Check mime type of resource against supported types
	// TODO: DIDL fragment parsing and handling of currentURIMetaData
		Log.i(TAG, "setAVTransportURI : instance id :" +instanceId.toString());

    
    	getInstance(instanceId).setURI(uri,currentURIMetaData);
	  }
    
    
    	
	@Override
	public String getCurrentTransportActions(UnsignedIntegerFourBytes instanceId)
			throws AVTransportException {
		// TODO Auto-generated method stub
	     return ModelUtil.toCommaSeparatedList(
	    	getInstance(instanceId).getCurrentTransportActions()
	    );
	}

	@Override
	public DeviceCapabilities getDeviceCapabilities(
			UnsignedIntegerFourBytes arg0) throws AVTransportException {
		// TODO Auto-generated method stub
		 return new DeviceCapabilities(new StorageMedium[]{StorageMedium.NETWORK});
	}

	@Override
	public MediaInfo getMediaInfo(UnsignedIntegerFourBytes instanceId)
			throws AVTransportException {
		// TODO Auto-generated method stub
		 return getInstance(instanceId).getCurrentMediaInfo();
	}

	@Override
	public PositionInfo getPositionInfo(UnsignedIntegerFourBytes instanceId)
			throws AVTransportException {
		// TODO Auto-generated method stub
		  PositionInfo info= getInstance(instanceId).getCurrentPositionInfo();

		//	Log.i(Common.TAG, "avtransport getPositionInfo RelCount:"+info.getElapsedPercent());
		//	Log.i(Common.TAG, "avtransport getPositionInfo RelCount:"+info.getTrackDurationSeconds());

		  return info;
	}

	@Override
	public TransportInfo getTransportInfo(UnsignedIntegerFourBytes instanceId)
			throws AVTransportException {
		// TODO Auto-generated method stub
		  return getInstance(instanceId).getCurrentTransportInfo();
	}

	@Override
	public TransportSettings getTransportSettings(UnsignedIntegerFourBytes instanceId)
			throws AVTransportException {
		getInstance(instanceId);
		return new TransportSettings(PlayMode.NORMAL);
		
	}
	

	@Override
	public void next(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
		// TODO Auto-generated method stub
		 getInstance(instanceId).next();	
	}

	@Override
	public void pause(UnsignedIntegerFourBytes instanceId)
			throws AVTransportException {
		 getInstance(instanceId).pause();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void play(UnsignedIntegerFourBytes instanceId, String speed)
			throws AVTransportException {
		// TODO Auto-generated method stub
		 getInstance(instanceId).play();
	}

	@Override
	public void previous(UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub
		Log.i(Common.TAG,"localAVTransport previous");
	}

	@Override
	public void record(UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void seek(UnsignedIntegerFourBytes instanceId, String unit, String target){
		//	throws AVTransportException {
		Log.i(Common.TAG, "localAVTransport seek :"+unit+target);
		 try {
			getInstance(instanceId).seek(ModelUtil.fromTimeString(target.substring(0, 7)));
			Log.i(Common.TAG, "seek finished");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i(Common.TAG, "============>>>> localAVTransport seek failed: "+e);
			e.printStackTrace();
		}

/*		 try {
			 
			 if (!unit.equals("REL_TIME")) {
				Log.i(Common.TAG, "localAVTransport Not Rel_time!SeekMode:"+unit+" time: "+target);
				 throw new IllegalArgumentException();
			 }
			 getInstance(instanceId).seek(ModelUtil.fromTimeString(target));
		 } catch (IllegalArgumentException ex) {
				Log.i(Common.TAG, "localAVTransport Not Rel_time!SeekMode:"+unit+" time: "+target);
			 throw new AVTransportException(
					 AVTransportErrorCode.SEEKMODE_NOT_SUPPORTED, "Unsupported seek mode: " + unit
					 );
		 }*/
		
	}



	@Override
	public void setNextAVTransportURI(UnsignedIntegerFourBytes arg0,
			String arg1, String arg2) throws AVTransportException {
		// TODO Auto-generated method stub
		Log.i(Common.TAG, "Avtransport : "+arg1 +" "+ arg2);
	}

	@Override
	public void setPlayMode(UnsignedIntegerFourBytes arg0, String arg1)
			throws AVTransportException {
		// TODO Auto-generated method stub
		Log.i(Common.TAG, "setPlayMode : "+arg1);
	
	}

	@Override
	public void setRecordQualityMode(UnsignedIntegerFourBytes arg0, String arg1)
			throws AVTransportException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
		// TODO Auto-generated method stub
		getInstance(instanceId).stop();
		Log.i(TAG, "stop : instance id :" +instanceId.toString());

	}
}	
