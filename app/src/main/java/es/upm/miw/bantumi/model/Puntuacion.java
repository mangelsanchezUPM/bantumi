package es.upm.miw.bantumi.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = Puntuacion.TABLA)
public class Puntuacion {
    static public final String TABLA = "puntuaciones";

    @PrimaryKey(autoGenerate = true)
    protected int uid;
    private String jugador;
    private Integer puntuacion;
    private Date fecha;

    public Puntuacion(String jugador, Integer puntuacion) {
        this.jugador = jugador;
        this.puntuacion = puntuacion;
        this.fecha = new Date();
    }

    public int getUid() { return uid; }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
