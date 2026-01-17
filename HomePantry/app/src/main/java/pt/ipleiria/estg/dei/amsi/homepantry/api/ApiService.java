package pt.ipleiria.estg.dei.amsi.homepantry.api;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.LoginRequest;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.LoginResponse;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Lista;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.ListaProduto;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.StockProduto;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.StockUpdate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);

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

    @PUT("api/lista/{id}")
    Call<Lista> updateLista(
            @Path("id") int listaId,
            @Body Lista lista
    );

    @DELETE("api/lista/{id}")
    Call<Void> deleteLista(@Path("id") int listaId);

    @GET("api/stock-produto")
    Call<List<StockProduto>> getStockProdutos(
            @Query("local_id") Integer localId,
            @Query("casa_id") Integer casaId
    );

    @PUT("api/stock-produto/{id}")
    Call<StockProduto> updateStock(
            @Path("id") int stockId,
            @Body StockUpdate body
    );

    //  GET produtos dentro de uma lista
    @GET("api/lista/{id}/produtos")
    Call<List<ListaProduto>> getProdutosLista(@Path("id") int listaId);

    //  POST adicionar produto a uma lista
    @POST("api/lista/{id}/produtos")
    Call<ListaProduto> addProdutoLista(@Path("id") int listaId, @Body ListaProduto body);

    //  PUT atualizar um item lista_produto
    @PUT("api/lista-produto/{id}")
    Call<ListaProduto> updateListaProduto(@Path("id") int id, @Body ListaProduto body);

    //  DELETE remover um item lista_produto
    @DELETE("api/lista-produto/{id}")
    Call<Void> deleteListaProduto(@Path("id") int id);
    
    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

}
