package rainbow.MediaRenderer;

import java.util.concurrent.ConcurrentHashMap;

import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.teleal.cling.support.lastchange.LastChange;

import android.content.Context;
import android.util.Log;

import rainbow.Common.Common;

public class LocalPlayers extends ConcurrentHashMap<UnsignedIntegerFourBytes, LocalPlayer> {


     final protected LastChange avTransportLastChange;
     final protected LastChange renderingControlLastChange;
     Context mContext;
    public LocalPlayers(int numberOfPlayers,
                             LastChange avTransportLastChange,
                           LastChange renderingControlLastChange,
                           Context context) {
         super(numberOfPlayers);
         this.avTransportLastChange = avTransportLastChange;
         this.renderingControlLastChange = renderingControlLastChange;
         
         mContext = context;
         for (int i = 0; i < numberOfPlayers; i++) {
 
             LocalPlayer player =
                     new LocalPlayer(
                             new UnsignedIntegerFourBytes(i),
                              avTransportLastChange,
                              renderingControlLastChange,
                              mContext
                     );
              put(player.getInstanceId(), player);
          }
      }
  
      protected void onPlay(LocalPlayer player) {
         Log.i(Common.TAG,"Player is playing: " + player.getInstanceId());
      }
  
      protected void onStop(LocalPlayer player) {
    	  Log.i(Common.TAG,"Player is stopping: " + player.getInstanceId());
      }
      
  }
