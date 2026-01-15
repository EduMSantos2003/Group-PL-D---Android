package pt.ipleiria.estg.dei.amsi.homepantry.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.homepantry.R;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private final ArrayList<Categoria> categorias;

    public CategoriaAdapter(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria c = categorias.get(position);
        holder.txtNome.setText(c.getNome());
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    // ✅ Atualiza o conteúdo do RecyclerView
    public void setCategorias(ArrayList<Categoria> novas) {
        categorias.clear();
        categorias.addAll(novas);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txt_nome_categoria);
        }
    }
}

