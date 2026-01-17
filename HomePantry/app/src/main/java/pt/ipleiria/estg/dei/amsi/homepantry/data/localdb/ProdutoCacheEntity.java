package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "produtos_cache")
public class ProdutoCacheEntity {

    @PrimaryKey
    public int id;

    public String nome;
    public String descricao;

    public ProdutoCacheEntity(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }
}
