package it.unimib.cookery.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.firebase.LoginRegisterUser;
import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.PantryWithIngredientPantry;
import it.unimib.cookery.models.Recipe;
import it.unimib.cookery.repository.DatabasePantryRepository;
import it.unimib.cookery.repository.RecipeRepository;
import it.unimib.cookery.utils.CsvReader;
import it.unimib.cookery.utils.ResponseCallbackDb;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ResponseCallbackDb {

    public static SharedPreferences sharedPreferences;
    private Menu drawerMenu;
    private DrawerLayout mDrawerLayout;
    private NavHostFragment mNavHostFragment;
    private NavController mNavController;
    private NavigationView mNavMenu;

    private boolean logged;
    private List<PantryWithIngredientPantry> list;
    private boolean firstAccess;
    private ArrayList<IngredientApi> ing;
    private DatabasePantryRepository dbIngredient;
    private Costants costants = new Costants();


    private RecipeRepository db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // riferimento al file

        sharedPreferences = getSharedPreferences(costants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        firstAccess = sharedPreferences.getBoolean(costants.FIRST_ACCESS, true);
        logged = sharedPreferences.getBoolean(costants.LOGGED, false);

        Log.d("logged", "" + logged);
        Log.d("sharedPreferences", "" + firstAccess);


        //NavHostController configuration and homeFragment set as startupFragment
        mNavHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.homeFragmentContainerView);
        mNavController = mNavHostFragment.getNavController();

        //BottomNavView configuration
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        NavigationUI.setupWithNavController(bottomNavigationView, mNavController);

        //DrawerLayout configuration
        mDrawerLayout = findViewById(R.id.drawerLayout);

        //Hamburger icon listener
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //NavigationMenu config
        mNavMenu = findViewById(R.id.navigationView);

        setDrawerMenu();


        /*creazione ricetta*/


        db = new RecipeRepository(getApplication(), this);

        dbIngredient = new DatabasePantryRepository(getApplication(), this);


        if (firstAccess) {

            CsvReader csv = new CsvReader();

            try {
                ing = csv.readCsv(getAssets().open(costants.CSV_FILE_NAME));
                for (int i = 0; i < ing.size(); i++) {
                    dbIngredient.create(ing.get(i));
                }

            } catch (IOException e) {
                e.printStackTrace();
                Snackbar.make(findViewById(android.R.id.content), R.string.csv_error, Snackbar.LENGTH_SHORT).show();
            }

            firstAccess = false;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(costants.FIRST_ACCESS, firstAccess);
            editor.apply();

        }

    }
    //(Not that dynamic) Dynamic Drawer and header menu that changes when user login or logout
    public void setDrawerMenu() {

        View view = mNavMenu.getHeaderView(0);

        if (logged) {
            if (view.equals(null)) {
                mNavMenu.inflateHeaderView(R.layout.drawer_menu_header);
            } else {
                mNavMenu.removeHeaderView(view);
                mNavMenu.inflateHeaderView(R.layout.drawer_menu_header);
            }

            mNavMenu.getMenu().clear();
            mNavMenu.inflateMenu(R.menu.drawer_nav_menu);
            mNavMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.logout_drawer:
                            FirebaseAuth.getInstance().signOut();
                            logged = sharedPreferences.edit().putBoolean(costants.LOGGED, false).commit();
                            finish();
                            startActivity(getIntent());
                            break;
                        case R.id.my_profile:
                            startActivity(new Intent(getApplicationContext(), UserProfile.class));
                            break;

                        case R.id.food_preferences:
                            startActivity(new Intent(getApplicationContext(), AlimentarPreferenceActivity.class));
                            break;


                    }
                    return false;
                }
            });


        } else {
            if (view.equals(null)) {
                mNavMenu.inflateHeaderView(R.layout.drawer_menu_header_not_logged);
            } else {
                mNavMenu.removeHeaderView(view);
                mNavMenu.inflateHeaderView(R.layout.drawer_menu_header_not_logged);
            }

            mNavMenu.getMenu().clear();
            mNavMenu.inflateMenu(R.menu.drawer_menu_not_logged);
            mNavMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.login_drawer:
                            startActivity(new Intent(getApplicationContext(), LoginRegisterUser.class));
                            break;
                        case R.id.foodPreferences:
                            startActivity(new Intent(getApplicationContext(), AlimentarPreferenceActivity.class));
                            break;

                    }
                    return false;
                }
            });
        }
    }


    @Override
    public void onResponse(Object obj) {

    }

    @Override
    public void onResponsePantry(Object obj) {

    }

    @Override
    public void onResponseSearchIngredient(Object obj) {

    }

    @Override
    public void onFailure(String errorMessage) {

    }
}