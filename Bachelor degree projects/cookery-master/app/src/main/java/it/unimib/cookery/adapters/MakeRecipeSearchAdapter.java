package it.unimib.cookery.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.IngredientPantry;
import it.unimib.cookery.repository.IngredientMeasureUnitRepository;
import it.unimib.cookery.ui.MakeRecipe;
import it.unimib.cookery.ui.PantryFragment;
import it.unimib.cookery.utils.IngredientUnitMeasureResponseCallback;

public class MakeRecipeSearchAdapter extends RecyclerView.Adapter<MakeRecipeSearchAdapter.IngredientViewHolder> implements IngredientUnitMeasureResponseCallback {
    private List<IngredientApi> mListIngredients;
    private  int k = 0;
    private Costants costants = new Costants();
    private boolean modified;
    private Dialog ingredientDialog;
    private Button addButton, deleteButton;
    private TextView ingredientName;
    private EditText editText;
    private SharedPreferences sharedPreferences;
    private IngredientViewHolder holder;
    private Spinner measureUnitSpinner;
    private IngredientMeasureUnitRepository ingredientMeasureUnitRepository =
            new IngredientMeasureUnitRepository(this);

    public  void setData(List<IngredientApi> list) {
        this.mListIngredients = list;
        notifydata();

    }

    public void notifydata(){
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip_ingredient_search, parent, false);
        return  new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        this.holder = holder;
        IngredientApi ingredient;
        ingredient = mListIngredients.get(position);
        if(ingredient == null){ return;}
        holder.chipIngredient.setText(ingredient.getName());
        holder.chipIngredient.setId(position);
        holder.chipIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ingredientMeasureUnitRepository.getMeasure(ingredient.getId(), holder.chipIngredient.getId());

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListIngredients != null){
            return  mListIngredients.size();
        }
        return 0;
    }

    @Override
    public void onFailure(int errorMessage) {

        Toast.makeText(holder.itemView.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getUnitMeasureResponse(ArrayList<String> measureUnit, int position) {

       Log.d("measureUnit", ""+ holder.chipIngredient.getId());

        openDialogAddProduct(holder.itemView, position , mListIngredients, measureUnit);
        notifyDataSetChanged();


    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{
        private Chip chipIngredient;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            chipIngredient = itemView.findViewById(R.id.chip_ingredient);
        }
    }

    public void openDialogAddProduct(View itemView, int id, List<IngredientApi> list, ArrayList<String> measureUnit){

        Log.d("chip", "creazione dialog ");
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

        // crea una dialog
        ingredientDialog = new Dialog(itemView.getContext());
        ingredientDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // elimina il titolo dalla dialog che non serve
        ingredientDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // permette l'uscita dalla dialog solo se si preme cancella
        //ingredientDialog.setCancelable(false);
        // setta il layout che poi verrà mostrato nella dialog
        ingredientDialog.setContentView(R.layout.layout_ingredient_make_recipe_dialog);

        // creo e trovo l'oggetto textView nella dialog
        ingredientName = ingredientDialog.findViewById(R.id.IngredientNameV2);

        // setto il teso della dialog la stringa andrà poi sostituita col nome dell'ingrediente da aggiungere
        ingredientName.setText(list.get(id).getName());

        // creo e trovo l'oggetto editText dove l'utente inserisce la quantità
        editText = ingredientDialog.findViewById(R.id.IngredientEditTextV2);

        measureUnitSpinner = (Spinner) ingredientDialog.findViewById(R.id.measureUnitSpinnerV2);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_spinner_dropdown_item, measureUnit);
        measureUnitSpinner.setAdapter(spinnerAdapter);

        addButton = ingredientDialog.findViewById(R.id.addIngredientButtonV2);

        //listener per il bottone per aggiungere l'ingrediente
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editText.getText().toString().equals("") && (Double.parseDouble(editText.getText().toString())) > 0) {

                    String measureUnit = measureUnitSpinner.getSelectedItem().toString();
                    list.get(id).setAmount(Double.parseDouble(editText.getText().toString()));
                    list.get(id).setUnit(measureUnit);
                    list.get(id).setAmount(Double.parseDouble(editText.getText().toString()));
                    MakeRecipe.updateArrayList(list.get(id));
                    ingredientDialog.dismiss();
                } else {
                    // stampa un toast di errore

                    Toast.makeText(itemView.getContext(), R.string.invalid_quantity, Toast.LENGTH_SHORT).show();

                }
            }

        });
        // creo e ottengo l'oggetto per il bottone di delete
        deleteButton = ingredientDialog.findViewById(R.id.deleteIngredientButtonV2);
        // listener del bottone di delete
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pr sicurezza setto la quantità a 0
                //quantità = 0;
                // chiude la dialog
                ingredientDialog.dismiss();
            }
        });
        // mostra la dialog a schermo
        ingredientDialog.show();


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(ingredientDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
      //  layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        ingredientDialog.getWindow().setAttributes(layoutParams);
    }

}
