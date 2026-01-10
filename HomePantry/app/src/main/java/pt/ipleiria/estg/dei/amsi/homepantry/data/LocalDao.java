package pt.ipleiria.estg.dei.amsi.homepantry.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.homepantry.listeners.LocalListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;

public class LocalDao {

    // ⚠️ BASE URL (sem casa fixa)
    private static final String BASE_URL =
            "http://192.168.1.11/Group-PL-D---Web/homepantry/backend/web/index.php/api";

    // ======================================================
    // GET LOCAIS (MASTER–DETAIL)
    // ======================================================
    public static void getLocais(int casaId, LocalListener listener) {

        new Thread(() -> {

            ArrayList<Local> listaLocais = new ArrayList<>();

            try {
                URL url = new URL(BASE_URL + "/casa/" + casaId + "/locais");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );

                    StringBuilder json = new StringBuilder();
                    String linha;

                    while ((linha = reader.readLine()) != null) {
                        json.append(linha);
                    }

                    reader.close();

                    // ✔️ API devolve ARRAY direto
                    JSONArray jsonArray = new JSONArray(json.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);

                        Local local = new Local();
                        local.setId(obj.getInt("id"));
                        local.setNome(obj.getString("nome"));

                        // descrição pode ou não vir
                        if (obj.has("descricao")) {
                            local.setDescricao(obj.getString("descricao"));
                        }

                        // ⚠️ FOTO NÃO VEM DA API
                        local.setFotoPath(null);

                        listaLocais.add(local);
                    }

                    listener.onGetLocais(listaLocais);

                } else {
                    listener.onError("Erro HTTP: " + responseCode);
                }

                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                listener.onError(e.getClass().getSimpleName() + ": " + e.getMessage());
            }

        }).start();
    }
}
