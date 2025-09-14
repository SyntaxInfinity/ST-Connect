package com.example.stconnect.ui.calificaciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stconnect.ui.calificaciones.CalificacionesActivity;
import com.example.stconnect.databinding.FragmentCalificacionesBinding;

public class CalificacionesFragment extends Fragment {

    private FragmentCalificacionesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalificacionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), CalificacionesActivity.class))
        );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
