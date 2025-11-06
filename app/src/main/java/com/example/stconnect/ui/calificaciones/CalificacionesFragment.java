package com.example.stconnect.ui.calificaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stconnect.R;
import com.google.android.material.card.MaterialCardView;
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

    private final List<Calificacion> calificaciones = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calificaciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarCalificaciones(view);

        for (int i = 1; i <= 8; i++) {
            int cardId = getResources().getIdentifier("card_asignatura_" + i, "id", requireContext().getPackageName());
            int notaId = getResources().getIdentifier("nota" + i, "id", requireContext().getPackageName());
            int tipoId = getResources().getIdentifier("tipo" + i, "id", requireContext().getPackageName());
            int pesoId = getResources().getIdentifier("peso" + i, "id", requireContext().getPackageName());

            MaterialCardView card = view.findViewById(cardId);
            TextView nota = view.findViewById(notaId);
            TextView tipo = view.findViewById(tipoId);
            TextView peso = view.findViewById(pesoId);

            if (card != null && nota != null && tipo != null && peso != null) {
                nota.setVisibility(View.GONE);
                tipo.setVisibility(View.GONE);
                peso.setVisibility(View.GONE);

                if (tipo.getText().toString().trim().isEmpty()) tipo.setText("Tipo: --");
                if (peso.getText().toString().trim().isEmpty()) peso.setText("Peso: --");

                card.setOnClickListener(v -> {
                    boolean visible = nota.getVisibility() == View.VISIBLE;
                    View[] detalles = {nota, tipo, peso};

                    for (View d : detalles) {
                        if (!visible) {
                            d.setAlpha(0f);
                            d.setVisibility(View.VISIBLE);
                            d.animate()
                                    .alpha(1f)
                                    .translationYBy(10f)
                                    .setDuration(250)
                                    .start();
                        } else {
                            d.animate()
                                    .alpha(0f)
                                    .translationYBy(-10f)
                                    .setDuration(200)
                                    .withEndAction(() -> d.setVisibility(View.GONE))
                                    .start();
                        }
                    }
                });
            }
        }
    }

    private void cargarCalificaciones(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("stconnect/calificaciones/" + uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                calificaciones.clear();

                if (!snapshot.exists()) {
                    Toast.makeText(getContext(), "No hay calificaciones disponibles", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String ramo = ds.child("ramo").getValue(String.class);
                    Double nota = ds.child("nota").getValue(Double.class);
                    String tipo = ds.child("tipo").getValue(String.class);
                    String peso = ds.child("peso").getValue(String.class);

                    if (ramo != null && nota != null && tipo != null && peso != null) {
                        calificaciones.add(new Calificacion(ramo, nota, tipo, peso));
                    }
                }

                mostrarCalificaciones(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al leer la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarCalificaciones(View view) {
        int[] asignaturas = {
                R.id.asignatura1, R.id.asignatura2, R.id.asignatura3, R.id.asignatura4,
                R.id.asignatura5, R.id.asignatura6, R.id.asignatura7, R.id.asignatura8
        };

        int[] notas = {
                R.id.nota1, R.id.nota2, R.id.nota3, R.id.nota4,
                R.id.nota5, R.id.nota6, R.id.nota7, R.id.nota8
        };

        int[] tipos = {
                R.id.tipo1, R.id.tipo2, R.id.tipo3, R.id.tipo4,
                R.id.tipo5, R.id.tipo6, R.id.tipo7, R.id.tipo8
        };

        int[] pesos = {
                R.id.peso1, R.id.peso2, R.id.peso3, R.id.peso4,
                R.id.peso5, R.id.peso6, R.id.peso7, R.id.peso8
        };

        for (int i = 0; i < asignaturas.length; i++) {
            TextView tvAsignatura = view.findViewById(asignaturas[i]);
            TextView tvNota = view.findViewById(notas[i]);
            TextView tvTipo = view.findViewById(tipos[i]);
            TextView tvPeso = view.findViewById(pesos[i]);

            if (i < calificaciones.size()) {
                Calificacion c = calificaciones.get(i);
                tvAsignatura.setText(c.getRamo());
                tvNota.setText("Nota: " + c.getNota());
                tvTipo.setText("Tipo: " + c.getTipo());
                tvPeso.setText("Peso: " + c.getPeso());
            } else {
                tvAsignatura.setText("Asignatura " + (i + 1));
                tvNota.setText("Nota: --");
                tvTipo.setText("Tipo: --");
                tvPeso.setText("Peso: --");
            }
        }
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