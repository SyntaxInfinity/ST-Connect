package com.example.stconnect.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stconnect.R;

public class ProfileFragment extends Fragment {
    private TextView profileName, profileEmail, profileGps, profileRut, profileCarrera;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        loadProfileData();
    }

    private void initViews(View view) {
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileGps = view.findViewById(R.id.profileGps);
        profileRut = view.findViewById(R.id.profileRut);
        profileCarrera = view.findViewById(R.id.profileCarrera);
    }

    private void loadProfileData() {

        String nombre = "Juan Pérez";
        String email = "juan.perez@correo.com";
        String gps = "CFT SANTO TOMAS RANCAGUA";
        String rut = "152203245-5";
        String carrera = "EN INFORMÁTICA";

        profileName.setText(nombre);
        profileEmail.setText(email);
        profileGps.setText(gps);
        profileRut.setText(rut);
        profileCarrera.setText(carrera);
    }
}
