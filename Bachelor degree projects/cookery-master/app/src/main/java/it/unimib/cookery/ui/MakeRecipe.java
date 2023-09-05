package it.unimib.cookery.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.cookery.R;
import it.unimib.cookery.adapters.IngredientChipAdapter;
import it.unimib.cookery.adapters.MakeRecipeSearchAdapter;
import it.unimib.cookery.adapters.RecipeProcedureAdapter;
import it.unimib.cookery.adapters.SearchChipAdapter;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.IngredientRecipe;
import it.unimib.cookery.models.Recipe;
import it.unimib.cookery.models.StepApi;
import it.unimib.cookery.models.StepApi;
import it.unimib.cookery.repository.DatabasePantryRepository;
import it.unimib.cookery.repository.RecipeRepository;
import it.unimib.cookery.utils.ResponseCallbackDb;

public class MakeRecipe extends AppCompatActivity implements ResponseCallbackDb {

    private Button searchIngredientBtn, addStepBtn, saveBtn, saveBtnStep, saveRecipe, deleteBtn;

    private RecyclerView ingredientListRV, addIngredientListRV, stepRV;

    private MakeRecipeSearchAdapter searchChipAdapter;
    private IngredientChipAdapter ingredientChipAdapter;
    private RecipeProcedureAdapter stepAdapter;

    private Dialog ingredientDialog;
    private Dialog stepDialog;

    private String description;

    private StepApi step;

    private SearchView searchView;

    private RecipeRepository db;
    private DatabasePantryRepository dbSearch;

    private EditText addStepEt, recipeNameEt, numServ;

    private ImageView recipeImage;
    private TextView recipeImageTextView;
    private Uri imageUri;
    private String uriImageString = "";

    private Spinner typeSpinner;

    private static ArrayList<IngredientApi> ingredientsList = new ArrayList<>();
    private static ArrayList<IngredientRecipe> ingredientsListDb = new ArrayList<>();
    private Costants costants = new Costants();

    private static ArrayList<StepApi> stepsList = new ArrayList<>();
    private static ArrayList<String> stepsListString = new ArrayList<>();
    private static ArrayList<Recipe> recipesList = new ArrayList<>();


    private ActivityResultLauncher<Intent> activityResultLauncher;


    public static void updateArrayList(IngredientApi ingredient) {
        IngredientRecipe ingredientRecipe;
        ingredientRecipe = new IngredientRecipe(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getAmount(),
                ingredient.getUnit()
        );
        ingredientsListDb.add(ingredientRecipe);
        ingredientsList.add(ingredient);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_recipe);

        // initializing RV and views
        addStepBtn = findViewById(R.id.add_step_button);
        searchIngredientBtn = findViewById(R.id.ingredient_button);
        ingredientListRV = findViewById(R.id.ingredient_list);
        stepRV = findViewById(R.id.step_list);
        recipeNameEt = findViewById(R.id.make_recipe_name);
        typeSpinner = findViewById(R.id.type_spinner);
        numServ = findViewById(R.id.numServ);
        saveRecipe = findViewById(R.id.save_recipe);
        recipeImage = findViewById(R.id.recipeImage);
        recipeImageTextView = findViewById(R.id.chooseImageText);


        //setting up typeSpinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        //setting db
        db = new RecipeRepository(getApplication(), this);
        dbSearch = new DatabasePantryRepository(getApplication(), this);
        //saving recipe
        saveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            //todo settare il controllo per gli step e gli ingredienti vuoti
            public void onClick(View v) {
                if (recipeNameEt.getText().toString().isEmpty()) {
                    Log.d("test", ": " + recipeNameEt.getText().toString());
                    recipeNameEt.setError(costants.NAME_ERROR);
                    recipeNameEt.requestFocus();
                    return;
                }
                if (numServ.getText().toString().isEmpty()) {
                    numServ.setError(costants.NUMBER_PEOPLE_ERROR);
                    numServ.requestFocus();
                    return;

                }
                if (stepsList.isEmpty() || ingredientsList.isEmpty()){
                    Toast.makeText(MakeRecipe.this, R.string.empty_field, Toast.LENGTH_LONG).show();
                    return;
                }
                Recipe newRecipe = new Recipe(recipeNameEt.getText().toString(), typeSpinner.getSelectedItem().toString(), uriImageString, Integer.parseInt(numServ.getText().toString()));
                //setting up db
                makeDb(newRecipe);
                finish();
            }
        });

        //setting adapters
        searchChipAdapter = new MakeRecipeSearchAdapter();
        ingredientChipAdapter = new IngredientChipAdapter();
        stepAdapter = new RecipeProcedureAdapter();


        //add ingredient
        searchIngredientBtn.setOnClickListener(v -> {
            openDialogAddIngredient(v);
        });

        //add step
        addStepBtn.setOnClickListener(v -> {
            openDialogAddStep(v);
        });



        // todo controllare vari file e settare le stringhe a costanti eccetera
        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, costants.STORAGE_PERMISSION_CODE);
            }
        });

        recipeImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, costants.STORAGE_PERMISSION_CODE);
            }
        });


        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            imageUri = data.getData();

                            getContentResolver().takePersistableUriPermission(imageUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                            uriImageString = imageUri.toString();
                            Glide.with(getApplicationContext())
                                    .load(imageUri.toString())
                                    .into(recipeImage);

                            recipeImageTextView.setVisibility(View.GONE);
                        }
                    }
                });

    }


    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }


    private void checkPermission(String permission, int requestCode) {

        if (ContextCompat.checkSelfPermission(MakeRecipe.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MakeRecipe.this, new String[]{permission}, requestCode);
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == costants.STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }


    private void makeDb(Recipe recipe) {
        //salvataggio della ricetta
        db.createRecipe(recipe, ingredientsListDb, stepsList);


    }

    private void openDialogAddStep(View view) {
        stepDialog = new Dialog(view.getContext());
        stepDialog.setContentView(R.layout.add_step_dialog);
        stepDialog.setCancelable(false);
        stepDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //setting steps RV
        LinearLayoutManager flexboxLayoutManagerStepListRv = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        stepRV.setLayoutManager(flexboxLayoutManagerStepListRv);
        stepRV.setFocusable(false);
        stepRV.setNestedScrollingEnabled(true);
        stepRV.setAdapter(stepAdapter);

        addStepEt = stepDialog.findViewById(R.id.add_step_et);

        saveBtnStep = stepDialog.findViewById(R.id.ingredient_dialog_btn);
        saveBtnStep.setOnClickListener(v -> {
            stepsListString.clear();
            description = addStepEt.getText().toString();
            step = new StepApi(description);

            stepsList.add(step);
            recipeStepParse(stepsList);
            stepAdapter.setData(stepsListString);
            stepDialog.dismiss();
        });
        stepDialog.show();

        deleteBtn = stepDialog.findViewById(R.id.ingredient_dialog_btn_delete);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepDialog.dismiss();
            }
        });


    }

    private void recipeStepParse(ArrayList<StepApi> stepsList) {
        for (int j = 0; j < stepsList.size(); j++) {
            stepsListString.add(stepsList.get(j).getStep());
        }
    }

    public void openDialogAddIngredient(View view) {
        ingredientDialog = new Dialog(view.getContext());
        ingredientDialog.setContentView(R.layout.add_ingredient_dialog);
        ingredientDialog.setCancelable(false);
        ingredientDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //setting RV
        addIngredientListRV = ingredientDialog.findViewById(R.id.ingredient_dialog_rv);

        //ingredientList RV layoutmanager
        FlexboxLayoutManager flexboxLayoutManagerIngredientListRv = new FlexboxLayoutManager(this);
        ingredientListRV.setLayoutManager(flexboxLayoutManagerIngredientListRv);
        ingredientListRV.setFocusable(false);
        ingredientListRV.setNestedScrollingEnabled(true);
        ingredientListRV.setAdapter(ingredientChipAdapter);

        //ingredientListSearch RV layoutmanager
        FlexboxLayoutManager flexboxLayoutManagerIngredientRv = new FlexboxLayoutManager(this);
        addIngredientListRV.setLayoutManager(flexboxLayoutManagerIngredientRv);
        addIngredientListRV.setFocusable(false);
        addIngredientListRV.setNestedScrollingEnabled(false);
        addIngredientListRV.setAdapter(searchChipAdapter);

        searchView = ingredientDialog.findViewById(R.id.ingredient_dialog_sv);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    dbSearch.readIngredientApi(newText);
                } else {
                    searchChipAdapter.setData(null);
                }
                return false;
            }
        });

        // saving the ingredients(data) on the adapter
        saveBtn = ingredientDialog.findViewById(R.id.ingredient_dialog_btn);
        saveBtn.setOnClickListener(v -> {
            ingredientChipAdapter.setData(ingredientsList, true);
            ingredientDialog.dismiss();
        });

        deleteBtn = ingredientDialog.findViewById(R.id.ingredient_dialog_btn_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsList.clear();
                ingredientDialog.dismiss();
            }
        });

        ingredientDialog.show();
    }

    @Override
    public void onResponse(Object obj) {

    }

    @Override
    public void onResponsePantry(Object obj) {

    }

    @Override
    public void onResponseSearchIngredient(Object obj) {
        if (obj != null) {
            if (obj instanceof List) {
                List<IngredientApi> listIngredient = (List) obj;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createSearchChips(listIngredient);
                    }
                });
            }
        }
    }

    @Override
    public void onFailure(String errorMessage) {

    }

    private void createSearchChips(List listData) {
        searchChipAdapter.setData(listData);
    }

    @Override
    public void onDestroy() {
        ingredientsList.removeAll(ingredientsList);
        stepsList.removeAll(stepsList);
        ingredientsListDb.removeAll(ingredientsListDb);
        stepsListString.removeAll(stepsListString);
        super.onDestroy();
    }
}