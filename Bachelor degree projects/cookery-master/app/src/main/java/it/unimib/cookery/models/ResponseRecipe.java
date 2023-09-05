package it.unimib.cookery.models;

import java.util.List;

public class ResponseRecipe {

    List<RecipeApi> recipes;

    public ResponseRecipe() {
    }

    public ResponseRecipe(List<RecipeApi> recipes) {
        this.recipes = recipes;
    }


    public List<RecipeApi> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeApi> recipes) {
        this.recipes = recipes;
    }
}
