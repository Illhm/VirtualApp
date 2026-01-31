package com.lody.virtual;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Lody
 */
public class GmsSupport {

    private static final List<String> GOOGLE_APP = Arrays.asList(
            "com.android.vending",
            "com.google.android.play.games",
            "com.google.android.wearable.app",
            "com.google.android.wearable.app.cn"
    );

    private static final List<String> GOOGLE_SERVICE = Arrays.asList(
            "com.google.android.gsf",
            "com.google.android.gms",
            "com.google.android.gsf.login",
            "com.google.android.backuptransport",
            "com.google.android.backup",
            "com.google.android.configupdater",
            "com.google.android.syncadapters.contacts",
            "com.google.android.feedback",
            "com.google.android.onetimeinitializer",
            "com.google.android.partnersetup",
            "com.google.android.setupwizard",
            "com.google.android.syncadapters.calendar"
    );

    public static final String TRICKY_STORE_PKG = "io.github.vvb2060.keyattestation";
    private static final List<String> TRICKY_STORE_APP = Collections.singletonList(TRICKY_STORE_PKG);

    public static boolean isGmsFamilyPackage(String packageName) {
        return packageName.equals("com.android.vending")
                || packageName.equals("com.google.android.gms");
    }

    public static boolean isGoogleFrameworkInstalled() {
        return VirtualCore.get().isAppInstalled("com.google.android.gms");
    }

    public static boolean isOutsideGoogleFrameworkExist() {
        return VirtualCore.get().isOutsideInstalled("com.google.android.gms");
    }

    private static void installFromPathOrSystem(String packageName, int userId) {
        VirtualCore core = VirtualCore.get();
        if (core.isAppInstalledAsUser(userId, packageName)) {
            return;
        }

        // Check external storage first
        File sdcard = Environment.getExternalStorageDirectory();
        File pluginApk = new File(sdcard, "VirtualApp/plugins/" + packageName + ".apk");

        if (pluginApk.exists()) {
             core.installPackage(pluginApk.getAbsolutePath(), InstallStrategy.DEPEND_SYSTEM_IF_EXIST);
        } else {
             // Fallback to host system
             ApplicationInfo info = null;
             try {
                 info = core.getUnHookPackageManager().getApplicationInfo(packageName, 0);
             } catch (PackageManager.NameNotFoundException e) {
                 // Ignore
             }
             if (info != null && info.sourceDir != null) {
                 core.installPackage(info.sourceDir, InstallStrategy.DEPEND_SYSTEM_IF_EXIST);
             }
        }

        if (userId != 0) {
            core.installPackageAsUser(userId, packageName);
        }
    }

    private static void installPackages(List<String> list, int userId) {
        for (String packageName : list) {
            installFromPathOrSystem(packageName, userId);
        }
    }

    public static void installGApps(int userId) {
        installPackages(GOOGLE_SERVICE, userId);
        installPackages(GOOGLE_APP, userId);
    }

    public static void installGoogleService(int userId) {
        installPackages(GOOGLE_SERVICE, userId);
    }

    public static void installGoogleApp(int userId) {
        installPackages(GOOGLE_APP, userId);
    }

    public static void installTrickyStore(int userId) {
        installPackages(TRICKY_STORE_APP, userId);
    }

    public static void configureKeyBox(int userId) {
        File sdcard = Environment.getExternalStorageDirectory();
        File keyboxFile = new File(sdcard, "VirtualApp/keybox.xml");
        if (keyboxFile.exists()) {
            File destDir = VEnvironment.getDataUserPackageDirectory(userId, TRICKY_STORE_PKG);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            File destFile = new File(destDir, "keybox.xml");
            copyFile(keyboxFile, destFile);
        }
    }

    private static void copyFile(File source, File dest) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
