package it.unimib.cookery.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "RecipeStep")
public class StepApi {

    @PrimaryKey(autoGenerate = true)
    private int idStep;
    private long recipeId;

    private int number;
    private String step;

   @Ignore
    public StepApi() {
    }

    public StepApi(int number, String step) {
        this.number = number;
        this.step = step;
    }
    @Ignore
    public StepApi(String description) {
        this.step = description;
    }


    @Override
    public String toString() {
        return "StepApi{" +
                "number=" + number +
                ", steps='" + step + '\'' +
                '}';
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String steps) {
        this.step = steps;
    }

    public int getIdStep() {
        return idStep;
    }

    public void setIdStep(int idStep) {
        this.idStep = idStep;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
