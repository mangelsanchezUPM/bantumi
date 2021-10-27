package es.upm.miw.bantumi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.miw.bantumi.model.Puntuacion;

public class PuntuacionListAdapter extends RecyclerView.Adapter<PuntuacionListAdapter.PuntuacionViewHolder> {

    class PuntuacionViewHolder extends RecyclerView.ViewHolder {
        private final TextView jugadorTV;
        private final TextView puntuacionJ1TV;
        private final TextView puntuacionJ2TV;
        private final TextView fechaTV;

        private PuntuacionViewHolder(View itemView) {
            super(itemView);
            jugadorTV = itemView.findViewById(R.id.nombreJugadortv);
            puntuacionJ1TV = itemView.findViewById(R.id.puntuacionJ1tv);
            puntuacionJ2TV = itemView.findViewById(R.id.puntuacionJ2tv);
            fechaTV = itemView.findViewById(R.id.fechatv);
        }
    }

    private final LayoutInflater mInflater;
    private List<Puntuacion> mPuntuaciones;

    public PuntuacionListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PuntuacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PuntuacionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PuntuacionViewHolder holder, int position) {
        if (mPuntuaciones != null) {
            Puntuacion current = mPuntuaciones.get(position);
            holder.jugadorTV.setText("Nombre: " + current.getJugador());
            holder.puntuacionJ1TV.setText("JUG: " + current.getPuntuacionJugador().toString());
            holder.puntuacionJ2TV.setText("CPU: " + current.getPuntuacionCPU().toString());
            holder.fechaTV.setText("Fecha: " + current.getFecha().toString());
        } else {
            // Covers the case of data not being ready yet.
        }
    }

    public void setPuntuaciones(List<Puntuacion> puntuaciones){
        mPuntuaciones = puntuaciones;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mPuntuaciones == null)
                ? 0
                : mPuntuaciones.size();
    }

    public Puntuacion getPuntuacionAtPosition (int position) {
        return mPuntuaciones.get(position);
    }
}
