package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stock_cache")
public class StockCacheEntity {

    @PrimaryKey
    public int id;         // id do produto/stock

    public String nome;    // nome do produto
    public int quantidade; // quantidade
    public int casaId;     // para guardar cache por casa (importante)

    public StockCacheEntity(int id, String nome, int quantidade, int casaId) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.casaId = casaId;
    }
}
