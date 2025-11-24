package com.example.stconnect.ui.profile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.stconnect.R;
import com.example.stconnect.data.model.LocationData;
import com.example.stconnect.data.model.User;
import com.example.stconnect.ui.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private TextView profileName, profileEmail, profileGps, profileRut, profileCarrera, profileNumero;
    private ProfileViewModel profileViewModel;
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    public ProfileFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        
        // Inicializar ViewModel
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        
        // Observar datos del usuario
        observeUserData();
        
        // Observar ubicación
        observeLocation();
        
        // Observar errores
        observeErrors();

        // Pedir permiso de ubicación si no está otorgado
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            profileViewModel.loadLocation();
        }

        // Cargar datos del perfil
        profileViewModel.loadUserData();
    }

    private void initViews(View view) {
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileGps = view.findViewById(R.id.profileGps);
        profileRut = view.findViewById(R.id.profileRut);
        profileCarrera = view.findViewById(R.id.profileCarrera);
        profileNumero = view.findViewById(R.id.numeroTelefono);
    }

    private void observeUserData() {
        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                profileName.setText(user.getNombre() != null ? user.getNombre() : "—");
                profileEmail.setText(user.getEmail() != null ? user.getEmail() : "—");
                profileRut.setText(user.getRut() != null ? user.getRut() : "—");
                profileCarrera.setText(user.getCarrera() != null ? user.getCarrera() : "—");
                profileNumero.setText(user.getNumero() != null ? user.getNumero() : "—");
            }
        });
    }

    private void observeLocation() {
        profileViewModel.getLocation().observe(getViewLifecycleOwner(), locationData -> {
            if (locationData != null && locationData.getAddress() != null) {
                profileGps.setText("Ubicación: " + locationData.getAddress());
            }
        });
    }

    private void observeErrors() {
        profileViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                profileViewModel.loadLocation();
            } else {
                if (profileGps != null)
                    profileGps.setText("Permiso de ubicación denegado");
            }
        }
    }
}
