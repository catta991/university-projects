package it.unimib.cookery.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredient_recipe")
public class IngredientRecipe extends IngredientApi {



    private double amount;
    private long idRecipe;
    private String unit;

    public IngredientRecipe(int id, String name,double amount,String unit) {
        super(id, name);
        this.amount =amount;
        this.unit = unit;
    }


    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(long idREcipe) {
        this.idRecipe = idREcipe;
    }
}