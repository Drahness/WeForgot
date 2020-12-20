package self.joanciscar.myapplication;


import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.Random;

import self.joanciscar.myapplication.ui.items.Item;
import self.joanciscar.myapplication.utilities.FirebaseUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FirebaseInstrumentedTest {
    static Context appContext;
    static FirebaseDatabase fbdbinstance;
    static FirebaseStorage fbsInstance;
    @BeforeClass
    public static void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        fbdbinstance = FirebaseDatabase.getInstance();
        fbsInstance = FirebaseStorage.getInstance();
    }

    @Test
    public void test1() {
        Random r = new Random();
        byte[] bytes = new byte[255];
        r.nextBytes(bytes);
        FirebaseUtils.putBytes(fbsInstance.getReference("asdasdNoexisto3"),bytes);
        Uri u = FirebaseUtils.getEagerlyUri(fbsInstance.getReference("asdasdNoexisto3"));
        assertNull(u);
    }

}