package it.unimib.cookery.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RecipeApi {


    private int id;
    private String title;
    private String image;
    private int servings;
    private int missedIngredientCount;
    private List<StepList> analyzedInstructions;
    private List<IngredientApi> extendedIngredients;
    private List<IngredientApi> missedIngredients;

    public RecipeApi() {
    }

    public RecipeApi(int id, String title, String image, int servings, int missedIngredientCount, List<StepList> analyzedInstructions, List<IngredientApi> extendedIngredients,
                     List<IngredientApi> missedIngredients) {

        this.id = id;
        this.title = title;
        this.image = image;
        this.servings = servings;
        this.missedIngredientCount = missedIngredientCount;
        this.analyzedInstructions = analyzedInstructions;
        this.extendedIngredients = extendedIngredients;
        this.missedIngredients = missedIngredients;
    }


    public int getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public void setMissedIngredientCount(int missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public List<IngredientApi> getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(List<IngredientApi> missedIngredients) {
        this.missedIngredients = missedIngredients;
    }

    public List<StepList> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public void setAnalyzedInstructions(List<StepList> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    @Override
    public String toString() {
        return "RecipeApi{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", servings=" + servings +
                ", analyzedInstructions=" + analyzedInstructions +
                ", extendedIngredients=" + extendedIngredients +
                '}';
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public List<IngredientApi> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<IngredientApi> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> extractSteps() {
        ArrayList<String> steps = new ArrayList<>();

        ArrayList<StepList> stepLists = (ArrayList<StepList>) analyzedInstructions;

        for (int i = 0; i < stepLists.size(); i++) {

            StepList list = stepLists.get(i);

            ArrayList<StepApi> stepApi = (ArrayList<StepApi>) list.getSteps();


            for (int j = 0; j < stepApi.size(); j++) {

                steps.add(stepApi.get(j).getStep());

            }
        }


        return steps;

    }


}
