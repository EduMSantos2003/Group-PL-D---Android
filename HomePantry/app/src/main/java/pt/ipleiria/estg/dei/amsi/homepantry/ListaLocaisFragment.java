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

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.LocalAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.data.LocalDao;
import pt.ipleiria.estg.dei.amsi.homepantry.listeners.LocalListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;

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

        rvLocais = view.findViewById(R.id.rv_listas_locais);
        rvLocais.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        listaLocais = new ArrayList<>();

        // ✅ ADAPTER COM LISTENER
        adapter = new LocalAdapter(listaLocais, this);
        rvLocais.setAdapter(adapter);

        int casaId = 1; // TEMPORÁRIO
        LocalDao.getLocais(casaId, this);
    }

    // ======================================================
    // CALLBACK API
    // ======================================================
    @Override
    public void onGetLocais(ArrayList<Local> locais) {
        requireActivity().runOnUiThread(() -> {
            listaLocais.clear();
            listaLocais.addAll(locais);
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

    // ======================================================
    // CLIQUE NO LOCAL
    // ======================================================
    @Override
    public void onLocalClick(int localId, String nomeLocal) {

        Toast.makeText(requireContext(),
                "Local: " + nomeLocal,
                Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putInt("localId", localId);
        bundle.putString("nomeLocal", nomeLocal);

        NavHostFragment.findNavController(this)
                .navigate(
                        R.id.action_listaLocais_to_dashboardLocal,
                        bundle
                );
    }
}
