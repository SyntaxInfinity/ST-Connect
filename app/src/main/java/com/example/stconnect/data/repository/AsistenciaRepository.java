package com.example.stconnect.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AsistenciaRepository {
    private final DatabaseReference databaseRef;
    private final MutableLiveData<AsistenciaResult> asistenciaResult = new MutableLiveData<>();

    public AsistenciaRepository() {
        databaseRef = FirebaseDatabase.getInstance().getReference("stconnect");
    }

    public LiveData<AsistenciaResult> registrarAsistencia(String ramo, String fecha, String nombre) {
        asistenciaResult.setValue(AsistenciaResult.loading());

        databaseRef.child("usuarios")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("nombre")
                .get()
                .addOnSuccessListener(snapshot -> {
                    String nombreUsuario = snapshot.getValue(String.class);
                    if (nombreUsuario == null) {
                        asistenciaResult.setValue(AsistenciaResult.error("No se encontró el nombre del usuario"));
                        return;
                    }

                    databaseRef.child("asistencia")
                            .child(ramo)
                            .child(fecha)
                            .child(nombreUsuario)
                            .setValue("P")
                            .addOnSuccessListener(unused -> {
                                asistenciaResult.setValue(AsistenciaResult.success("Asistencia registrada"));
                            })
                            .addOnFailureListener(e -> {
                                asistenciaResult.setValue(AsistenciaResult.error("Error al registrar asistencia: " + e.getMessage()));
                            });
                })
                .addOnFailureListener(e -> {
                    asistenciaResult.setValue(AsistenciaResult.error("Error al obtener datos del usuario: " + e.getMessage()));
                });

        return asistenciaResult;
    }

    // Clase interna para manejar resultados
    public static class AsistenciaResult {
        private final boolean success;
        private final String message;
        private final String error;
        private final boolean loading;

        private AsistenciaResult(boolean success, String message, String error, boolean loading) {
            this.success = success;
            this.message = message;
            this.error = error;
            this.loading = loading;
        }

        public static AsistenciaResult success(String message) {
            return new AsistenciaResult(true, message, null, false);
        }

        public static AsistenciaResult error(String error) {
            return new AsistenciaResult(false, null, error, false);
        }

        public static AsistenciaResult loading() {
            return new AsistenciaResult(false, null, null, true);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getError() {
            return error;
        }

        public boolean isLoading() {
            return loading;
        }
    }
}

