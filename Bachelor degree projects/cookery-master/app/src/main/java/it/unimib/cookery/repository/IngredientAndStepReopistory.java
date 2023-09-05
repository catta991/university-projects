package it.unimib.cookery.repository;

import android.app.Application;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.List;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.database.IngredientRecipeDao;
import it.unimib.cookery.database.RecipeDao;
import it.unimib.cookery.database.RoomDatabase;
import it.unimib.cookery.database.StepDao;
import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.RecipeApi;
import it.unimib.cookery.models.StepApi;
import it.unimib.cookery.models.StepList;
import it.unimib.cookery.service.SpoonacularApiService;
import it.unimib.cookery.utils.ResponseCallbackDb;
import it.unimib.cookery.utils.ResponseCallbackStepAndIngredients;
import it.unimib.cookery.utils.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientAndStepReopistory {

    // da sistemare stringhe

    private SpoonacularApiService spoonacularApiService;
    private ResponseCallbackStepAndIngredients responseCallbackStepAndIngredients;
    private List<StepApi> stepApis;
    private List<IngredientApi> ingredientApis;
    private int servings;
    private Costants costants = new Costants();


    //variabili database
    private RecipeDao mRecipeDao;
    private StepDao mStepDao;
    private IngredientRecipeDao mIngredientDao;



    public IngredientAndStepReopistory(ResponseCallbackStepAndIngredients responseCallbackStepAndIngredients) {
        this.responseCallbackStepAndIngredients = responseCallbackStepAndIngredients;
        this.spoonacularApiService = ServiceLocator.getInstance().getSpoonacularApiService();

    }
    public IngredientAndStepReopistory(Application application, ResponseCallbackStepAndIngredients responseCallbackStepAndIngredients) {
        this.responseCallbackStepAndIngredients = responseCallbackStepAndIngredients;
        //this.spoonacularApiService = ServiceLocator.getInstance().getSpoonacularApiService();
        RoomDatabase roomDatabase = ServiceLocator.getInstance().getDao(application);
        this.mRecipeDao = roomDatabase.recipeDao();
        this.mStepDao = roomDatabase.recipeStepDao();
        this.mIngredientDao = roomDatabase.ingredientRecipeDao();
    }



    public void getRecipeSteps(int id) {


        Call<List<StepList>> RecipeSteps =
                spoonacularApiService.getRecipeSteps(id, costants.API_KEY);

        RecipeSteps.enqueue(new Callback<List<StepList>>() {
            @Override
            public void onResponse(Call<List<StepList>> call, Response<List<StepList>> response) {

                if(response.body() != null && response.isSuccessful()) {
                    Log.d("retrofit", ""+response.raw().request().url());
                    //  Log.d("retrofit", ""+response.body());

                    for(int i=0; i< response.body().size(); i++) {
                        if(i==0)
                            stepApis = response.body().get(i).getSteps();
                        else
                            stepApis.addAll(response.body().get(i).getSteps());
                    }
                    //  Log.d("retrofit", "sssss "+stepApis.size());

                //    Log.d("retrofit", ""+stepApis.toString());

                    responseCallbackStepAndIngredients.onResponseRecipeSteps(stepApis);
                }else
                    responseCallbackStepAndIngredients.onFailureIngredientAndStep(R.string.errorRetriveData);
            }


            @Override
            public void onFailure(Call<List<StepList>> call, Throwable t) {

                responseCallbackStepAndIngredients.onFailureIngredientAndStep(R.string.connectionError);
            }
        });


    }

    public void getRecipeIngredients(int id, boolean nutrition){

        Call<RecipeApi> RecipeIngredient =
                spoonacularApiService.getRecipeIngredients(id, costants.API_KEY, nutrition);

        RecipeIngredient.enqueue(new Callback<RecipeApi>() {
            @Override
            public void onResponse(Call<RecipeApi> call, Response<RecipeApi> response) {

                if (response.body() != null && response.isSuccessful()) {
                    Log.d("retrofit", "" + response.raw().request().url());
                    //  Log.d("retrofit", ""+response.body());

                    ingredientApis = response.body().getExtendedIngredients();
                    servings = response.body().getServings();
                    responseCallbackStepAndIngredients.onResponseRecipeIngredients(ingredientApis, servings);

                }
                //  Log.d("retrofit", "sssss "+stepApis.size());
                else
                    responseCallbackStepAndIngredients.onFailureIngredientAndStep(R.string.errorRetriveData);
            }

            @Override
            public void onFailure(Call<RecipeApi> call, Throwable t) {
                responseCallbackStepAndIngredients.onFailureIngredientAndStep(R.string.connectionError);
            }
        });




    }
    //Read
    public void readIngredientsAndStepsRecipe(long idRecipe){
        Log.d("ciao:","idRecipe: - "+idRecipe);
        Runnable runnable = new Runnable() {
            @Override
            public void  run() {
                /*for(int k=0; k < mIngredientDao.selectIngrediensByRecipeId(idRecipe).size();k++){
                    String  nome =mIngredientDao.selectIngrediensByRecipeId(idRecipe).get(k).getName();
                    Log.d("ciao:","nome: - "+nome);
                }*/
                Log.d("ciao:","chiamata al db"+mIngredientDao.selectIngrediensByRecipeId(idRecipe).size());
                responseCallbackStepAndIngredients.onResponseRecipeSteps(mStepDao.selectStepsByRecipeId(idRecipe));
                responseCallbackStepAndIngredients.onResponseRecipeIngredientsDb(mIngredientDao.selectIngrediensByRecipeId(idRecipe));
            }
        };
        new Thread(runnable).start();
    }
    //delete recipe
    public void deleteRecipe(long idRecipe){
        Runnable runnable = new Runnable() {
            @Override
            public void  run() {
                mStepDao.deleteByStepRecipe(idRecipe);
                mIngredientDao.deleteByIngredientRecipe(idRecipe);
                mRecipeDao.deleteByRecipe(idRecipe);
            }
        };
        new Thread(runnable).start();
    }

}
