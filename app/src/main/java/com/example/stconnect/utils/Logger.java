package com.example.stconnect.utils;

import com.example.stconnect.data.model.LogEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Logger {

    public static void logAction(String action) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("stconnect/usuarios/" + user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String userName = "Desconocido";
                if (snapshot.exists() && snapshot.child("nombre").getValue() != null) {
                    userName = snapshot.child("nombre").getValue(String.class);
                }
                
                writeLog(user.getUid(), userName, action);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                writeLog(user.getUid(), "Error obteniendo nombre", action);
            }
        });
    }

    private static void writeLog(String userId, String userName, String action) {
        DatabaseReference logsRef = FirebaseDatabase.getInstance().getReference("stconnect/logs");
        String logId = logsRef.push().getKey();
        
        if (logId != null) {
            LogEntry entry = new LogEntry(userId, userName, action, System.currentTimeMillis());
            entry.setId(logId);
            logsRef.child(logId).setValue(entry);
        }
    }
}
