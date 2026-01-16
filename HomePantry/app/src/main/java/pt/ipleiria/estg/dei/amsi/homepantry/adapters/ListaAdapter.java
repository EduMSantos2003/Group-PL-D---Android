package pt.ipleiria.estg.dei.amsi.homepantry.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.R;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Lista;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {

    private List<Lista> listas;

    public interface OnListaClickListener {
        void onListaClick(Lista lista);
    }

    private OnListaClickListener listener;

    public ListaAdapter(List<Lista> listas, OnListaClickListener listener) {
        this.listas = listas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lista lista = listas.get(position);
        holder.txtNome.setText(lista.getNome());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onListaClick(lista);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeLista);
        }
    }
}
