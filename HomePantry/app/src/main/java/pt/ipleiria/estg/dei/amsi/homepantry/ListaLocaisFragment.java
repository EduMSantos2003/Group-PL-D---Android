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
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
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

        ImageButton btnAdicionarLocal = view.findViewById(R.id.btn_adicionar_local);

        btnAdicionarLocal.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_listaLocais_to_criarNovoLocal)
        );

        rvLocais = view.findViewById(R.id.rv_listas_locais);
        rvLocais.setLayoutManager(new LinearLayoutManager(getContext()));

        carregarLocais();
    }

    private void carregarLocais() {
        RetrofitClient.getApiService(requireContext())
                .getLocais()
                .enqueue(new Callback<List<Local>>() {

                    @Override
                    public void onResponse(Call<List<Local>> call, Response<List<Local>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            rvLocais.setAdapter(
                                    new LocalAdapter(response.body(), ListaLocaisFragment.this)
                            );
                        } else {
                            Toast.makeText(getContext(), "Erro ao carregar locais", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Local>> call, Throwable t) {
                        Toast.makeText(getContext(), "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
    }

    // ======================================================
    // LocalListener
    // ======================================================
    @Override
    public void onLocalClick(int localId, String nomeLocal) {
        Toast.makeText(requireContext(), "Local: " + nomeLocal, Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putInt("localId", localId);
        bundle.putString("nomeLocal", nomeLocal);

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_listaLocais_to_dashboardLocal, bundle);
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
