package com.example.stconnect.ui.profile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import com.example.stconnect.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private TextView profileName, profileEmail, profileGps, profileRut, profileCarrera;
    private FusedLocationProviderClient fusedLocationClient;
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Pedir permiso de ubicación si no está otorgado
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLastLocation();
        }

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("stconnect/usuarios/" + uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String rut = snapshot.child("rut").getValue(String.class);
                    String carrera = snapshot.child("carrera").getValue(String.class);
                    String correo = user.getEmail(); // viene del login

                    profileName.setText(nombre != null ? nombre : "—");
                    profileEmail.setText(correo != null ? correo : "—");
                    profileRut.setText(rut != null ? rut : "—");
                    profileCarrera.setText(carrera != null ? carrera : "—");
                } else {
                    Toast.makeText(getContext(), "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al conectar con Realtime Database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                String city = address.getLocality();
                                String region = address.getAdminArea();
                                String country = address.getCountryName();

                                String ubicacion = "Ubicación: ";
                                if (city != null) ubicacion += city;
                                if (region != null && !region.equals(city)) ubicacion += ", " + region;
                                if (country != null) ubicacion += ", " + country;

                                profileGps.setText(ubicacion);
                            } else {
                                profileGps.setText("Ubicación desconocida (" + lat + ", " + lon + ")");
                            }
                        } catch (IOException e) {
                            profileGps.setText("Error obteniendo dirección");
                            e.printStackTrace();
                        }
                    } else {
                        profileGps.setText("Ubicación no disponible");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                if (profileGps != null)
                    profileGps.setText("Permiso de ubicación denegado");
            }
        }
    }
}
