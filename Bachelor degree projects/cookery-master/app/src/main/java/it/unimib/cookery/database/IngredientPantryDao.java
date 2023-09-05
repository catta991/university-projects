package it.unimib.cookery.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import it.unimib.cookery.models.IngredientPantry;
import it.unimib.cookery.models.Pantry;
import it.unimib.cookery.models.PantryWithIngredientPantry;


/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface IngredientPantryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredientPantry(IngredientPantry... pantry);

    @Query("SELECT * FROM  ingredient_pantry")
    public List<IngredientPantry> ingredientPantryALL();

    @Update
    public void update(IngredientPantry... users);

    @Query("DELETE FROM ingredient_pantry WHERE idIngredient = :idPantry")
    public void deleteByIngredientPantry(int idPantry);
}

