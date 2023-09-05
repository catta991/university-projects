package it.unimib.cookery.repository;

import android.app.Application;
import android.util.Log;

import java.util.List;

import it.unimib.cookery.database.IngredientApiDao;
import it.unimib.cookery.database.IngredientPantryDao;
import it.unimib.cookery.database.IngredientRecipeDao;
import it.unimib.cookery.database.PantryDao;
import it.unimib.cookery.database.RoomDatabase;
import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.IngredientPantry;
import it.unimib.cookery.models.IngredientRecipe;
import it.unimib.cookery.models.Pantry;
import it.unimib.cookery.utils.ResponseCallbackDb;
import it.unimib.cookery.utils.ServiceLocator;


/**
 * Repository to get the news using the API
 * provided by NewsApi.org (https://newsapi.org).
 */

public class DatabasePantryRepository {
    private static final String TAG = "DatabasePantryRepository";

    private final Application mApplication;
    private final PantryDao mPantryDao;
    private final IngredientApiDao mIngredientApi;
    private final IngredientPantryDao ingredientPantryDao;
    private final ResponseCallbackDb mResponseCallbackDb;
    private IngredientRecipeDao mIngredientDao;

    public DatabasePantryRepository(Application application, ResponseCallbackDb responseCallback) {
        this.mApplication = application;
        RoomDatabase roomDatabase = ServiceLocator.getInstance().getDao(application);

        //gestione Pantry
        this.mPantryDao = roomDatabase.pantryDao();
        this.ingredientPantryDao = roomDatabase.ingredientPantryDao();
        this.mResponseCallbackDb = responseCallback;
        this.mIngredientApi = roomDatabase.ingredientApiDao();
    }

    //Crud
    //Create
    public void create(Object obj){
        saveObj(obj);
    }
    private void saveObj(Object obj){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //oggetto che descrive il tipo di dispensa
                if(obj instanceof Pantry) {
                    mPantryDao.insertPantry((Pantry) obj);
                }
                //oggetto che descrive il tipo di ingrediente della dispensa
                else if(obj instanceof IngredientPantry){
                    Log.d("test","ingrediente");
                    ingredientPantryDao.insertIngredientPantry((IngredientPantry) obj);
                }else if(obj instanceof  IngredientApi){
                    mIngredientApi.insertIngredientApi((IngredientApi) obj);
                }
                //notificare l'aggiornamento
                synchronized(DatabasePantryRepository.this){
                    DatabasePantryRepository.this.notifyAll();
                }

            }
        };
        new Thread(runnable).start();
    }

    //Read
    public void readIngredientApi(String name){
        Runnable runnable = new Runnable() {
            @Override
            public void  run() {
                Log.d("nomeIng", ""+ name);
                mResponseCallbackDb.onResponseSearchIngredient(mIngredientApi.findIngredientsWithName(name));
            }
        };
        new Thread(runnable).start();
    }


    public void readAllIngredientApi(){
        Runnable runnable = new Runnable() {
            @Override
            public void  run() {
                mResponseCallbackDb.onResponse(mIngredientApi.ingredientApiALL());
            }
        };
        new Thread(runnable).start();
    }

   public void readAllIngredientPantry() {
        Runnable runnable = new Runnable() {
            @Override
            public void  run() {
                mResponseCallbackDb.onResponse(ingredientPantryDao.ingredientPantryALL());
            }
        };
        new Thread(runnable).start();
    }

    public void readAllIngredientPantryTest() {
        Runnable runnable = new Runnable() {
            @Override
            public void  run() {
                mResponseCallbackDb.onResponsePantry(ingredientPantryDao.ingredientPantryALL());
            }
        };
        new Thread(runnable).start();
    }

    //Update
    public void update(Object obj){
        Runnable runnable = new Runnable() {
            @Override
            public void  run() {
                if(obj instanceof IngredientPantry) {
                    ingredientPantryDao.update((IngredientPantry) obj);
                    readAllIngredientPantry();
                }
            }
        };
        new Thread(runnable).start();
    }
    //Delete
    public void delete(Object obj){
        Runnable runnable = new Runnable() {
            @Override
            public void  run() {
                if(obj instanceof IngredientPantry) {
                    int id = ((IngredientPantry) obj).getIdIngredient();
                    Log.d("test","delete" + id);
                    ingredientPantryDao.deleteByIngredientPantry(id);
                    readAllIngredientPantry();
                }
            }
        };
        new Thread(runnable).start();
    }
}
