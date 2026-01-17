package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.ProdutoAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.CachePrefs;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.data.localdb.AppDatabase;
import pt.ipleiria.estg.dei.amsi.homepantry.data.localdb.ProdutoCacheEntity;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaProdutosFragment extends Fragment {

    private RecyclerView recyclerView;

    public ListaProdutosFragment() { }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_lista_produtos,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_lista_produtos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //  1) mostrar cache primeiro
        carregarProdutosDaCache();

        //  2) chamar API e atualizar cache
        carregarProdutosDaApi();
    }

    // ---------------- CACHE (ROOM) ----------------

    private void carregarProdutosDaCache() {
        AppDatabase db = AppDatabase.getInstance(requireContext());

        new Thread(() -> {
            try {
                List<ProdutoCacheEntity> cached = db.produtoCacheDao().getAll();
                if (cached == null || cached.isEmpty()) return;

                ArrayList<Produto> listaCache = new ArrayList<>();

                for (ProdutoCacheEntity e : cached) {
                    Produto p = new Produto();
                    p.setId(e.id);
                    p.setNome(e.nome);
                    p.setDescricao(e.descricao);
                    listaCache.add(p);
                }

                if (!isAdded()) return;

                requireActivity().runOnUiThread(() -> {
                    recyclerView.setAdapter(new ProdutoAdapter(listaCache));
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void guardarProdutosNaCache(List<Produto> produtos) {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        CachePrefs prefs = new CachePrefs(requireContext());

        new Thread(() -> {
            try {
                List<ProdutoCacheEntity> entities = new ArrayList<>();

                for (Produto p : produtos) {
                    if (p == null) continue;

                    entities.add(new ProdutoCacheEntity(
                            p.getId(),
                            p.getNome() == null ? "" : p.getNome(),
                            p.getDescricao() == null ? "" : p.getDescricao()
                    ));
                }

                db.produtoCacheDao().deleteAll();
                db.produtoCacheDao().insertAll(entities);

                prefs.setLastProdutosSync(System.currentTimeMillis());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // ---------------- API ----------------

    private void carregarProdutosDaApi() {

        RetrofitClient.getApiService(requireContext())
                .getProdutos()
                .enqueue(new Callback<List<Produto>>() {

                    @Override
                    public void onResponse(
                            Call<List<Produto>> call,
                            Response<List<Produto>> response) {

                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {

                            List<Produto> body = response.body();

                            // mostrar na UI
                            recyclerView.setAdapter(new ProdutoAdapter(body));

                            // guardar cache
                            guardarProdutosNaCache(body);

                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Erro ao carregar produtos (API)",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Produto>> call, Throwable t) {
                        if (!isAdded()) return;

                        Toast.makeText(
                                requireContext(),
                                "Sem ligação. A mostrar cache local.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
