package it.unimib.cookery.utils;

import java.util.List;

import it.unimib.cookery.models.RecipeApi;

public interface ResponseCallbackApi {


    void onResponseRandomRecipeDessert(List<RecipeApi> recipes);
    void onResponseRandomRecipeMainCourse(List<RecipeApi> recipes);
    void onResponseRandomRecipeFirstCourse(List<RecipeApi> recipes);
    void onResponseRecipeByIngredient(List<RecipeApi> recipes);
    void onResponseRandomRecipe(List<RecipeApi> recipes);

    void onFailure(int errorMessage);
}
