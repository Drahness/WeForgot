package self.joanciscar.myapplication.utilities;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Looper;

import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class Utils {

    public static boolean isUiThread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? Looper.getMainLooper().isCurrentThread()
                : Thread.currentThread() == Looper.getMainLooper().getThread();
    }
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        return byteArray;
    }
}
