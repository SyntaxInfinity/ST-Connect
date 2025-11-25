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
import androidx.lifecycle.ViewModelProvider;

import com.example.stconnect.R;
import com.example.stconnect.ui.viewmodel.ScanQRViewModel;
import com.journeyapps.barcodescanner.ScanOptions;
import com.journeyapps.barcodescanner.ScanContract;

public class ScanQRFragment extends Fragment {

    private ScanQRViewModel scanQRViewModel;
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
        
        scanQRViewModel = new ViewModelProvider(this).get(ScanQRViewModel.class);
        
        observeResults();
        
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
        scanQRViewModel.procesarQR(qr);
    }

    private void observeResults() {
        scanQRViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        scanQRViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}