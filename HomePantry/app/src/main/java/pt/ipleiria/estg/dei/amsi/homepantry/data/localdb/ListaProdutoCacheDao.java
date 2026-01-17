package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListaProdutoCacheDao {

    @Query("SELECT * FROM lista_produtos_cache WHERE listaId = :listaId")
    List<ListaProdutoCacheEntity> getByLista(int listaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ListaProdutoCacheEntity> itens);

    @Query("DELETE FROM lista_produtos_cache WHERE listaId = :listaId")
    void deleteByLista(int listaId);
}
