package pt.ipleiria.estg.dei.amsi.homepantry.api;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("index.php?r=api/produto/index")
    Call<List<Produto>> getProdutos();
}
