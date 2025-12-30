package pt.ipleiria.estg.dei.amsi.homepantry.listeners;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

public interface ProdutoListener {

    void onProdutoCreated(Produto produto);

    void onProdutoError(String erro);
}
