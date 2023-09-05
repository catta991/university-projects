package it.unimib.cookery.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.IngredientPantry;


/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface IngredientApiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredientApi(IngredientApi... ingredientApi);

   @Query("SELECT * FROM  ingredient_api")
    public List<IngredientApi> ingredientApiALL();

    @Query("SELECT * FROM ingredient_api WHERE name LIKE '%' || :search || '%' ")
    public List<IngredientApi> findIngredientsWithName(String search);

}

