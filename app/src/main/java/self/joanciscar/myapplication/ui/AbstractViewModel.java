package self.joanciscar.myapplication.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import self.joanciscar.myapplication.data.Entity;
import self.joanciscar.myapplication.data.GenericDAO;
import self.joanciscar.myapplication.utilities.Utils;

public abstract class AbstractViewModel<Type extends Entity<?> ,DAO extends GenericDAO<Type,?>> extends AndroidViewModel {
    MutableLiveData<List<Type>> mutableValues;
    DAO dao;
    public AbstractViewModel(@NonNull Application application) {
        super(application);
        dao = getDao(application,this);
    }

    public abstract DAO getDao(Application app, AbstractViewModel<Type,DAO> vm);

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
            }
        };
        List<Type> list = dao.getAllDetails(); // even with a thread, this is costly
        AbstractViewModel.this.setValues(list);
        //t.start();
        /*List<Type> list = dao.getAllDetails(); // this is costly
        this.setReminders(list);*/
    }

    /**
     * Its better to use add or remove.
     * @param list
     */
    public void setValues(List<Type> list) {
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
        dao.insert(rem);
        mutableValues.setValue(clonedReminders);
        return b;
    }

    public void remove(Type rem) {
        List<Type> reminders = mutableValues.getValue();
        List<Type> clonedReminders;
        if(reminders == null) {
            clonedReminders = new ArrayList<>();
        }
        else {
            clonedReminders = new ArrayList<>(reminders.size());
            for (Type item: reminders) {
                if(!item.getId().equals(rem.getId())) { // Check for the identity
                    clonedReminders.add(item);
                }
            }
        }
        dao.delete(rem);
        this.setValues(clonedReminders);
    }
    public boolean update(Type old, Type updated) {
        List<Type> reminders = mutableValues.getValue();
        List<Type> clonedReminders;
        if(reminders == null) {
            clonedReminders = new ArrayList<>();
        }
        else {
            clonedReminders = new ArrayList<>(reminders.size());
            for (Type item: reminders) {
                if(!item.getId().equals(old.getId())) { // Check for the identity
                    clonedReminders.add(item);
                }
                else {
                    clonedReminders.add(updated);
                }
            }
            //clonedReminders.remove(old);
            //clonedReminders.add(updated);
        }
        dao.update(updated);
        this.setValues(clonedReminders);
        return true;
    }

}
