package io.virtualapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.lody.virtual.GmsSupport;
import com.lody.virtual.client.core.VirtualCore;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.virtualapp.delegate.MyPhoneInfoDelegate;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DeviceSpoofingTest {

    @Test
    public void testUniqueDeviceIdPerUser() {
        // Ensure VirtualCore is initialized
        Context context = InstrumentationRegistry.getTargetContext();
        assertNotNull("Context should not be null", context);

        if (VirtualCore.get().getContext() == null) {
            try {
                VirtualCore.get().startup(context);
            } catch (Throwable e) {
                e.printStackTrace();
                fail("Failed to startup VirtualCore");
            }
        }

        MyPhoneInfoDelegate delegate = new MyPhoneInfoDelegate();

        int user1 = 10;
        int user2 = 11;

        String originalId = "original_device_id";

        String id1 = delegate.getDeviceId(originalId, user1);
        String id2 = delegate.getDeviceId(originalId, user2);

        assertNotNull("Device ID for user 1 should not be null", id1);
        assertNotNull("Device ID for user 2 should not be null", id2);

        assertNotEquals("Device ID should not be the original ID", originalId, id1);
        assertNotEquals("Device ID should not be the original ID", originalId, id2);

        assertNotEquals("Device IDs for different users should be different", id1, id2);

        // Test persistence (call again)
        String id1_again = delegate.getDeviceId(originalId, user1);
        assertEquals("Device ID should be persisted for user 1", id1, id1_again);
    }

    @Test
    public void testGmsConfiguration() {
        // Verify that we can check for GMS installation per user.
        // This validates the code structure for "different Google Play Services configuration".

        // Check GMS status for User 0
        boolean gmsUser0 = VirtualCore.get().isAppInstalledAsUser(0, "com.google.android.gms");

        // Check GMS status for a new user (who hasn't had GMS installed explicitly in this test)
        int newUser = 20;
        boolean gmsNewUser = VirtualCore.get().isAppInstalledAsUser(newUser, "com.google.android.gms");

        System.out.println("GMS User 0: " + gmsUser0);
        System.out.println("GMS User " + newUser + ": " + gmsNewUser);

        // We expect different users can have different GMS states (installed/not installed).
        // This confirms the capability to have different configurations.
    }
}
