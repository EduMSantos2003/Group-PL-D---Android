package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.ListaAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Lista;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaListasFragment extends Fragment {

    private RecyclerView rvListas;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_listas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvListas = view.findViewById(R.id.rv_listas);

        if (rvListas == null) {
            Toast.makeText(getContext(), "ERRO: rv_listas não encontrado no layout", Toast.LENGTH_LONG).show();
            return;
        }

        rvListas.setLayoutManager(new LinearLayoutManager(getContext()));

        carregarListas();
    }

    private void carregarListas() {
        int casaId = 1;

        RetrofitClient.getApiService()
                .getListas(casaId)
                .enqueue(new Callback<List<Lista>>() {

                    @Override
                    public void onResponse(Call<List<Lista>> call, Response<List<Lista>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {

                            rvListas.setAdapter(new ListaAdapter(response.body(), lista -> {

                                Bundle bundle = new Bundle();
                                bundle.putInt("listaId", lista.getId());
                                bundle.putString("nomeLista", lista.getNome());

                                // navegar usando ACTION (back stack correto)
                                NavHostFragment.findNavController(ListaListasFragment.this)
                                        .navigate(R.id.action_ListaListasFragment_to_ListaComprasFragment, bundle);

                            }));

                        } else {
                            Toast.makeText(getContext(),
                                    "Erro ao carregar listas. Código: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Lista>> call, Throwable t) {
                        if (!isAdded()) return;

                        Toast.makeText(getContext(),
                                "Falha Retrofit: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();

                        Log.e("LISTAS", "Erro Retrofit", t);
                    }
                });
    }
}
