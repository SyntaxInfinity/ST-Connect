package com.example.stconnect.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.stconnect.data.repository.AuthRepository;

public class LoginViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<AuthRepository.AuthResult> authResult = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public void login(String email, String password) {
        authRepository.signIn(email, password).observeForever(result -> {
            if (result != null) {
                authResult.setValue(result);
            }
        });
    }

    public LiveData<AuthRepository.AuthResult> getAuthResult() {
        return authResult;
    }
}

