package self.joanciscar.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DBController extends SQLiteOpenHelper {
    public static final int version = 2;
    public static final String db_name = "We_forgot";

    public DBController(Context context) {
        super(context,db_name,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
            Alarma: Hora exacta.
                    Dias a la semana. FLAGS 1 2 4 8 16 32 64
                    FLAG: dias concretos o a la semana?
                    Nombre UNICO, key?
                    Tono ?

            Recordatorio:   Hora exacta u horas, atributo compuesto. Por ahora singular
                            Importancia: Baja, notificacion normal
                                         Media, Ventanita emergente abajo
                                         Alta, Te ocupa la pantalla
                                         Imprescindible booleano, notificacion permanente,
                                            capacidad de hacer check
                            Horas de accion SOLO si es imprescindible, las horas en las que estara la
                                        notificacion.
                            Fin descanso. si hace check in, el tiempo que te
                                        sera eliminada la notificacion permanente
                            Items a recordar, foreign key atributo compuesto

             Item:          Nombre item,
                            ID item, PK AUTOINCREMENTAL?
                            Foto item
            Items a recordar:   ID Item
                                ID Recordatorio


         */
        db.execSQL("CREATE TABLE ITEMS (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    "NAME VARCHAR," +
                                    "PHOTO VARCHAR," + // enlace a la foto en FIREBASE
                                    "LOCALPHOTO BLOB" +
                                    ")");
        db.execSQL("CREATE TABLE REMINDERS (" +
                                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    "HOUR TIME NOT NULL," +
                                    "ENDHOUR TIME," +
                                    "IMPORTANCE INTEGER NOT NULL," +
                                    "GRACETIME INTEGER," +
                                    "ACTIVATED BOOLEAN" +
                                    ")");
        db.execSQL("CREATE TABLE ITEMS_REMINDERS (" +
                                    "ID_ITEM INTEGER," +
                                    "ID_REMINDER INTEGER," +
                                    "PRIMARY KEY (ID_ITEM,ID_REMINDER)," +
                                    "FOREIGN KEY (ID_ITEM) REFERENCES ITEM(ID) ON DELETE CASCADE," +
                                    "FOREIGN KEY (ID_REMINDER) REFERENCES REMINDERS(ID) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion) {
            if(newVersion == 2) {
                db.execSQL("ALTER TABLE REMINDERS ADD COLUMN ACTIVATED BOOLEAN");
            }
        }
    }
}

