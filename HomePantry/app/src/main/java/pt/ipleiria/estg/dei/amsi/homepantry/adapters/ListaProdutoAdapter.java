package pt.ipleiria.estg.dei.amsi.homepantry.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.R;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.ListaProduto;

public class ListaProdutoAdapter extends RecyclerView.Adapter<ListaProdutoAdapter.ViewHolder> {

    private List<ListaProduto> itens;

    public ListaProdutoAdapter(List<ListaProduto> itens) {
        this.itens = itens;
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

        holder.txtInfo.setText(
                lp.getProduto_nome() +
                        " | Qt: " + lp.getQuantidade() +
                        " | SubTotal: " + lp.getSubTotal()
        );
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtInfoProdutoLista);
        }
    }
}
