package com.example.stconnect.ui.horario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class HorarioFragment extends Fragment {

    private CalendarView calendarView;
    private DatabaseReference ref;
    private LinearLayout contenedorHorario;

    public HorarioFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_horario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        contenedorHorario = view.findViewById(R.id.contenedorHorario);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        ref = FirebaseDatabase.getInstance()
                .getReference("stconnect/horarios/" + user.getUid());

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
            String diaSemana = obtenerDiaDeFecha(year, month, dayOfMonth);
            cargarClasesDelDia(diaSemana, fechaSeleccionada);
        });
    }

    private void cargarClasesDelDia(String dia, String fecha) {
        contenedorHorario.removeAllViews();

        ref.child(dia).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    mostrarMensajeSinClases();
                    return;
                }

                Map<String, DataSnapshot> clasesOrdenadas = new TreeMap<>();
                for (DataSnapshot horaSnap : snapshot.getChildren()) {
                    clasesOrdenadas.put(horaSnap.getKey(), horaSnap);
                }

                List<String> horas = new ArrayList<>(clasesOrdenadas.keySet());
                Collections.sort(horas);

                MaterialCardView card = new MaterialCardView(getContext());
                card.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                card.setRadius(20);
                card.setCardElevation(8);
                card.setUseCompatPadding(true);
                card.setPadding(20, 20, 20, 20);

                LinearLayout layoutInterno = new LinearLayout(getContext());
                layoutInterno.setOrientation(LinearLayout.VERTICAL);

                TextView titulo = new TextView(getContext());
                titulo.setText("Clases del " + dia + " (" + fecha + ")");
                titulo.setTextSize(18);
                titulo.setTypeface(null, android.graphics.Typeface.BOLD);
                titulo.setPadding(0, 0, 0, 16);
                layoutInterno.addView(titulo);

                for (String hora : horas) {
                    DataSnapshot clase = clasesOrdenadas.get(hora);

                    String ramo = clase.child("ramo").getValue(String.class);
                    String sala = clase.child("sala").getValue(String.class);
                    String piso = String.valueOf(clase.child("piso").getValue());
                    String edificio = clase.child("edificio").getValue(String.class);

                    View fila = getLayoutInflater().inflate(R.layout.item_horario, layoutInterno, false);

                    TextView tvRamo = fila.findViewById(R.id.tvRamo);
                    TextView tvHora = fila.findViewById(R.id.tvHora);
                    TextView tvSala = fila.findViewById(R.id.tvSala);
                    TextView tvPiso = fila.findViewById(R.id.tvPiso);
                    TextView tvEdificio = fila.findViewById(R.id.tvEdificio);

                    tvRamo.setText(ramo);
                    tvHora.setText(hora);
                    tvSala.setText(sala);
                    tvPiso.setText(piso != null && !piso.equals("null") ? piso : "-");
                    tvEdificio.setText(edificio != null ? edificio : "-");

                    layoutInterno.addView(fila);
                }

                card.addView(layoutInterno);
                contenedorHorario.addView(card);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mostrarMensajeError();
            }
        });
    }

    private TextView crearTextView(String texto) {
        TextView tv = new TextView(getContext());
        tv.setText(texto);
        tv.setTextSize(14);
        tv.setTextColor(getResources().getColor(android.R.color.black));
        return tv;
    }


    private void mostrarMensajeSinClases() {
        TextView txt = new TextView(getContext());
        txt.setText("No hay clases registradas para este día");
        txt.setPadding(30, 30, 30, 30);
        contenedorHorario.addView(txt);
    }

    private void mostrarMensajeError() {
        TextView txt = new TextView(getContext());
        txt.setText("Error al cargar los horarios");
        txt.setPadding(30, 30, 30, 30);
        contenedorHorario.addView(txt);
    }

    private String obtenerDiaDeFecha(int year, int month, int day) {
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfSalida = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
            Date date = sdfEntrada.parse(day + "/" + (month + 1) + "/" + year);
            String dia = sdfSalida.format(date);
            dia = dia.substring(0, 1).toUpperCase() + dia.substring(1).replace("é", "e").replace("á", "a").replace("í", "i").replace("ó", "o").replace("ú", "u");
            return dia;
        } catch (Exception e) {
            return "Lunes";
        }
    }

}