package self.joanciscar.myapplication.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import self.joanciscar.myapplication.utilities.DBUtils;

public class ItemDAO implements GenericDAO<Item,Long>{
    public static final List<Item> cache = new ArrayList<>();
    private static final HashMap<Long,Integer> keyCache = new HashMap<>(); // Key, Index in cache
    private final SQLiteOpenHelper helper;

    public ItemDAO(Context context) {
        helper = new DBController(context);
    }

    @Override
    public Item getDetails(Long key) {
        if(!keyCache.containsKey(key)) {
            Item item = null;
            SQLiteDatabase rdb = helper.getReadableDatabase();
            Cursor c = rdb.rawQuery("SELECT ID,NAME,PHOTO,LOCALPHOTO FROM ITEMS WHERE ID = ?",new String[]{key.toString()});
            while (c.moveToNext()) {
                item = new Item();
                item.setId(DBUtils.getSafeColumn(c, 0));
                item.setName(DBUtils.getSafeColumn(c, 1));
                item.setFoto(DBUtils.getSafeColumn(c, 2));
                byte[] array = DBUtils.getSafeColumn(c, 3);
                item.setLocalfoto(array);
                keyCache.put(item.getId(),cache.size());
                cache.add(item);
            }
            c.close();
            return item;
        } else {
            return cache.get(keyCache.get(key));
        }
    }

    @Override
    public List<Item> getAllDetails() {
        SQLiteDatabase rdb = helper.getReadableDatabase();
        Cursor c = rdb.rawQuery("SELECT ID,NAME,PHOTO,LOCALPHOTO FROM ITEMS",null);
        List<Item> items = new ArrayList<>();
        while(c.moveToNext()) {
            Item item = new Item();
            item.setId(c.getInt(0));
            item.setName(c.getString(1));
            item.setFoto(c.getString(2));
            item.setLocalfoto(c.getBlob(3));
            items.add(item);
            keyCache.put(item.getId(),cache.size());
            cache.add(item);
        }
        c.close();
        return items;
    }

    @Override
    public int update(Item entity) {
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement statement = wdb.compileStatement("UPDATE ITEMS SET NAME = ?, PHOTO = ?, LOCALPHOTO = ? WHERE ID = ?");
        // "UPDATE ITEMS SET ID = ?, NAME = ?, PHOTO = ?, LOCALPHOTO = ? WHERE ID = ?"
        DBUtils.bindObject(statement,1,entity.getName());
        DBUtils.bindObject(statement,2,entity.getFoto());
        DBUtils.bindObject(statement,3,entity.getLocalfoto());
        DBUtils.bindObject(statement,4,entity.getId());
        keyCache.put(entity.getId(),cache.size());
        cache.add(entity);
        return statement.executeUpdateDelete();
    }

    @Override
    public int delete(Item entity) {
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement deleteBase = wdb.compileStatement("DELETE FROM ITEMS WHERE ID = ?");
        //SQLiteStatement deleteRelation = wdb.compileStatement("DELETE FROM ITEMS_REMINDERS WHERE ID_ITEM = ?");
        // "UPDATE ITEMS SET ID = ?, NAME = ?, PHOTO = ?, LOCALPHOTO = ? WHERE ID = ?"
        DBUtils.bindObject(deleteBase,1,entity.getId());
        //Utils.bindObject(deleteRelation,1,entity.getId());
        keyCache.remove(entity.getId());
        cache.remove(entity);
        return deleteBase.executeUpdateDelete() /*+ deleteRelation.executeUpdateDelete()*/;
    }

    @Override
    public long insert(Item entity) {
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement statement = wdb.compileStatement("INSERT INTO ITEMS (ID,NAME,PHOTO,LOCALPHOTO) VALUES (?,?,?,?)");
        // "INSERT INTO ITEMS (ID,NAME,PHOTO,LOCALPHOTO) VALUES (?,?,?,?)"
        DBUtils.bindObject(statement,1,entity.getId());
        DBUtils.bindObject(statement,2,entity.getName());
        DBUtils.bindObject(statement,3,entity.getFoto());
        DBUtils.bindObject(statement,4,entity.getLocalfoto());
        keyCache.put(entity.getId(),cache.size());
        cache.add(entity);
        return statement.executeInsert();
    }

    @Override
    public List<Item> getCachedEntities() {
        return cache;
    }

    @Override
    public Map<Long, Integer> getCachedPairKeyIndexMap() {
        return keyCache;
    }
}
