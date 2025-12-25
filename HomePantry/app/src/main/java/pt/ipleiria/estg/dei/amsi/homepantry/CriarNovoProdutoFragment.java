package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.data.AppDatabase;
import pt.ipleiria.estg.dei.amsi.homepantry.data.CategoriaDao;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CriarNovoProdutoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CriarNovoProdutoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Valores opcionais de inicialização do fragment
    private String mParam1;
    private String mParam2;

    // Views do layout
    private Spinner dropdown_escolher_categoria;
    private EditText txtNome;
    private EditText txtDescricao;
    private EditText txtUnidade;
    private EditText txtPreco;
    private EditText txtValidade;
    private Button btnEscolherImagem;
    private Button btnGuardarProduto;

    // Lista de categorias vindas da BD local (Room)
    private List<Categoria> listaCategorias = new ArrayList<>();

    // Guarda o ID da categoria selecionada (FK do produto)
    private int idCategoriaSelecionada = -1;

    public CriarNovoProdutoFragment() {
        // Required empty public constructor
    }

    public static CriarNovoProdutoFragment newInstance(String param1, String param2) {
        CriarNovoProdutoFragment fragment = new CriarNovoProdutoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lê os parâmetros, se tiverem sido passados na criação do fragment
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout fragment_criar_novo_produto
        return inflater.inflate(R.layout.fragment_criar_novo_produto, container, false);
    }

    // Aqui é onde ligamos o layout às variáveis Java e tornamos o formulário funcional
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) Ligar as views do XML às variáveis Java
        dropdown_escolher_categoria = view.findViewById(R.id.dropdown_escolher_categoria);
        txtNome           = view.findViewById(R.id.txt_nome);
        txtDescricao      = view.findViewById(R.id.txt_descricao);
        txtUnidade        = view.findViewById(R.id.txt_unidade);
        txtPreco          = view.findViewById(R.id.txt_preco);
        txtValidade       = view.findViewById(R.id.txt_validade);
        btnEscolherImagem = view.findViewById(R.id.btn_escolher_imagem);
        btnGuardarProduto = view.findViewById(R.id.btn_guardar_produto);

        // 2) Carregar a lista de categorias da base de dados (Room)
        CategoriaDao categoriaDao = AppDatabase
                .getInstance(requireContext())
                .categoriaDao();

        categoriaDao.getAllCategorias()
                .observe(getViewLifecycleOwner(), categorias -> {

                    // Garante que nunca ficas com lista null
                    if (categorias == null) {
                        listaCategorias = new ArrayList<>();
                    } else {
                        listaCategorias = categorias;
                    }

                    // Adapter para mostrar as categorias no Spinner
                    ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            listaCategorias
                    );
                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );

                    dropdown_escolher_categoria.setAdapter(adapter);

                    // Se existir pelo menos uma categoria, selecionar a primeira
                    if (!listaCategorias.isEmpty()) {
                        dropdown_escolher_categoria.setSelection(0);
                        idCategoriaSelecionada = listaCategorias.get(0).getId();
                    }
                });

        // 3) Listener do Spinner para saber que categoria o utilizador escolheu
        dropdown_escolher_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View viewSelecionado,
                                       int position,
                                       long id) {
                Categoria categoria = (Categoria) parent.getItemAtPosition(position);
                idCategoriaSelecionada = categoria.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada a fazer aqui na maioria dos casos
            }
        });

        // 4) Clique no botão "Escolher imagem"
        btnEscolherImagem.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Funcionalidade de escolher imagem ainda por implementar.",
                        Toast.LENGTH_SHORT).show()
        );

        // 5) Clique no botão "GUARDAR PRODUTO"
        btnGuardarProduto.setOnClickListener(v -> guardarProduto());
    }

    // 6) Método que lê os campos do formulário e (por enquanto) apenas valida e mostra um Toast
    private void guardarProduto() {

        String nome      = txtNome.getText().toString().trim();
        String descricao = txtDescricao.getText().toString().trim();
        String unidade   = txtUnidade.getText().toString().trim();
        String precoStr  = txtPreco.getText().toString().trim();
        String validade  = txtValidade.getText().toString().trim();

        if (nome.isEmpty()) {
            txtNome.setError("Obrigatório");
            txtNome.requestFocus();
            return;
        }

        if (precoStr.isEmpty()) {
            txtPreco.setError("Obrigatório");
            txtPreco.requestFocus();
            return;
        }

        if (idCategoriaSelecionada == -1) {
            Toast.makeText(requireContext(),
                    "Escolhe uma categoria.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double preco;
        try {
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            txtPreco.setError("Preço inválido");
            txtPreco.requestFocus();
            return;
        }

        // TODO: quando tiveres entidade Produto e ProdutoDao,
        //       cria o objeto e insere na BD (Room).
        //
        // Produto p = new Produto(nome, descricao, unidade, preco, validade, idCategoriaSelecionada);
        // AppDatabase.getInstance(requireContext()).produtoDao().insert(p);

        String msg = "Produto: " + nome +
                "\nCategoria ID: " + idCategoriaSelecionada +
                "\nPreço: " + preco +
                "\nQuantidade: " + unidade +
                "\nValidade: " + validade;

        Toast.makeText(requireContext(),
                "Guardado (simulação):\n" + msg,
                Toast.LENGTH_LONG).show();
    }
}
