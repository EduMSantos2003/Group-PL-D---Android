package pt.ipleiria.estg.dei.amsi.homepantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import pt.ipleiria.estg.dei.amsi.homepantry.api.SessionManager;

public class MenuMainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private NavController navController;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_main);

        session = new SessionManager(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        if (drawerLayout == null) {
            throw new IllegalStateException("DrawerLayout não encontrado (id drawerLayout)");
        }
        if (navView == null) {
            throw new IllegalStateException("NavigationView não encontrado (id navView)");
        }
        if (toolbar == null) {
            throw new IllegalStateException("Toolbar não encontrada (id toolbar)");
        }

        setSupportActionBar(toolbar);

        //  Header com user logado
        preencherHeader();

        //  NavController
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment não encontrado (id nav_host_fragment)");
        }

        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.ListaStockFragment,
                R.id.ListaProdutosFragment,
                R.id.ListaCategoriasFragment,
                R.id.ListaLocaisFragment,
                R.id.ListaListasFragment
        ).setOpenableLayout(drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //  Menu clique (inclui Logout)
        navView.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_logout) {
                session.logout();

                Toast.makeText(MenuMainActivity.this, "Sessão terminada", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MenuMainActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

            if (handled) {
                drawerLayout.closeDrawers();
            }

            return handled;
        });

        //  Abrir logo o fragment pedido
        int destino = getIntent().getIntExtra(MainActivity.EXTRA_DESTINO, -1);
        if (destino != -1 && navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() != destino) {
            navController.navigate(destino);
        }
    }

    private void preencherHeader() {
        View headerView = navView.getHeaderView(0);

        TextView txtUserNome = headerView.findViewById(R.id.txtUserNome);

        String username = session.getUsername();
        String email = session.getEmail();

        if (txtUserNome != null) {
            txtUserNome.setText(username != null && !username.isEmpty() ? username : "Utilizador");
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
