package com.example.stconnect.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.stconnect.CalificacionesActivity;
import com.example.stconnect.databinding.FragmentCalificacionesBinding;

public class CalificacionesFragment extends Fragment {

    private FragmentCalificacionesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentCalificacionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Abrir la Activity al tocar el fragment
        root.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CalificacionesActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
