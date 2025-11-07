package com.example.stconnect.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stconnect.data.model.Notificacion;
import com.example.stconnect.data.repository.NotificacionesRepository;

import java.util.List;

public class NotificacionesViewModel extends AndroidViewModel {
    private final NotificacionesRepository notificacionesRepository;
    private final LiveData<List<Notificacion>> notificaciones;
    private final LiveData<String> error;

    public NotificacionesViewModel(@NonNull Application application) {
        super(application);
        notificacionesRepository = new NotificacionesRepository();
        notificaciones = notificacionesRepository.getNotificaciones();
        error = notificacionesRepository.getError();
    }

    public LiveData<List<Notificacion>> getNotificaciones() {
        return notificaciones;
    }

    public LiveData<String> getError() {
        return error;
    }
}

