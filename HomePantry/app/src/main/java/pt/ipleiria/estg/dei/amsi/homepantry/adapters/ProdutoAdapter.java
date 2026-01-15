package pt.ipleiria.estg.dei.amsi.homepantry.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.homepantry.R;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

public class ProdutoAdapter
        extends RecyclerView.Adapter<ProdutoAdapter.ViewHolder> {


    private List<Produto> produtos;

    public ProdutoAdapter(List<Produto> produtos) {
        this.produtos = produtos;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Produto p = produtos.get(position);

        holder.txtNome.setText(p.getNome());
        holder.txtDescricao.setText(p.getDescricao());
        holder.txtValidade.setText("Validade: " + p.getValidade());

        // ==========================
        // VERIFICAR SE ESTÁ EXPIRADO
        // ==========================
        try {
            LocalDate validade = LocalDate.parse(p.getValidade());
            LocalDate hoje = LocalDate.now();

            if (validade.isBefore(hoje)) {
                holder.txtNome.setTextColor(Color.RED);
                holder.txtValidade.setText("EXPIRADO");
                holder.txtValidade.setTextColor(Color.RED);
            } else {
                holder.txtNome.setTextColor(Color.BLACK);
                holder.txtValidade.setTextColor(Color.DKGRAY);
            }

        } catch (Exception e) {
            // Se a data vier mal formatada, não rebenta a app
            holder.txtNome.setTextColor(Color.BLACK);
            holder.txtValidade.setTextColor(Color.DKGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    // ==========================
    // VIEW HOLDER
    // ==========================
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNome;
        TextView txtDescricao;
        TextView txtValidade;

        ViewHolder(View itemView) {
            super(itemView);

            txtNome = itemView.findViewById(R.id.txt_nome_produto);
            txtDescricao = itemView.findViewById(R.id.txt_desc_produto);
            txtValidade = itemView.findViewById(R.id.txt_validade_produto);
        }
    }
}
