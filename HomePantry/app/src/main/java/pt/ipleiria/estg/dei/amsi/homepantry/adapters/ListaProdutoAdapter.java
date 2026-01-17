package pt.ipleiria.estg.dei.amsi.homepantry.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.R;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.ListaProduto;

public class ListaProdutoAdapter extends RecyclerView.Adapter<ListaProdutoAdapter.ViewHolder> {

    public interface OnListaProdutoListener {
        void onVer(ListaProduto item);
        void onEditar(ListaProduto item);
        void onApagar(ListaProduto item);
        void onMais(ListaProduto item);
        void onMenos(ListaProduto item);
    }

    private List<ListaProduto> itens;
    private OnListaProdutoListener listener;

    public ListaProdutoAdapter(List<ListaProduto> itens, OnListaProdutoListener listener) {
        this.itens = itens;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_produto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListaProduto lp = itens.get(position);

        // ✅ Mostra informação como no Stock
        // (Aqui ajustas os textos ao que tu queres mostrar na lista de compras)

        holder.txtLocal.setText("Produto: " + lp.getProduto_nome());
        holder.txtValidade.setText("SubTotal: " + lp.getSubTotal());
        holder.txtQtd.setText("Qtd: " + lp.getQuantidade());

        // ✅ Ícones à direita
        holder.btnVer.setOnClickListener(v -> {
            if (listener != null) listener.onVer(lp);
        });

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) listener.onEditar(lp);
        });

        holder.btnApagar.setOnClickListener(v -> {
            if (listener != null) listener.onApagar(lp);
        });

        // ✅ Setas em baixo
        holder.btnMais.setOnClickListener(v -> {
            if (listener != null) listener.onMais(lp);
        });

        holder.btnMenos.setOnClickListener(v -> {
            if (listener != null) listener.onMenos(lp);
        });
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtLocal, txtValidade, txtQtd;
        ImageButton btnVer, btnEditar, btnApagar, btnMais, btnMenos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtLocal = itemView.findViewById(R.id.txtLocal);
            txtValidade = itemView.findViewById(R.id.txtValidade);
            txtQtd = itemView.findViewById(R.id.txtQtd);

            btnVer = itemView.findViewById(R.id.btnVer);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnApagar = itemView.findViewById(R.id.btnApagar);

            btnMais = itemView.findViewById(R.id.btnMais);
            btnMenos = itemView.findViewById(R.id.btnMenos);
        }
    }
}
