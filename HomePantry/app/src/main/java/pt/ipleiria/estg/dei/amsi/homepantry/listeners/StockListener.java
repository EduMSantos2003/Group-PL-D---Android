package pt.ipleiria.estg.dei.amsi.homepantry.listeners;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

public interface StockListener {

    public interface ProdutoListener {

        void onStockCreated(Produto produto);

        void onStockError(String erro);
    }
}

