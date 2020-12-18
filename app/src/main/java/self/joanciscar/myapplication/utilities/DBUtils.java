package self.joanciscar.myapplication.utilities;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.NonNull;

public class DBUtils {
    /**
     *
     * @param c cursor of the query
     * @param columnIndex index of the column in cursor
     * @param <T> the class inferenced of the method
     * @return the object, the type of the object is inferred in the column or null
     */

    @SuppressWarnings("unchecked")  // I know is unchecked.
    public static <T> T getSafeColumn(Cursor c, int columnIndex) {
        if(!c.isNull(columnIndex)) {
            return (T) getObject(c,columnIndex,c.getType(columnIndex));
        } else {
            return null;
        }
    }
    @SuppressWarnings("unchecked")  // I know is unchecked.
    public static <T> T getSafeColumn(Class<T> klass,Cursor c, int columnIndex) {
        if(!c.isNull(columnIndex)) {
            return (T) getObject(c,columnIndex,c.getType(columnIndex));
        } else {
            return null;
        }
    }

    public static <T> T getSafeColumn(Class<T> klass,Cursor c, int columnIndex,@NonNull T ifNullValue) {
        T obj = getSafeColumn(klass,c,columnIndex);
        if(obj == null) {
            return ifNullValue;
        } else {
            return obj;
        }
    }

    public static Object getObject(Cursor c, int columnIndex,int type) {
        switch (type) {
            case Cursor.FIELD_TYPE_INTEGER:
                return c.getLong(columnIndex);
            case Cursor.FIELD_TYPE_FLOAT:
                return c.getDouble(columnIndex);
            case Cursor.FIELD_TYPE_STRING:
                return c.getString(columnIndex);
            case Cursor.FIELD_TYPE_BLOB:
                return c.getBlob(columnIndex);
            case Cursor.FIELD_TYPE_NULL:
                return null;
        }
        throw new RuntimeException("Type object not supported. lol check self.joanciscar.myapplication.Utils#getObject");
    }

    /**
     *
     * @param statement to bind the value
     * @param columnIndex 1 based column Index
     * @param o value to bind to a statement
     */
    public static void bindObject(SQLiteStatement statement, int columnIndex, Object o) {
        if(o == null) {
            statement.bindNull(columnIndex);
        }
        else if(o instanceof Long || o instanceof Integer) {
            long l = o instanceof Long ? (Long) o : ((Integer) o).longValue();
            statement.bindLong(columnIndex, l);
        }
        else if(o instanceof Double || o instanceof Float) {
            double d = o instanceof Double ? (Double) o : ((Float) o).doubleValue();
            statement.bindDouble(columnIndex, d);
        }
        else if(o instanceof byte[]) {
            statement.bindBlob(columnIndex,(byte[]) o);
        }
        else if(o instanceof Boolean) {
            if((Boolean) o) {
                statement.bindLong(columnIndex,1);
            } else {
                statement.bindLong(columnIndex,0);
            }
        }
        else {
            statement.bindString(columnIndex,o.toString());
        }
    }
}
