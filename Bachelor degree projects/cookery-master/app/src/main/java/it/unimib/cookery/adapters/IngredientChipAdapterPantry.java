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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.models.IngredientPantry;
import it.unimib.cookery.ui.PantryFragment;

public class IngredientChipAdapterPantry extends RecyclerView.Adapter<IngredientChipAdapterPantry.IngredientViewHolder> {
    private List<IngredientPantry> mListIngredients = new ArrayList<>();
    private int k = 0;
    private boolean singleClic = true;
    private boolean modified;
    private Costants costants = new Costants();

    public void setData(List<IngredientPantry> list) {
        this.mListIngredients = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientPantry ingredient = mListIngredients.get(position);
        if (ingredient == null) {
            return;
        }
        holder.tvIngredient.setText(ingredient.getName());

        holder.tvQuantity.setText(" " + ingredient.getQuantity() + " " + ingredient.getMeasureUnit());
        holder.tvIngredient.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("test", "nome click lungo:" + ingredient);
                holder.imgButtonDelete.setVisibility(View.VISIBLE);
                singleClic = false;
                return false;
            }
        });


        // sistemare variabile modified share preferences
        holder.imgButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PantryFragment.deleteIngredient(ingredient);
                SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences(costants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                modified = true;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(costants.MODIFIED, modified);
                editor.apply();

            }
        });
        holder.tvIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (singleClic) {
                    Log.d("test", "nome click veloce:" + holder.tvIngredient.getText());
                    openDialogModifyProduct(holder.itemView, ingredient);
                }
                singleClic = true;
            }
        });
    }

    /**
     * dialog  modifica quantità
     */
    public void openDialogModifyProduct(View itemView, IngredientPantry ingredientSelected) {

        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        // crea una dialog
        Dialog ingredientDialog = new Dialog(itemView.getContext());
        ingredientDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // elimina il titolo dalla dialog che non serve
        ingredientDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ingredientDialog.setContentView(R.layout.layout_ingredient_quantity_dialog);
        // creo e trovo l'oggetto textView nella dialog
        TextView ingredientName = ingredientDialog.findViewById(R.id.IngredientName);
        ingredientName.setText(ingredientSelected.getName());

        EditText editText = ingredientDialog.findViewById(R.id.IngredientEditText);

        Button addButton = ingredientDialog.findViewById(R.id.addIngredientButton);
        //listener per il bottone per aggiungere l'ingrediente
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("") && (Integer.parseInt(editText.getText().toString())) > 0) {

                    ingredientSelected.setQuantity(Double.parseDouble(editText.getText().toString()));
                    PantryFragment.updateQuantity(ingredientSelected);
                    ingredientDialog.dismiss();
                } else {
                    // stampa un toast di errore
                    Toast.makeText(itemView.getContext(), R.string.invalid_quantity, Toast.LENGTH_SHORT).show();
                }
            }

        });
        // creo e ottengo l'oggetto per il bottone di delete
        Button deleteButton = ingredientDialog.findViewById(R.id.deleteIngredientButton);

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
    }


    @Override
    public int getItemCount() {
        if (mListIngredients != null) {
            return mListIngredients.size();
        }
        return 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIngredient;
        private TextView tvQuantity;
        private ImageButton imgButtonDelete;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredient = itemView.findViewById(R.id.tv_ingredient);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            imgButtonDelete = itemView.findViewById(R.id.imgdelete);
        }

    }
}
