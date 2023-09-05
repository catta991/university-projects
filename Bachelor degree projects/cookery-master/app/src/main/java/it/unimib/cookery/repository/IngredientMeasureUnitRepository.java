package it.unimib.cookery.repository;

import android.util.Log;

import java.util.ArrayList;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.service.SpoonacularApiService;
import it.unimib.cookery.utils.IngredientUnitMeasureResponseCallback;
import it.unimib.cookery.utils.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientMeasureUnitRepository {




    SpoonacularApiService spoonacularApiService;
    IngredientUnitMeasureResponseCallback ingredientUnitMeasureResponseCallback;
    private ArrayList<String> possibleUnits;
    Costants costants = new Costants();





    public IngredientMeasureUnitRepository(IngredientUnitMeasureResponseCallback ingredientUnitMeasureResponseCallback) {
        spoonacularApiService= ServiceLocator.getInstance().getSpoonacularApiService();
        this.ingredientUnitMeasureResponseCallback = ingredientUnitMeasureResponseCallback;
    }


    public void getMeasure(int id, int idChip){

        Call<IngredientApi> IngredintUnitMeasure =
                spoonacularApiService.getIngredientMeasureUnit(id, costants.API_KEY, 1);


        IngredintUnitMeasure.enqueue(new Callback<IngredientApi>() {
            @Override
            public void onResponse(Call<IngredientApi> call, Response<IngredientApi> response) {

                Log.d("retrofit", "" + response.raw().request().url());

                if(response.body() != null && response.isSuccessful()) {

                    possibleUnits = response.body().getPossibleUnits();

                    ingredientUnitMeasureResponseCallback.getUnitMeasureResponse(possibleUnits, idChip);
                }else
                    ingredientUnitMeasureResponseCallback.onFailure(R.string.errorRetriveData);

            }

            @Override
            public void onFailure(Call<IngredientApi> call, Throwable t) {

                Log.d("retrofit", "on Failure "+ t);

                ingredientUnitMeasureResponseCallback.onFailure(R.string.connectionError);
            }
        });

    }


}
