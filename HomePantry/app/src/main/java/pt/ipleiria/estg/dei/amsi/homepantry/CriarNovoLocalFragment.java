package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.data.AppDatabase;

import pt.ipleiria.estg.dei.amsi.homepantry.data.CasaDao;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Casa;

public class CriarNovoLocalFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Dropdown onde vamos mostrar as casas
    private AutoCompleteTextView autoCasaLocal;

    // Lista onde vamos guardar as casas lidas da Base de Dados
    private List<Casa> listaCasas = new ArrayList<>();

    // Variável para guardar o ID da casa escolhida (vai ser a FK ao gravar o Local)
    private int idCasaSelecionada = -1;

    public CriarNovoLocalFragment() {
        // Required empty public constructor
    }

    public static CriarNovoLocalFragment newInstance(String param1, String param2) {
        CriarNovoLocalFragment fragment = new CriarNovoLocalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_criar_novo_local, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) Ligar o AutoCompleteTextView do XML à variável Java
        autoCasaLocal = view.findViewById(R.id.auto_casa_local);

        // 2) Ir buscar o DAO da tabela Casa (Room)
        CasaDao casaDao = AppDatabase.getInstance(requireContext()).casaDao();

        // 3) Fazer a query à BD para obter todas as Casas (LiveData)
        casaDao.getAllCasas().observe(getViewLifecycleOwner(), casas -> {
            // 4) Guardar a lista vinda da BD na nossa variável
            listaCasas = casas;

            // 5) Criar um adapter para transformar a lista em itens visíveis no dropdown
            ArrayAdapter<Casa> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    listaCasas
            );

            // 6) Aplicar o adapter ao AutoCompleteTextView (isto “enche” o dropdown)
            autoCasaLocal.setAdapter(adapter);

            // opcional: selecionar logo a primeira casa
            if (!listaCasas.isEmpty()) {
                autoCasaLocal.setText(listaCasas.get(0).toString(), false);
                idCasaSelecionada = listaCasas.get(0).getId();
            }
        });

        // 7) Listener: detetar quando o utilizador escolhe uma casa
        autoCasaLocal.setOnItemClickListener((parent, viewSelecionado, position, id) -> {
            Casa casaSelecionada = (Casa) parent.getItemAtPosition(position);
            idCasaSelecionada = casaSelecionada.getId();
        });
    }
}
