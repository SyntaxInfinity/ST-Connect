package com.example.stconnect.ui.notificaciones;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stconnect.R;
import com.example.stconnect.data.model.Notificacion;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.ViewHolder> {

    private final List<Notificacion> lista = new ArrayList<>();

    public void setData(List<Notificacion> nuevas) {
        lista.clear();
        lista.addAll(nuevas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notificaciones, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        Notificacion n = lista.get(pos);

        holder.titulo.setText(n.getTitulo() != null ? n.getTitulo() : "");
        holder.mensaje.setText(n.getMensaje() != null ? n.getMensaje() : "");
        holder.fecha.setText(n.getFecha() != null ? n.getFecha() : "");
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, mensaje, fecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tvTitulo);
            mensaje = itemView.findViewById(R.id.tvMensaje);
            fecha = itemView.findViewById(R.id.tvFecha);
        }
    }
}