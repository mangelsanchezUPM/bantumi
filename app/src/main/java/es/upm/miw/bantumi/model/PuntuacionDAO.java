package es.upm.miw.bantumi.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PuntuacionDAO {
    @Query("SELECT * FROM " + Puntuacion.TABLA)
    LiveData<List<Puntuacion>> getAll();

    @Query("SELECT * FROM " + Puntuacion.TABLA + " ORDER BY MAX(puntuacionJugador, puntuacionCPU) DESC LIMIT 10")
    LiveData<List<Puntuacion>> getMejoresPuntuaciones();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Puntuacion puntuacion);

    @Query("DELETE FROM " + Puntuacion.TABLA)
    void deleteAll();

    @Delete
    void delete(Puntuacion puntuacion);
}
