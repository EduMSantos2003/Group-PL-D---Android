package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorias_cache")
public class CategoriaCacheEntity {

    @PrimaryKey
    public int id;

    public String nome;

    public CategoriaCacheEntity(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
