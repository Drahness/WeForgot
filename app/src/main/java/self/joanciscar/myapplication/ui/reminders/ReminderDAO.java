package self.joanciscar.myapplication.ui.reminders;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import self.joanciscar.myapplication.data.DBController;
import self.joanciscar.myapplication.data.GenericDAO;
import self.joanciscar.myapplication.ui.items.Item;
import self.joanciscar.myapplication.ui.items.ItemDAO;
import self.joanciscar.myapplication.utilities.DBUtils;

public class ReminderDAO implements GenericDAO<Reminder, Long> {
    public static final List<Reminder> cache = new ArrayList<>(); // entities
    private static final HashMap<Long,Integer> keyCache = new HashMap<>(); // Key, Index in cache
    private final SQLiteOpenHelper helper;
    private final Context context;
    private ViewModel vm;

    public ReminderDAO(Context context) {
        helper = new DBController(context);
        this.context = context;
    }

    public ReminderDAO(Context context, ViewModel vm) {
        helper = new DBController(context);
        this.context = context;
        this.vm = vm;
    }

    @Override
    public Reminder getDetails(Long key) {
        if(!keyCache.containsKey(key)) {
            Reminder item = null;
            SQLiteDatabase rdb = helper.getReadableDatabase();
            Cursor c = rdb.rawQuery("SELECT ID,HOUR,ENDHOUR,IMPORTANCE,GRACETIME FROM REMINDERS WHERE ID = ?",new String[]{key.toString()});
            while (c.moveToNext()) {
                item = new Reminder();
                item.setId(DBUtils.getSafeColumn(c, 0));
                item.setTime(DBUtils.getSafeColumn(String.class,c, 1));
                item.setEndTime(DBUtils.getSafeColumn(String.class,c, 2));
                item.setImportance(DBUtils.getSafeColumn(c,3));
                item.setGraceTime(DBUtils.getSafeColumn(c,4));
                keyCache.put(item.getId(),cache.size());
                cache.add(item);
            }
            c.close();
            rdb.close();
            return item;
        } else {
            return cache.get(keyCache.get(key));
        }
    }

    @Override
    public List<Reminder> getAllDetails() {
        List<Reminder> items = new ArrayList<>();

        SQLiteDatabase rdb = helper.getReadableDatabase();
        Cursor c = rdb.rawQuery("SELECT ID,HOUR,ENDHOUR,IMPORTANCE,GRACETIME,ACTIVATED FROM REMINDERS",null);
        while (c.moveToNext()) {
            Reminder item = new Reminder();
            item.setId(DBUtils.getSafeColumn(Long.class,c, 0));
            item.setTime(DBUtils.getSafeColumn(String.class,c, 1));
            item.setEndTime(DBUtils.getSafeColumn(String.class,c, 2));
            item.setImportance(DBUtils.getSafeColumn(Long.class,c,3).intValue());
            item.setGraceTime(DBUtils.getSafeColumn(Long.class,c,4).intValue());
            item.setActivated(DBUtils.getSafeColumn(long.class,c,5));
            GenericDAO<Item,Long> dao = new ItemDAO(context);
            Cursor relation = rdb.rawQuery("SELECT ID_ITEM FROM ITEMS_REMINDERS WHERE ID_REMINDER = ?", null);
            List<Long> keys = new ArrayList<>();
            while(relation.moveToNext()) {
                keys.add(relation.getLong(0));
            }
            relation.close();
            item.setItems(dao.getDetails(keys));
            keyCache.put(item.getId(),cache.size());
            cache.add(item);
            items.add(item);
        }
        c.close();
        rdb.close();
        return items;
    }

    @Override
    public int update(Reminder entity) {
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement statement = wdb.compileStatement("UPDATE REMINDERS SET HOUR = ?, ENDHOUR = ?, IMPORTANCE = ?, GRACETIME = ?, ACTIVATED = ? WHERE ID = ?");
        // "UPDATE ITEMS SET ID = ?, NAME = ?, PHOTO = ?, LOCALPHOTO = ? WHERE ID = ?"
        int i = 0;
        DBUtils.bindObject(statement,1,entity.getTime());
        DBUtils.bindObject(statement,2,entity.getEndTime());
        DBUtils.bindObject(statement,3,entity.getImportance());
        DBUtils.bindObject(statement,4,entity.getGraceTime());
        DBUtils.bindObject(statement,5,entity.isActivated());
        DBUtils.bindObject(statement,6,entity.getId());
        GenericDAO<Item,Long> dao = new ItemDAO(context);
        for(Item item: entity.getItems()) {
            SQLiteStatement deleteStatement = wdb.compileStatement("DELETE FROM ITEMS_REMINDERS WHERE ID_REMINDER = ?");
            i += deleteStatement.executeUpdateDelete();
            SQLiteStatement insertStatement = wdb.compileStatement("INSERT INTO ITEMS_REMINDERS (ID_ITEM,ID_REMINDER) VALUES (?,?)");
            DBUtils.bindObject(insertStatement,1,item.getId());
            DBUtils.bindObject(insertStatement,2,entity.getId());
            i += insertStatement.executeUpdateDelete();
        }
        i += statement.executeUpdateDelete();
        wdb.close();
        return i;
    }

    @Override
    public int delete(Reminder entity) {
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement statement = wdb.compileStatement("DELETE FROM REMINDERS WHERE ID = ?");
        DBUtils.bindObject(statement,1,entity.getId());
        keyCache.remove(entity.getId());
        cache.remove(entity);
        int i = statement.executeUpdateDelete();
        wdb.close();
        return i;
    }

    @Override
    public long insert(Reminder entity) {
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement statement = wdb.compileStatement("INSERT INTO REMINDERS (HOUR,ENDHOUR,IMPORTANCE,GRACETIME,ACTIVATED) VALUES (?,?,?,?,?)");
        // "UPDATE ITEMS SET ID = ?, NAME = ?, PHOTO = ?, LOCALPHOTO = ? WHERE ID = ?"
        DBUtils.bindObject(statement,1,entity.getTime());
        DBUtils.bindObject(statement,2,entity.getEndTime());
        DBUtils.bindObject(statement,3,entity.getImportance());
        DBUtils.bindObject(statement,4,entity.getGraceTime());
        DBUtils.bindObject(statement,5,entity.isActivated());
        GenericDAO<Item,Long> dao = new ItemDAO(context);
        long i = statement.executeInsert();
        entity.setId(i);
        for(Item item: entity.getItems()) {
            SQLiteStatement deleteStatement = wdb.compileStatement("DELETE FROM ITEMS_REMINDERS WHERE ID_REMINDER = ?");
            deleteStatement.executeUpdateDelete();
            SQLiteStatement insertStatement = wdb.compileStatement("INSERT INTO ITEMS_REMINDERS (ID_ITEM,ID_REMINDER) VALUES (?,?)");
            DBUtils.bindObject(insertStatement,1,item.getId());
            DBUtils.bindObject(insertStatement,2,entity.getId());
            insertStatement.executeUpdateDelete();
        }
        keyCache.put(entity.getId(),cache.size());
        cache.add(entity);
        wdb.close();
        return i;
    }

    @Override
    public List<Reminder> getCachedEntities() {
        return cache;
    }

    @Override
    public Map<Long, Integer> getCachedPairKeyIndexMap() {
        return keyCache;
    }
}
