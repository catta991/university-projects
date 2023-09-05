package it.unimib.cookery.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toolbar;


import com.google.android.material.chip.Chip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.IngredientPantry;
import it.unimib.cookery.models.Recipe;
import it.unimib.cookery.adapters.AdapterClass;
import it.unimib.cookery.repository.DatabasePantryRepository;
import it.unimib.cookery.repository.RecipeRepository;
import it.unimib.cookery.utils.ResponseCallbackDb;


public class MyRecipesFragment extends Fragment implements ResponseCallbackDb {


    /* robe di comodo */
    private String nomeRicettaTest = " pasta al forno";
    private static final String TAG = "premuto";

    /* fine robe di comodo */
    /* ottengo le stringhe costanti per dialog filtri */
    private Costants myRecipeCostants = new Costants();


    /* dichiaro un oggetto di tipoGridView */
    private GridView myRecipiesGridView;

    /* dichiaro un oggetto di tipo adapter */
    private AdapterClass adapter;

    /* dichiaro un oggetto di tipo SearchView */
    private SearchView recipiesSearch;

    /* dichiaro un oggetto di tipo chip*/
    private Chip chip;

    /* dichiaro un oggetto di tipo textButton*/
    private Button buttonFilter;
    private ArrayList<IngredientPantry> ingredientPantries;
    private ArrayList<String> igredientPantriesString = new ArrayList<>();

    /* crea l'array list di elementi da mostrare nell gridView */
    ArrayList<Recipe> recipeArrayList = new ArrayList<Recipe>();

    /* array list per i filtri */
    ArrayList<String> filterList = new ArrayList<>();

    static private RecipeRepository db;
     private DatabasePantryRepository dbPantry;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("state", "on create view");
        //connessione al db e caricamento delle ricette
        db = new RecipeRepository(requireActivity().getApplication(), this);


        dbPantry = new DatabasePantryRepository(requireActivity().getApplication(), this);
        dbPantry.readAllIngredientPantryTest();

        // pulizia preventiva perchè a volte le duplica non so perchè
        recipeArrayList.clear();

        /* creo un elemento di tipo view */
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);


        // --- inizio codice gridview --

        /* ottengo la Gridview */
        myRecipiesGridView = view.findViewById(R.id.gridView);


        /* aggiungo gli elementi alla gridView */
        // recipeArrayList = risultato query database



        /* creo l'oggetto adapter e lo inizializzo con la view corrente e con l'array list*/
        adapter = new AdapterClass(getContext(), recipeArrayList); /* al posto di recipeArrayList si metterà il risultato della query al database */

        /*associo alla gridView l'adapter creato sopra */
        myRecipiesGridView.setAdapter(adapter);



        /* serve per intercettare l'oggetto premuto */
        myRecipiesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* stampa di debug */
                Log.d(TAG, "PREMUTO " + recipeArrayList.get(position).getIdDb());

                // quando schiaccio una card crea l'intent e salva le
                // informazioni da passare all'activity SingleRecipeActivity il back stack è gestito in automatico
                Intent intent = new Intent(getActivity(), SingleRecipeActivity.class);
                // da aggiungere passaggio id ricetta

                intent.putExtra(myRecipeCostants.RECIPE_NAME,recipeArrayList.get(position).getName());
                intent.putExtra(myRecipeCostants.TYPE, "true");
                intent.putExtra(String.valueOf(myRecipeCostants.ID_RECIPE_DB),recipeArrayList.get(position).getIdDb());
                intent.putExtra(myRecipeCostants.RECIPE_IMAGE,recipeArrayList.get(position).getImageUrl());
                intent.putExtra(myRecipeCostants.RECIPE_SERVINGS, recipeArrayList.get(position).getnPerson());
                intent.putStringArrayListExtra(myRecipeCostants.PANTRY, igredientPantriesString);
                // starta l'activity
                startActivity(intent);


            }
        });

        //-- fine codice grid view --


        //-- inizio codice searchView --

        /* ottengo la search view */
        recipiesSearch = view.findViewById(R.id.searchViewMyRecipes);

        /* listener per la query */

        recipiesSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });

        //-- fine codice searchView--


        // -- inizio codice chip --

        /* ottengo l'elemento chip */
        chip = view.findViewById(R.id.addChip);


        /* listener dell'oggetto chip*/
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("premuto", "premuto chip add");
                startActivity(new Intent(getContext(), MakeRecipe.class));
                /* fare update database*/
                //creazione della ricetta
                // aggiungo la ricetta e la mostro sulla gridview
                //recipeArrayList.add(new Recipe("aggiunta", "Desserts", R.drawable.ic_baseline_add_24));
                // adapter=new AdapterClass(getContext(), recipeArrayList);
                myRecipiesGridView.setAdapter(adapter);




            }
        });

        // -- fine codice chip --


        // --inizio codice button filter --

        // array di nomi che verranno mostrati sulla dialog
        String[] filterArray = {myRecipeCostants.FILTER0, myRecipeCostants.FILTER1, myRecipeCostants.FILTER2,
                myRecipeCostants.FILTER3, myRecipeCostants.FILTER4};

        // serve per salvare se un elemento è selezionato o meno
        boolean[] selectedFilter = new boolean[filterArray.length];


        /* ottengo l'oggetto button */
        buttonFilter = view.findViewById(R.id.buttonFilter);


        /* listener dell'oggetto buttonFilter */
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("premuto", "premuto button filter");

                // inizializza l'alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // setta il titolo
                builder.setTitle(myRecipeCostants.dialogTitle);

                // messo a false non permette di uscire dalla dialog se non premendo sul tasto close
                builder.setCancelable(false);

                // creo il menu a scelta multipla
                builder.setMultiChoiceItems(filterArray, selectedFilter, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        String filter = "";
                        switch (i) {
                            case 0:
                                filter = myRecipeCostants.FILTER0;
                                break;
                            case 1:
                                filter = myRecipeCostants.FILTER1;
                                break;
                            case 2:
                                filter = myRecipeCostants.FILTER2;
                                break;
                            case 3:
                                filter = myRecipeCostants.FILTER3;
                                break;
                            case 4:
                                filter = myRecipeCostants.FILTER4;
                                break;

                        }


                        // check condition
                        if (b) {
                            // quando la checkbox è selezionata
                            // creo l'elemento da aggiungere
                            // lo aggiungo
                            filterList.add(filter);


                        } else {
                            // quando deseleziono la checkbox rimuovo l'oggetto dalla lista
                            filterList.remove(filter);

                        }
                    }
                });

                // crea bottone ok
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // se premuto ok far partire la ricerca
                        adapter.applyFilter(filterList);

                    }

                });


                // crea bottone close
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // se premuto colose chiude l'alert dialog
                        dialogInterface.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                // mostra l'alert dialog
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#54B3AB"));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#54B3AB"));





            }
        });


        // -- fine codice button filter --
        // Inflate the layout for this fragment
        return view;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onResponse(Object obj) {
        if (obj != null) {
            if (obj instanceof List) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recipeArrayList.clear();
                        recipeArrayList = (ArrayList) obj;
                        adapter = new AdapterClass(getContext(), recipeArrayList);
                        myRecipiesGridView.setAdapter(adapter);

                    }
                });
            }
        }


    }

    @Override
    public void onResponsePantry(Object obj) {
        igredientPantriesString.clear();
        ingredientPantries = (ArrayList<IngredientPantry>) obj;
        if (ingredientPantries.size() > 0){
            for(int k = 0; k < ingredientPantries.size(); k++){
                igredientPantriesString.add(ingredientPantries.get(k).getName());
            }
        }
        db.readAllRecipe();
    }

    @Override
    public void onResponseSearchIngredient(Object obj) {

    }


    @Override
    public void onFailure(String errorMessage) {

    }

    @Override
    public void onResume() {
        super.onResume();
        db.readAllRecipe();
    }
}