package self.joanciscar.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.BeforeClass;
import org.junit.Test;

import self.joanciscar.myapplication.data.DBController;
import self.joanciscar.myapplication.ui.items.Item;
import self.joanciscar.myapplication.utilities.DBUtils;

public class SQLiteInstrumentedTest {
    static DBController helper;
    @BeforeClass
    public static void beforeCla() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        helper = new DBController(appContext);
    }

    @Test
    public void test() {
        Item entity = new Item();
        entity.setName("Soy un item");
        entity.setFoto("soy una foto");
        entity.setLocalfoto(new byte[255]);
        SQLiteDatabase wdb = helper.getWritableDatabase();
        SQLiteStatement statement = wdb.compileStatement("INSERT INTO ITEMS (NAME,PHOTO,LOCALPHOTO) VALUES (?,?,?)");
        //DBUtils.bindObject(statement,1,entity.getId());
        DBUtils.bindObject(statement,1,entity.getName());
        DBUtils.bindObject(statement,2,entity.getFoto());
        DBUtils.bindObject(statement,3,entity.getLocalfoto());
        System.out.println("ExecuteInsert()");
    }
}
