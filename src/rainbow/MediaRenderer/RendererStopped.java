package rainbow.MediaRenderer;

import java.net.URI;

import org.teleal.cling.support.avtransport.impl.state.AbstractState;
import org.teleal.cling.support.avtransport.impl.state.Stopped;
import org.teleal.cling.support.model.SeekMode;

import rainbow.Common.Common;
import android.util.Log;

public class RendererStopped extends Stopped { // DOC:INC1

    public RendererStopped(org.teleal.cling.support.model.AVTransport transport) {
        super(transport);
    	Log.i(Common.TAG, "RendererStopped creat: "+transport.toString());

    }

    @Override
	public void onEntry() {
        super.onEntry();
        // Optional: Stop playing, release resources, etc.
    	Log.i(Common.TAG, "RendererStopped onEntry: ");
    }

    public void onExit() {
        // Optional: Cleanup etc.
    }

    @Override
    public Class<? extends AbstractState> setTransportURI(URI uri, String metaData) {
        // This operation can be triggered in any state, you should think
        // about how you'd want your player to react. If we are in Stopped
        // state nothing much will happen, except that you have to set
        // the media and position info, just like in MyRendererNoMediaPresent.
        // However, if this would be the RendererStopped state, would you
        // prefer stopping first?
    	
    	Log.i(Common.TAG, "RendererStopped setTransportURI: "+uri);
    	Log.i(Common.TAG, "RendererStopped setTransportURI: "+metaData);


        return RendererStopped.class;
    }

    @Override
    public Class<? extends AbstractState> stop() {
        /// Same here, if you are stopped already and someone calls STOP, well...
        return RendererStopped.class;
    }

    @Override
    public Class<? extends AbstractState> play(String speed) {
    	
    	Log.i(Common.TAG, "RendererStopped play: "+speed);

        // It's easier to let this classes' onEntry() method do the work
        return RendererStopped.class;
    }

    @Override
    public Class<? extends AbstractState> next() {
        return RendererStopped.class;
    }

    @Override
    public Class<? extends AbstractState> previous() {
        return RendererStopped.class;
    }

    @Override
    public Class<? extends AbstractState> seek(SeekMode unit, String target) {
        // Implement seeking with the stream in stopped state!
        return RendererStopped.class;
    }
} // DOC:INC1