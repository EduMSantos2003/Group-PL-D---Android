package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoriaCacheDao {

    @Query("SELECT * FROM categorias_cache")
    List<CategoriaCacheEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoriaCacheEntity> categorias);

    @Query("DELETE FROM categorias_cache")
    void clearAll();
}
