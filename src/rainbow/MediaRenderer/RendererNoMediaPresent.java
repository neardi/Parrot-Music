package rainbow.MediaRenderer;

import java.net.URI;

import org.teleal.cling.support.avtransport.impl.state.AbstractState;
import org.teleal.cling.support.avtransport.impl.state.NoMediaPresent;
import org.teleal.cling.support.avtransport.lastchange.AVTransportVariable;
import org.teleal.cling.support.model.MediaInfo;
import org.teleal.cling.support.model.PositionInfo;

import rainbow.Common.Common;

import android.util.Log;

public class RendererNoMediaPresent extends NoMediaPresent { // DOC:INC1
	

    public RendererNoMediaPresent(org.teleal.cling.support.model.AVTransport transport) {
        super(transport);
    	Log.i(Common.TAG, "RendererNoMediaPresent create: "+transport.toString());
    }

    @Override
    public Class<? extends AbstractState> setTransportURI(URI uri, String metaData) {
    	Log.i(Common.TAG, "URI: "+uri);
    	Log.i(Common.TAG, "metaData: "+metaData);

        getTransport().setMediaInfo(
                new MediaInfo(uri.toString(), metaData)
        );

        // If you can, you should find and set the duration of the track here!
        getTransport().setPositionInfo(
                new PositionInfo(1, metaData, uri.toString())
        );

        // It's up to you what "last changes" you want to announce to event listeners
        getTransport().getLastChange().setEventedValue(
                getTransport().getInstanceId(),
                new AVTransportVariable.AVTransportURI(uri),
                new AVTransportVariable.CurrentTrackURI(uri)
        );
        
        return RendererStopped.class;
    }


} // DOC:INC1
