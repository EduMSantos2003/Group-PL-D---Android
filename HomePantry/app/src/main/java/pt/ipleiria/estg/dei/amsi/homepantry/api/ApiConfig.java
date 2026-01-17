package pt.ipleiria.estg.dei.amsi.homepantry.api;

import android.content.Context;
import android.content.SharedPreferences;

public class ApiConfig {

    private static final String PREF_NAME = "api_config";
    private static final String KEY_BASE_URL = "base_url";

    // URL default (o teu atual)
    private static final String DEFAULT_BASE_URL =
            "http://172.22.21.242/Group-PL-D---Web/homepantry/backend/web/index.php/";

    private final SharedPreferences prefs;

    public ApiConfig(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getBaseUrl() {
        return prefs.getString(KEY_BASE_URL, DEFAULT_BASE_URL);
    }

    public void setBaseUrl(String baseUrl) {
        prefs.edit().putString(KEY_BASE_URL, baseUrl).apply();
    }
}
