package pt.ipleiria.estg.dei.amsi.homepantry.modelos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entidade Room que representa a tabela "casa" na BD local
@Entity(tableName = "casa")
public class Casa {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;

    // Construtor vazio obrigatório para o Room
    public Casa() {
    }

    // Construtor opcional, útil se quiseres criar objetos manualmente
    public Casa(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // O dropdown (AutoCompleteTextView) usa este texto para mostrar cada Casa
    @NonNull
    @Override
    public String toString() {
        return nome;
    }
}
