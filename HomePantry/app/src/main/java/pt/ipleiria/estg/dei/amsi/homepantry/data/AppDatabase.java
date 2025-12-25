package pt.ipleiria.estg.dei.amsi.homepantry.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Casa;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;
import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Produto;

// Base de dados Room da app (tabelas locais)
@Database(entities = {Casa.class, Categoria.class, Produto.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Singleton (apenas uma instância da BD em toda a app)
    private static volatile AppDatabase INSTANCE;

    // DAOs que dão acesso às tabelas
    public abstract CasaDao casaDao();
    public abstract CategoriaDao categoriaDao();
    public abstract ProdutoDao produtoDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "homepantry_db"   // nome do ficheiro da BD
                    )
                    .fallbackToDestructiveMigration()// Permite recriar a base de dados quando há alterações no schema,
                    // evitando erros de migration durante a fase de desenvolvimento.
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
