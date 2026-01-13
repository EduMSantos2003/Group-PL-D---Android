package pt.ipleiria.estg.dei.amsi.homepantry.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.R;
import pt.ipleiria.estg.dei.amsi.homepantry.listeners.LocalListener;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalViewHolder> {

    private List<Local> listaLocais;
    private LocalListener listener;

    // ✅ CONSTRUTOR CORRETO
    public LocalAdapter(List<Local> listaLocais, LocalListener listener) {
        this.listaLocais = listaLocais;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_local, parent, false);

        return new LocalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalViewHolder holder, int position) {

        Local local = listaLocais.get(position);

        holder.txtNome.setText(local.getNome());
        holder.txtDescricao.setText(local.getDescricao());

        // FOTO (se existir)
        if (local.getFotoPath() != null) {
            File file = new File(local.getFotoPath());
            if (file.exists()) {
                holder.imgFoto.setImageURI(Uri.fromFile(file));
            } else {
                holder.imgFoto.setImageResource(android.R.color.darker_gray);
            }
        } else {
            holder.imgFoto.setImageResource(android.R.color.darker_gray);
        }

        // ✅ CLIQUE NO LOCAL
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLocalClick(
                        local.getId(),
                        local.getNome()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaLocais.size();
    }

    // ======================================================
    // VIEW HOLDER
    // ======================================================
    static class LocalViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFoto;
        TextView txtNome;
        TextView txtDescricao;

        public LocalViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFoto = itemView.findViewById(R.id.img_item_local);
            txtNome = itemView.findViewById(R.id.txt_nome_local);
            txtDescricao = itemView.findViewById(R.id.txt_descricao_local);
        }
    }
}
