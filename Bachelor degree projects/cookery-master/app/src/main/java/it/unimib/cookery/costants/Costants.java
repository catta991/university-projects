package it.unimib.cookery.costants;

public class Costants {


    /* inizio stringhe costanti Alimentar Prefences Fragment */


    public static final String GLUTEN = "gluten";
    public static final String EGG = "egg";
    public static final String GRAIN = "grain";
    public static final String PENAUT = "penaut";
    public static final String SEAFOOD = "seafood";
    public static final String SESAME = "sesame";
    public static final String SHELLFISH = "shellfish";
    public static final String SOY = "soy";
    public static final String SULFITE = "sulfite";
    public static final String WHEAT = "wheat";
    public static final String DAIRY = "dairy";
    public static final String TREE_NUT = "tree nut";
    public static final String VEGAN = "vegan";
    public static final String VEGETARIAN = "vegetarian";
    public static final String PESCETARIAN = "pescetarian";
    public static final String GLUTEN_FREE = "gluten free";
    public static final String PALEO = "paleo";
    public static final String KETOGENIC = "ketogenic";

    public static final String DIET = "diet";
    public static final String INTOLLERANCES = "intollerances";
    public static final String PREFERENCES_MODIFIED = "preferencesModified";




    /* fine  stringhe costanti Alimentar Prefences Fragment */



    /* inizio stringhe costanti My Recipes Fragment */

    public static final String FILTER0 = "Appetizers";
    public static final String FILTER1 = "First course";
    public static final String FILTER2 = "Main meal";
    public static final String FILTER3 = "Side dish";
    public static final String FILTER4 = "Desserts";
    public static final String dialogTitle = "Select recipe type";


    /* fine stringhe costanti My Recipes Fragment */





    /* stringhe tag per intent */

    public static final String RECIPE_NAME = "Recipe name";
    public static final String TYPE = "Type";
    public static final String RECIPE_ID = "Recipe id";
    public static final String RECIPE_IMAGE = "Recipe image";
    public static final String STEP_ARRAYLIST = "StepArraylist";
    public static final String INGREDIENT_ARRAYLIST = "IngredientArraylist";
    public static final String RECIPE_SERVINGS = "Servings";
    public static final String MISSING_INGREDIENTS = "missingIngredients";
    public static final String READY_TO_COOCK = "readyToCoock";
    public static final String OTHER = "other";
    public static final String PANTRY = "pantry";


    /* stringhe costanti per intent */

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "dbCookery";
    public static final String ID_RECIPE_DB = "Recipe id";

    /* fine stringhe costanti per intent */

    /* stringhe costanti  retrofit */
    public static final String BASE_URL = "https://api.spoonacular.com/";

    /* 
        ***************************************************************
        before run the application uncomment and insert API key in the line below

        public static final String API_KEY = insert_api_key_here;

        ******************************************************************
    
    */

    /* fine stringhe costanti per reftrofit */


    /* stringhe per singleRecipeActivity */

    public static final String PEOPLE = " People";
    public static final String PERSON = " Person";

    /* stringhe per singleRecipeActivity */


    /* stringhe main activity */

    public static final String SHARED_PREFERENCES_FILE_NAME = "sharedPreferences";
    public static final String FIRST_ACCESS = "firstAccess";
    public static final String LOGGED = "logged";
    public static final String CSV_FILE_NAME = "ingredient.csv";

    /* fine stringhe main activity */

    /* stringhe per modifica */
    public static final String MODIFIED = "modified";
    /* fine stringhe per modifica */


    /* stringhe per accesso galleria*/
    public static final int PICK_IMAGE = 100;
    public static int STORAGE_PERMISSION_CODE=113;
    /* fine stringhe per accesso galleria*/

    /* stringhe make recipe */
    public static final String NAME_ERROR= "Enter recipe name";
    public static final String NUMBER_PEOPLE_ERROR= "Enter a number of people";
    /* fine stringhe make recipe */

    /* stringhe costanti per utente*/
    public static final String EMAIL_REQUIRED="Email is required!";
    public static final String VALID_EMAIL="Please enter a valid Email address";
    public static final String PASSWORD_REQUIRED ="Password is required!";
    public static final String PASSWORD_LENGTH="Minimum password length is 9 characters";
    public static final String REENTER_PASSWORD= "Enter password again";
    public static final String NO_MATCH_PASSWORD= "Password does not match";
    public static final String INVALID_EMAIL="Please provide a valid email address";
    /* fine stringhe costanti per utente*/


    public Costants() {

    }


}
