package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.homepantry.data.CategoriaDao;
import pt.ipleiria.estg.dei.amsi.homepantry.listeners.CategoriaListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CriarNovaCategoriaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CriarNovaCategoriaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editNome;
    private Button btnCriar;


    public CriarNovaCategoriaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CriarNovaCategoriaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CriarNovaCategoriaFragment newInstance(String param1, String param2) {
        CriarNovaCategoriaFragment fragment = new CriarNovaCategoriaFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1️⃣ Inflar o layout
        View v = inflater.inflate(R.layout.fragment_criar_nova_categoria, container, false);

        // 2️⃣ Ligar os elementos do layout às variáveis Java
        editNome = v.findViewById(R.id.EditText_nome);
        btnCriar = v.findViewById(R.id.btn_criar_categoria);

        // 3️⃣ Definir o comportamento do botão
        btnCriar.setOnClickListener(view -> criarCategoriaNaApi());

        return v;
//        return inflater.inflate(R.layout.fragment_criar_nova_categoria, container, false);
    }

    /**
     * Método chamado quando o utilizador clica no botão "Criar Categoria".
     * Valida o input e chama a API.
     */
    private void criarCategoriaNaApi() {

        // 4️⃣ Ler o texto introduzido pelo utilizador
        String nome = editNome.getText().toString().trim();

        // 5️⃣ Validação simples do formulário
        if (nome.isEmpty()) {
            editNome.setError("Obrigatório");
            editNome.requestFocus();
            return;
        }

        // 6️⃣ Evitar múltiplos cliques enquanto a API responde
        btnCriar.setEnabled(false);

        // 7️⃣ Criar o objeto Categoria a enviar para a API
        Categoria categoria = new Categoria();
        categoria.setNome(nome);

        // 8️⃣ Criar instância do DAO da API
        CategoriaDao categoriaDao = new CategoriaDao();

        // 9️⃣ Chamar a API (assíncrono)
        categoriaDao.criarCategoria(categoria, new CategoriaListener() {
            /**
             * Chamado quando a API cria a categoria com sucesso
             */
            @Override
            public void onCategoriaCreated(Categoria categoria) {

                // ⚠️ O callback vem de uma Thread → voltar à UI thread
                if (!isAdded()) return;

                requireActivity().runOnUiThread(() -> {

                    // 10️⃣ Reativar botão
                    btnCriar.setEnabled(true);

                    // 11️⃣ Feedback ao utilizador
                    Toast.makeText(requireContext(),
                            "Categoria criada com sucesso na API!",
                            Toast.LENGTH_SHORT).show();

                    // 12️⃣ Limpar o campo de texto
                    editNome.setText("");
                });
            }

            /**
             * Chamado quando ocorre um erro na criação da categoria
             */
            @Override
            public void onError(String erro) {

                if (!isAdded()) return;

                requireActivity().runOnUiThread(() -> {

                    // 13️⃣ Reativar botão
                    btnCriar.setEnabled(true);

                    // 14️⃣ Mostrar mensagem de erro devolvida
                    Toast.makeText(requireContext(),
                            "Erro ao criar categoria: " + erro,
                            Toast.LENGTH_LONG).show();
                });
            }

            // ✅ tens de implementar, mesmo que não uses aqui
            @Override
            public void onGetCategorias(ArrayList<Categoria> categoria) {
                // não usado neste fragment
            }

            // ✅ tens de implementar, mesmo que não uses aqui
            @Override
            public void onCategoriaClick(int categoriaId, String nomeCategoria) {
                // não usado neste fragment
            }
        });
    }
}