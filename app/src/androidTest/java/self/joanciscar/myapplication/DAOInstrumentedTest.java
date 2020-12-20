package self.joanciscar.myapplication;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import self.joanciscar.myapplication.ui.items.Item;
import self.joanciscar.myapplication.ui.items.ItemDAO;
import self.joanciscar.myapplication.ui.reminders.Reminder;
import self.joanciscar.myapplication.ui.reminders.ReminderDAO;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOInstrumentedTest {
    static ItemDAO dao;
    static ReminderDAO dao2;
    static List<Item> instances = new ArrayList<>();
    static List<Reminder> instances2 = new ArrayList<>();
    static Random r;

    @BeforeClass
    public static void setUp() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        r = new Random();
        dao = new ItemDAO(appContext);
        dao2 = new ReminderDAO(appContext);
    }

    @AfterClass
    public static void cleanUp() {
        dao2.deleteAll(instances2);
        dao.deleteAll(instances);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("self.joanciscar.myapplication", appContext.getPackageName());
    }

    @Test
    public void insert1() {
        dao.insertAll(instances);
        dao.insertAll(List.of(new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()),
                new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong()), new Item(r.nextLong())));
    }

    @Test
    public void insert2() {
        dao2.insertAll(List.of(new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong()),
                new Reminder(r.nextLong()), new Reminder(r.nextLong())));
        dao2.insert(instances2.remove(1));
        dao2.insertAll(instances2);
        instances2 = dao2.getCachedEntities();
    }

    @Test
    public void delete1() {
        instances = dao.getAllDetails();
        for (int i = 0; i < 10; i++) {
            dao.delete(instances.get(Math.abs(r.nextInt()) % instances.size()));
        }
    }

    @Test
    public void delete2() {
        instances2 = dao2.getAllDetails();
        for (int i = 0; i < 10; i++) {
            dao2.delete(instances2.get(Math.abs(r.nextInt()) % instances2.size()));
        }
    }

    @Test()
    public void Aget() {
        List<?> instances = dao.getAllDetails();
    }

    @Test
    public void Aget2() {
        List<?> instances = dao2.getAllDetails();
    }

    @Test
    public void update1() {
        Item i = instances.get(Math.abs(r.nextInt()) % instances.size());
        i.setName("asdasdas");
        i.setFoto("adsada");
        dao.update(i);
    }

    @Test
    public void update2() {
        Reminder re = instances2.get(Math.abs(r.nextInt()) % instances2.size());
        re.setActivated(true);
        re.setEndTime("2:23:00");
        re.setTime(new Time(1234L));
        dao2.update(re);
    }
}