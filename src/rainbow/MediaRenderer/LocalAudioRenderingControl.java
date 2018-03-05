package rainbow.MediaRenderer;


import org.teleal.cling.model.types.ErrorCode;
  import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
  import org.teleal.cling.model.types.UnsignedIntegerTwoBytes;
  import org.teleal.cling.support.lastchange.LastChange;
  import org.teleal.cling.support.renderingcontrol.AbstractAudioRenderingControl;
  import org.teleal.cling.support.renderingcontrol.RenderingControlException;
  import org.teleal.cling.support.model.Channel;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import rainbow.Common.Common;
  
  /**
   * @author Paulo zhang
   */
  public class LocalAudioRenderingControl extends AbstractAudioRenderingControl {
  
	  private Context mContext;
	  private AudioManager mAudiomanage;
	// private Map<UnsignedIntegerFourBytes, GstMediaPlayer> players;
  
/*     protected LocalAudioRenderingControl(LastChange lastChange, Map<UnsignedIntegerFourBytes, GstMediaPlayer> players) {
          super(lastChange);
        this.players = players;
      }*/
     
     public LocalAudioRenderingControl(LastChange lastChange ,Context context) {
         super(lastChange);
         mContext = context ;
         Log.i(Common.TAG, "LocalAudioRenderingControl lastChange:  "+lastChange.toString());
         
     	 mAudiomanage = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
     
     }
     
  
  
      protected void getInstance(UnsignedIntegerFourBytes instanceId) throws RenderingControlException {
/*          GstMediaPlayer player = getPlayers().get(instanceId);
          if (player == null) {
              throw new RenderingControlException(RenderingControlErrorCode.INVALID_INSTANCE_ID);
          }
          return player;*/
      }
  
      protected void checkChannel(String channelName) throws RenderingControlException {
          if (!getChannel(channelName).equals(Channel.Master)) {
              throw new RenderingControlException(ErrorCode.ARGUMENT_VALUE_INVALID, "Unsupported audio channel: " + channelName);
          }
      }
  
      @Override
      public boolean getMute(UnsignedIntegerFourBytes instanceId, String channelName) throws RenderingControlException {
          checkChannel(channelName);
         
           if(mAudiomanage.getStreamVolume(AudioManager.STREAM_MUSIC) < 1)
        	   return true;
          return false;
      }
  
      @Override
      public void setMute(UnsignedIntegerFourBytes instanceId, String channelName, boolean desiredMute) throws RenderingControlException {
           checkChannel(channelName);
           if(desiredMute)
        	   mAudiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
           else
        	   mAudiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL); 
        
          mAudiomanage.setStreamMute(AudioManager.STREAM_MUSIC, desiredMute);
          mAudiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, mAudiomanage.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
          Log.i(Common.TAG,"setMute: ");
     }
  
      @Override
      public UnsignedIntegerTwoBytes getVolume(UnsignedIntegerFourBytes instanceId, String channelName) throws RenderingControlException {
          checkChannel(channelName);
//          int vol = (int) (getInstance(instanceId).getVolume() * 100);
//          log.fine("Getting backend volume: " + vol);
          
          return new UnsignedIntegerTwoBytes(mAudiomanage.getStreamVolume(AudioManager.STREAM_MUSIC));
          
      }
  
      @Override
      public void setVolume(UnsignedIntegerFourBytes instanceId, String channelName, UnsignedIntegerTwoBytes desiredVolume) throws RenderingControlException {
          checkChannel(channelName);
          int vol = desiredVolume.getValue().intValue();
      	  if(mAudiomanage.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
      		  mAudiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
      		
          Log.i(Common.TAG,"Setting backend volume to: " + vol);
          mAudiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, vol,AudioManager.FLAG_SHOW_UI);
      }
  
  }
