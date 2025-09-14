package com.example.stconnect.ui.evaluaciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stconnect.ui.evaluaciones.EvaluacionesActivity;
import com.example.stconnect.databinding.FragmentEvaluacionesBinding;

public class EvaluacionesFragment extends Fragment {

    private FragmentEvaluacionesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEvaluacionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), EvaluacionesActivity.class))
        );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
