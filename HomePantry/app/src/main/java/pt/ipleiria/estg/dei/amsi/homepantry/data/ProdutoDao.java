package pt.ipleiria.estg.dei.amsi.homepantry.data;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.homepantry.listeners.ProdutoListListener;


import pt.ipleiria.estg.dei.amsi.homepantry.listeners.ProdutoListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

public class ProdutoDao {

    private static final String BASE_URL =
            "http://10.0.2.2/Group-PL-D---Web/homepantry/backend/web/index.php/api/produto";

    public void criarProduto(Produto produto, ProdutoListener listener) {

        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("nome", produto.getNome());
                json.put("descricao", produto.getDescricao());
                json.put("preco", produto.getPreco());
                json.put("unidade", produto.getUnidade());
                json.put("validade", produto.getValidade());
                json.put("categoria_id", produto.getCategoria_id());

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK ||
                        responseCode == HttpURLConnection.HTTP_CREATED) {

                    listener.onProdutoCreated(produto);
                } else {
                    listener.onProdutoError("Erro HTTP: " + responseCode);
                }

                conn.disconnect();

            } catch (Exception e) {
                listener.onProdutoError(e.getMessage());
            }
        }).start();
    }

    public static void getProdutosPorLocal(
            int localId,
            ProdutoListListener listener
    ) {
        new Thread(() -> {
            ArrayList<Produto> produtos = new ArrayList<>();

            try {
                URL url = new URL(
                        "http://192.168.1.4/Group-PL-D---Web/homepantry/backend/web/index.php/api/local/"
                                + localId + "/produtos"
                );

                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );

                    StringBuilder json = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        json.append(line);
                    }

                    reader.close();

                    JSONArray array = new JSONArray(json.toString());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);

                        Produto p = new Produto();
                        p.setId(obj.getInt("id"));
                        p.setNome(obj.getString("nome"));
                        p.setDescricao(obj.getString("descricao"));

                        produtos.add(p);
                    }

                    listener.onGetProdutos(produtos);

                } else {
                    listener.onError("Erro HTTP " + conn.getResponseCode());
                }

            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

}
