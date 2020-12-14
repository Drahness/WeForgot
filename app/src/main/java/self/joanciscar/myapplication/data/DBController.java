package self.joanciscar.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class DBController extends SQLiteOpenHelper {
    public static final int version = 1;
    public static final String db_name = "We_forgot";

    public DBController(Context context) {
        super(context,db_name,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
            Alarma: Hora exacta.
                    Dias a la semana
                    Dias concretos.
                    FLAG: dias concretos o a la semana?
                    Nombre UNICO, key?
                    Tono ?

            Recordatorio:   Hora exacta u horas, atributo compuesto.
                            Importancia: Baja, notificacion normal
                                         Media, Ventanita emergente abajo
                                         Alta, Te ocupa la pantalla
                                         Imprescindible booleano, notificacion permanente,
                                            capacidad de hacer check
                            Horas de accion SOLO si es imprescindible, las horas en las que estara la
                                        notificacion.
                            Fin descanso. si hace check in, el tiempo que te
                                        sera eliminada la notificacion permanente
                            Items a recordar, foreign key

             Item:          Nombre item,
                            ID item, PK AUTOINCREMENTAL?
                            Foto item


         */
        db.execSQL("CREATE TABLE Alarms");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
