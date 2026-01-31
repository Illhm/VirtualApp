package io.virtualapp.delegate;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class TrickyStoreInstaller {
    public static void install(Context context, int userId) {
        File trickyStoreDir = new File(context.getFilesDir(), "tricky_store_" + userId);
        if (!trickyStoreDir.exists()) {
            if (!trickyStoreDir.mkdirs()) {
                return; // Failed to create directory
            }
        }
        File keyboxFile = new File(trickyStoreDir, "keybox.xml");
        if (!keyboxFile.exists()) {
             try {
                InputStream is = context.getAssets().open("keybox.xml");
                FileOutputStream os = new FileOutputStream(keyboxFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
