package pt.ipleiria.estg.dei.amsi.homepantry.api;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("api/produto")
    Call<List<Produto>> getProdutos();

    @GET("api/categoria")
    Call<List<Categoria>> getCategorias();

    @GET("api/local")
    Call<List<Categoria>> getLocais();
}
