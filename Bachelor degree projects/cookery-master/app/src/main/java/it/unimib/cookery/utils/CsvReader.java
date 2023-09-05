package it.unimib.cookery.utils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import it.unimib.cookery.models.IngredientApi;


public class CsvReader {


    public CsvReader() {
    }


    public ArrayList<IngredientApi> readCsv(InputStream st){

        ArrayList<IngredientApi> ingredientFromFile = new ArrayList<>();



        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(st));

            String line="";


            while ((line = reader.readLine()) != null) {

                String[] stringIngredient = line.split(";");

                ingredientFromFile.add(new IngredientApi(Integer.parseInt(stringIngredient[1]), stringIngredient[0]));


            }


        } catch (IOException e) {
            e.printStackTrace();
        }




        return ingredientFromFile;

    }



}
