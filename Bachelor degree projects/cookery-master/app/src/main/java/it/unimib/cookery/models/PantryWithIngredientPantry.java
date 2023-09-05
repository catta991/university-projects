package it.unimib.cookery.models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

public class PantryWithIngredientPantry {
    @Embedded public Pantry pantry;
    @Relation(
            parentColumn = "idPantry",
            entityColumn = "pantryId"
    )
    public List<IngredientPantry> ingredientPantry;
}
