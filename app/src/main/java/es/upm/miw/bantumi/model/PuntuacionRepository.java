package es.upm.miw.bantumi.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PuntuacionRepository {
    private PuntuacionDAO mPuntuacionDao;
    private LiveData<List<Puntuacion>> mPuntuaciones;

    /**
     * Constructor
     *
     * @param application app
     */
    public PuntuacionRepository(Application application) {
        PuntuacionRoomDatabase db = PuntuacionRoomDatabase.getDatabase(application);
        mPuntuacionDao = db.puntuacionDAO();
        mPuntuaciones = mPuntuacionDao.getAll();
    }

    public LiveData<List<Puntuacion>> getAllPuntuaciones() {
        return mPuntuaciones;
    }

    public long insert(Puntuacion puntuacion) {
        return mPuntuacionDao.insert(puntuacion);
    }

    public void deleteAll() {
        mPuntuacionDao.deleteAll();
    }

    public void deletePuntuacion(Puntuacion puntuacion) {
        mPuntuacionDao.delete(puntuacion);
    }
}
