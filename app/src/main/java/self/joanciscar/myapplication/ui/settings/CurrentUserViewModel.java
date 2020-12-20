package self.joanciscar.myapplication.ui.settings;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import self.joanciscar.myapplication.utilities.Utils;

public class CurrentUserViewModel extends ViewModel {
    private final MutableLiveData<String> usuario;
    private final MutableLiveData<String> email;
    private final MutableLiveData<Uri> photoUrl;
    private final MutableLiveData<String> uuid;

    public CurrentUserViewModel() {
        uuid = new MutableLiveData<>();
        usuario = new MutableLiveData<>();
        email = new MutableLiveData<>();
        photoUrl = new MutableLiveData<>();
    }

    public void setUser(FirebaseUser user) {
        String suuid;
        String susuario;
        String semail;
        Uri pic;
        if(user == null) {
            suuid = "";
            susuario = "Anonymous";
            semail = "";
            pic = null;
        } else {
            suuid = user.getUid();
            susuario = user.getDisplayName();
            semail = user.getEmail();
            pic = user.getPhotoUrl();
        }
        if(Utils.isUiThread()) {
            usuario.setValue(susuario);
            email.setValue(semail);
            photoUrl.setValue(pic);
            uuid.setValue(suuid);
        } else {
            usuario.postValue(susuario);
            email.postValue(semail);
            photoUrl.postValue(pic);
            uuid.postValue(suuid);
        }
    }

    public MutableLiveData<String> getUsuario() {
        return usuario;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<Uri> getPhotoUrl() {
        return photoUrl;
    }

    public MutableLiveData<String> getUuid() {
        return uuid;
    }
}
