package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "listas_cache")
public class ListaCacheEntity {

    @PrimaryKey
    public int id;

    public String nome;
    public String tipo;
    public int casaId;

    public ListaCacheEntity(int id, String nome, String tipo, int casaId) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.casaId = casaId;
    }
}
