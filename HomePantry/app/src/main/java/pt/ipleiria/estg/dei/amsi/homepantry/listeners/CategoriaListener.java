package pt.ipleiria.estg.dei.amsi.homepantry.listeners;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;

public interface CategoriaListener {
    void onCategoriaCreated(Categoria categoria);
    void onCategoriaError(String erro);
}
