package com.example.stconnect.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stconnect.R;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView profileImage = root.findViewById(R.id.profile_image);
        TextView profileName = root.findViewById(R.id.profile_name);
        TextView profileEmail = root.findViewById(R.id.profile_email);

        profileName.setText("Alex Ponce");
        profileEmail.setText("alex.ponce@ejemplo.com");

        return root;
    }
}
