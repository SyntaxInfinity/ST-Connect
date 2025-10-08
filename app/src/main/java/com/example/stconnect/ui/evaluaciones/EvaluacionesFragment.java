package com.example.stconnect.ui.evaluaciones;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stconnect.R;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

public class EvaluacionesFragment extends Fragment {

    private List<Evaluacion> listaEvaluaciones;

    public EvaluacionesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_evaluaciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarDatos();
        configurarBotones(view);
    }

    private void inicializarDatos() {
        listaEvaluaciones = new ArrayList<>();

        // Datos de ejemplo - puedes reemplazar con datos reales
        listaEvaluaciones.add(new Evaluacion(
                "Integracion de Competencias",
                "15/09",
                "Evaluación sobre la integración de competencias profesionales"
        ));

        listaEvaluaciones.add(new Evaluacion(
                "Iot",
                "15/09",
                "Examen práctico de Internet de las Cosas"
        ));

        listaEvaluaciones.add(new Evaluacion(
                "Ingles Basico II",
                "15/09",
                "Examen de nivel básico de inglés"
        ));
    }

    private void configurarBotones(View view) {
        Button btnDetalles1 = view.findViewById(R.id.btn_detalles_1);
        if (btnDetalles1 != null) {
            btnDetalles1.setOnClickListener(v -> {
                if (listaEvaluaciones.size() > 0) {
                    mostrarDetallesEvaluacion(listaEvaluaciones.get(0));
                }
            });
        }

        Button btnDetalles2 = view.findViewById(R.id.btn_detalles_2);
        if (btnDetalles2 != null) {
            btnDetalles2.setOnClickListener(v -> {
                if (listaEvaluaciones.size() > 1) {
                    mostrarDetallesEvaluacion(listaEvaluaciones.get(1));
                }
            });
        }

        Button btnDetalles3 = view.findViewById(R.id.btn_detalles_3);
        if (btnDetalles3 != null) {
            btnDetalles3.setOnClickListener(v -> {
                if (listaEvaluaciones.size() > 2) {
                    mostrarDetallesEvaluacion(listaEvaluaciones.get(2));
                }
            });
        }

        configurarClicksEnCards(view);
    }

    private void configurarClicksEnCards(View view) {
        MaterialCardView card1 = view.findViewById(R.id.card_evaluacion_1);
        if (card1 != null) {
            card1.setOnClickListener(v -> {
                if (listaEvaluaciones.size() > 0) {
                    mostrarDetallesEvaluacion(listaEvaluaciones.get(0));
                }
            });
        }

        MaterialCardView card2 = view.findViewById(R.id.card_evaluacion_2);
        if (card2 != null) {
            card2.setOnClickListener(v -> {
                if (listaEvaluaciones.size() > 1) {
                    mostrarDetallesEvaluacion(listaEvaluaciones.get(1));
                }
            });
        }

        MaterialCardView card3 = view.findViewById(R.id.card_evaluacion_3);
        if (card3 != null) {
            card3.setOnClickListener(v -> {
                if (listaEvaluaciones.size() > 2) {
                    mostrarDetallesEvaluacion(listaEvaluaciones.get(2));
                }
            });
        }
    }

    private void mostrarDetallesEvaluacion(Evaluacion evaluacion) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_detalles_evaluaciones, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView txtPrueba = dialogView.findViewById(R.id.txtPrueba);
        TextView txtProfesor = dialogView.findViewById(R.id.txtProfesor);
        TextView txtSala = dialogView.findViewById(R.id.txtSala);
        Button btnCerrar = dialogView.findViewById(R.id.btnCerrar);

        txtPrueba.setText("Asignatura: " + evaluacion.getAsignatura());
        txtProfesor.setText("Profesor: Juan Pérez");
        txtSala.setText("Sala: B-203");

        btnCerrar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }



    public static class Evaluacion {
        private String asignatura;
        private String fecha;
        private String descripcion;

        public Evaluacion(String asignatura, String fecha, String descripcion) {
            this.asignatura = asignatura;
            this.fecha = fecha;
            this.descripcion = descripcion;
        }

        // Getters
        public String getAsignatura() { return asignatura; }
        public String getFecha() { return fecha; }
        public String getDescripcion() { return descripcion; }
    }
}