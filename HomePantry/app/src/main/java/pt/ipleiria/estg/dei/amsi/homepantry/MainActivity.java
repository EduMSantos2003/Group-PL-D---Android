package pt.ipleiria.estg.dei.amsi.homepantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnStockProduto;
    private Button btnListas;
    private Button btnCategorias;
    private Button btnLocais;

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

        // 1) Ligar aos botões do XML
        btnStockProduto = findViewById(R.id.btnStockProduto);
        btnListas       = findViewById(R.id.btnListas);
        btnCategorias   = findViewById(R.id.btnCategorias);
        btnLocais       = findViewById(R.id.btnLocais);

        // 2) Definir ações de cada botão

        // Produto / Stock
        btnStockProduto.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListasProdutosActivity.class);
            startActivity(intent);
        });

        // Listas de compras
        btnListas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListasComprasActivity.class);
            startActivity(intent);
        });

        // Categorias
        btnCategorias.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListasCategoriasActivity.class);
            startActivity(intent);
        });

        // Locais
        btnLocais.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListasLocaisActivity.class);
            startActivity(intent);
        });
    }
}
