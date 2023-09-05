package it.unimib.cookery.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
/*creata la classe della ricetta sia per il fragment delle ricette dell'utente, e ci servirà in seguito */
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int idDb;

    private int id;
    private String name;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "image_id")
    private int imageId;

    private String category;

    @ColumnInfo(name = "n_person")
    private int nPerson;


    @Ignore
    ArrayList<IngredientApi> ingredientList= new ArrayList<>();


    public Recipe(int id, String imageUrl, String name){
        this.id =id;
        this.imageUrl = imageUrl;
        this.name = name;
    }


    @Ignore
    public Recipe(String name, String category, String imageUrl, int n) {
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
        this.nPerson = n;
    }
    public int getIdDb() {
        return idDb;
    }

    public void setIdDb(int idDb) {
        this.idDb = idDb;
    }
    public  void setNPerson(int n){
        for(int i = 0; i < ingredientList.size(); i++){
            double qBase = ingredientList.get(i).getAmount() / nPerson;
            ingredientList.get(i).setAmount(qBase * n);
            Log.d("test","nome:" + ingredientList.get(i).getName()+"- quantita:"+ingredientList.get(i).getAmount());
        }
        nPerson = n;
    }




    public ArrayList<IngredientApi> getIngredientList() {

        return ingredientList;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", id=" + id +
                '}';
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }


    public int getImageId() {
        return imageId;
    }
    public  int getNPerson(){
        return nPerson;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setIngredientList(IngredientApi ingredientList) {
        this.ingredientList.add(ingredientList);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getnPerson() {
        return nPerson;
    }

    public void setnPerson(int nPerson) {
        this.nPerson = nPerson;
    }
}