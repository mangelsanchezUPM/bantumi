package es.upm.miw.bantumi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import es.upm.miw.bantumi.model.BantumiViewModel;
import es.upm.miw.bantumi.model.Puntuacion;
import es.upm.miw.bantumi.model.PuntuacionViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String FICH_GUARDADO = "partidaGuardada";
    protected final String LOG_TAG = "MiW";
    JuegoBantumi juegoBantumi;
    BantumiViewModel bantumiVM;
    int numInicialSemillas;
    String prefNombreJugador1;
    PuntuacionViewModel puntuacionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Instancia el ViewModel y el juego, y asigna observadores a los huecos
        numInicialSemillas = getResources().getInteger(R.integer.intNumInicialSemillas);
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);
        puntuacionViewModel = new ViewModelProvider(this).get(PuntuacionViewModel.class);
        juegoBantumi = new JuegoBantumi(bantumiVM, JuegoBantumi.Turno.turnoJ1, numInicialSemillas);
        crearObservadores();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        prefNombreJugador1 = sharedPref.getString("nombreJugador", getString(R.string.prefDefaultNombreJugador));
        ((TextView) findViewById(R.id.tvPlayer1)).setText(prefNombreJugador1);
    }

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero, actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    integer -> mostrarValor(finalI, juegoBantumi.getSemillas(finalI)));
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                turno -> marcarTurno(juegoBantumi.turnoActual())
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        TextView tvJugador2 = findViewById(R.id.tvPlayer2);
        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setTextColor(getColor(R.color.design_default_color_primary));
                tvJugador2.setTextColor(getColor(R.color.black));
                break;
            case turnoJ2:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.design_default_color_primary));
                break;
            default:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.black));
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posici??n <i>pos</i>
     *
     * @param pos   posici??n a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAjustes:
                startActivity(new Intent(this, BantumiPrefs.class));
                return true;
            case R.id.opcRecuperarPartida:
                recuperarPartida();
                return true;
            case R.id.opcGuardarPartida:
                guardarPartida();
                return true;
            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;
            case R.id.opcReiniciarPartida:
                new RestartAlertDialog().show(getSupportFragmentManager(), "ALERT_DIALOG");
                return true;
            case R.id.opcMejoresResultados:
                Intent intent = new Intent(MainActivity.this, MejoresPuntuacionesActivity.class);
                startActivity(intent);
            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }

    /**
     * Acci??n que se ejecuta al pulsar sobre un hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);
        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                juegaComputador();
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    /**
     * Elige una posici??n aleatoria del campo del jugador2 y realiza la siembra
     * Si mantiene turno -> vuelve a jugar
     */
    void juegaComputador() {
        while (juegoBantumi.turnoActual() == JuegoBantumi.Turno.turnoJ2) {
            int pos = 7 + (int) (Math.random() * 6);    // posici??n aleatoria
            Log.i(LOG_TAG, "juegaComputador(), pos=" + pos);
            if (juegoBantumi.getSemillas(pos) != 0 && (pos < 13)) {
                juegoBantumi.jugar(pos);
            } else {
                Log.i(LOG_TAG, "\t posici??n vac??a");
            }
        }
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    private void finJuego() {
        String texto = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? "Gana Jugador 1"
                : "Gana Jugador 2";
        Snackbar.make(
                findViewById(android.R.id.content),
                texto,
                Snackbar.LENGTH_LONG
        )
                .show();

        this.guardarPuntuacion();
        new FinalAlertDialog().show(getSupportFragmentManager(), "ALERT_DIALOG");
    }


    /**
     * Se guarda la partida al seleccionar la opci??n
     */
    private void guardarPartida() {
        try {
            FileOutputStream fos =
                    openFileOutput(FICH_GUARDADO, Context.MODE_PRIVATE);
            fos.write(this.juegoBantumi.serializa().getBytes());
            fos.close();
            Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.txtExitoGuardarPartida),
                    Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.txtErrorGuardarPartida),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Lee la partida guardada en el fichero de guardado
     */
    private void recuperarPartida() {
        try {
            BufferedReader fich = new BufferedReader(
                    new InputStreamReader(openFileInput(FICH_GUARDADO)));
            String partidaRecuperada = fich.readLine();
            fich.close();
            this.juegoBantumi.deserializa(partidaRecuperada);
            Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.txtExitoRecuperarPartida),
                    Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.txtErrorRecuperarPartida),
                    BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    /**
     * Se guarda la puntuaci??n del jugador
     */
    private void guardarPuntuacion() {
        Puntuacion puntuacion = new Puntuacion(prefNombreJugador1,
                bantumiVM.getNumSemillas(6).getValue(),
                bantumiVM.getNumSemillas(13).getValue());
        puntuacionViewModel.insert(puntuacion);
    }
}