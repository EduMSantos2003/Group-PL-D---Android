package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.StockProdutoAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.StockProduto;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.StockUpdate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaStockFragment extends Fragment {

    private final Map<Integer, String> produtoNomePorId = new HashMap<>();
    private final Map<Integer, String> localNomePorId = new HashMap<>();

    private boolean produtosCarregados = false;
    private boolean locaisCarregados = false;

    private RecyclerView rvStock;
    private StockProdutoAdapter adapter;

    // Lista "fonte" (no fragment)
    private final ArrayList<StockProduto> lista = new ArrayList<>();

    // Evita spam de cliques em +/-
    private boolean aAtualizar = false;

    public ListaStockFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_stock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvStock = view.findViewById(R.id.rv_lista_stock);
        rvStock.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvStock.setHasFixedSize(true);

        adapter = new StockProdutoAdapter(lista, new StockProdutoAdapter.OnStockActionListener() {

            @Override
            public void onEditar(@NonNull StockProduto stock) {
                mostrarDialogEditarQuantidade(stock);
            }

            @Override
            public void onApagar(@NonNull StockProduto stock) {
                // Como tu pediste “editar para mais ou menos”, o apagar pode ser “meter a 0”
                alterarQuantidadeDireta(stock, 0);
            }

            @Override
            public void onAumentar(@NonNull StockProduto stock) {
                alterarPorDelta(stock, +1);
            }

            @Override
            public void onDiminuir(@NonNull StockProduto stock) {
                alterarPorDelta(stock, -1);
            }
        });

        rvStock.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarStock();
    }

    // -------------------- GET STOCK --------------------

    private void carregarStock() {

        Integer casaId = null;
        Integer localId = null;

        // Se mais tarde quiseres filtrar por casa/local:
        // - passa por argumentos (Bundle) e eu ajusto o fluxo contigo
        if (getArguments() != null) {
            if (getArguments().containsKey("casa_id")) {
                casaId = getArguments().getInt("casa_id");
            }
            if (getArguments().containsKey("local_id")) {
                localId = getArguments().getInt("local_id");
            }
        }

        RetrofitClient.getApiService(requireContext())
                .getStockProdutos(localId, casaId)
                .enqueue(new Callback<List<StockProduto>>() {
                    @Override
                    public void onResponse(Call<List<StockProduto>> call,
                                           Response<List<StockProduto>> response) {
                        if (!isAdded()) return;

                        if (!response.isSuccessful()) {
                            Toast.makeText(requireContext(),
                                    "Erro ao carregar stock (HTTP " + response.code() + ")",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<StockProduto> body = response.body();
                        if (body == null) body = new ArrayList<>();

                        // Atualiza lista fonte (apenas quantidade > 0)
                        lista.clear();
                        for (StockProduto s : body) {
                            if (s != null && s.getQuantidade() > 0) {
                                lista.add(s);
                            }
                        }

                        // IMPORTANTÍSSIMO: como o adapter guarda uma lista interna,
                        // temos de usar setItens()
//                        adapter.setItens(lista);
                        carregarProdutosELocais();
                    }

                    @Override
                    public void onFailure(Call<List<StockProduto>> call, Throwable t) {
                        if (!isAdded()) return;

                        Toast.makeText(requireContext(),
                                "Falha de ligação: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void carregarProdutosELocais() {

        // 1) Produtos
        RetrofitClient.getApiService(requireContext())
                .getProdutos()
                .enqueue(new Callback<List<pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto>>() {
                    @Override
                    public void onResponse(Call<List<pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto>> call,
                                           Response<List<pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto>> response) {

                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            produtoNomePorId.clear();
                            for (pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto p : response.body()) {
                                produtoNomePorId.put(p.getId(), p.getNome());
                            }
                            produtosCarregados = true;
                        }

                        tentarAplicarNomesEAtualizar();
                    }

                    @Override
                    public void onFailure(Call<List<pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto>> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(),
                                "Falha a carregar produtos: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        tentarAplicarNomesEAtualizar();
                    }
                });

        // 2) Locais
        RetrofitClient.getApiService(requireContext())
                .getLocais()
                .enqueue(new Callback<List<pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local>>() {
                    @Override
                    public void onResponse(Call<List<pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local>> call,
                                           Response<List<pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local>> response) {

                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            localNomePorId.clear();
                            for (pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local l : response.body()) {
                                localNomePorId.put(l.getId(), l.getNome());
                            }
                            locaisCarregados = true;
                        }

                        tentarAplicarNomesEAtualizar();
                    }

                    @Override
                    public void onFailure(Call<List<pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local>> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(),
                                "Falha a carregar locais: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        tentarAplicarNomesEAtualizar();
                    }
                });
    }

    private void tentarAplicarNomesEAtualizar() {
        // Quando os dois tiverem tentado carregar, já dá para preencher o máximo possível
        if (!produtosCarregados && !locaisCarregados) return;

        for (StockProduto sp : lista) {
            // Nome do produto pelo produto_id
            String nomeProd = produtoNomePorId.get(sp.getProduto_id());
            if (nomeProd != null) sp.setNome(nomeProd);

            // Nome do local pelo local_id (se existir)
            Integer lid = sp.getLocal_id();
            if (lid != null) {
                String nomeLoc = localNomePorId.get(lid);
                if (nomeLoc != null) sp.setLocal(nomeLoc);
            }
        }

        adapter.setItens(lista);
    }


    // -------------------- PUT STOCK (+ / -) --------------------

    private void alterarPorDelta(@NonNull StockProduto stock, int delta) {

        if (aAtualizar) return;
        aAtualizar = true;

        int antigaQtd = stock.getQuantidade();
        int novaQtd = antigaQtd + delta;

        if (novaQtd < 0) {
            aAtualizar = false;
            Toast.makeText(requireContext(), "Stock não pode ser negativo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update otimista (UI reage logo)
        stock.setQuantidade(novaQtd);
        adapter.setItens(lista);

        RetrofitClient.getApiService(requireContext())
                .updateStock(stock.getId(), new StockUpdate(novaQtd))
                .enqueue(new Callback<StockProduto>() {
                    @Override
                    public void onResponse(Call<StockProduto> call, Response<StockProduto> response) {
                        if (!isAdded()) return;
                        aAtualizar = false;

                        if (!response.isSuccessful()) {
                            // rollback
                            stock.setQuantidade(antigaQtd);
                            adapter.setItens(lista);

                            Toast.makeText(requireContext(),
                                    "Erro ao atualizar (HTTP " + response.code() + ")",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Se a API devolve o objeto atualizado, sincroniza:
                        if (response.body() != null) {
                            stock.setQuantidade(response.body().getQuantidade());
                        }

                        // Regra: só listar positivos
                        if (stock.getQuantidade() <= 0) {
                            removerDaListaPorId(stock.getId());
                        }

                        adapter.setItens(lista);
                    }

                    @Override
                    public void onFailure(Call<StockProduto> call, Throwable t) {
                        if (!isAdded()) return;
                        aAtualizar = false;

                        // rollback
                        stock.setQuantidade(antigaQtd);
                        adapter.setItens(lista);

                        Toast.makeText(requireContext(),
                                "Falha ao atualizar: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void alterarQuantidadeDireta(@NonNull StockProduto stock, int novaQtd) {

        if (aAtualizar) return;
        aAtualizar = true;

        if (novaQtd < 0) {
            aAtualizar = false;
            Toast.makeText(requireContext(), "Stock não pode ser negativo", Toast.LENGTH_SHORT).show();
            return;
        }

        int antigaQtd = stock.getQuantidade();

        // Update otimista
        stock.setQuantidade(novaQtd);
        adapter.setItens(lista);

        RetrofitClient.getApiService(requireContext())
                .updateStock(stock.getId(), new StockUpdate(novaQtd))
                .enqueue(new Callback<StockProduto>() {
                    @Override
                    public void onResponse(Call<StockProduto> call, Response<StockProduto> response) {
                        if (!isAdded()) return;
                        aAtualizar = false;

                        if (!response.isSuccessful()) {
                            stock.setQuantidade(antigaQtd);
                            adapter.setItens(lista);

                            Toast.makeText(requireContext(),
                                    "Erro ao atualizar (HTTP " + response.code() + ")",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (response.body() != null) {
                            stock.setQuantidade(response.body().getQuantidade());
                        }

                        if (stock.getQuantidade() <= 0) {
                            removerDaListaPorId(stock.getId());
                        }

                        adapter.setItens(lista);
                    }

                    @Override
                    public void onFailure(Call<StockProduto> call, Throwable t) {
                        if (!isAdded()) return;
                        aAtualizar = false;

                        stock.setQuantidade(antigaQtd);
                        adapter.setItens(lista);

                        Toast.makeText(requireContext(),
                                "Falha ao atualizar: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void removerDaListaPorId(int stockId) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == stockId) {
                lista.remove(i);
                return;
            }
        }
    }

    // -------------------- DIALOG EDITAR --------------------

    private void mostrarDialogEditarQuantidade(@NonNull StockProduto stock) {

        EditText edt = new EditText(requireContext());
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt.setText(String.valueOf(stock.getQuantidade()));
        edt.setSelection(edt.getText().length());

        new AlertDialog.Builder(requireContext())
                .setTitle("Editar quantidade")
                .setMessage("Define a quantidade em stock:")
                .setView(edt)
                .setNegativeButton("Cancelar", (d, w) -> d.dismiss())
                .setPositiveButton("Guardar", (d, w) -> {
                    String txt = edt.getText().toString().trim();
                    if (txt.isEmpty()) {
                        Toast.makeText(requireContext(), "Insere uma quantidade", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int novaQtd;
                    try {
                        novaQtd = Integer.parseInt(txt);
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Quantidade inválida", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    alterarQuantidadeDireta(stock, novaQtd);
                })
                .show();
    }
}
