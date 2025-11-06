package com.example.stconnect.ui.asistencia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.ScanOptions;
import com.journeyapps.barcodescanner.ScanContract;

public class ScanQRFragment extends Fragment {

    private final ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null)
                    procesarQR(result.getContents());
                else
                    Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr, container, false);
        view.findViewById(R.id.btnEscanear).setOnClickListener(v -> iniciarEscaneo());
        return view;
    }

    private void iniciarEscaneo() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Escanear código QR de asistencia");
        options.setBeepEnabled(true);
        qrLauncher.launch(options);
    }

    private void procesarQR(String qr) {
        String[] p = qr.split("\\|");
        if (p.length < 3) {
            Toast.makeText(getContext(), "QR inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        String ramo = normalizar(p[0]);
        String fecha = p[1];
        String hora = p[2];

        if (!dentroDeLos10Min(hora)) {
            Toast.makeText(getContext(), "Fuera de horario permitido", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance()
                .getReference("stconnect/usuarios/" + uid + "/nombre")
                .get()
                .addOnSuccessListener(snapshot -> {
                    String nombre = snapshot.getValue(String.class);
                    if (nombre == null) return;

                    FirebaseDatabase.getInstance()
                            .getReference("stconnect/asistencia/" + ramo + "/" + fecha + "/" + nombre)
                            .setValue("P")
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(getContext(), "Asistencia registrada", Toast.LENGTH_LONG).show()
                            );
                });
    }

    private boolean dentroDeLos10Min(String h) { return true; }

    private String normalizar(String t) {
        t = t.toLowerCase()
                .replace(" ", "")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");

        if (t.contains("programacionweb")) return "progweb";
        if (t.contains("basededatos")) return "basedatos";
        if (t.contains("matematicaaplicada")) return "matematica";
        if (t.contains("redesycomunicaciones")) return "redes";

        return t;
    }
}