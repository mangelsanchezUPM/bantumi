package es.upm.miw.bantumi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.upm.miw.bantumi.model.PuntuacionViewModel;

public class MejoresPuntuacionesActivity extends AppCompatActivity {
    PuntuacionViewModel puntuacionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mejores_puntuaciones);
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final PuntuacionListAdapter adapter = new PuntuacionListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        puntuacionViewModel = new ViewModelProvider(this).get(PuntuacionViewModel.class);
        puntuacionViewModel.getAllPuntuacions().observe(this, puntuaciones -> adapter.setPuntuaciones(puntuaciones));
    }

}
