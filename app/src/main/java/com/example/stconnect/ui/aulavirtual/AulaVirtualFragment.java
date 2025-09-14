package com.example.stconnect.ui.aulavirtual;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stconnect.ui.aulavirtual.AulaVirtualActivity;
import com.example.stconnect.databinding.FragmentWebBinding;

public class AulaVirtualFragment extends Fragment {

    private FragmentWebBinding binding; // <- usa FragmentWebBinding porque tu XML se llama fragment_web.xml

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWebBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Al hacer click en el fragment, abre AulaVirtualActivity
        root.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AulaVirtualActivity.class))
        );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
