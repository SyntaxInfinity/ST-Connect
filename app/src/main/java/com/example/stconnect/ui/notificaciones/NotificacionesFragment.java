package com.example.stconnect.ui.notificaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stconnect.R;
import com.example.stconnect.data.model.Notificacion;
import com.example.stconnect.ui.viewmodel.NotificacionesViewModel;

import java.util.List;

public class NotificacionesFragment extends Fragment {

    private NotificacionesAdapter adapter;
    private NotificacionesViewModel notificacionesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificaciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rvNotificaciones);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificacionesAdapter();
        rv.setAdapter(adapter);

        // Inicializar ViewModel
        notificacionesViewModel = new ViewModelProvider(this).get(NotificacionesViewModel.class);
        
        // Observar notificaciones
        observeNotificaciones();
        
        // Observar errores
        observeErrors();
    }

    private void observeNotificaciones() {
        notificacionesViewModel.getNotificaciones().observe(getViewLifecycleOwner(), notificaciones -> {
            if (notificaciones != null) {
                adapter.setData(notificaciones);
            }
        });
    }

    private void observeErrors() {
        notificacionesViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}