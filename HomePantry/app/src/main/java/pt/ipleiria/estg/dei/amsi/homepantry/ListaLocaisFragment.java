package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;   // ✅ FALTA ISTO
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.LocalAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.CachePrefs;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.data.localdb.AppDatabase;
import pt.ipleiria.estg.dei.amsi.homepantry.data.localdb.LocalCacheEntity;
import pt.ipleiria.estg.dei.amsi.homepantry.listeners.LocalListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaLocaisFragment extends Fragment implements LocalListener {

    private RecyclerView rvLocais;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_lista_locais, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        rvLocais = view.findViewById(R.id.rv_listas_locais);
        rvLocais.setLayoutManager(new LinearLayoutManager(getContext()));

        carregarLocais();
    }

//    private void carregarLocais() {
//        RetrofitClient.getApiService(requireContext())
//                .getLocais()
//                .enqueue(new Callback<List<Local>>() {
//
//                    @Override
//                    public void onResponse(Call<List<Local>> call, Response<List<Local>> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            rvLocais.setAdapter(
//                                    new LocalAdapter(response.body(), ListaLocaisFragment.this)
//                            );
//                        } else {
//                            Toast.makeText(getContext(), "Erro ao carregar locais", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Local>> call, Throwable t) {
//                        Toast.makeText(getContext(), "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                        t.printStackTrace();
//                    }
//                });
//    }
    private void carregarLocais() {
        RetrofitClient.getApiService(requireContext())
                .getLocais()
                .enqueue(new Callback<List<Local>>() {

                    @Override
                    public void onResponse(Call<List<Local>> call, Response<List<Local>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            List<Local> locaisApi = response.body();

                            // 1) Mostrar na UI
                            rvLocais.setAdapter(
                                    new LocalAdapter(locaisApi, ListaLocaisFragment.this)
                            );

                            // 2) Guardar na cache (Room)
                            new Thread(() -> {
                                AppDatabase db = AppDatabase.getInstance(requireContext());

                                List<LocalCacheEntity> locaisCache = new ArrayList<>();
                                for (Local local : locaisApi) {
                                    LocalCacheEntity e = new LocalCacheEntity(
                                            local.getId(),
                                            local.getNome(),
                                            local.getDescricao(),
                                            local.getFotoPath()
                                    );
                                    locaisCache.add(e);
                                }

                                db.localCacheDao().clearAll();
                                db.localCacheDao().insertLocais(locaisCache);
                            }).start();

                        } else {
                            Toast.makeText(getContext(), "Erro ao carregar locais", Toast.LENGTH_SHORT).show();
                        }
                    }

//                    @Override
//                    public void onFailure(Call<List<Local>> call, Throwable t) {
//                        Toast.makeText(getContext(), "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                        t.printStackTrace();
//                        // Aqui depois vamos buscar aos locais da cache, se quiseres.
//                    }
                    @Override
                    public void onFailure(Call<List<Local>> call, Throwable t) {
                        Toast.makeText(getContext(), "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();

                        // Tentar carregar da cache (Room)
                        new Thread(() -> {
                            AppDatabase db = AppDatabase.getInstance(requireContext());
                            List<LocalCacheEntity> locaisCache = db.localCacheDao().getAllLocais();

                            if (!locaisCache.isEmpty()) {
                                List<Local> locais = new ArrayList<>();
                                for (LocalCacheEntity e : locaisCache) {
                                    Local local = new Local();
                                    local.setId(e.getId());
                                    local.setNome(e.getNome());
                                    local.setDescricao(e.getDescricao());
                                    local.setFotoPath(e.getFotoPath());
                                    locais.add(local);
                                }

                                requireActivity().runOnUiThread(() ->
                                        rvLocais.setAdapter(
                                                new LocalAdapter(locais, ListaLocaisFragment.this)
                                        )
                                );
                            }
                        }).start();
                    }

                });
    }


    // ======================================================
    // LocalListener
    // ======================================================
//    @Override
//    public void onLocalClick(int localId, String nomeLocal) {
//    }
    @Override
    public void onLocalClick(int localId, String nomeLocal) {
        CachePrefs cachePrefs = new CachePrefs(requireContext());
        cachePrefs.setSelectedLocalId(localId);

        Toast.makeText(getContext(),
                "Local selecionado: " + nomeLocal,
                Toast.LENGTH_SHORT
        ).show();

        // Se quiseres navegar para outro ecrã:
        // NavHostFragment.findNavController(this)
        //        .navigate(R.id.action_listaLocaisFragment_to_listaProdutosFragment);
    }

    @Override
    public void onGetLocais(ArrayList<Local> locais) {
        // Não estás a usar LocalDao agora, mas é obrigatório pela interface
    }

    @Override
    public void onError(String erro) {
        // Não estás a usar LocalDao agora, mas é obrigatório pela interface
    }
}
