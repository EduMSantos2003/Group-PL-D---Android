package pt.ipleiria.estg.dei.amsi.homepantry;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.adapters.ListaAdapter;
import pt.ipleiria.estg.dei.amsi.homepantry.api.RetrofitClient;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Lista;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaListasFragment extends Fragment {

    private RecyclerView rvListas;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_listas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvListas = view.findViewById(R.id.rv_listas);
        rvListas.setLayoutManager(new LinearLayoutManager(getContext()));

        //  Botão + (POST): abre dialog para criar
        ImageButton btnAdicionarLista = view.findViewById(R.id.btn_adicionar_lista);
        if (btnAdicionarLista != null) {
            btnAdicionarLista.setOnClickListener(v -> abrirDialogLista(null));
        }

        carregarListas();
    }

    // ============================================================
    // DIALOG (Create / Update / Delete)
    // ============================================================
    private void abrirDialogLista(@Nullable Lista lista) {
        boolean editar = (lista != null);

        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_lista, null);

        EditText edtNome = dialogView.findViewById(R.id.edtNomeLista);
        EditText edtTipo = dialogView.findViewById(R.id.edtTipoLista);

        if (editar) {
            edtNome.setText(lista.getNome());
            edtTipo.setText(lista.getTipo());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(editar ? "Editar Lista" : "Criar Lista");
        builder.setView(dialogView);

        builder.setPositiveButton(editar ? "Guardar" : "Criar", (d, which) -> {
            String nome = edtNome.getText().toString().trim();
            String tipo = edtTipo.getText().toString().trim();

            if (nome.isEmpty() || tipo.isEmpty()) {
                Toast.makeText(getContext(), "Preenche nome e tipo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (editar) {
                Lista editada = new Lista();
                editada.setNome(nome);
                editada.setTipo(tipo);
                fazerPUT(lista.getId(), editada);
            } else {
                Lista nova = new Lista();
                nova.setNome(nome);
                nova.setTipo(tipo);
                fazerPOST(nova);
            }
        });

        if (editar) {
            builder.setNeutralButton("Apagar", (d, which) -> fazerDELETE(lista.getId()));
        }

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ============================================================
    // PUT
    // ============================================================
    private void fazerPUT(int id, Lista editada) {
        RetrofitClient.getApiService()
                .updateLista(id, editada)
                .enqueue(new Callback<Lista>() {
                    @Override
                    public void onResponse(Call<Lista> call, Response<Lista> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Lista atualizada!", Toast.LENGTH_SHORT).show();
                            carregarListas();
                        } else {
                            Toast.makeText(getContext(), "Erro ao atualizar: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Lista> call, Throwable t) {
                        Toast.makeText(getContext(), "Falha: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ============================================================
    //  POST
    // ============================================================
    private void fazerPOST(Lista nova) {
        RetrofitClient.getApiService()
                .createLista(nova)
                .enqueue(new Callback<Lista>() {
                    @Override
                    public void onResponse(Call<Lista> call, Response<Lista> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Lista criada!", Toast.LENGTH_SHORT).show();
                            carregarListas();
                        } else {
                            Toast.makeText(getContext(), "Erro ao criar: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Lista> call, Throwable t) {
                        Toast.makeText(getContext(), "Falha: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ============================================================
    // DELETE
    // ============================================================
    private void fazerDELETE(int id) {
        RetrofitClient.getApiService()
                .deleteLista(id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 204) {
                            Toast.makeText(getContext(), "Lista apagada!", Toast.LENGTH_SHORT).show();
                            carregarListas();
                        } else if (response.code() == 409) {
                            Toast.makeText(getContext(),
                                    "Não podes apagar: a lista tem produtos!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(),
                                    "Erro ao apagar: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Falha: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ============================================================
    // GET LISTAS + CLIQUE / LONG CLICK
    // ============================================================
    private void carregarListas() {
        int casaId = 1;

        RetrofitClient.getApiService()
                .getListas(casaId)
                .enqueue(new Callback<List<Lista>>() {

                    @Override
                    public void onResponse(Call<List<Lista>> call, Response<List<Lista>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {

                            rvListas.setAdapter(new ListaAdapter(
                                    response.body(),

                                    // clique normal -> abrir lista
                                    lista -> {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("listaId", lista.getId());
                                        bundle.putString("nomeLista", lista.getNome());

                                        NavHostFragment.findNavController(ListaListasFragment.this)
                                                .navigate(R.id.action_ListaListasFragment_to_ListaComprasFragment, bundle);
                                    },

                                    // long click -> editar/apagar (dialog)
                                    lista -> abrirDialogLista(lista)
                            ));

                        } else {
                            Toast.makeText(getContext(),
                                    "Erro ao carregar listas. Código: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Lista>> call, Throwable t) {
                        if (!isAdded()) return;

                        Toast.makeText(getContext(),
                                "Falha Retrofit: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();

                        Log.e("LISTAS", "Erro Retrofit", t);
                    }
                });
    }
}
