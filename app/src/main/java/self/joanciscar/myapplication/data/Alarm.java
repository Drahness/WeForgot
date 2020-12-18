package self.joanciscar.myapplication.data;

import java.time.LocalTime;

public class Alarm {
    private int id;
    private int dias;  // FLAGS
    private String nombre;
    private LocalTime hour;
    private int tono;

    public Alarm() {}
    public Alarm(int id, int dias, String nombre, LocalTime hour, int tono) {
        this.id = id;
        this.dias = dias;
        this.nombre = nombre;
        this.hour = hour;
        this.tono = tono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTono() {
        return tono;
    }

    public void setTono(int tono) {
        this.tono = tono;
    }
}
