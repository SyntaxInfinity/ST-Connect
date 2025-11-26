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
import com.example.stconnect.utils.NotificationScheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.util.Locale;

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

        askNotificationPermission();
        cargarEvaluacionesDesdeFirebase();

        return view;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission granted
                    Log.d("EvaluacionesFragment", "Notification permission granted");
                } else {
                    // Permission denied
                    Log.w("EvaluacionesFragment", "Notification permission denied");
                }
            });

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
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
                    String hora = evaluacionSnap.child("hora").getValue(String.class);

                    agregarCardEvaluacion(ramo, fecha, tipo, peso, hora);
                    programarNotificaciones(ramo, fecha, hora);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al leer evaluaciones", error.toException());
            }
        });
    }

    private void agregarCardEvaluacion(String ramo, String fecha, String tipo, String peso, String hora) {
        View cardView = LayoutInflater.from(context).inflate(R.layout.item_evaluacion, contenedorEvaluaciones, false);

        TextView tvRamo = cardView.findViewById(R.id.tvRamo);
        TextView tvFecha = cardView.findViewById(R.id.tvFecha);

        tvRamo.setText(ramo);
        tvFecha.setText(fecha);

        contenedorEvaluaciones.addView(cardView);

        View.OnClickListener listener = v -> mostrarDetallesDialog(ramo, tipo, peso, fecha, hora);
        cardView.setOnClickListener(listener);
    }

    private void mostrarDetallesDialog(String ramo, String tipo, String peso, String fecha, String hora) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_detalles_evaluaciones, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        ((TextView) dialogView.findViewById(R.id.txtPrueba)).setText("Asignatura: " + ramo);
        ((TextView) dialogView.findViewById(R.id.txtProfesor)).setText("Tipo: " + tipo);
        ((TextView) dialogView.findViewById(R.id.txtSala)).setText("Fecha: " + fecha);
        ((TextView) dialogView.findViewById(R.id.txtHora)).setText("Hora: " + hora);
        ((TextView) dialogView.findViewById(R.id.Txttemario)).setText("Peso: " + peso);

        dialogView.findViewById(R.id.btnCerrar).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    private void programarNotificaciones(String ramo, String fechaStr, String horaStr) {
        if (fechaStr == null || horaStr == null) return;

        // Formats: yyyy-MM-dd and HH:mm
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date fechaEvaluacion = sdf.parse(fechaStr + " " + horaStr);
            if (fechaEvaluacion == null) return;

            long tiempoEvaluacion = fechaEvaluacion.getTime();
            int uniqueId = (ramo + fechaStr + horaStr).hashCode();

            // 3 days before
            long tresDiasAntes = tiempoEvaluacion - (3 * 24 * 60 * 60 * 1000);
            NotificationScheduler.scheduleNotification(
                    context,
                    "Evaluación Próxima: " + ramo,
                    "Tienes una evaluación de " + ramo + " en 3 días.",
                    tresDiasAntes,
                    uniqueId + 1
            );

            // 3 hours before
            long tresHorasAntes = tiempoEvaluacion - (3 * 60 * 60 * 1000);
            NotificationScheduler.scheduleNotification(
                    context,
                    "Evaluación Inminente: " + ramo,
                    "Tu evaluación de " + ramo + " es en 3 horas.",
                    tresHorasAntes,
                    uniqueId + 2
            );
            
            Log.d("EvaluacionesFragment", "Scheduled for: " + ramo + " at " + fechaEvaluacion.toString());
            
            // Optional: Toast for debugging/verification (can be removed later)
            // android.widget.Toast.makeText(context, "Alarma programada para " + ramo, android.widget.Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            Log.e("EvaluacionesFragment", "Error parsing date/time for: " + ramo + " | " + fechaStr + " " + horaStr, e);
        }
    }
}
