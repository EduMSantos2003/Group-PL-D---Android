package pt.ipleiria.estg.dei.amsi.homepantry;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;

public class CriarNovoLocalFragment extends Fragment {

    // Views
    private EditText txtNomeLocal;
    private EditText txtDescricaoLocal;
    private Button btnGuardarLocal;
    private ImageView imgFotoLocal;

    // Foto
    private String fotoPathSelecionada;
    private static final int REQUEST_GALERIA = 100;

    public CriarNovoLocalFragment() {
        // construtor obrigatório
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
        txtNomeLocal = view.findViewById(R.id.edt_nome_local);
        txtDescricaoLocal = view.findViewById(R.id.edt_descricao_local);
        btnGuardarLocal = view.findViewById(R.id.btn_criar_local);
        imgFotoLocal = view.findViewById(R.id.img_foto_local);

        Button btnFoto = view.findViewById(R.id.btn_carregar_foto_novo_local);
        btnFoto.setOnClickListener(v -> abrirGaleria());

        btnGuardarLocal.setOnClickListener(v -> guardarLocal());
    }

    // ======================================================
    // GUARDAR LOCAL (SEM API AINDA)
    // ======================================================
    private void guardarLocal() {

        String nomeLocal = txtNomeLocal.getText().toString().trim();
        String descricaoLocal = txtDescricaoLocal.getText().toString().trim();

        if (nomeLocal.isEmpty()) {
            txtNomeLocal.setError("Obrigatório");
            txtNomeLocal.requestFocus();
            return;
        }

        // Criar objeto Local (MODEL)
        Local local = new Local(nomeLocal, descricaoLocal);
        local.setFotoPath(fotoPathSelecionada);

        Toast.makeText(requireContext(),
                "Local criado (simulação)",
                Toast.LENGTH_SHORT).show();

        // Voltar atrás
        requireActivity().onBackPressed();
    }

    // ======================================================
    // FOTO DO LOCAL
    // ======================================================
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALERIA
                && resultCode == getActivity().RESULT_OK
                && data != null) {

            Uri imageUri = data.getData();
            guardarImagemLocal(imageUri);
        }
    }

    private void guardarImagemLocal(Uri imageUri) {
        try {
            InputStream inputStream = requireContext()
                    .getContentResolver()
                    .openInputStream(imageUri);

            File file = new File(
                    requireContext().getFilesDir(),
                    "local_" + System.currentTimeMillis() + ".jpg"
            );

            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            fotoPathSelecionada = file.getAbsolutePath();

            // Preview
            imgFotoLocal.setImageURI(Uri.fromFile(file));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(),
                    "Erro ao guardar imagem",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
