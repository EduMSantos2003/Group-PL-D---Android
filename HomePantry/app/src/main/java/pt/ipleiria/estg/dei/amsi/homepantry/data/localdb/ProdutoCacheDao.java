package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProdutoCacheDao {

    @Query("SELECT * FROM produtos_cache")
    List<ProdutoCacheEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProdutoCacheEntity> produtos);

    @Query("DELETE FROM produtos_cache")
    void deleteAll();
}
