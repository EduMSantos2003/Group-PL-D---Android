package pt.ipleiria.estg.dei.amsi.homepantry.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static String lastBaseUrl = null;

    // agora precisa Context para buscar token E base url configurada
    public static ApiService getApiService(Context context) {

        // buscar URL configurada nas prefs
        ApiConfig config = new ApiConfig(context);
        String baseUrl = config.getBaseUrl();

        // garantir que termina com /
        if (baseUrl != null && !baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }

        // se mudou URL, recria Retrofit
        if (retrofit == null || lastBaseUrl == null || !baseUrl.equals(lastBaseUrl)) {

            lastBaseUrl = baseUrl;

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit.create(ApiService.class);
    }
}
