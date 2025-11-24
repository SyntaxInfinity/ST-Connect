package com.example.stconnect.ui.calificaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CalificacionesFragment extends Fragment {

    private LinearLayout contenedorCalificaciones;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calificaciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contenedorCalificaciones = view.findViewById(R.id.contenedorCalificaciones);
        cargarCalificaciones();
    }

    private void cargarCalificaciones() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("stconnect/calificaciones/" + uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contenedorCalificaciones.removeAllViews();

                if (!dataSnapshot.exists()) {
                    Toast.makeText(getContext(), "No hay calificaciones disponibles", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot asignaturaSnapshot : dataSnapshot.getChildren()) {
                    String ramo = asignaturaSnapshot.child("ramo").getValue(String.class);
                    if (ramo == null) continue;

                    List<Calificacion> notasAsignatura = new ArrayList<>();

                    for (DataSnapshot notaSnapshot : asignaturaSnapshot.getChildren()) {
                        if (notaSnapshot.getKey().equals("ramo")) continue;

                        Double notaVal = null;
                        Object notaObj = notaSnapshot.child("nota").getValue();
                        if (notaObj instanceof Long) {
                            notaVal = ((Long) notaObj).doubleValue();
                        } else if (notaObj instanceof Double) {
                            notaVal = (Double) notaObj;
                        } else if (notaObj instanceof String) {
                             try {
                                 notaVal = Double.parseDouble((String) notaObj);
                             } catch (NumberFormatException e) {
                                 continue;
                             }
                        }

                        String tipo = notaSnapshot.child("tipo").getValue(String.class);
                        String peso = notaSnapshot.child("peso").getValue(String.class);

                        if (notaVal != null) {
                            notasAsignatura.add(new Calificacion(ramo, notaVal, tipo, peso));
                        }
                    }

                    agregarVistaAsignatura(ramo, notasAsignatura);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error al leer la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarVistaAsignatura(String ramo, List<Calificacion> notas) {
        if (getContext() == null) return;

        View asignaturaView = LayoutInflater.from(getContext()).inflate(R.layout.item_calificacion_asignatura, contenedorCalificaciones, false);
        TextView tvAsignatura = asignaturaView.findViewById(R.id.tvAsignaturaNombre);
        LinearLayout contenedorNotas = asignaturaView.findViewById(R.id.contenedorNotas);

        tvAsignatura.setText(ramo);

        // Ocultar notas inicialmente
        contenedorNotas.setVisibility(View.GONE);

        for (Calificacion calificacion : notas) {
            View notaView = LayoutInflater.from(getContext()).inflate(R.layout.item_nota_individual, contenedorNotas, false);
            TextView tvNota = notaView.findViewById(R.id.tvNota);
            TextView tvTipo = notaView.findViewById(R.id.tvTipo);
            TextView tvPeso = notaView.findViewById(R.id.tvPeso);

            tvNota.setText("Nota: " + calificacion.getNota());
            tvTipo.setText("Tipo: " + (calificacion.getTipo() != null ? calificacion.getTipo() : "--"));
            tvPeso.setText("Peso: " + (calificacion.getPeso() != null ? calificacion.getPeso() : "--"));

            contenedorNotas.addView(notaView);
        }

        // Agregar listener para expandir/colapsar
        asignaturaView.setOnClickListener(v -> {
            boolean visible = contenedorNotas.getVisibility() == View.VISIBLE;
            if (!visible) {
                contenedorNotas.setAlpha(0f);
                contenedorNotas.setVisibility(View.VISIBLE);
                contenedorNotas.animate()
                        .alpha(1f)
                        .setDuration(250)
                        .start();
            } else {
                contenedorNotas.animate()
                        .alpha(0f)
                        .setDuration(200)
                        .withEndAction(() -> contenedorNotas.setVisibility(View.GONE))
                        .start();
            }
        });

        contenedorCalificaciones.addView(asignaturaView);
    }

    private static class Calificacion {
        private final String ramo;
        private final double nota;
        private final String tipo;
        private final String peso;

        public Calificacion(String ramo, double nota, String tipo, String peso) {
            this.ramo = ramo;
            this.nota = nota;
            this.tipo = tipo;
            this.peso = peso;
        }

        public String getRamo() { return ramo; }
        public double getNota() { return nota; }
        public String getTipo() { return tipo; }
        public String getPeso() { return peso; }
    }
}