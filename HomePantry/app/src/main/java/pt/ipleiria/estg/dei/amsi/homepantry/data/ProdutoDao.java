package pt.ipleiria.estg.dei.amsi.homepantry.data;

import androidx.room.Dao;
import androidx.room.Insert;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

@Dao
public interface ProdutoDao {

    @Insert
    long insert(Produto produto);
}
