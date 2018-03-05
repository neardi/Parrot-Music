package rainbow.MediaShare;

import java.util.logging.Level;

import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;

import rainbow.Common.Common;

import android.util.Log;

public class LocalMediaShare {

	
	public class DeviceListRegistryListener extends DefaultRegistryListener {

		/* Discovery performance optimization for very slow Android devices! */

		@Override
		public void remoteDeviceDiscoveryStarted(Registry registry,
				RemoteDevice device) {
		}

		@Override
		public void remoteDeviceDiscoveryFailed(Registry registry,
				final RemoteDevice device, final Exception ex) {
		}

		/*
		 * End of optimization, you can remove the whole block if your Android
		 * handset is fast (>= 600 Mhz)
		 */

		@Override
		public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

	/*		if (device.getType().getNamespace().equals("schemas-upnp-org")
					&& device.getType().getType().equals("MediaServer")) {*/
//				final DeviceItem display = new DeviceItem(device, device
//						.getDetails().getFriendlyName(),
//						device.getDisplayString(), "(REMOTE) "
//								+ device.getType().getDisplayString());
//				deviceAdded(display);
				Log.d(Common.TAG, "Add remote device: "+device.getDisplayString());
				Log.d(Common.TAG, "base url:  "+device.toString());
			//	Log.d(Common.TAG, "base url:  "+device.getDetails().getDlnaCaps().toString());

				Log.d(Common.TAG, "base url:  "+device.getIdentity().toString());
				Log.d(Common.TAG, "base url:  "+device.getIdentity().getUdn().toString());
				Log.d(Common.TAG, "base url:  "+device.getIdentity().getInterfaceMacAddress());
				Log.d(Common.TAG, "base url:  "+device.getIdentity().getDiscoveredOnLocalAddress().toString());

			//}
		}

		@Override
		public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
//			final DeviceItem display = new DeviceItem(device,
//					device.getDisplayString());
//			deviceRemoved(display);
			
			Log.d(Common.TAG, "remove remote device: "+device.getDisplayString());
			Log.d(Common.TAG, "remove remote device:  "+device.toString());

		}

		@Override
		public void localDeviceAdded(Registry registry, LocalDevice device) {
//			final DeviceItem display = new DeviceItem(device, device
//					.getDetails().getFriendlyName(), device.getDisplayString(),
//					"(REMOTE) " + device.getType().getDisplayString());
//			deviceAdded(display);
			Log.d(Common.TAG, "Add local Device: "+device.getDisplayString());
			

		}

		@Override
		public void localDeviceRemoved(Registry registry, LocalDevice device) {
//			final DeviceItem display = new DeviceItem(device,
//					device.getDisplayString());
//			deviceRemoved(display);
			
			Log.d(Common.TAG, "Remove local Device: "+device.getDisplayString());

		}

	/*	public void deviceAdded(final DeviceItem di) {
			runOnUiThread(new Runnable() {
				public void run() {

					int position = deviceListAdapter.getPosition(di);
					if (position >= 0) {
						// Device already in the list, re-set new value at same
						// position
						deviceListAdapter.remove(di);
						deviceListAdapter.insert(di, position);
					} else {
						deviceListAdapter.add(di);
					}

					// Sort it?
					// listAdapter.sort(DISPLAY_COMPARATOR);
					// listAdapter.notifyDataSetChanged();
				}
			});
		}

		public void deviceRemoved(final DeviceItem di) {
			runOnUiThread(new Runnable() {
				public void run() {
					deviceListAdapter.remove(di);
				}
			});
		}*/
	}
	
}
