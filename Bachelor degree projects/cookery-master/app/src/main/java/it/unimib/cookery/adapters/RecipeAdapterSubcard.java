package it.unimib.cookery.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.models.Recipe;
import it.unimib.cookery.models.RecipeApi;
import it.unimib.cookery.ui.SingleRecipeActivity;

public class RecipeAdapterSubcard extends RecyclerView.Adapter<RecipeAdapterSubcard.Viewholder> {

    private Context context;
    private ArrayList<RecipeApi> recipeArrayList;
    private ArrayList<String> ingredientPantry;

    /* oggetto per le costanti */
    private Costants costants = new Costants();

    public RecipeAdapterSubcard(Context context, ArrayList<RecipeApi> recipeArrayList, ArrayList<String> ingredientPantry) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
        this.ingredientPantry = ingredientPantry;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewTestS);
            imageView = itemView.findViewById(R.id.imageViewTestS);

            // creo il listener che quando schiaccio una card crea l'intent e salva le
            // informazioni da passare all'activity SingleRecipeActivity il back stack è gestito in automatico
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<String> step = new ArrayList<>();
                    step.addAll(recipeArrayList.get(getAdapterPosition()).extractSteps());

                    Intent intent = new Intent(context, SingleRecipeActivity.class);
                    // da aggiungere il passaggio dell'id della ricetta
                    intent.putExtra(costants.RECIPE_ID, recipeArrayList.get(getAdapterPosition()).getId());
                    intent.putExtra(costants.RECIPE_IMAGE, recipeArrayList.get(getAdapterPosition()).getImage());
                    intent.putExtra(costants.RECIPE_NAME, recipeArrayList.get(getAdapterPosition()).getTitle());
                    intent.putExtra(costants.TYPE, costants.OTHER);
                    intent.putExtra(costants.RECIPE_SERVINGS, recipeArrayList.get(getAdapterPosition()).getServings());
                    intent.putStringArrayListExtra(costants.STEP_ARRAYLIST, step);
                    intent.putParcelableArrayListExtra(costants.INGREDIENT_ARRAYLIST, (ArrayList<? extends Parcelable>) recipeArrayList.
                            get(getAdapterPosition()).getExtendedIngredients());
                    intent.putStringArrayListExtra(costants.PANTRY, ingredientPantry);
                    context.startActivity(intent);

                }
            });
        }
    }

    @NonNull
    @Override
    public RecipeAdapterSubcard.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_subcard, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterSubcard.Viewholder holder, int position) {
        RecipeApi model = recipeArrayList.get(position);
        holder.textView.setText(model.getTitle());


        String url = model.getImage();

        if (url == null) {
            holder.imageView.setImageResource(R.drawable.generic_recipe);
        } else {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_baseline_cloud_download_24)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }
}
