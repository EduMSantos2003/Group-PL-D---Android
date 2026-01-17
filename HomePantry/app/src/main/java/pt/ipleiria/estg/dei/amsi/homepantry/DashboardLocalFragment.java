package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.StockProdutoAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.StockProduto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardLocalFragment extends Fragment {

    private int localId = -1;
    private String nomeLocal = "Local";

    private RecyclerView rvProdutos;
    private ArrayList<StockProduto> listaStock;
    private StockProdutoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            localId = getArguments().getInt("localId", -1);
            nomeLocal = getArguments().getString("nomeLocal", "Local");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_local, container, false);

        TextView txtNome = view.findViewById(R.id.txt_nome_local_dashboard);
        txtNome.setText(nomeLocal);

        rvProdutos = view.findViewById(R.id.rv_produtos_local);
        rvProdutos.setLayoutManager(new LinearLayoutManager(requireContext()));

        listaStock = new ArrayList<>();

        adapter = new StockProdutoAdapter(listaStock, new StockProdutoAdapter.OnStockActionListener() {
            @Override
            public void onVer(@NonNull StockProduto stock) {
                Toast.makeText(requireContext(), stock.getNome(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEditar(@NonNull StockProduto stock) {
                Toast.makeText(requireContext(), "Editar: " + stock.getNome(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApagar(@NonNull StockProduto stock) {
                Toast.makeText(requireContext(), "Apagar: " + stock.getNome(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAumentar(@NonNull StockProduto stock) {
                Toast.makeText(requireContext(), "+1: " + stock.getNome(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDiminuir(@NonNull StockProduto stock) {
                Toast.makeText(requireContext(), "-1: " + stock.getNome(), Toast.LENGTH_SHORT).show();
            }
        });

        rvProdutos.setAdapter(adapter);

        if (localId == -1) {
            Toast.makeText(requireContext(), "Erro: localId inválido", Toast.LENGTH_LONG).show();
            return view;
        }

        carregarStockDoLocal();

        return view;
    }

    private void carregarStockDoLocal() {
        RetrofitClient.getApiService(requireContext())
                .getStockProdutos(localId, null)
                .enqueue(new Callback<List<StockProduto>>() {
                    @Override
                    public void onResponse(Call<List<StockProduto>> call, Response<List<StockProduto>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setItens(response.body()); // ✅ melhor
                        } else {
                            Toast.makeText(getContext(),
                                    "Erro ao carregar stock: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StockProduto>> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(getContext(),
                                "Falha: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e("DASH_LOCAL", "Erro retrofit", t);
                    }
                });
    }
}
