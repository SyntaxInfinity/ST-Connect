package com.example.stconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HorarioActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_horario);
        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_horario,
                R.id.nav_calificaciones,
                R.id.nav_evaluaciones,
                R.id.nav_web,
                R.id.nav_certificados,
                R.id.nav_notificaciones,
                R.id.nav_credencial
        )
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            drawer.closeDrawer(GravityCompat.START);

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (!handled) {
                handleManualNavigation(item.getItemId());
            }
            return true;
        });
    }

    private void handleManualNavigation(int itemId) {
        if (itemId == R.id.nav_horario) {
            navController.navigate(R.id.nav_horario);
        } else if (itemId == R.id.nav_calificaciones) {
            navController.navigate(R.id.nav_calificaciones);
        } else if (itemId == R.id.nav_evaluaciones) {
            navController.navigate(R.id.nav_evaluaciones);
        } else if (itemId == R.id.nav_web) {
            navController.navigate(R.id.nav_web);
        } else if (itemId == R.id.nav_certificados) {
            navController.navigate(R.id.nav_certificados);
        } else if (itemId == R.id.nav_notificaciones) {
            navController.navigate(R.id.nav_notificaciones);
        } else if (itemId == R.id.nav_credencial) {
            navController.navigate(R.id.nav_credencial);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void mostrarpopup(View v) {
        FloatingActionButton fab = findViewById(R.id.btnservice);
        PopupMenu pms = new PopupMenu(this, fab);
        getMenuInflater().inflate(R.menu.popup_ayuda, pms.getMenu());

        pms.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.correoservice) {
                    Toast.makeText(HorarioActivity.this, "correo dae", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.phoneservice) {
                    Toast.makeText(HorarioActivity.this, "telefono dae", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

        pms.show();
    }

    public void Logout(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
