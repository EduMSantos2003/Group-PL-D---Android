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

import android.widget.ScrollView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.data.ProdutoDao;
import pt.ipleiria.estg.dei.amsi.homepantry.listeners.ProdutoListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

public class CriarNovoProdutoFragment extends Fragment {

    // Views
    private Spinner spinnerCategorias;
    private EditText txtNome, txtDescricao, txtUnidade, txtPreco, txtValidade;
    private Button btnEscolherImagem, btnGuardarProduto;
    private ScrollView scrollView;


    // Dados
    private List<Categoria> listaCategorias = new ArrayList<>();
    private int idCategoriaSelecionada = -1;

    public CriarNovoProdutoFragment() {
        // obrigatório
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_criar_novo_produto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ScrollView
        scrollView = view.findViewById(R.id.scroll_criar_produto);

        // Ligar views
        spinnerCategorias = view.findViewById(R.id.dropdown_escolher_categoria);
        txtNome = view.findViewById(R.id.txt_nome);
        txtDescricao = view.findViewById(R.id.txt_descricao);
        txtUnidade = view.findViewById(R.id.txt_unidade);
        txtPreco = view.findViewById(R.id.txt_preco);
        txtValidade = view.findViewById(R.id.txt_validade);
        btnEscolherImagem = view.findViewById(R.id.btn_escolher_imagem);
        btnGuardarProduto = view.findViewById(R.id.btn_guardar_produto);

        carregarCategoriasFake(); // ⚠️ temporário até ligares API categorias

        // Só agora
        configurarAutoScroll();

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view,
                                       int position,
                                       long id) {
                Categoria c = (Categoria) parent.getItemAtPosition(position);
                idCategoriaSelecionada = c.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idCategoriaSelecionada = -1;
            }
        });

        btnEscolherImagem.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Imagem ainda não implementada",
                        Toast.LENGTH_SHORT).show()
        );

        btnGuardarProduto.setOnClickListener(v -> guardarProduto());
    }

    // ==========================================================
    // 🔥 MÉTODO CRÍTICO – POST PARA A API
    // ==========================================================
    private void guardarProduto() {

        String nome = txtNome.getText().toString().trim();
        String descricao = txtDescricao.getText().toString().trim();
        String unidadeStr = txtUnidade.getText().toString().trim();
        String precoStr = txtPreco.getText().toString().trim();
        String validade = txtValidade.getText().toString().trim();

        // Validações
        if (nome.isEmpty()) {
            txtNome.setError("Obrigatório");
            txtNome.requestFocus();
            return;
        }

        if (precoStr.isEmpty()) {
            txtPreco.setError("Obrigatório");
            focarEsubir(txtNome);
//            txtPreco.requestFocus();
            return;
        }

        if (idCategoriaSelecionada == -1) {
            Toast.makeText(requireContext(),
                    "Escolhe uma categoria",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int unidade = 0;
        try {
            if (!unidadeStr.isEmpty())
                unidade = Integer.parseInt(unidadeStr);
        } catch (NumberFormatException e) {
            txtUnidade.setError("Número inválido");
            return;
        }

        double preco;
        try {
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            txtPreco.setError("Preço inválido");
            return;
        }

        // Criar objeto Produto (PURO – SEM ROOM)
        Produto produto = new Produto(
                nome,
                descricao,
                unidade,
                preco,
                validade,
                idCategoriaSelecionada
        );

        ProdutoDao produtoDao = new ProdutoDao();

        produtoDao.criarProduto(produto, new ProdutoListener() {
            @Override
            public void onProdutoCreated(Produto produtoCriado) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(),
                            "Produto criado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                    limparFormulario();
                });
            }

            @Override
            public void onProdutoError(String erro) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(),
                                "Erro: " + erro,
                                Toast.LENGTH_LONG).show()
                );
            }
        });
    }
    private void focarEsubir(View campo) {
        campo.requestFocus();
        if (scrollView != null) {
            scrollView.post(() -> scrollView.smoothScrollTo(0, campo.getBottom()));
        }
    }

    // ==========================================================
    // Auxiliares
    // ==========================================================
    private void limparFormulario() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtUnidade.setText("");
        txtPreco.setText("");
        txtValidade.setText("");
        spinnerCategorias.setSelection(0);
    }

    // TEMPORÁRIO (até ligares API de categorias)
    private void carregarCategoriasFake() {
        listaCategorias.clear();
        listaCategorias.add(new Categoria(1, "Laticínios"));
        listaCategorias.add(new Categoria(2, "Bebidas"));
        listaCategorias.add(new Categoria(3, "Cereais"));

        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listaCategorias
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapter);

        idCategoriaSelecionada = listaCategorias.get(0).getId();
    }

    private void configurarAutoScroll() {
        View.OnFocusChangeListener listener = (v, hasFocus) -> {
            if (hasFocus && scrollView != null) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, v.getBottom()));
            }
        };

        txtNome.setOnFocusChangeListener(listener);
        txtDescricao.setOnFocusChangeListener(listener);
        txtUnidade.setOnFocusChangeListener(listener);
        txtPreco.setOnFocusChangeListener(listener);
        txtValidade.setOnFocusChangeListener(listener);
    }
}
