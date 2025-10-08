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
import com.google.android.material.card.MaterialCardView;

import java.util.Arrays;
import java.util.List;

public class CalificacionesFragment extends Fragment {

    private List<String> asignaturas = Arrays.asList(
            "Matemáticas - 95 (A)",
            "Lenguaje - 88 (B+)",
            "Ciencias - 92 (A-)",
            "Historia - 85 (B)",
            "Inglés - 90 (A-)",
            "Tecnologia - 96 - (A+)",
            "Musica - 35 - (F-)",
            "Ed fisica - 100 -(A+)"
    );

    public CalificacionesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calificaciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupCardsWithData(view);
    }

    private void setupCardsWithData(View view) {
        int[] cardIds = {
                R.id.card_asignatura_1,
                R.id.card_asignatura_2,
                R.id.card_asignatura_3,
                R.id.card_asignatura_4,
                R.id.card_asignatura_5,
                R.id.card_asignatura_6,
                R.id.card_asignatura_7,
                R.id.card_asignatura_8
        };

        int[] detailIds = {
                R.id.layout_detalle_1,
                R.id.layout_detalle_2,
                R.id.layout_detalle_3,
                R.id.layout_detalle_4,
                R.id.layout_detalle_5,
                R.id.layout_detalle_6,
                R.id.layout_detalle_7,
                R.id.layout_detalle_8
        };

        int[] notaIds = {
                R.id.tv_nota_1,
                R.id.tv_nota_2,
                R.id.tv_nota_3,
                R.id.tv_nota_4,
                R.id.tv_nota_5,
                R.id.tv_nota_6,
                R.id.tv_nota_7,
                R.id.tv_nota_8
        };

        for (int i = 0; i < asignaturas.size(); i++) {
            String asignaturaData = asignaturas.get(i);
            String[] partes = asignaturaData.split(" - ");
            String nombre = partes[0];
            String nota = partes[1];

            MaterialCardView card = view.findViewById(cardIds[i]);
            LinearLayout detalle = view.findViewById(detailIds[i]);
            TextView tvNota = view.findViewById(notaIds[i]);

            TextView tvTitulo = card.findViewById(
                    getResources().getIdentifier("tv_asignatura_" + (i + 1), "id", requireContext().getPackageName())
            );

            tvTitulo.setText(nombre);
            tvNota.setText("Calificación: " + nota);

            card.setOnClickListener(v -> toggleDetalle(detalle));
        }
    }

    private void toggleDetalle(View detalle) {
        if (detalle.getVisibility() == View.GONE) {
            // Expandir
            detalle.setVisibility(View.VISIBLE);
            detalle.setAlpha(0f);
            detalle.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start();
        } else {
            // Colapsar
            detalle.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> detalle.setVisibility(View.GONE))
                    .start();
        }
    }
}