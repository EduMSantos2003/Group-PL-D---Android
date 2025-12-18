package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MenuMainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        // ✅ Validações (evitam crashes silenciosos por ids/layout errados)
        if (drawerLayout == null) {
            throw new IllegalStateException("DrawerLayout não encontrado. Confirma android:id=\"@+id/drawerLayout\" no activity_menu_main.xml");
        }
        if (navView == null) {
            throw new IllegalStateException("NavigationView não encontrado. Confirma android:id=\"@+id/navView\" no activity_menu_main.xml");
        }
        if (toolbar == null) {
            throw new IllegalStateException("Toolbar não encontrada. Confirma android:id=\"@+id/toolbar\" no layout incluído (ex.: app_bar_main.xml)");
        }

        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment não encontrado. Confirma android:id=\"@+id/nav_host_fragment\" no layout (FragmentContainerView).");
        }

        NavController navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.ListaProdutosFragment,
                R.id.ListaComprasFragment,
                R.id.ListaCategoriasFragment,
                R.id.ListaLocaisFragment
        ).setOpenableLayout(drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // ✅ Abrir logo o fragment pedido pela MainActivity
        int destino = getIntent().getIntExtra(MainActivity.EXTRA_DESTINO, -1);
        if (destino != -1 && navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() != destino) {
            navController.navigate(destino);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) return super.onSupportNavigateUp();

        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
