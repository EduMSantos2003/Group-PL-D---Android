package pt.ipleiria.estg.dei.amsi.homepantry.listeners;

import java.util.ArrayList;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;

public interface CategoriaListener {

    // GET (para preencher Spinner / Recycler / etc.)
    void onGetCategorias(ArrayList<Categoria> categoria);

    // POST (quando crias uma categoria)
    void onCategoriaCreated(Categoria categoria);

    // Erros gerais (GET/POST)
    void onError(String erro);

    // Clique (se um dia tiveres RecyclerView de categorias)
    void onCategoriaClick(int categoriaId, String nomeCategoria);
}