package com.example.stconnect.ui.notificaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesFragment extends Fragment {

    private NotificacionesAdapter adapter;

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

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("stconnect/notificaciones/" + user.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Notificacion> lista = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Notificacion n = ds.getValue(Notificacion.class);
                    if (n != null) lista.add(n);
                }

                adapter.setData(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar notificaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}