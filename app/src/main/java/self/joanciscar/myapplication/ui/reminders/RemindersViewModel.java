package self.joanciscar.myapplication.ui.reminders;

import android.app.Application;
import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import self.joanciscar.myapplication.data.GenericDAO;
import self.joanciscar.myapplication.data.Reminder;
import self.joanciscar.myapplication.data.ReminderDAO;
import self.joanciscar.myapplication.ui.AbstractViewModel;
import self.joanciscar.myapplication.utilities.Utils;

public class RemindersViewModel extends AbstractViewModel<Reminder, GenericDAO<Reminder,Long>> {

    public RemindersViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public ReminderDAO getDao(Application app) {
        return new ReminderDAO(app);
    }
    /*private MutableLiveData<List<Reminder>> mutableReminders;
    private final ReminderDAO dao;

    public RemindersViewModel(Application app) {
        super(app);
        dao = new ReminderDAO(app.getApplicationContext());
    }

    private void loadReminders() {
        // load in another thread
        List<Reminder> list = dao.getAllDetails(); // this is costly
        this.setReminders(list);
    }

    public LiveData<List<Reminder>> getReminders() {
        if(mutableReminders == null) {
            mutableReminders = new MutableLiveData<>();
            loadReminders();
        }
        return mutableReminders;
    }

    public boolean addReminder(Reminder rem) {
        List<Reminder> reminders = mutableReminders.getValue();
        List<Reminder> clonedReminders;
        if(reminders == null) {
            clonedReminders = new ArrayList<>();
        }
        else {
            clonedReminders = new ArrayList<>(reminders.size());
            clonedReminders.addAll(reminders);
        }
        boolean b = clonedReminders.add(rem);
        mutableReminders.setValue(clonedReminders);
        return b;
    }

    public boolean removeReminder(Reminder rem) {
        List<Reminder> reminders = mutableReminders.getValue();
        List<Reminder> clonedReminders;
        if(reminders == null) {
            clonedReminders = new ArrayList<>();
        }
        else {
            clonedReminders = new ArrayList<>(reminders.size());
            clonedReminders.addAll(reminders);
        }
        boolean b = clonedReminders.remove(rem);
        this.setReminders(clonedReminders);
        return b;
    }

    public void setReminders(List<Reminder> list) {
        if(Utils.isUiThread()) {
            // Current Thread is Main Thread.
            mutableReminders.setValue(list);
        } else {
            mutableReminders.postValue(list);
        }
    }

*/
}