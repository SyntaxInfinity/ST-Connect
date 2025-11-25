package com.example.stconnect.data.remote;

import com.example.stconnect.data.model.Asistencia;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("asistencia/registrar")
    Call<AsistenciaResponse> registrarAsistencia(@Body Asistencia asistencia);

    class AsistenciaResponse {
        public boolean success;
        public String message;
    }
}