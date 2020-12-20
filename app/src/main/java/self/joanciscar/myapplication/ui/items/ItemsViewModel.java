package self.joanciscar.myapplication.ui.items;

import android.app.Application;

import androidx.annotation.NonNull;

import self.joanciscar.myapplication.ui.AbstractViewModel;

public class ItemsViewModel extends AbstractViewModel<Item,ItemDAO> {

    public ItemsViewModel(@NonNull Application application) {
        super(application);

    }

    @Override
    public ItemDAO getDao(Application app, AbstractViewModel<Item, ItemDAO> vm) {
        return new ItemDAO(app,vm);
    }
}