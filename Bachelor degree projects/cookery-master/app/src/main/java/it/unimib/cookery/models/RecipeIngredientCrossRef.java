package it.unimib.cookery.models;

import androidx.room.Entity;

@Entity(primaryKeys = {"idRecipe", "idIngredient"})
public class RecipeIngredientCrossRef {
    public long idRecipe;
    public long idIngredient;
}
