package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pt.ipleiria.estg.dei.amsi.homepantry.api.ApiConfig;

public class ConfiguracaoServidorFragment extends Fragment {

    private EditText edtBaseUrl;
    private Button btnGuardar;

    public ConfiguracaoServidorFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configuracao_servidor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtBaseUrl = view.findViewById(R.id.edtBaseUrl);
        btnGuardar = view.findViewById(R.id.btnGuardarServidor);

        ApiConfig config = new ApiConfig(requireContext());
        edtBaseUrl.setText(config.getBaseUrl());

        btnGuardar.setOnClickListener(v -> {
            String url = edtBaseUrl.getText().toString().trim();

            if (url.isEmpty()) {
                Toast.makeText(requireContext(), "Insere o URL do servidor", Toast.LENGTH_SHORT).show();
                return;
            }

            // garantir que termina com /
            if (!url.endsWith("/")) url = url + "/";

            config.setBaseUrl(url);

            Toast.makeText(requireContext(),
                    "Servidor guardado:\n" + url,
                    Toast.LENGTH_LONG).show();
        });
    }
}
