package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocalCacheDao {

    @Query("SELECT * FROM locais_cache ORDER BY nome")
    List<LocalCacheEntity> getAllLocais();

    @Query("SELECT * FROM locais_cache WHERE id = :id LIMIT 1")
    LocalCacheEntity getLocalById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocal(LocalCacheEntity local);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocais(List<LocalCacheEntity> locais);

    @Update
    void updateLocal(LocalCacheEntity local);

    @Delete
    void deleteLocal(LocalCacheEntity local);

    @Query("DELETE FROM locais_cache")
    void clearAll();
}
