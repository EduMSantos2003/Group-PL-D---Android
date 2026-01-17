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

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.ProdutoAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
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

        // Botão adicionar produto
        ImageButton btnAdicionarProduto =
                view.findViewById(R.id.btn_adicionar_produto);

        btnAdicionarProduto.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(
                                R.id.action_listaProdutos_to_criarNovoProduto
                        )
        );

        // RecyclerView (ID CERTO do XML)
        recyclerView = view.findViewById(R.id.rv_lista_produtos);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        // CHAMAR A API
        carregarProdutos();
    }

    private void carregarProdutos() {

        RetrofitClient.getApiService(requireContext())
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
                                    "Erro ao carregar produtos",
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
