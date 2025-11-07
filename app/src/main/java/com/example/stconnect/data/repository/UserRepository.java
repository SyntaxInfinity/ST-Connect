package com.example.stconnect.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.stconnect.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {
    private final DatabaseReference databaseRef;

    public UserRepository() {
        databaseRef = FirebaseDatabase.getInstance().getReference("stconnect/usuarios");
    }

    public LiveData<User> getUserData(String uid) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            userLiveData.setValue(null);
            return userLiveData;
        }

        databaseRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        user.setUid(uid);
                        user.setEmail(firebaseUser.getEmail());
                    }
                    userLiveData.setValue(user);
                } else {
                    userLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                userLiveData.setValue(null);
            }
        });

        return userLiveData;
    }
}

