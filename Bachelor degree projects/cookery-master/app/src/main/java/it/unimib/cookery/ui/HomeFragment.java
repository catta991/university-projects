package it.unimib.cookery.ui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unimib.cookery.R;

import it.unimib.cookery.adapters.RecipeAdapter;
import it.unimib.cookery.adapters.RecipeAdapterSubcard;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.databinding.FragmentHomeBinding;
import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.IngredientPantry;
import it.unimib.cookery.models.RecipeApi;
import it.unimib.cookery.repository.DatabasePantryRepository;
import it.unimib.cookery.repository.RecipeRepository;
import it.unimib.cookery.utils.ResponseCallbackApi;
import it.unimib.cookery.utils.ResponseCallbackDb;


public class HomeFragment extends Fragment implements ResponseCallbackApi, ResponseCallbackDb {

    private ArrayList<RecipeApi> recipeArrayListReadyToCoock;
    private ArrayList<RecipeApi> recipeArrayListDessert;
    private ArrayList<RecipeApi> recipeArrayListMainCourse;
    private ArrayList<RecipeApi> recipeArrayListFirstCourse;
    private ArrayList<IngredientPantry> ingredientPantries;

    private String ingredient = "";
    private  ArrayList<String> pantriesArrayList;
    private String tags="";
    private String intollerances;
    private String diet;
    private boolean preferencesModified;



    private Costants costants = new Costants();
    private boolean modified;


    private DatabasePantryRepository repositoryDatabase;


    private RecyclerView recyclerViewRTC;
    private RecyclerView recyclerViewHome2;
    private RecyclerView recyclerViewHome3;
    private RecyclerView recyclerViewHome4;
    private RecipeRepository recipeRepository;

    private RecipeAdapter recipeAdapter;
    private RecipeAdapterSubcard recipeAdapterSubcard1;
    private RecipeAdapterSubcard recipeAdapterSubcard2;
    private RecipeAdapterSubcard recipeAdapterSubcard3;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        recipeRepository = new RecipeRepository(getActivity().getApplication(), (ResponseCallbackApi) this);
        repositoryDatabase = new DatabasePantryRepository(getActivity().getApplication(), (ResponseCallbackDb) this);

        // da recuperare e passare le preferenze dell'utente per inserire le
        // preferenze
        // da mettere tag a tutti metodi


        recipeArrayListReadyToCoock = new ArrayList<>();
        recipeArrayListDessert = new ArrayList<>();
        recipeArrayListMainCourse = new ArrayList<>();
        recipeArrayListFirstCourse = new ArrayList<>();
        ingredientPantries = new ArrayList<>();

         setPreferences();




        repositoryDatabase.readAllIngredientPantry();

        // provare con shared preferences ogni volta che si modifica il pantry fare le chiamte


        //Snackbar.make(requireActivity().findViewById(android.R.id.content), "fffffff", Snackbar.LENGTH_SHORT).show();


    }


    public void setPreferences(){

        Log.d("retrofit", "set preferences");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(costants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

       intollerances="";
       diet="";
       tags="";

        intollerances = sharedPreferences.getString(costants.INTOLLERANCES, "");
        diet = sharedPreferences.getString(costants.DIET, "");

        if(!intollerances.equals("") && !diet.equals(""))
            tags = intollerances+","+diet;
        else if(!intollerances.equals("") && diet.equals(""))
            tags = intollerances;
        else if(intollerances.equals("") && !diet.equals(""))
            tags = diet;


        Log.d("retrofit", "tags "+tags);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("state", "onCreateView");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(costants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        modified = sharedPreferences.getBoolean(costants.MODIFIED, true);

        preferencesModified = sharedPreferences.getBoolean(costants.PREFERENCES_MODIFIED, false);

        Log.d("boolean", "" + modified);

        if (modified) {
            repositoryDatabase.readAllIngredientPantry();
            modified = false;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(costants.MODIFIED, modified);
            editor.apply();
        }

        if(preferencesModified){

            setPreferences();

            recipeRepository.getRandomRecipeFirstCourse(tags);
            recipeRepository.getRandomRecipeMainCourse(tags);
            recipeRepository.getRandomRecipeDessert(tags);

            preferencesModified = false;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(costants.PREFERENCES_MODIFIED, preferencesModified);
            editor.apply();
        }

        recyclerViewRTC = view.findViewById(R.id.recyclerViewRTC);
        recyclerViewHome2 = view.findViewById(R.id.recyclerViewHome2);
        recyclerViewHome3 = view.findViewById(R.id.recyclerViewHome3);
        recyclerViewHome4 = view.findViewById(R.id.recyclerViewHome4);

        LinearLayoutManager linearLayoutManagerRTC = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManagerHome2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManagerHome3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManagerHome4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRTC.setLayoutManager(linearLayoutManagerRTC);
        recyclerViewHome2.setLayoutManager(linearLayoutManagerHome2);
        recyclerViewHome3.setLayoutManager(linearLayoutManagerHome3);
        recyclerViewHome4.setLayoutManager(linearLayoutManagerHome4);


        if (recipeArrayListReadyToCoock.size() > 0 && recipeArrayListDessert.size() > 0 &&
                recipeArrayListMainCourse.size() > 0 && recipeArrayListFirstCourse.size() > 0
        ) {

            // da creare un variabile che mi dice se ci sono o meno ingredienti
            recipeAdapter = new RecipeAdapter(getContext(), recipeArrayListReadyToCoock, false, pantriesArrayList);
            recipeAdapterSubcard1 = new RecipeAdapterSubcard(getContext(), recipeArrayListDessert, pantriesArrayList);
            recipeAdapterSubcard2 = new RecipeAdapterSubcard(getContext(), recipeArrayListMainCourse, pantriesArrayList);
            recipeAdapterSubcard3 = new RecipeAdapterSubcard(getContext(), recipeArrayListFirstCourse, pantriesArrayList);
            recyclerViewRTC.setAdapter(recipeAdapter);
            recyclerViewHome4.setAdapter(recipeAdapterSubcard1);
            recyclerViewHome3.setAdapter(recipeAdapterSubcard2);
            recyclerViewHome2.setAdapter(recipeAdapterSubcard3);
        }

        return view;
    }


    @Override
    public void onResponseRandomRecipeDessert(List<RecipeApi> recipes) {
        recipeArrayListDessert.clear();
        recipeArrayListDessert.addAll(recipes);
        recipeAdapterSubcard1 = new RecipeAdapterSubcard(getContext(), recipeArrayListDessert, pantriesArrayList);
        recyclerViewHome4.setAdapter(recipeAdapterSubcard1);
    }

    @Override
    public void onResponseRandomRecipeMainCourse(List<RecipeApi> recipes) {
        recipeArrayListMainCourse.clear();
        recipeArrayListMainCourse.addAll(recipes);
        recipeAdapterSubcard2 = new RecipeAdapterSubcard(getContext(), recipeArrayListMainCourse, pantriesArrayList);
        recyclerViewHome3.setAdapter(recipeAdapterSubcard2);

    }

    @Override
    public void onResponseRandomRecipeFirstCourse(List<RecipeApi> recipes) {
        recipeArrayListFirstCourse.clear();
        recipeArrayListFirstCourse.addAll(recipes);
        for (RecipeApi r : recipeArrayListFirstCourse)
            Log.d("Gson", "FirstCourse " + r.toString());
        recipeAdapterSubcard3 = new RecipeAdapterSubcard(getContext(), recipeArrayListFirstCourse, pantriesArrayList);
        recyclerViewHome2.setAdapter(recipeAdapterSubcard3);

    }


    @Override
    public void onResponseRecipeByIngredient(List<RecipeApi> recipes) {

        recipeArrayListReadyToCoock.clear();
        recipeArrayListReadyToCoock.addAll(recipes);
        recipeAdapter = new RecipeAdapter(getContext(), recipeArrayListReadyToCoock, false, pantriesArrayList);
        recyclerViewRTC.setAdapter(recipeAdapter);

        // da fare sorting
    }

    @Override
    public void onResponseRandomRecipe(List<RecipeApi> recipes) {

        recipeArrayListReadyToCoock.clear();
        recipeArrayListReadyToCoock.addAll(recipes);
        recipeAdapter = new RecipeAdapter(getContext(), recipeArrayListReadyToCoock, true, pantriesArrayList);
        recyclerViewRTC.setAdapter(recipeAdapter);

    }

    @Override
    public void onFailure(int errorMessage) {

        Snackbar.make(requireActivity().findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object obj) {
         ingredientPantries = (ArrayList<IngredientPantry>) obj;
        String ingredient = "";

        if (ingredientPantries.size() > 0) {
            for (int i = 0; i < ingredientPantries.size() - 1; i++)
                ingredient += ingredientPantries.get(i).getName() + ",";
            ingredient += ingredientPantries.get(ingredientPantries.size() - 1).getName();
            Log.d("ingredient7", "" + ingredient);

           // da passare all'adapter e poi da passare alla single recipe activity
            // da riprovare col parcelable per gli ingredientiPantry
            pantriesArrayList = new ArrayList<String>(Arrays.asList(ingredient.split(",")));



            recipeRepository.getRecipeByIngredient(ingredient);
        } else {
            Log.d("ingredient7", "else");
            recipeRepository.getRandomRecipe(tags);
        }
        recipeRepository.getRandomRecipeFirstCourse(tags);
        recipeRepository.getRandomRecipeMainCourse(tags);
        recipeRepository.getRandomRecipeDessert(tags);

    }

    @Override
    public void onResponsePantry(Object obj) {

    }

    @Override
    public void onResponseSearchIngredient(Object obj) {
    }

    @Override
    public void onFailure(String errorMessage) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
    }
}