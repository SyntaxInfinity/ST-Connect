package com.example.stconnect.ui.evaluaciones;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class EvaluacionesFragment extends Fragment {

    private LinearLayout contenedorEvaluaciones;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluaciones, container, false);
        contenedorEvaluaciones = view.findViewById(R.id.contenedorEvaluaciones);
        context = requireContext();

        cargarEvaluacionesDesdeFirebase();

        return view;
    }

    private void cargarEvaluacionesDesdeFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("stconnect/evaluaciones")
                .child(user.getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contenedorEvaluaciones.removeAllViews();

                if (!snapshot.exists()) {
                    TextView sinDatos = new TextView(context);
                    sinDatos.setText("No hay evaluaciones registradas");
                    sinDatos.setTextSize(18);
                    sinDatos.setPadding(20, 20, 20, 20);
                    contenedorEvaluaciones.addView(sinDatos);
                    return;
                }

                for (DataSnapshot evaluacionSnap : snapshot.getChildren()) {
                    String ramo = evaluacionSnap.child("ramo").getValue(String.class);
                    String fecha = evaluacionSnap.child("fecha").getValue(String.class);
                    String tipo = evaluacionSnap.child("tipo").getValue(String.class);
                    String peso = evaluacionSnap.child("peso").getValue(String.class);

                    agregarCardEvaluacion(ramo, fecha, tipo, peso);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al leer evaluaciones", error.toException());
            }
        });
    }

    private void agregarCardEvaluacion(String ramo, String fecha, String tipo, String peso) {
        MaterialCardView card = new MaterialCardView(context);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) card.getLayoutParams()).setMargins(0, 0, 0, 24);
        card.setCardElevation(6);
        card.setRadius(18);
        card.setUseCompatPadding(true);

        LinearLayout layoutInterno = new LinearLayout(context);
        layoutInterno.setOrientation(LinearLayout.HORIZONTAL);
        layoutInterno.setPadding(20, 20, 20, 20);
        layoutInterno.setGravity(android.view.Gravity.CENTER_VERTICAL);

        TextView tvRamo = new TextView(context);
        tvRamo.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        tvRamo.setText(ramo);
        tvRamo.setTextSize(18);
        tvRamo.setTypeface(null, android.graphics.Typeface.BOLD);
        tvRamo.setTextColor(getResources().getColor(R.color.black));

        TextView tvFecha = new TextView(context);
        tvFecha.setText(fecha);
        tvFecha.setTextColor(getResources().getColor(android.R.color.darker_gray));
        tvFecha.setPadding(0, 0, 20, 0);

        Button btnDetalles = new Button(context);
        btnDetalles.setText("DETALLES");
        btnDetalles.setTextColor(getResources().getColor(android.R.color.white));
        btnDetalles.setBackgroundTintList(context.getColorStateList(R.color.green_400));

        layoutInterno.addView(tvRamo);
        layoutInterno.addView(tvFecha);
        layoutInterno.addView(btnDetalles);

        card.addView(layoutInterno);
        contenedorEvaluaciones.addView(card);

        View.OnClickListener listener = v -> mostrarDetallesDialog(ramo, tipo, peso, fecha);
        btnDetalles.setOnClickListener(listener);
        card.setOnClickListener(listener);
    }

    private void mostrarDetallesDialog(String ramo, String tipo, String peso, String fecha) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_detalles_evaluaciones, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        ((TextView) dialogView.findViewById(R.id.txtPrueba)).setText("Asignatura: " + ramo);
        ((TextView) dialogView.findViewById(R.id.txtProfesor)).setText("Tipo: " + tipo);
        ((TextView) dialogView.findViewById(R.id.txtSala)).setText("Fecha: " + fecha);
        ((TextView) dialogView.findViewById(R.id.Txttemario)).setText("Peso: " + peso);

        dialogView.findViewById(R.id.btnCerrar).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
