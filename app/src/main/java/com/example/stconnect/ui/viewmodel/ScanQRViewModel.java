package com.example.stconnect.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.stconnect.data.repository.AsistenciaRepository;

public class ScanQRViewModel extends AndroidViewModel {

    private final AsistenciaRepository asistenciaRepository;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public ScanQRViewModel(@NonNull Application application) {
        super(application);
        asistenciaRepository = new AsistenciaRepository();
    }

    public void procesarQR(String qrContent) {
        String[] partes = qrContent.split("\\|");
        if (partes.length < 3) {
            errorLiveData.setValue("QR inválido");
            return;
        }

        String ramo = normalizar(partes[0]);
        String fecha = partes[1];
        String hora = partes[2];

        if (!dentroDeLos10Min(hora)) {
            errorLiveData.setValue("Fuera de horario permitido");
            return;
        }

        LiveData<AsistenciaRepository.AsistenciaResult> result =
                asistenciaRepository.registrarAsistencia(ramo, fecha, hora);

        Observer<AsistenciaRepository.AsistenciaResult> observer = new Observer<AsistenciaRepository.AsistenciaResult>() {
            @Override
            public void onChanged(AsistenciaRepository.AsistenciaResult asistenciaResult) {
                if (asistenciaResult != null) {
                    if (asistenciaResult.isSuccess()) {
                        messageLiveData.setValue(asistenciaResult.getMessage());
                    } else {
                        errorLiveData.setValue(asistenciaResult.getError());
                    }
                }
                result.removeObserver(this);
            }
        };

        result.observeForever(observer);
    }

    private boolean dentroDeLos10Min(String hora) {
        return true;
    }

    private String normalizar(String texto) {
        texto = texto.toLowerCase()
                .replace(" ", "")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");

        if (texto.contains("programacionweb")) return "progweb";
        if (texto.contains("basededatos")) return "basedatos";
        if (texto.contains("matematicaaplicada")) return "matematica";
        if (texto.contains("redesycomunicaciones")) return "redes";

        return texto;
    }

    public LiveData<String> getMessage() {
        return messageLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }
}