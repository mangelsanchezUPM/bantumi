package es.upm.miw.bantumi.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Puntuacion.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PuntuacionRoomDatabase extends RoomDatabase {

    public static final String BASE_DATOS = Puntuacion.TABLA + ".db";

    public abstract PuntuacionDAO puntuacionDAO();

    private static volatile PuntuacionRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PuntuacionRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PuntuacionRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PuntuacionRoomDatabase.class, BASE_DATOS)
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Delete all content and repopulate the database whenever the app is started
    private static Callback sRoomDatabaseCallback =
            new Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);

                    // If you want to keep data through app restarts,
                    // comment out the following block
                    databaseWriteExecutor.execute(() -> {
                        // Populate the database in the background.
                        // If you want to start with more groups, just add them.
                        PuntuacionDAO dao = INSTANCE.puntuacionDAO();
                    });
                }
            };
}
