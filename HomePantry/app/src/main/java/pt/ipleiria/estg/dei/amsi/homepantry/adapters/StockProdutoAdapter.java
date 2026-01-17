package pt.ipleiria.estg.dei.amsi.homepantry.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.R;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.StockProduto;

public class StockProdutoAdapter extends RecyclerView.Adapter<StockProdutoAdapter.StockViewHolder> {

    public interface OnStockActionListener {
        void onVer(@NonNull StockProduto stock);
        void onEditar(@NonNull StockProduto stock);
        void onApagar(@NonNull StockProduto stock);
        void onAumentar(@NonNull StockProduto stock);
        void onDiminuir(@NonNull StockProduto stock);
    }

    private final ArrayList<StockProduto> itens = new ArrayList<>();
    private final OnStockActionListener listener;

    public StockProdutoAdapter(@NonNull List<StockProduto> listaInicial,
                               @NonNull OnStockActionListener listener) {
        this.listener = listener;
        setItens(listaInicial);
        setHasStableIds(true);
    }

    /**
     * Atualiza a lista do adapter (ele guarda uma cópia interna).
     * Usa isto sempre que mudares a lista no Fragment.
     */
    public void setItens(@NonNull List<StockProduto> novos) {
        itens.clear();
        itens.addAll(novos);
        notifyDataSetChanged();
    }

    public StockProduto getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        // ID estável ajuda o RecyclerView a “não baralhar” itens
        return itens.get(position).getId();
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock_produto, parent, false);
        return new StockViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder h, int position) {
        StockProduto s = itens.get(position);

        // Nome
        h.txtNome.setText(safe(s.getNome()));

        // Quantidade
        h.txtQuantidade.setText("Qtd: " + s.getQuantidade());

        // Local / Validade
        h.txtLocal.setText("Local: " + safe(s.getLocal()));
        h.txtValidade.setText("Validade: " + safe(s.getValidade()));

        // Preço total: no teu modelo StockProduto não existe preço, por isso fica "-"
        h.txtPrecoTotal.setText("-");

        // Badge expirado: por agora escondido (ativa se implementares lógica de validade)
        h.txtBadgeExpirado.setVisibility(View.GONE);

        // Imagem: no teu XML está comentada; deixo preparado sem rebentar
        if (h.imgProduto != null) {
            h.imgProduto.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        // Botões + / -
        h.btnMais.setOnClickListener(v -> listener.onAumentar(s));
        h.btnMenos.setOnClickListener(v -> listener.onDiminuir(s));

        // Ações laterais
        h.btnVer.setOnClickListener(v -> listener.onVer(s));
        h.btnEditar.setOnClickListener(v -> listener.onEditar(s));
        h.btnApagar.setOnClickListener(v -> listener.onApagar(s));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class StockViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduto; // pode ser null (no XML está comentado)
        TextView txtNome, txtBadgeExpirado, txtQuantidade, txtPrecoTotal, txtLocal, txtValidade;
        ImageButton btnVer, btnEditar, btnApagar, btnMais, btnMenos;

        StockViewHolder(@NonNull View itemView) {
            super(itemView);

            // Se voltares a ativar a imagem no XML, descomenta:
            // imgProduto = itemView.findViewById(R.id.img_produto);

            txtNome = itemView.findViewById(R.id.txt_nome_produto);
            txtBadgeExpirado = itemView.findViewById(R.id.txt_badge_expirado);

            txtQuantidade = itemView.findViewById(R.id.txt_quantidade);
            txtPrecoTotal = itemView.findViewById(R.id.txt_preco_total);
            txtLocal = itemView.findViewById(R.id.txt_local);
            txtValidade = itemView.findViewById(R.id.txt_validade);

            btnVer = itemView.findViewById(R.id.btn_ver);
            btnEditar = itemView.findViewById(R.id.btn_editar);
            btnApagar = itemView.findViewById(R.id.btn_apagar);

            btnMais = itemView.findViewById(R.id.btn_mais);
            btnMenos = itemView.findViewById(R.id.btn_menos);
        }
    }

    private static String safe(String s) {
        return (s == null || s.trim().isEmpty()) ? "-" : s.trim();
    }
}
