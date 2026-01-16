package pt.ipleiria.estg.dei.amsi.homepantry.api;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Lista;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.ListaProduto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api/produto")
    Call<List<Produto>> getProdutos();

    @GET("api/categoria")
    Call<List<Categoria>> getCategorias();

    @GET("api/local")
    Call<List<Local>> getLocais();

    //  LISTAS (Master)
    @GET("api/lista")
    Call<List<Lista>> getListas(@Query("casa_id") int casaId);

    @POST("api/lista")
    Call<Lista> createLista(@Body Lista lista);

    @DELETE("api/lista/{id}")
    Call<Void> deleteLista(@Path("id") int listaId);

    //  PRODUTOS DE UMA LISTA (Detail)
    @GET("api/lista/{id}/produtos")
    Call<List<ListaProduto>> getProdutosLista(@Path("id") int listaId);


    @POST("api/lista/{id}/adicionar-produto")
    Call<ListaProduto> addProdutoLista(@Path("id") int listaId, @Body ListaProduto body);
}
