package com.example.stconnect.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.stconnect.data.model.Asistencia;
import com.example.stconnect.data.remote.ApiClient;
import com.example.stconnect.data.remote.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsistenciaRepository {

    public LiveData<AsistenciaResult> registrarAsistencia(String nombre, String rut, String estado) {
        MutableLiveData<AsistenciaResult> data = new MutableLiveData<>();

        Asistencia asistencia = new Asistencia(nombre, rut, estado);

        ApiService apiService = ApiClient.getApiService();
        Call<ApiService.AsistenciaResponse> call = apiService.registrarAsistencia(asistencia);

        call.enqueue(new Callback<ApiService.AsistenciaResponse>() {
            @Override
            public void onResponse(Call<ApiService.AsistenciaResponse> call, Response<ApiService.AsistenciaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiService.AsistenciaResponse body = response.body();

                    if (body.success) {
                        data.postValue(new AsistenciaResult(true, body.message, null));
                    } else {
                        data.postValue(new AsistenciaResult(false, null, body.message));
                    }
                } else {
                    data.postValue(new AsistenciaResult(false, null, "Error en el servidor"));
                }
            }

            @Override
            public void onFailure(Call<ApiService.AsistenciaResponse> call, Throwable t) {
                data.postValue(new AsistenciaResult(false, null, "No hay conexión con el servidor"));
            }
        });

        return data;
    }

    public static class AsistenciaResult {
        private final boolean success;
        private final String message;
        private final String error;

        public AsistenciaResult(boolean success, String message, String error) {
            this.success = success;
            this.message = message;
            this.error = error;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getError() { return error; }
    }
}