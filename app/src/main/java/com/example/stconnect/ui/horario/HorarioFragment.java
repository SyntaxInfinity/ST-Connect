package com.example.stconnect.ui.horario;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import com.example.stconnect.R;

public class HorarioFragment extends Fragment {

    private CalendarView calendarView;

    public HorarioFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_horario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
            mostrarDetallesHorario(fechaSeleccionada);
        });
    }

    private void mostrarDetallesHorario(String fecha) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_detalles_horario, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        TextView txtAsignatura = dialogView.findViewById(R.id.txtAsignatura);
        TextView txtProfesor = dialogView.findViewById(R.id.txtProfesor);
        TextView txtSala = dialogView.findViewById(R.id.txtSala);
        TextView txtHora = dialogView.findViewById(R.id.txtHora);
        Button btnCerrar = dialogView.findViewById(R.id.btnCerrar);

        if (fecha.equals("15/10/2025")) {
            txtAsignatura.setText("Asignatura: Programación Móvil");
            txtProfesor.setText("Profesor: Carlos Díaz");
            txtSala.setText("Sala: B-204");
            txtHora.setText("Hora: 10:00 - 11:30");
        } else if (fecha.equals("16/10/2025")) {
            txtAsignatura.setText("Asignatura: Redes de Computadores");
            txtProfesor.setText("Profesor: Ana Rojas");
            txtSala.setText("Sala: C-102");
            txtHora.setText("Hora: 09:00 - 10:30");
        } else {
            txtAsignatura.setText("No hay clases registradas");
            txtProfesor.setText("");
            txtSala.setText("");
            txtHora.setText("");
        }

        btnCerrar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
