package self.joanciscar.myapplication.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.logging.Logger;

public class Item {
    private long id;
    private String name;
    private String foto; // URL
    private Bitmap localfoto;

    public Item() {}
    public Item(long id) {
        this.id = id;
    }
    public Item(long id, String name, String foto) {
        this.id = id;
        this.name = name;
        this.foto = foto;
    }

    public Item(int id, String name, Bitmap foto) {
        this.id = id;
        this.name = name;
        this.localfoto = foto;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Bitmap getLocalfoto() {
        return localfoto;
    }

    public void setLocalfoto(Bitmap localfoto) {
        this.localfoto = localfoto;
    }

    public void setLocalfoto(byte[] localfoto) {
        if(localfoto != null) {
            this.localfoto = BitmapFactory.decodeByteArray(localfoto, 0 ,localfoto.length);
        }
    }

}
