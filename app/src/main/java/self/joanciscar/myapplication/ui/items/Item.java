package self.joanciscar.myapplication.ui.items;

import android.graphics.Bitmap;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import self.joanciscar.myapplication.data.Entity;

public class Item extends Entity<Long> {
    private Long id;
    private String name;
    private String foto; // URL
    private byte[] localfoto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != item.id) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (foto != null ? !foto.equals(item.foto) : item.foto != null) return false;
        return Arrays.equals(localfoto, item.localfoto);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (foto != null ? foto.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(localfoto);
        return result;
    }

    public Item() {}
    public Item(long id) {
        this.id = id;
    }
    public Item(long id, String name, String foto) {
        this.id = id;
        this.name = name;
        this.foto = foto;
    }

    public Item(long id, String name, byte[] foto) {
        this.id = id;
        this.name = name;
        this.localfoto = foto;
    }
    public Long getId() {
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

    public Uri getFotoAsUri() {
        return Uri.parse(this.foto);
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto.toString();
    }

    public byte[] getLocalfoto() {
        return localfoto;
    }

    public void setLocalfoto(byte[] localfoto) {
        this.localfoto = localfoto;
    }

    public void setLocalfoto(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        localfoto = stream.toByteArray();
        bitmap.recycle();
    }
    public Map<String,Object> toMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("foto",foto);
        map.put("name",name);
        map.put("id",id);
        return map;
    }

    public void fromMap(@NotNull Map<String,Object> map) {
        this.foto = (String) map.get("foto");
        this.name = (String) map.get("name");
        this.id = (long) map.get("id");
    }

}
