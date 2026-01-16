package pt.ipleiria.estg.dei.amsi.homepantry;

import static androidx.core.content.ContentProviderCompat.requireContext;

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

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.LocalAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.adapters.ProdutoAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.data.LocalDao;
import pt.ipleiria.estg.dei.amsi.homepantry.listeners.LocalListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaLocaisFragment extends Fragment
        implements LocalListener {

    private RecyclerView rvLocais;
    private ArrayList<Local> listaLocais;
    private LocalAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_lista_locais,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        ImageButton btnAdicionarLocal =
                view.findViewById(R.id.btn_adicionar_local);

        btnAdicionarLocal.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_listaLocais_to_criarNovoLocal)
        );

        // RecyclerView (ID CERTO do XML)
        View recyclerView = view.findViewById(R.id.rv_listas_locais);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        // CHAMAR A API
        carregarLocais();
    }

    private void carregarLocais() {

        RetrofitClient.getApiService()
                .getProdutos()
                .enqueue(new Callback<List<Produto>>() {

                    @Override
                    public void onResponse(
                            Call<List<Produto>> call,
                            Response<List<Produto>> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            recyclerView.setAdapter(
                                    new ProdutoAdapter(response.body())
                            );
                        } else {
                            Toast.makeText(
                                    getContext(),
                                    "Erro ao carregar Local",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Produto>> call, Throwable t) {
                        Toast.makeText(
                                getContext(),
                                "Erro: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                        t.printStackTrace();
                    }
                });
    }
}
//        rvLocais = view.findViewById(R.id.rv_listas_locais);
//        rvLocais.setLayoutManager(
//                new LinearLayoutManager(requireContext())
//        );
//
//        listaLocais = new ArrayList<>();
//
//        // ✅ ADAPTER COM LISTENER
//        adapter = new LocalAdapter(listaLocais, this);
//        rvLocais.setAdapter(adapter);
//
//        int casaId = 1; // TEMPORÁRIO
//        LocalDao.getLocais(casaId, this);
//    }
//
//    // ======================================================
//    // CALLBACK API
//    // ======================================================
//    @Override
//    public void onGetLocais(ArrayList<Local> locais) {
//        requireActivity().runOnUiThread(() -> {
//            listaLocais.clear();
//            listaLocais.addAll(locais);
//            adapter.notifyDataSetChanged();
//        });
//    }
//
//    @Override
//    public void onError(String erro) {
//        requireActivity().runOnUiThread(() ->
//                Toast.makeText(
//                        requireContext(),
//                        erro,
//                        Toast.LENGTH_SHORT
//                ).show()
//        );
//    }
//
//    // ======================================================
//    // CLIQUE NO LOCAL
//    // ======================================================
//    @Override
//    public void onLocalClick(int localId, String nomeLocal) {
//
//        Toast.makeText(requireContext(),
//                "Local: " + nomeLocal,
//                Toast.LENGTH_SHORT).show();
//
//        Bundle bundle = new Bundle();
//        bundle.putInt("localId", localId);
//        bundle.putString("nomeLocal", nomeLocal);
//
//        NavHostFragment.findNavController(this)
//                .navigate(
//                        R.id.action_listaLocais_to_dashboardLocal,
//                        bundle
//                );
//    }
//}
