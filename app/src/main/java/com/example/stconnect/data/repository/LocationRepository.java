package com.example.stconnect.data.repository;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.stconnect.data.model.LocationData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationRepository {
    private final FusedLocationProviderClient fusedLocationClient;
    private final Geocoder geocoder;
    private final MutableLiveData<LocationData> locationLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LocationRepository(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public LiveData<LocationData> getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        String address = getAddressFromLocation(lat, lon);
                        locationLiveData.setValue(new LocationData(address, lat, lon));
                    } else {
                        errorLiveData.setValue("Ubicación no disponible");
                    }
                })
                .addOnFailureListener(e -> {
                    errorLiveData.setValue("Error al obtener ubicación: " + e.getMessage());
                });

        return locationLiveData;
    }

    private String getAddressFromLocation(double lat, double lon) {
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String city = address.getLocality();
                String region = address.getAdminArea();
                String country = address.getCountryName();

                StringBuilder ubicacion = new StringBuilder();
                if (city != null) ubicacion.append(city);
                if (region != null && !region.equals(city)) {
                    if (ubicacion.length() > 0) ubicacion.append(", ");
                    ubicacion.append(region);
                }
                if (country != null) {
                    if (ubicacion.length() > 0) ubicacion.append(", ");
                    ubicacion.append(country);
                }

                return ubicacion.length() > 0 ? ubicacion.toString() : "Ubicación desconocida";
            } else {
                return "Ubicación desconocida (" + lat + ", " + lon + ")";
            }
        } catch (IOException e) {
            return "Error obteniendo dirección";
        }
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }
}

