package com.example.stconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.stconnect.ui.viewmodel.LoginViewModel;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPass;
    private Button btnlogin;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        etEmail = findViewById(R.id.TxtEmail);
        etPass = findViewById(R.id.TxtPassword);
        btnlogin = findViewById(R.id.btnLogin);
        
        // Inicializar ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        
        // Observar resultados de autenticación
        observeAuthResult();
        
        btnlogin.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        // Validación básica en la UI
        if (email.isEmpty()) {
            etEmail.setError("Requerido");
            etEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            etPass.setError("Requerido");
            etPass.requestFocus();
            return;
        }
        if (pass.length() < 8) {
            etPass.setError("Mínimo 8 caracteres");
            etPass.requestFocus();
            return;
        }

        // Llamar al ViewModel para iniciar sesión
        loginViewModel.login(email, pass);
    }

    private void observeAuthResult() {
        loginViewModel.getAuthResult().observe(this, authResult -> {
            if (authResult == null) return;
            
            if (authResult.isLoading()) {
                // Mostrar loading si es necesario
                return;
            }
            
            if (authResult.isSuccess()) {
                // Guardar UID en SharedPreferences
                SharedPreferences prefs = getSharedPreferences("Usuario", MODE_PRIVATE);
                prefs.edit().putString("uid", authResult.getUid()).apply();
                
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
                irAMain();
            } else if (authResult.getError() != null) {
                Toast.makeText(this, authResult.getError(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void irAMain() {
        Intent i = new Intent(MainActivity.this, HorarioActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}