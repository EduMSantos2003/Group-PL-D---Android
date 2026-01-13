package pt.ipleiria.estg.dei.amsi.homepantry.listeners;

import java.util.ArrayList;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

public interface ProdutoListListener {
    void onGetProdutos(ArrayList<Produto> produtos);
    void onError(String erro);
}
