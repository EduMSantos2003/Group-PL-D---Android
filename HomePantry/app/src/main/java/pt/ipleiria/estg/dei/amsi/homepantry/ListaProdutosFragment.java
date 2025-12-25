package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


//import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListaProdutosFragment extends Fragment {

    public ListaProdutosFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_produtos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnAdicionarProduto = view.findViewById(R.id.btn_adicionar_produto);

        btnAdicionarProduto.setOnClickListener(v ->
                NavHostFragment.findNavController(ListaProdutosFragment.this)
                        .navigate(R.id.action_listaProdutos_to_criarNovoProduto)
        );
        
    }
}