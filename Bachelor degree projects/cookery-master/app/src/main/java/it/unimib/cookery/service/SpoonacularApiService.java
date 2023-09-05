package it.unimib.cookery.service;


import java.util.List;

import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.RecipeApi;
import it.unimib.cookery.models.ResponseRecipe;
import it.unimib.cookery.models.StepList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface SpoonacularApiService {

    @GET("recipes/random")
    Call<ResponseRecipe> getRandomRecipe (@Query("apiKey") String apiKey, @Query("number") int number, @Query("tags") String tags);

    @GET("recipes/random")
    Call<ResponseRecipe> getRandomRecipeNoTags (@Query("apiKey") String apiKey, @Query("number") int number);

// https://api.spoonacular.com/recipes/324694/analyzedInstructions

   // @GET("{id}/analyzedInstructions")
   // Call<String> getRecipeDetails (@Path("id") int id, @Query("apiKey") String apiKey);

   @GET("recipes/findByIngredients")
   Call<List<RecipeApi>> getRecipeByIngredient(@Query("apiKey") String apiKey, @Query("ingredients") String ingredients, @Query("number") int number);

    @GET("recipes/{id}/analyzedInstructions")
    Call<List<StepList>> getRecipeSteps (@Path("id") int id, @Query("apiKey") String apiKey);

    //https://api.spoonacular.com/recipes/716429/information?includeNutrition=false

    @GET("recipes/{id}/information")
    Call<RecipeApi> getRecipeIngredients (@Path("id") int id, @Query("apiKey") String apiKey, @Query("includeNutrition") boolean includeNutrition);

    @GET("food/ingredients/{id}/information")
    Call<IngredientApi> getIngredientMeasureUnit (@Path("id") int id, @Query("apiKey") String apiKey, @Query("amount") int amount );
}
