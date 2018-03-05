package rainbow.service;

import rainbow.Common.Common;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AutoRunReciever extends BroadcastReceiver   
{   
	
	static final String TAG = Common.TAG;
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";   

    public void onReceive(Context context, Intent intent)    
    {   
        if (intent.getAction().equals(ACTION))    
        {   
        	
        	context.startService(new Intent(context,    
                		  MainService.class));
   /*               context.startActivity(new Intent(context,    
                		  rainbow.MediaRenderer.LocalImageView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
   */     }   
    }   
}