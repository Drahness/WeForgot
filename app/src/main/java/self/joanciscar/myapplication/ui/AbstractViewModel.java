package self.joanciscar.myapplication.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import self.joanciscar.myapplication.data.GenericDAO;
import self.joanciscar.myapplication.data.Item;
import self.joanciscar.myapplication.data.Reminder;
import self.joanciscar.myapplication.utilities.Utils;

public abstract class AbstractViewModel<Type,DAO extends GenericDAO<Type,?>> extends AndroidViewModel {
    MutableLiveData<List<Type>> mutableValues;
    DAO dao;
    public AbstractViewModel(@NonNull Application application) {
        super(application);
        dao = getDao(application);
    }

    public abstract DAO getDao(Application app);

    public LiveData<List<Type>> getLiveData() {
        if(mutableValues == null) {
            mutableValues = new MutableLiveData<>();
            loadValues();
        }
        return mutableValues;
    }

    public void loadValues() {
        // load in another thread
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                List<Type> list = dao.getAllDetails(); // even with a thread, this is costly
                AbstractViewModel.this.setReminders(list);
            }
        };
        t.start();
        /*List<Type> list = dao.getAllDetails(); // this is costly
        this.setReminders(list);*/
    }

    public void setReminders(List<Type> list) {
        if(Utils.isUiThread()) {
            // Current Thread is Main Thread.
            mutableValues.setValue(list);
        } else {
            mutableValues.postValue(list);
        }
    }
    public boolean add(Type rem) {
        List<Type> reminders = mutableValues.getValue();
        List<Type> clonedReminders;
        if(reminders == null) {
            clonedReminders = new ArrayList<>();
        }
        else {
            clonedReminders = new ArrayList<>(reminders.size());
            clonedReminders.addAll(reminders);
        }
        boolean b = clonedReminders.add(rem);
        mutableValues.setValue(clonedReminders);
        return b;
    }

    public boolean remove(Type rem) {
        List<Type> reminders = mutableValues.getValue();
        List<Type> clonedReminders;
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

}
