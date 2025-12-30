package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Casa;

public class CriarNovoLocalFragment extends Fragment {

    // Views
    private AutoCompleteTextView spinnerCasas;

    private EditText txtNomeLocal;
    private Button btnGuardarLocal;

    // Dados
    private List<Casa> listaCasas = new ArrayList<>();
    private int idCasaSelecionada = -1;

    public CriarNovoLocalFragment() {
        // obrigatório
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

        // Ligar views
        spinnerCasas = view.findViewById(R.id.auto_casa_local);
        txtNomeLocal = view.findViewById(R.id.edt_nome_local);
        btnGuardarLocal = view.findViewById(R.id.btn_criar_local);

        // ⚠️ TEMPORÁRIO — dados fake (até ligares API)
        carregarCasasFake();

        // ✅ LISTENER CERTO PARA AutoCompleteTextView
        spinnerCasas.setOnItemClickListener((parent, view1, position, id) -> {
            Casa casa = (Casa) parent.getItemAtPosition(position);
            idCasaSelecionada = casa.getId();
        });

        btnGuardarLocal.setOnClickListener(v -> guardarLocal());
    }


    // ======================================================
    // MÉTODO CRÍTICO — GUARDAR LOCAL (API no futuro)
    // ======================================================
    private void guardarLocal() {

        String nomeLocal = txtNomeLocal.getText().toString().trim();

        if (nomeLocal.isEmpty()) {
            txtNomeLocal.setError("Obrigatório");
            txtNomeLocal.requestFocus();
            return;
        }

        if (idCasaSelecionada == -1) {
            Toast.makeText(requireContext(),
                    "Escolhe uma casa",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 POR AGORA: apenas simulação
        String msg = "Local: " + nomeLocal +
                "\nCasa ID: " + idCasaSelecionada;

        Toast.makeText(requireContext(),
                "Guardado (simulação):\n" + msg,
                Toast.LENGTH_LONG).show();

        limparFormulario();
    }

    // ======================================================
    // AUXILIARES
    // ======================================================
    private void limparFormulario() {
        txtNomeLocal.setText("");
        spinnerCasas.setSelection(0);
    }

    // ⚠️ TEMPORÁRIO — até ligares API de Casas
    private void carregarCasasFake() {
        listaCasas.clear();
        listaCasas.add(new Casa(1, "Casa Principal"));
        listaCasas.add(new Casa(2, "Casa da Praia"));
        listaCasas.add(new Casa(3, "Casa dos Pais"));

        ArrayAdapter<Casa> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listaCasas
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerCasas.setAdapter(adapter);

        idCasaSelecionada = listaCasas.get(0).getId();
    }
}
