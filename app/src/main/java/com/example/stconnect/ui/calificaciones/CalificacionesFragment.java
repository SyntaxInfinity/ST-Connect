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
import java.util.Arrays;
import java.util.List;

public class CalificacionesFragment extends Fragment {

    private List<String> asignaturas = Arrays.asList(
            "Matemáticas - 95 (A)",
            "Lenguaje - 88 (B+)",
            "Ciencias - 92 (A-)",
            "Historia - 85 (B)",
            "Inglés - 90 (A-)"
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
        MaterialCardView[] cards = {
                view.findViewById(R.id.card_asignatura_1),
                view.findViewById(R.id.card_asignatura_2),
                view.findViewById(R.id.card_asignatura_3),
                view.findViewById(R.id.card_asignatura_4),
                view.findViewById(R.id.card_asignatura_5)
        };

        for (int i = 0; i < Math.min(cards.length, asignaturas.size()); i++) {
            if (cards[i] != null) {
                final String asignaturaData = asignaturas.get(i);

                TextView textView = null;
                if (cards[i].getChildCount() > 0) {
                    View child = cards[i].getChildAt(0);
                    if (child instanceof TextView) {
                        textView = (TextView) child;
                    }
                }

                if (textView != null) {
                    textView.setText(asignaturaData.split(" - ")[0]);

                    int index = i;
                    cards[i].setOnClickListener(v -> {
                        mostrarDetalleCalificacion(asignaturaData);
                    });
                }
            }
        }
    }

    private void mostrarDetalleCalificacion(String asignaturaData) {
        String[] partes = asignaturaData.split(" - ");
        String mensaje = String.format("Asignatura: %s\nCalificación: %s", partes[0], partes[1]);

        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }
}