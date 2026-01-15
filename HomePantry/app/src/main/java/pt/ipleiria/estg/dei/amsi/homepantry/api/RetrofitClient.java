package pt.ipleiria.estg.dei.amsi.homepantry.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "http://172.22.21.242/Group-PL-D---Web/homepantry/backend/web/";

    private static Retrofit retrofit;

    public static ApiService getApiService() {

        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create(gson)
                    )
                    .build();
        }

        return retrofit.create(ApiService.class);
    }
}
