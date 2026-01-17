package pt.ipleiria.estg.dei.amsi.homepantry.data.localdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {
        StockCacheEntity.class,
        ProdutoCacheEntity.class,
        ListaCacheEntity.class,
        ListaProdutoCacheEntity.class,
        LocalCacheEntity.class
}, version = 5, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ListaCacheDao listaCacheDao();


    public abstract StockCacheDao stockCacheDao();

    public abstract ListaProdutoCacheDao listaProdutoCacheDao();
    public abstract ProdutoCacheDao produtoCacheDao();
    public abstract LocalCacheDao localCacheDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "homepantry_cache_db"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}