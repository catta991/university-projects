package it.unimib.cookery.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.cookery.R;
import it.unimib.cookery.models.IngredientPantry;
import it.unimib.cookery.models.Recipe;


public class AdapterClass extends ArrayAdapter<Recipe> implements Filterable {

    /* array list per i filtri */
    private ArrayList<Recipe> listdata;
    private ArrayList<Recipe> filterData;

    /* salvo il contesto */
    private Context context;



    // array list per le ricette che non hanno categoria desiderata dall'utente
    private ArrayList<Recipe> Removed = new ArrayList<>();


    public AdapterClass(@NonNull Context context, ArrayList<Recipe> recipesArrayList) {
        super(context, 0, recipesArrayList);
        this.context = context;
        for (Recipe r : recipesArrayList)
            Log.d("stampa", "" + r.getName());
        Log.d("qui", "adapter class");
        listdata = recipesArrayList;
        filterData = new ArrayList<>(listdata);

    }
    public  void setData( ArrayList<Recipe> list){
        this.listdata = list;
        for(int i=0; i < listdata.size();i++){
            Log.i("test", "Adapter" + listdata.get(i).getName());
        }
        filterData = new ArrayList<>(listdata);
        Log.i("test", "Adapter" + listdata.size());
        Log.i("test", "----------------------------------------------");
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        }
        /* ottiene la text view dell'elemento dell'array list e ne setta il nome*/
        ((TextView) convertView.findViewById(R.id.textViewRicetta))
                .setText(listdata.get(position).getName());

        /* ottengo l'url dell'immagine */
        // String imgUrl = listdata.get(position).getImageUrl();




        /* ottiene l'image view dell'elemento dell'array list e ne setta l'immagine */

        // todo settare un immagine di default se non viene selezionata la foto

        ImageView img =(ImageView) convertView.findViewById(R.id.imageViewRicetta);
        if(!listdata.get(position).getImageUrl().equals("")) {
            Glide.with(parent.getContext()).load(listdata.get(position).getImageUrl()).into(img);

        }else {

            img.setImageResource(R.drawable.generic_recipe);
        }

        return convertView;
    }


    public void applyFilter(ArrayList<String> arr) {
        // serve per resettare i filtri
        if (Removed.size() > 0) {
            for (Recipe r : Removed) {
                if (!listdata.contains(r))
                    listdata.add(r);
                if (!filterData.contains(r))
                    filterData.add(r);
            }
            Removed.clear();
        }
        // se c'è almeno un filtro selezionato trovo tutte le ricette che non rispettano i filtri
        // e le sottraggo dalle due array list e le aggiungo all'array list Removed che serve
        // per il ripristino
        if (arr.size() > 0) {
            for (int i = 0; i < listdata.size(); i++) {
                Recipe r = listdata.get(i);
                String category = r.getCategory();
                if (!arr.contains(category)) {
                    Removed.add(r);
                }
            }
            // rimuovo da entrambe le liste gli elementi che non rispecchiano i filtri
            listdata.removeAll(Removed);
            filterData.removeAll(Removed);
        }
        // notifico del cambiamento del dato per aggiornare la grid view
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        return filterNotification;
    }


    private Filter filterNotification = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Recipe> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(filterData);
            } else {
                String searchStr = constraint.toString().toLowerCase().trim();
                for (Recipe recipe : filterData) {
                    String nameLowerCase = recipe.getName().toLowerCase();
                    if (nameLowerCase.contains(searchStr)) {
                        filterList.add(recipe);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            listdata.clear();
            listdata.addAll((ArrayList<Recipe>) filterResults.values);

            // se la lista risultato è vuota ritorna un messaggio di nessun risultato trovato
            if (listdata.size() == 0) {
                // non riesce a rislvere il metodo make quindi aspetto a implementare le snackbar
                //Snackbar.make(getContext(), "prova", Snackbar.LENGTH_SHORT).show();


                // Snackbar.make(, "fffffff", Snackbar.LENGTH_SHORT).show();

                Toast.makeText(getContext(), R.string.result_not_found, Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            } else {
                notifyDataSetChanged();
            }

        }
    };


}
