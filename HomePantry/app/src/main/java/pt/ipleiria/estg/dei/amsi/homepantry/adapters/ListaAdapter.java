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

    // Click normal (abrir lista)
    public interface OnListaClickListener {
        void onListaClick(Lista lista);
    }

    // Long click (editar/apagar)
    public interface OnListaLongClickListener {
        void onListaLongClick(Lista lista);
    }

    private OnListaClickListener clickListener;
    private OnListaLongClickListener longClickListener;

    public ListaAdapter(List<Lista> listas,
                        OnListaClickListener clickListener,
                        OnListaLongClickListener longClickListener) {
        this.listas = listas;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
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

        //  click normal
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onListaClick(lista);
            }
        });

        //  long click
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onListaLongClick(lista);
                return true;
            }
            return false;
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
