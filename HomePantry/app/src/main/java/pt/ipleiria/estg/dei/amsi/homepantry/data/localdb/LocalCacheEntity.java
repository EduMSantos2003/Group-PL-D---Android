package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locais_cache")
public class LocalCacheEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;          // mesmo ID que vem da API

    private String nome;
    private String descricao;
    private String fotoPath; // pode ser null

    // Construtor vazio obrigatório para Room
    public LocalCacheEntity() {
    }

    public LocalCacheEntity(int id, String nome, String descricao, String fotoPath) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.fotoPath = fotoPath;
    }

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getFotoPath() { return fotoPath; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }
}
