package self.joanciscar.myapplication.utilities;

import android.os.Build;
import android.os.Looper;

public class Utils {

    public static boolean isUiThread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? Looper.getMainLooper().isCurrentThread()
                : Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
