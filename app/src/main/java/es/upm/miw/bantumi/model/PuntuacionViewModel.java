package es.upm.miw.bantumi.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PuntuacionViewModel extends AndroidViewModel {

    private PuntuacionRepository mRepository;

    private LiveData<List<Puntuacion>> mAllPuntuaciones;

    public PuntuacionViewModel(Application application) {
        super(application);
        mRepository = new PuntuacionRepository(application);
        mAllPuntuaciones = mRepository.getAllPuntuaciones();
    }

    public LiveData<List<Puntuacion>> getAllPuntuacions() {
        return mAllPuntuaciones;
    }

    public void insert(Puntuacion puntuacion) {
        mRepository.insert(puntuacion);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deletePuntuacion(Puntuacion puntuacion) {
        mRepository.deletePuntuacion(puntuacion);
    }
}
