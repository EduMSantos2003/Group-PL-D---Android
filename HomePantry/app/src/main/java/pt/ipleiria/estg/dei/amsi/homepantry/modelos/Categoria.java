package pt.ipleiria.estg.dei.amsi.homepantry.modelos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entidade Room que representa a tabela "categoria" na BD local
@Entity(tableName = "categoria")
public class Categoria {

    @PrimaryKey
    private int id;

    private String nome ;

    // Construtor vazio obrigatório para o Room
    public Categoria() {
    }

    // Construtor opcional, útil se quiseres criar objetos manualmente
    public Categoria(int id, String nome) {
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

    // O Spinner vai usar este texto para mostrar cada categoria
    @NonNull
    @Override
    public String toString() {
        return nome;
    }
}
