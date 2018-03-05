package rainbow.MediaRenderer;

import java.net.URI;

import org.teleal.cling.support.avtransport.impl.state.AbstractState;
import org.teleal.cling.support.avtransport.impl.state.Playing;
import org.teleal.cling.support.model.AVTransport;
import org.teleal.cling.support.model.SeekMode;

import rainbow.Common.Common;
import android.util.Log;

public class RendererPlayer extends Playing {

	public RendererPlayer(AVTransport transport) {
		super(transport);
		// TODO Auto-generated constructor stub
		
    	Log.i(Common.TAG, "RendererPlayer creat: "+transport.toString());

	}
	
	  @Override
	    public void onEntry() {
	        super.onEntry();
	        Log.i(Common.TAG, "RendererPlayer onEntry: ");
	        // Start playing now!
	    }

	    @Override
	    public Class<? extends AbstractState> setTransportURI(URI uri, String metaData) {
	        // Your choice of action here, and what the next state is going to be!
	    	
	    	 Log.i(Common.TAG, "RendererPlayer setTransportURI: " +uri);
	        return RendererStopped.class;
	    }
	    
	    

	@Override
	public Class next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class pause() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class play(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class previous() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class seek(SeekMode arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Class stop() {
		 Log.i(Common.TAG, "RendererPlayer seek: ");
		// TODO Auto-generated method stub
		return null;
	}

}
