package it.unimib.cookery.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.unimib.cookery.models.IngredientRecipe;


/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface IngredientRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredient(IngredientRecipe... ingredientRecipe);

    @Query("SELECT * FROM ingredient_recipe WHERE IdRecipe = :idRecipe")
    List<IngredientRecipe> selectIngrediensByRecipeId(long idRecipe);


    @Query("DELETE FROM ingredient_recipe WHERE idRecipe = :idRecipe")
    public void deleteByIngredientRecipe(long idRecipe);

    /*@Query("SELECT * FROM RecipeStep")

    @Delete
    void deleteAllWithoutQuery(RecipeStep... recipeStep);*/
}
