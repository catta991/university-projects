package it.unimib.cookery.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.unimib.cookery.models.Recipe;


/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecipe(Recipe recipe);

    @Query("SELECT * FROM recipe")
    List<Recipe> getAll();

    @Query("DELETE FROM recipe WHERE idDb = :idRecipe")
    public void deleteByRecipe(long idRecipe);
   /*
    @Delete
    void deleteAllWithoutQuery(Recipe... recipe);*/
}

