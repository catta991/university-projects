package it.unimib.cookery.utils;

import android.app.Application;
import android.provider.SyncStateContract;

import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.database.RoomDatabase;
import it.unimib.cookery.service.SpoonacularApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 *  Registry to provide the dependencies for the classes
 *  used in the application.
 */
public class ServiceLocator {

    private static ServiceLocator instance = null;
    private Costants costants = new Costants();

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized(ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    public SpoonacularApiService getSpoonacularApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(costants.BASE_URL).
                addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(SpoonacularApiService.class);
    }


    public RoomDatabase getDao(Application application) {
        return RoomDatabase.getDatabase(application);
    }
}
