package pt.ipleiria.estg.dei.amsi.homepantry.data;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import pt.ipleiria.estg.dei.amsi.homepantry.listeners.CategoriaListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;

public class CategoriaDao {

    // Endpoint da API para criação de categorias
//    private static final String BASE_URL =
//            "http://192.168.1.4:8000/api";
    private static final String BASE_URL =
            "http://10.0.2.2/Group-PL-D---Web/homepantry/backend/web/index.php/api";

//    private static final String BASE_URL =
//            "http://172.22.21.242/Group-PL-D---Web/homepantry/backend/web/index.php/api";

    /**
     * Cria uma categoria na API.
     * Esta chamada é assíncrona (Thread) para não bloquear a UI.
     */
    public void criarCategoria(Categoria categoria, CategoriaListener listener) {

        new Thread(() -> {
            HttpURLConnection conn = null;

            try {
                // 1️⃣ Abrir ligação HTTP
                URL url = new URL(BASE_URL + "/categoria");
                conn = (HttpURLConnection) url.openConnection();

                // 2️⃣ Configuração do request
                conn.setRequestMethod("POST");
                conn.setRequestProperty(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );
                conn.setRequestProperty(
                        "Accept",
                        "application/json"
                );
                conn.setConnectTimeout(10000); // 10s para ligar
                conn.setReadTimeout(10000);    // 10s para resposta
                conn.setDoOutput(true);

                // 3️⃣ Construção do JSON enviado à API
                JSONObject json = new JSONObject();
                json.put("nome", categoria.getNome());

                // 4️⃣ Escrita do corpo do pedido (UTF-8 para evitar erros com acentos)
                byte[] body = json.toString().getBytes(StandardCharsets.UTF_8);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body);
                    os.flush();
                }

                // 5️⃣ Obter código de resposta HTTP
                int responseCode = conn.getResponseCode();

                // 6️⃣ Ler resposta da API (sucesso ou erro)
                InputStream is = (responseCode >= 200 && responseCode < 300)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                String responseBody = readStream(is);

                // 7️⃣ Tratamento do resultado
                if (responseCode == HttpURLConnection.HTTP_OK ||
                        responseCode == HttpURLConnection.HTTP_CREATED) {

                    // Categoria criada com sucesso na API
                    listener.onCategoriaCreated(categoria);

                } else {
                    // Erro devolvido pela API (ex.: validação, duplicado, etc.)
                    listener.onError(
                            "Erro HTTP " + responseCode + ": " + responseBody
                    );
                }

            } catch (Exception e) {
                // Erros de rede, JSON, URL, etc.
//                listener.onError(e.getMessage());
                listener.onError(e.getClass().getSimpleName() + ": " + e.getMessage());

            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }

    public void getCategorias(CategoriaListener listener) {

        new Thread(() -> {
            HttpURLConnection conn = null;

            try {
                // ✅ Endpoint correto
                URL url = new URL(BASE_URL + "/categoria");
                conn = (HttpURLConnection) url.openConnection();

                // ✅ Timeouts
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                // ✅ Método
                conn.setRequestMethod("GET");

                // ✅ Pedir JSON (para não vir XML)
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();

                InputStream is = (responseCode >= 200 && responseCode < 300)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                String responseBody = readStream(is);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Espera-se: [{"id":1,"nome":"..."}, ...]
                    org.json.JSONArray arr = new org.json.JSONArray(responseBody);

                    java.util.ArrayList<Categoria> lista = new java.util.ArrayList<>();

                    for (int i = 0; i < arr.length(); i++) {
                        org.json.JSONObject obj = arr.getJSONObject(i);

                        Categoria c = new Categoria();
                        if (obj.has("id")) c.setId(obj.getInt("id"));
                        if (obj.has("nome")) c.setNome(obj.getString("nome"));

                        lista.add(c);
                    }

                    listener.onGetCategorias(lista);

                } else {
                    listener.onError("Erro HTTP " + responseCode + ": " + responseBody);
                }

            } catch (Exception e) {
                listener.onError(e.getClass().getSimpleName() + ": " + e.getMessage());
            } finally {
                if (conn != null) conn.disconnect();
            }

        }).start();
    }


    /**
     * Lê o conteúdo de um InputStream e devolve uma String.
     * Usado para ler a resposta da API.
     */
    private static String readStream(InputStream is) throws Exception {
        if (is == null) return "";

        BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        return sb.toString();
    }
}
