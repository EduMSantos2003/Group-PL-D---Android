package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListaCacheDao {

    @Query("SELECT * FROM listas_cache WHERE casaId = :casaId")
    List<ListaCacheEntity> getByCasa(int casaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ListaCacheEntity> listas);

    @Query("DELETE FROM listas_cache WHERE casaId = :casaId")
    void deleteByCasa(int casaId);
}
