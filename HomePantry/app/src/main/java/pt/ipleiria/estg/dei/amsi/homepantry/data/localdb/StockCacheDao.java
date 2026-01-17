package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StockCacheDao {

    @Query("SELECT * FROM stock_cache WHERE casaId = :casaId")
    List<pt.ipleiria.estg.dei.amsi.homepantry.data.localdb.StockCacheEntity> getStockByCasa(int casaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<pt.ipleiria.estg.dei.amsi.homepantry.data.localdb.StockCacheEntity> stock);

    @Query("DELETE FROM stock_cache WHERE casaId = :casaId")
    void deleteByCasa(int casaId);
}
