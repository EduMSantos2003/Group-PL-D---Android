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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.ListaProduto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListasComprasFragment extends Fragment {

    private RecyclerView rvProdutos;
    private int listaId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listas_compras, container, false);
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

        Toast.makeText(getContext(), "Abriste lista ID: " + listaId, Toast.LENGTH_SHORT).show();

        carregarProdutos();
    }

    private void carregarProdutos() {
        RetrofitClient.getApiService(requireContext())
                .getProdutosLista(listaId)
                .enqueue(new Callback<List<ListaProduto>>() {
                    @Override
                    public void onResponse(Call<List<ListaProduto>> call, Response<List<ListaProduto>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getContext(),
                                    "Produtos encontrados: " + response.body().size(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),
                                    "Erro ao carregar produtos. Código: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ListaProduto>> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(getContext(), "Falha: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("LISTA_PRODUTOS", "Erro retrofit", t);
                    }
                });
    }
}
