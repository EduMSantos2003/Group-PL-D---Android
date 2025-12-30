package pt.ipleiria.estg.dei.amsi.homepantry.data;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
                json.put("categoria_id", produto.getCategoriaId());

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
}
