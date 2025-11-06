package com.example.stconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPass;
    private Button btnlogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etEmail = findViewById(R.id.TxtEmail);
        etPass = findViewById(R.id.TxtPassword);
        auth = FirebaseAuth.getInstance();
        btnlogin=findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(v -> iniciarSesion());
    }
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = auth.getCurrentUser();
//        if (user != null) {
//            irAMain();
//        }
//    }
private void iniciarSesion() {
    String email = etEmail.getText().toString().trim();
    String pass  = etPass.getText().toString().trim();

    if (!validar(email, pass)) return;

    auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();

                        getSharedPreferences("Usuario", MODE_PRIVATE)
                                .edit()
                                .putString("uid", uid)
                                .apply();

                        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        irAMain();
                    }
                } else {
                    Toast.makeText(this, "No perteneces a la institución",
                            Toast.LENGTH_LONG).show();
                }
            });
}

    private boolean validar(String email, String pass) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Requerido");
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(pass)) {
            etPass.setError("Requerido");
            etPass.requestFocus();
            return false;
        }
        if (pass.length() < 8) {
            etPass.setError("Mínimo 8 caracteres");
            etPass.requestFocus();
            return false;
        }
        return true;
    }
    private void irAMain() {
        Intent i = new Intent(MainActivity.this, HorarioActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}