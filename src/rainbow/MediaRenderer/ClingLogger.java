package rainbow.MediaRenderer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.teleal.common.logging.LoggingUtil;

public class ClingLogger {

	public void SetLogLevel(Level level){
	    // Fix the logging integration between java.util.logging and Android internal logging
	    LoggingUtil.resetRootHandler(new FixedAndroidHandler());
	    Logger.getLogger("org.teleal.cling").setLevel(level);
	
	    // UDP communication
	    Logger.getLogger("org.teleal.cling.transport.spi.DatagramIO").setLevel(level);
	    Logger.getLogger("org.teleal.cling.transport.spi.MulticastReceiver").setLevel(level);
	
	    // Discovery
	    Logger.getLogger("org.teleal.cling.protocol.ProtocolFactory").setLevel(level);
	    Logger.getLogger("org.teleal.cling.protocol.async").setLevel(level);
	
	    // Description
	    Logger.getLogger("org.teleal.cling.protocol.ProtocolFactory").setLevel(level);
	    Logger.getLogger("org.teleal.cling.protocol.RetrieveRemoteDescriptors").setLevel(level);
	    Logger.getLogger("org.teleal.cling.protocol.sync.ReceivingRetrieval").setLevel(level);
	    Logger.getLogger("org.teleal.cling.binding.xml.DeviceDescriptorBinder").setLevel(level);
	    Logger.getLogger("org.teleal.cling.binding.xml.ServiceDescriptorBinder").setLevel(level);
	
	    // Registry
	    Logger.getLogger("org.teleal.cling.registry.Registry").setLevel(level);
	    Logger.getLogger("org.teleal.cling.registry.LocalItems").setLevel(level);
	    Logger.getLogger("org.teleal.cling.registry.RemoteItems").setLevel(level);
	} 
}
