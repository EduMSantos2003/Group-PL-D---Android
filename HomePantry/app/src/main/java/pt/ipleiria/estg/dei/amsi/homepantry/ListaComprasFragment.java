package pt.ipleiria.estg.dei.amsi.homepantry;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.ListaProdutoAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.ListaProduto;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaComprasFragment extends Fragment {

    private RecyclerView rvProdutos;
    private int listaId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // ✅ ativa o menu no fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_compras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProdutos = view.findViewById(R.id.rv_produtos_lista);
        rvProdutos.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            listaId = getArguments().getInt("listaId", -1);
        }

        if (listaId == -1) {
            Toast.makeText(getContext(), "Erro: listaId inválido", Toast.LENGTH_LONG).show();
            return;
        }

        carregarProdutos();
    }

    // ✅ CRIA O MENU NO TOPO
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_lista_compras, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // ✅ QUANDO CLICA NO "+"
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_produto) {
            dialogAdicionarProduto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // -------------------------
    // POST - adicionar produto
    // -------------------------
    private void dialogAdicionarProduto() {

        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_adicionar_produto_lista, null);

        android.widget.Spinner spProdutos = dialogView.findViewById(R.id.spProdutos);
        EditText edtQuantidade = dialogView.findViewById(R.id.edtQuantidade);

        // 1) buscar produtos ao backend
        RetrofitClient.getApiService(requireContext())
                .getProdutos()
                .enqueue(new Callback<List<Produto>>() {
                    @Override
                    public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {

                            List<Produto> produtos = response.body();

                            android.widget.ArrayAdapter<Produto> adapter =
                                    new android.widget.ArrayAdapter<>(
                                            requireContext(),
                                            android.R.layout.simple_spinner_dropdown_item,
                                            produtos
                                    );

                            spProdutos.setAdapter(adapter);

                            // 2) só abre o dialog depois do spinner estar cheio
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Adicionar Produto")
                                    .setView(dialogView)
                                    .setPositiveButton("Adicionar", (dialog, which) -> {

                                        Produto produtoSelecionado = (Produto) spProdutos.getSelectedItem();

                                        if (produtoSelecionado == null) {
                                            Toast.makeText(getContext(), "Escolhe um produto", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        String qtdStr = edtQuantidade.getText().toString().trim();

                                        if (qtdStr.isEmpty()) {
                                            Toast.makeText(getContext(), "Quantidade inválida", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        double quantidade = Double.parseDouble(qtdStr);

                                        adicionarProduto(produtoSelecionado.getId(), quantidade);
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .show();

                        } else {
                            Toast.makeText(getContext(),
                                    "Erro ao carregar produtos: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Produto>> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(getContext(),
                                "Falha ao carregar produtos: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void adicionarProduto(int produtoId, double quantidade) {

        ListaProduto body = new ListaProduto(produtoId, quantidade);
        body.setLista_id(listaId); // ✅ ISTO resolve o required no Yii2

        RetrofitClient.getApiService(requireContext())
                .addProdutoLista(listaId, body)
                .enqueue(new Callback<ListaProduto>() {
                    @Override
                    public void onResponse(Call<ListaProduto> call, Response<ListaProduto> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Produto adicionado!", Toast.LENGTH_SHORT).show();
                            carregarProdutos();
                        } else {
                            Toast.makeText(getContext(),
                                    "Erro POST: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ListaProduto> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(getContext(),
                                "Falha POST: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


    // -------------------------
    // GET - carregar produtos
    // -------------------------
    private void carregarProdutos() {
        RetrofitClient.getApiService(requireContext())
                .getProdutosLista(listaId)
                .enqueue(new Callback<List<ListaProduto>>() {
                    @Override
                    public void onResponse(Call<List<ListaProduto>> call, Response<List<ListaProduto>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {

                            List<ListaProduto> listaProdutos = response.body();

                            rvProdutos.setAdapter(new ListaProdutoAdapter(
                                    listaProdutos,
                                    new ListaProdutoAdapter.OnListaProdutoListener() {

                                        @Override
                                        public void onVer(ListaProduto item) {
                                            Toast.makeText(getContext(),
                                                    item.getProduto_nome(),
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onEditar(ListaProduto item) {
                                            dialogEditarQuantidade(item); // PUT
                                        }

                                        @Override
                                        public void onApagar(ListaProduto item) {
                                            confirmarApagar(item); // DELETE
                                        }

                                        @Override
                                        public void onMais(ListaProduto item) {
                                            double atual = item.getQuantidade();
                                            item.setQuantidade(atual + 1);
                                            atualizarItem(item); // PUT
                                        }

                                        @Override
                                        public void onMenos(ListaProduto item) {
                                            double atual = item.getQuantidade();

                                            if (atual > 1) {
                                                item.setQuantidade(atual - 1);
                                                atualizarItem(item); // PUT
                                            } else {
                                                Toast.makeText(getContext(),
                                                        "Quantidade mínima = 1",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                            ));

                        } else {
                            Toast.makeText(getContext(),
                                    "Erro ao carregar produtos. Código: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ListaProduto>> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(getContext(),
                                "Falha: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e("LISTA_PRODUTOS", "Erro retrofit", t);
                    }
                });
    }

    // -------------------------
    // PUT - editar quantidade
    // -------------------------
    private void dialogEditarQuantidade(ListaProduto item) {
        EditText input = new EditText(getContext());
        input.setHint("Nova quantidade");
        input.setText(String.valueOf(item.getQuantidade()));

        new AlertDialog.Builder(getContext())
                .setTitle("Editar quantidade")
                .setMessage(item.getProduto_nome())
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {

                    String txt = input.getText().toString().trim();
                    if (txt.isEmpty()) {
                        Toast.makeText(getContext(), "Quantidade inválida", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double novaQtd = Double.parseDouble(txt);
                    item.setQuantidade(novaQtd);

                    atualizarItem(item);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void atualizarItem(ListaProduto item) {
        RetrofitClient.getApiService(requireContext())
                .updateListaProduto(item.getId(), item)
                .enqueue(new Callback<ListaProduto>() {
                    @Override
                    public void onResponse(Call<ListaProduto> call, Response<ListaProduto> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            carregarProdutos();
                        } else {
                            Toast.makeText(getContext(),
                                    "Erro ao atualizar (PUT). Código: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ListaProduto> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(getContext(),
                                "Falha PUT: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // -------------------------
    // DELETE - apagar item
    // -------------------------
    private void confirmarApagar(ListaProduto item) {
        new AlertDialog.Builder(getContext())
                .setTitle("Apagar item")
                .setMessage("Queres remover \"" + item.getProduto_nome() + "\" da lista?")
                .setPositiveButton("Apagar", (dialog, which) -> apagarItem(item.getId()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void apagarItem(int listaProdutoId) {
        RetrofitClient.getApiService(requireContext())
                .deleteListaProduto(listaProdutoId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            carregarProdutos();
                        } else {
                            Toast.makeText(getContext(),
                                    "Erro ao apagar (DELETE). Código: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(getContext(),
                                "Falha DELETE: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
