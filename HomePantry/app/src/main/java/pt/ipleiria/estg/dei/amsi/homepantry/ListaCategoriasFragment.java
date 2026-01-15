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

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.CategoriaAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaCategoriasFragment extends Fragment {

    private RecyclerView rvCategorias;
    private CategoriaAdapter adapter;
    private final ArrayList<Categoria> lista = new ArrayList<>();

    public ListaCategoriasFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_categorias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Botão para navegar para Criar Categoria
        ImageButton btnAdicionarCategoria = view.findViewById(R.id.btn_adicionar_categoria);
        btnAdicionarCategoria.setOnClickListener(v ->
                NavHostFragment.findNavController(ListaCategoriasFragment.this)
                        .navigate(R.id.action_listaCategorias_to_criarNovaCategoria)
        );

        // RecyclerView
        rvCategorias = view.findViewById(R.id.rv_listas_categorias);
        rvCategorias.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new CategoriaAdapter(lista);
        rvCategorias.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarCategorias();
    }

    private void carregarCategorias() {

        RetrofitClient.getApiService().getCategorias()
                .enqueue(new Callback<List<Categoria>>() {
                    @Override
                    public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<Categoria> categorias = new ArrayList<>(response.body());
                            adapter.setCategorias(categorias);
                        } if (response.isSuccessful() && response.body() != null) {
                            ArrayList<Categoria> categorias = new ArrayList<>(response.body());
                            adapter.setCategorias(categorias);
                        } else {
                            Toast.makeText(requireContext(),
                                    "Erro HTTP: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Categoria>> call, Throwable t) {
                        if (!isAdded()) return;

                        Toast.makeText(requireContext(),
                                "Falha API: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
