package com.example.stconnect.data.repository;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private final FirebaseAuth auth;
    private final MutableLiveData<AuthResult> authResult = new MutableLiveData<>();
    private final MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();

    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
        // Observar cambios en el usuario autenticado
        auth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            currentUser.setValue(user);
        });
    }

    public LiveData<AuthResult> signIn(String email, String password) {
        authResult.setValue(AuthResult.loading());

        // Validación
        if (TextUtils.isEmpty(email)) {
            authResult.setValue(AuthResult.error("El email es requerido"));
            return authResult;
        }
        if (TextUtils.isEmpty(password)) {
            authResult.setValue(AuthResult.error("La contraseña es requerida"));
            return authResult;
        }
        if (password.length() < 8) {
            authResult.setValue(AuthResult.error("La contraseña debe tener mínimo 8 caracteres"));
            return authResult;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            authResult.setValue(AuthResult.success(user.getUid()));
                        } else {
                            authResult.setValue(AuthResult.error("Usuario no encontrado"));
                        }
                    } else {
                        authResult.setValue(AuthResult.error("No perteneces a la institución"));
                    }
                })
                .addOnFailureListener(e -> {
                    authResult.setValue(AuthResult.error(e.getMessage()));
                });

        return authResult;
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        FirebaseUser user = auth.getCurrentUser();
        currentUser.setValue(user);
        return currentUser;
    }

    public void signOut() {
        auth.signOut();
    }

    // Clase interna para manejar resultados de autenticación
    public static class AuthResult {
        private final boolean success;
        private final String uid;
        private final String error;
        private final boolean loading;

        private AuthResult(boolean success, String uid, String error, boolean loading) {
            this.success = success;
            this.uid = uid;
            this.error = error;
            this.loading = loading;
        }

        public static AuthResult success(String uid) {
            return new AuthResult(true, uid, null, false);
        }

        public static AuthResult error(String error) {
            return new AuthResult(false, null, error, false);
        }

        public static AuthResult loading() {
            return new AuthResult(false, null, null, true);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getUid() {
            return uid;
        }

        public String getError() {
            return error;
        }

        public boolean isLoading() {
            return loading;
        }
    }
}

