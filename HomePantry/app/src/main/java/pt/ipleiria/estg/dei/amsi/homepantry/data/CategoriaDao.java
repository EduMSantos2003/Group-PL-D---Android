package pt.ipleiria.estg.dei.amsi.homepantry.data;

import pt.ipleiria.estg.dei.amsi.homepantry.listeners.CategoriaListener;

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
    private static final String BASE_URL =
            "http://192.168.1.4:8000/api";

    /**
     * Cria uma categoria na API.
     * Esta chamada é assíncrona (Thread) para não bloquear a UI.
     */
    public void criarCategoria(Categoria categoria, CategoriaListener listener) {

        new Thread(() -> {
            HttpURLConnection conn = null;

            try {
                // 1️⃣ Abrir ligação HTTP
                URL url = new URL(BASE_URL);
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
                    listener.onCategoriaError(
                            "Erro HTTP " + responseCode + ": " + responseBody
                    );
                }

            } catch (Exception e) {
                // Erros de rede, JSON, URL, etc.
                listener.onCategoriaError(e.getMessage());

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
