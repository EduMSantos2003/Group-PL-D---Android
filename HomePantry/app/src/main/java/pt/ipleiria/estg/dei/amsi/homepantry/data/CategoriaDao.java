package pt.ipleiria.estg.dei.amsi.homepantry.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Categoria;

@Dao
public interface CategoriaDao {

    @Query("SELECT * FROM categoria")  // tableName da entidade Categoria
    LiveData<List<Categoria>> getAllCategorias();
}
