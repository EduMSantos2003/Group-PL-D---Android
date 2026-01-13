package pt.ipleiria.estg.dei.amsi.homepantry.listeners;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

public interface ProdutoStockListener {
    void onAdicionar(Produto produto);
    void onRemover(Produto produto);
}