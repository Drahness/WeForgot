package self.joanciscar.myapplication.ui.reminders;

import android.app.Application;

import androidx.annotation.NonNull;

import self.joanciscar.myapplication.data.GenericDAO;
import self.joanciscar.myapplication.ui.AbstractViewModel;

public class RemindersViewModel extends AbstractViewModel<Reminder, GenericDAO<Reminder,Long>> {

    public RemindersViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public GenericDAO<Reminder, Long> getDao(Application app, AbstractViewModel<Reminder, GenericDAO<Reminder, Long>> vm) {
        return new ReminderDAO(app,this);
    }

}