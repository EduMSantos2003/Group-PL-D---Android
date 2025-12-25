package pt.ipleiria.estg.dei.amsi.homepantry.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Casa;

// DAO = Data Access Object da tabela "casa"
@Dao
public interface CasaDao {

    // Devolve todas as casas guardadas na BD local
    @Query("SELECT * FROM casa")
    LiveData<List<Casa>> getAllCasas();
}
