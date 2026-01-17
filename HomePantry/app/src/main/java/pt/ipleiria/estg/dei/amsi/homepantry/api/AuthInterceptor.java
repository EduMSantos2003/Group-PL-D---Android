package pt.ipleiria.estg.dei.amsi.homepantry.api;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();

        SessionManager session = new SessionManager(context);
        String token = session.getToken();

        // se não existir token, segue sem header
        if (token == null) {
            return chain.proceed(original);
        }

        Request requestComToken = original.newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(requestComToken);
    }
}
