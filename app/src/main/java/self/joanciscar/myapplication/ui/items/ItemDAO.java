package self.joanciscar.myapplication.ui.items;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import self.joanciscar.myapplication.data.DBController;
import self.joanciscar.myapplication.data.GenericDAO;
import self.joanciscar.myapplication.utilities.DBUtils;
import self.joanciscar.myapplication.utilities.Utils;

public class ItemDAO implements GenericDAO<Item,Long> {
    public static final List<Item> cache = new ArrayList<>();
    private static final HashMap<Long,Integer> keyCache = new HashMap<>(); // Key, Index in cache
    private ItemsViewModel viewModel;
    private boolean isSyncedWithCloud = false;
    private boolean isSyncedWithPictures = false;
    private boolean isAutheticated = false;

    private DatabaseReference root_Item_Reference;
    private StorageReference root_Item_Reference_Storage;
    private String uuidUser;
    private SharedPreferences sharedPreferences;
    private final SQLiteOpenHelper helper;

    private final ValueEventListener valueListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {}

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            System.out.println(error);
            System.out.println(error.getCode());
            System.out.println(error.getMessage());
        }
    };
    private final ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if(sharedPreferences.getBoolean("sync",false)) {
                try {
                    HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                    Item oldIfAny = ItemDAO.this.getFromCache((Long) values.get("id"));
                    Item newItem = new Item();
                    newItem.fromMap(Objects.requireNonNull(values));
                    newItem.setLocalfoto(oldIfAny.getLocalfoto());
                    if(newItem.getFoto() == null) {
                        newItem.setFoto(oldIfAny.getFoto());
                    }
                    if (oldIfAny != null && oldIfAny.getId().equals(newItem.getId())) {
                        ItemDAO.this.viewModel.update(oldIfAny, newItem);
                    } else {
                        ItemDAO.this.viewModel.add(newItem);
                    }
                } catch (ClassCastException classCastException) {
                    System.out.println("Class cast exception in onChildAdded. " + snapshot);
                } catch (NullPointerException nullPointerException) {
                    System.out.println("Null pointer exception in onChildAdded " + snapshot);
                    nullPointerException.printStackTrace();
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if(sharedPreferences.getBoolean("sync",false)) {
                try {
                    HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                    Item old = ItemDAO.this.getFromCache((Long) Objects.requireNonNull(values).get("id"));
                    Item newItem = new Item();
                    newItem.fromMap(values);
                    newItem.setLocalfoto(old.getLocalfoto());
/*                    if(newItem.getFoto() == null) {
                        newItem.setFoto(old.getFoto());
                    }*/
                    ItemDAO.this.addCache(newItem);
                    ItemDAO.this.viewModel.update(old, newItem);
                } catch (ClassCastException classCastException) {
                    System.out.println("Class cast exception in onChildChanged. " + snapshot);
                    classCastException.printStackTrace();
                } catch (NullPointerException nullPointerException) {
                    System.out.println("Null pointer exception in onChildChanged " + snapshot);
                    nullPointerException.printStackTrace();
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            if(sharedPreferences.getBoolean("sync",false)) {
                try {
                    HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                    Item i = ItemDAO.this.getFromCache((Long) Objects.requireNonNull(values.get("id")));
                    ItemDAO.this.removeCache(i);
                    ItemDAO.this.delete(i);
                    ItemDAO.this.viewModel.remove(i);
                } catch (ClassCastException classCastException) {
                    System.out.println("Class cast exception in onChildRemoved. " + snapshot);
                    classCastException.printStackTrace();
                } catch (NullPointerException nullPointerException) {
                    System.out.println("Null pointer exception in onChildRemoved " + snapshot);
                    nullPointerException.printStackTrace();
                }
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    public ItemDAO(Context context) {
        helper = new DBController(context);
    }
    public ItemDAO(Context context, ViewModel vm) {
        this.viewModel = (ItemsViewModel) vm;
        helper = new DBController(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("sync_pictures")) {
                    isSyncedWithPictures = sharedPreferences.getBoolean("sync_pictures",false);
                    System.out.println("sync_pictures preferences changed to "+ isSyncedWithPictures);
                    if(isSyncedWithPictures) {

                    } else {
                        // TODO - remove listener - No hay listener. Tengo que hacer check de esto cada vez.
                    }
                }
                if(key.equals("sync")) {
                    isSyncedWithCloud = sharedPreferences.getBoolean("sync",false);
                    System.out.println("sync preference changed to "+ isSyncedWithCloud);
                    if (isSyncedWithCloud) {
                        root_Item_Reference.addChildEventListener(childEventListener);
                        root_Item_Reference.addValueEventListener(valueListener);
                    } else {
                        root_Item_Reference.removeEventListener(valueListener);
                        root_Item_Reference.removeEventListener(childEventListener);
                    }
                }
                if(key.equals("authenticated")) {
                    isAutheticated = sharedPreferences.getBoolean("authenticated",false);
                    if(isAutheticated) {
                        uuidUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        root_Item_Reference = FirebaseDatabase.getInstance().getReference(uuidUser+"/item");
                    }
                    else {
                        root_Item_Reference = null;
                        uuidUser = null;
                    }
                }
            }
        });
        isSyncedWithCloud = sharedPreferences.getBoolean("sync",false);
        isSyncedWithPictures = sharedPreferences.getBoolean("sync_pictures",false);
        isAutheticated = sharedPreferences.getBoolean("authenticated",false);
        if(isAutheticated) {
            uuidUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            // TODO tira error si le das a des-autenticar con una instancia ya creada.
            root_Item_Reference = FirebaseDatabase.getInstance().getReference(uuidUser+"/item");
            root_Item_Reference_Storage = FirebaseStorage.getInstance().getReference(uuidUser+"/item");
            if(isSyncedWithCloud) {
                root_Item_Reference.addChildEventListener(childEventListener);
                root_Item_Reference.addValueEventListener(valueListener);
            }
            if (isSyncedWithPictures) {

            }
        }
        else {
            isSyncedWithCloud = false;
            isSyncedWithPictures = false;
        }
    }

    @Override
    public Item getDetails(Long key) {
        if(!keyCache.containsKey(key)) {
            Item item = null;
            SQLiteDatabase rdb = helper.getReadableDatabase();
            Cursor c = rdb.rawQuery("SELECT ID,NAME,PHOTO,LOCALPHOTO FROM ITEMS WHERE ID = ?",new String[]{key.toString()});
            while (c.moveToNext()) {
                item = new Item();
                item.setId(Objects.requireNonNull(DBUtils.getSafeColumn(c, 0)));
                item.setName(DBUtils.getSafeColumn(c, 1));
                item.setFoto(DBUtils.getSafeColumn(String.class, c, 2));
                byte[] array = DBUtils.getSafeColumn(c, 3);
                item.setLocalfoto(array);
                this.addCache(item);
            }
            c.close();
            return item;
        } else {
            return cache.get(Objects.requireNonNull(keyCache.get(key)));
        }
    }

    @Override
    public List<Item> getAllDetails() {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase rdb = helper.getReadableDatabase();
        Cursor c = rdb.rawQuery("SELECT ID,NAME,PHOTO,LOCALPHOTO FROM ITEMS",null);
        while(c.moveToNext()) {
            Item item = new Item();
            item.setId(DBUtils.getSafeColumn(c,0));
            item.setName(DBUtils.getSafeColumn(c,1));
            item.setFoto(DBUtils.getSafeColumn(String.class,c,2));
            item.setLocalfoto(DBUtils.getSafeColumn(byte[].class,c,3));
            items.add(item);
            this.addCache(item);
        }
        c.close();
        System.out.println("LOCAL FINISHED");
        System.out.println(Utils.isUiThread()+ "IS MAIN THREAD LOCO?");
        if (isSyncedWithCloud) {
            for (Item item : items) {
                root_Item_Reference.child(item.getId().toString()).setValue(item.toMap());
            }
        } // TODO REFACTOR THIS TO ONE LOOP
        if(isSyncedWithPictures) {
            for(Item item: items) {
                if(item.getLocalfoto() != null) { // Puto google
                    Task<?> task = root_Item_Reference_Storage.child("/"+item.getId().toString()).putBytes(item.getLocalfoto());
                    while(!task.isComplete() && !task.isCanceled() && !task.isSuccessful()) {

                    }
                    task = root_Item_Reference_Storage.child("/"+item.getId().toString()).getDownloadUrl();
                    while(!task.isComplete() && !task.isCanceled() && !task.isSuccessful()) {

                    }
                    item.setFoto((Uri) task.getResult());
                    System.out.println();
                }
            }
        }
        return items;
    }

    @Override
    public int update(@NotNull Item entity) {
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement statement = wdb.compileStatement("UPDATE ITEMS SET NAME = ?, PHOTO = ?, LOCALPHOTO = ? WHERE ID = ?");
        DBUtils.bindObject(statement,1,entity.getName());
        DBUtils.bindObject(statement,2,entity.getFoto());
        DBUtils.bindObject(statement,3,entity.getLocalfoto());
        DBUtils.bindObject(statement,4,entity.getId());
        //this.addCache(entity);
        if (isSyncedWithCloud) {
            root_Item_Reference.child(entity.getId().toString()).setValue(entity.toMap());
        }
        if (isSyncedWithPictures) {

        }
        return statement.executeUpdateDelete();
    }

    @Override
    public int delete(@NotNull Item entity) {
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement deleteBase = wdb.compileStatement("DELETE FROM ITEMS WHERE ID = ?");
        DBUtils.bindObject(deleteBase,1,entity.getId());
        keyCache.remove(entity.getId());
        cache.remove(entity);
        if (isSyncedWithCloud) {
            root_Item_Reference.child(entity.getId().toString()).removeValue();
        }
        return deleteBase.executeUpdateDelete();
    }

    @Override
    public long insert(@NotNull Item entity) {
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement statement;
        long id;
        if(entity.getId() == null) {
            statement = wdb.compileStatement("INSERT INTO ITEMS (NAME,PHOTO,LOCALPHOTO) VALUES (?,?,?)");
            DBUtils.bindObject(statement, 1, entity.getName());
            DBUtils.bindObject(statement, 2, entity.getFoto());
            DBUtils.bindObject(statement, 3, entity.getLocalfoto());
            id = statement.executeInsert();
            entity.setId(id);
            this.addCache(entity);

        } else {
            statement = wdb.compileStatement("INSERT INTO ITEMS (ID,NAME,PHOTO,LOCALPHOTO) VALUES (?,?,?,?)");
            DBUtils.bindObject(statement,1,entity.getId());
            DBUtils.bindObject(statement, 2, entity.getName());
            DBUtils.bindObject(statement, 3, entity.getFoto());
            DBUtils.bindObject(statement, 4, entity.getLocalfoto());
            id = statement.executeInsert();
            this.addCache(entity);
        }
        if(isSyncedWithCloud) {
            root_Item_Reference.child(entity.getId().toString()).setValue(entity.toMap());
        }
        return id;
    }

    @Override
    public List<Item> getCachedEntities() {
        return cache;
    }

    @Override
    public Map<Long, Integer> getCachedPairKeyIndexMap() {
        return keyCache;
    }

    public void addCache(@NotNull Item item) {
        if(!keyCache.containsKey(item.getId())) {
            keyCache.put(item.getId(),cache.size());
            cache.add(item);
        }
        else {
            for (Item cachedItem: cache) {
                if(cachedItem.getId().equals(item.getId())) {
                    cachedItem.setLocalfoto(item.getLocalfoto());
                    cachedItem.setFoto(item.getFoto());
                    cachedItem.setName(item.getName());
                    //this.update(cachedItem);
                }
            }
        }
    }

    /**
     *
     * @param id the id of the entity to get.
     * @return a item cached, and if dont exists creates a new entity and caches it.
     */
    public Item getFromCache(Long id) {
        Item item = null;
        if(keyCache.containsKey(id)) {
            return cache.get(Objects.requireNonNull(keyCache.get(id)));
        } else if((item = getDetails(id)) != null) {
            return item;
        } else {
            Item i = new Item();
            i.setId(id);
            this.addCache(i);
            this.insert(i);
            return i;
        }
    }

    public void removeCache(@NotNull Item item) {
        if(keyCache.containsKey(item.getId())) {
            cache.remove(item);
            keyCache.remove(item.getId());
        }
        else {
            System.out.println("WARNINIG TRIED TO REMOVE A ITEM IN A LIST WHO DONT CONTAIN THE KEY, SEE METHOD removeCache(Item item)");
        }
    }
}
