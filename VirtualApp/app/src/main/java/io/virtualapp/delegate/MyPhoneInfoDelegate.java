package io.virtualapp.delegate;

import android.content.Context;
import android.content.SharedPreferences;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.delegate.PhoneInfoDelegate;
import java.util.UUID;

/**
 * Fake the Device ID.
 */
public class MyPhoneInfoDelegate implements PhoneInfoDelegate {

    private static final String PREFS_NAME = "device_spoof_prefs";
    private static final String KEY_DEVICE_ID_PREFIX = "spoofed_device_id_";

    @Override
    public String getDeviceId(String oldDeviceId, int userId) {
        Context context = VirtualCore.get().getContext();
        if (context == null) {
            return oldDeviceId;
        }

        // Use SharedPreferences to persist the ID.
        // Since we are running inside the VApp process, IO redirection handles
        // storing this in the user's virtual data directory.
        // This ensures isolation between users (profiles).
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String key = KEY_DEVICE_ID_PREFIX + userId;
        String spoofedId = prefs.getString(key, null);

        if (spoofedId == null) {
            spoofedId = UUID.randomUUID().toString();
            prefs.edit().putString(key, spoofedId).apply();
        }

        return spoofedId;
    }

    @Override
    public String getBluetoothAddress(String oldAddress, int userId) {
        return oldAddress;
    }

    @Override
    public String getMacAddress(String oldAddress, int userId) {
        return oldAddress;
    }
}
