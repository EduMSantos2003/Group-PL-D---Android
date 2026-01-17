package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lista_produtos_cache")
public class ListaProdutoCacheEntity {

    @PrimaryKey
    public int id; // id do lista-produto (tabela da API)

    public int listaId;
    public int produtoId;
    public String produtoNome;

    public double quantidade;

    public ListaProdutoCacheEntity(int id, int listaId, int produtoId, String produtoNome, double quantidade) {
        this.id = id;
        this.listaId = listaId;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.quantidade = quantidade;
    }
}
