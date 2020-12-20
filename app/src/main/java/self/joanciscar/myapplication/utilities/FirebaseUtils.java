package self.joanciscar.myapplication.utilities;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class FirebaseUtils {

    /**
     * The method waits if it is an upload and waits to get the Uri.
     * @param sr
     * @return
     */
    public static Uri getEagerlyUri(StorageReference sr) {
        final Uri[] u = {null};
        List<UploadTask> uploadTasks = sr.getActiveUploadTasks();
        if(uploadTasks.size() > 0) {
            while(!allUploadsAreComplete(uploadTasks)) {

            }
        }
        Task<Uri> task = sr.getDownloadUrl();
        task.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                u[0] = task.getResult();
            }
        });
        while(!task.isComplete() && !task.isCanceled() && !task.isSuccessful()) {

        }
        return u[0];
    }

    public static boolean allUploadsAreComplete(List<UploadTask> uploadTasks) {
        for (Task<?> task: uploadTasks) {
            while(!task.isComplete() && !task.isCanceled() && !task.isSuccessful()) {
                return false;
            }
        }

        return true;
    }
    public static UploadTask putBytes(StorageReference sr, byte[] bytes) {
        return sr.putBytes(bytes);
    }
}
