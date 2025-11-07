package com.example.stconnect.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.stconnect.data.model.Notificacion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesRepository {
    private final DatabaseReference databaseRef;
    private final MutableLiveData<List<Notificacion>> notificacionesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public NotificacionesRepository() {
        databaseRef = FirebaseDatabase.getInstance().getReference("stconnect/notificaciones");
    }

    public LiveData<List<Notificacion>> getNotificaciones() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            notificacionesLiveData.setValue(new ArrayList<>());
            return notificacionesLiveData;
        }

        databaseRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Notificacion> lista = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Notificacion n = ds.getValue(Notificacion.class);
                    if (n != null) {
                        lista.add(n);
                    }
                }
                notificacionesLiveData.setValue(lista);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                errorLiveData.setValue("Error al cargar notificaciones: " + error.getMessage());
                notificacionesLiveData.setValue(new ArrayList<>());
            }
        });

        return notificacionesLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }
}

