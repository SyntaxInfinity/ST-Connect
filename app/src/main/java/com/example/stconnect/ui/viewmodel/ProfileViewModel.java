package com.example.stconnect.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.stconnect.data.model.LocationData;
import com.example.stconnect.data.model.User;
import com.example.stconnect.data.repository.LocationRepository;
import com.example.stconnect.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<LocationData> locationLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
        locationRepository = new LocationRepository(application);
    }

    public void loadUserData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            errorLiveData.setValue("Usuario no autenticado");
            return;
        }

        LiveData<User> userData = userRepository.getUserData(firebaseUser.getUid());
        // Observar cambios en los datos del usuario (una sola vez)
        userData.observeForever(user -> {
            if (user != null) {
                userLiveData.setValue(user);
            } else {
                errorLiveData.setValue("No se encontraron datos del usuario");
            }
        });
    }

    public void loadLocation() {
        LiveData<LocationData> location = locationRepository.getLastLocation();
        location.observeForever(locationData -> {
            if (locationData != null) {
                locationLiveData.setValue(locationData);
            }
        });

        LiveData<String> locationError = locationRepository.getError();
        locationError.observeForever(error -> {
            if (error != null && !error.isEmpty()) {
                errorLiveData.setValue(error);
            }
        });
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public LiveData<LocationData> getLocation() {
        return locationLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }
}

