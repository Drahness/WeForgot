package self.joanciscar.myapplication.ui.items;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import self.joanciscar.myapplication.data.GenericDAO;
import self.joanciscar.myapplication.data.Item;
import self.joanciscar.myapplication.data.ItemDAO;
import self.joanciscar.myapplication.data.Reminder;
import self.joanciscar.myapplication.data.ReminderDAO;
import self.joanciscar.myapplication.ui.AbstractViewModel;
import self.joanciscar.myapplication.utilities.Utils;

public class ItemsViewModel extends AbstractViewModel<Item,ItemDAO> {

    public ItemsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public ItemDAO getDao(Application app) {
        return new ItemDAO(app);
    }
}