package rainbow.MediaRenderer;



import java.net.URI;

import org.teleal.cling.support.avtransport.impl.AVTransportStateMachine;
import org.teleal.cling.support.avtransport.impl.state.AbstractState;
import org.teleal.cling.support.model.SeekMode;
import org.teleal.common.statemachine.States;

import rainbow.Common.Common;

import android.util.Log;

@States({
        RendererNoMediaPresent.class,
        RendererStopped.class,
        RendererPlayer.class
})

//interface RendererStateMachine extends AVTransportStateMachine{}

public class RendererStateMachine implements AVTransportStateMachine {

	@Override
	public void forceState(Class<? extends AbstractState> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractState getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void play(String arg0) {
		// TODO Auto-generated method stub
		Log.i(Common.TAG, "RendererStateMachine  play");	
	}

	@Override
	public void previous() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void record() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void seek(SeekMode arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.i(Common.TAG, "RendererStateMachine  seek");
	
	}

	@Override
	public void setNextTransportURI(URI arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.i(Common.TAG, "RendererStateMachine  setTransportURI");
	
	}

	@Override
	public void setTransportURI(URI arg0, String arg1) {
		Log.i(Common.TAG, "RendererStateMachine  setTransportURI");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
}

