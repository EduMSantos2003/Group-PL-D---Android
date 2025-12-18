package pt.ipleiria.estg.dei.amsi.homepantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_DESTINO = "DESTINO_FRAGMENT";

    public Button btnStockProduto;
    public Button btnListaCompras;
    public Button btnCategorias;
    public Button btnLocais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // IDs dos botões do activity_main.xml
        btnStockProduto = findViewById(R.id.btnStockProduto);
        btnListaCompras = findViewById(R.id.btnListaCompras);
        btnCategorias   = findViewById(R.id.btnCategorias);
        btnLocais       = findViewById(R.id.btnLocais);

        // Button  redirecionar para o fragment
        btnStockProduto.setOnClickListener(v -> abrirMenuComDestino(R.id.ListaProdutosFragment));
        btnListaCompras.setOnClickListener(v -> abrirMenuComDestino(R.id.ListaComprasFragment));
        btnCategorias.setOnClickListener(v -> abrirMenuComDestino(R.id.ListaCategoriasFragment));
        btnLocais.setOnClickListener(v -> abrirMenuComDestino(R.id.ListaLocaisFragment));
    }

    private void abrirMenuComDestino(int destinoId) {
        Intent intent = new Intent(this, MenuMainActivity.class);
        intent.putExtra(EXTRA_DESTINO, destinoId);
        startActivity(intent);
    }
}
