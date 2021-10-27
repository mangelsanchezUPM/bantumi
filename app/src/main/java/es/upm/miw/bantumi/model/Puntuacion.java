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
    private Integer puntuacionJugador;
    private Integer puntuacionCPU;
    private Date fecha;

    public Puntuacion(String jugador, Integer puntuacionJugador, Integer puntuacionCPU) {
        this.jugador = jugador;
        this.puntuacionJugador = puntuacionJugador;
        this.puntuacionCPU = puntuacionCPU;
        fecha = new Date();
    }

    public int getUid() {
        return uid;
    }

    public String getJugador() {
        return jugador;
    }

    public Integer getPuntuacionJugador() {
        return puntuacionJugador;
    }

    public Integer getPuntuacionCPU() {
        return puntuacionCPU;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public void setPuntuacionJugador(Integer puntuacionJugador) {
        this.puntuacionJugador = puntuacionJugador;
    }

    public void setPuntuacionCPU(Integer puntuacionCPU) {
        this.puntuacionCPU = puntuacionCPU;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

