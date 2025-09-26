package com.example.stconnect;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer y NavView
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_horario);
        navController = navHostFragment.getNavController();

        // Configuración del AppBar - INCLUYE TODOS LOS DESTINOS
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_horario,
                R.id.nav_calificaciones,
                R.id.nav_evaluaciones,
                R.id.nav_web
        )
                .setOpenableLayout(drawer)
                .build();

        // Vincular Toolbar con NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Vincular NavigationView con NavController
        NavigationUI.setupWithNavController(navigationView, navController);

        // Opcional: Agregar listener para debuggear
        navigationView.setNavigationItemSelectedListener(item -> {
            // Cerrar el drawer
            drawer.closeDrawer(GravityCompat.START);

            // Manejar la navegación
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (!handled) {
                // Si no se maneja, hacer navegación manual
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
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
