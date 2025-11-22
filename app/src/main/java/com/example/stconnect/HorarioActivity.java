package com.example.stconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                R.id.nav_biblioteca,
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

        mostrarNombreUsuario();
    }

    private void mostrarNombreUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        View headerView = navigationView.getHeaderView(0);
        TextView txtNombreUsuario = headerView.findViewById(R.id.txtNombreUsuario);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("stconnect/usuarios")
                .child(user.getUid())
                .child("nombre");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String nombreCompleto = snapshot.getValue(String.class);
                if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
                    txtNombreUsuario.setText("Usuario");
                    return;
                }

                String[] partes = nombreCompleto.trim().split("\\s+");
                String primerNombre = partes.length > 0 ? partes[0] : "";
                String primerApellido = partes.length > 1 ? partes[1] : "";

                txtNombreUsuario.setText((primerNombre + " " + primerApellido).trim());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                txtNombreUsuario.setText("Usuario");
            }
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
        } else if (itemId == R.id.nav_biblioteca) {
            navController.navigate(R.id.nav_biblioteca);
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

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            navController.navigate(R.id.nav_credencial);
            return true;
        } else if (id == R.id.action_contact) {
            mostrarpopup(findViewById(R.id.action_contact));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mostrarpopup(View v) {
        // View v can be null or the toolbar item view, but PopupMenu needs a non-null view anchor.
        // If v is null or we want to anchor to the toolbar, we might need a reference to the toolbar or a specific view.
        // For simplicity, let's try to use the toolbar itself if v is not suitable, or just the anchor passed.
        // Since onOptionsItemSelected passes the view of the menu item (which might not be easily accessible as a View object directly from MenuItem),
        // we might need a workaround. However, let's see.
        // Actually, MenuItem is not a View.
        // We can anchor the popup to the toolbar.
        
        View anchor = v;
        if (anchor == null) {
             anchor = findViewById(R.id.toolbar);
        }

        PopupMenu pms = new PopupMenu(this, anchor);
        getMenuInflater().inflate(R.menu.popup_ayuda, pms.getMenu());

        pms.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.correoservice) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"daegeneral@santotomas.cl"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta desde STCONNECT");
                try {
                    startActivity(Intent.createChooser(intent, "Enviar correo con:"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HorarioActivity.this, "No hay aplicaciones de correo instaladas", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (id == R.id.phoneservice) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+56933221144"));
                startActivity(intent);
                return true;
            }

            return false;
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
