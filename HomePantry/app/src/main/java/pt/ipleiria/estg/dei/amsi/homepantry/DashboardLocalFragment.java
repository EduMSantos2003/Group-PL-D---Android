package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.ProdutoAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.data.ProdutoDao;
import pt.ipleiria.estg.dei.amsi.homepantry.listeners.ProdutoListListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

public class DashboardLocalFragment extends Fragment
        implements ProdutoListListener {

    private int localId;
    private String nomeLocal;

    private RecyclerView rvProdutos;
    private ArrayList<Produto> listaProdutos;
    private ProdutoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            localId = getArguments().getInt("localId");
            nomeLocal = getArguments().getString("nomeLocal");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_dashboard_local,
                container,
                false
        );

        TextView txtNome = view.findViewById(R.id.txt_nome_local_dashboard);
        txtNome.setText(nomeLocal);

        rvProdutos = view.findViewById(R.id.rv_produtos_local);
        rvProdutos.setLayoutManager(new LinearLayoutManager(requireContext()));

        listaProdutos = new ArrayList<>();
        adapter = new ProdutoAdapter(listaProdutos);
        rvProdutos.setAdapter(adapter);

        // 🔥 CHAMADA À API
        ProdutoDao produtoDao = new ProdutoDao();
        produtoDao.getProdutosPorLocal(localId, this);

        return view;
    }

    // ==============================
    // CALLBACKS DA API
    // ==============================
    @Override
    public void onGetProdutos(ArrayList<Produto> produtos) {
        requireActivity().runOnUiThread(() -> {
            listaProdutos.clear();
            listaProdutos.addAll(produtos);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onError(String erro) {
        requireActivity().runOnUiThread(() ->
                Toast.makeText(
                        requireContext(),
                        erro,
                        Toast.LENGTH_SHORT
                ).show()
        );
    }
}
