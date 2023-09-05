package it.unimib.cookery.ui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

import it.unimib.cookery.R;
import it.unimib.cookery.costants.Costants;
import it.unimib.cookery.firebase.LoginRegisterUser;

public class AlimentarPreferenceActivity extends AppCompatActivity {

    /* dichiaro gli oggetti checkbox */
    private CheckBox gluten, dairy, treeNut, egg, grain, penaut,
            seafood, sesame, shellfish, soy, sulfite, wheat,
            vegetarian, vegan, pescetarian, glutenFree, ketogenic, paleo;

    private ArrayList<String> intolleranceChoosen = new ArrayList<>();
    private ArrayList<String> preferencesChoosen = new ArrayList<>();
    private String intollerances = "";
    private String diet = "";
    private boolean logged;
    private boolean preferenciesModified;
    public static SharedPreferences sharedPreferences;
    private Costants costants = new Costants();


    private DrawerLayout mDrawerLayout;
    private NavigationView mNavMenu;




    // oggetto classe Costants per accedere alle costanti
    private Costants alimentarPreferenceCostants = new Costants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("statusDebug", "on create preferences");

        sharedPreferences = getSharedPreferences(costants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        logged = sharedPreferences.getBoolean(costants.LOGGED, false);


        setContentView(R.layout.alimentar_preference_activity);

        SharedPreferences sharedPreferences = getSharedPreferences(alimentarPreferenceCostants.SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);

        intollerances = sharedPreferences.getString(alimentarPreferenceCostants.INTOLLERANCES, "");
        diet = sharedPreferences.getString(alimentarPreferenceCostants.DIET, "");
        preferenciesModified = sharedPreferences.getBoolean(alimentarPreferenceCostants.PREFERENCES_MODIFIED, false);
        //logged =


        intolleranceChoosen = new ArrayList<String>(Arrays.asList(intollerances.split(",")));
        preferencesChoosen = new ArrayList<String>(Arrays.asList(diet.split(",")));




        mDrawerLayout = findViewById(R.id.drawerLayout);

        //NavigationMenu config
        mNavMenu = findViewById(R.id.navigationView);

        setDrawerMenu();






        gluten = findViewById(R.id.checkBoxIntolleranceGluten);
        dairy = findViewById(R.id.checkBoxIntolleranceDairy);
        treeNut = findViewById(R.id.checkBoxIntolleranceTreeNuts);
        egg = findViewById(R.id.checkBoxIntolleranceEgg);
        grain = findViewById(R.id.checkBoxIntolleranceGrain);
        penaut = findViewById(R.id.checkBoxIntollerancePenaut);
        seafood = findViewById(R.id.checkBoxIntolleranceSeaFood);
        sesame = findViewById(R.id.checkBoxIntolleranceSesame);
        shellfish = findViewById(R.id.checkBoxIntolleranceShellyfish);
        soy = findViewById(R.id.checkBoxIntolleranceSoy);
        wheat = findViewById(R.id.checkBoxIntolleranceWheat);
        sulfite = findViewById(R.id.checkBoxIntolleranceSulfite);

        vegetarian = findViewById(R.id.checkBoxPreferencesVegetarian);
        vegan = findViewById(R.id.checkboxPreferencesVegan);
        pescetarian = findViewById(R.id.checkBoxPreferencesPescetarian);
        glutenFree = findViewById(R.id.checkBoxPreferencesGlutenFree);
        paleo = findViewById(R.id.checkBoxPreferencesPaleo);
        ketogenic = findViewById(R.id.checkBoxPreferencesKetogenic);

        Button saveButton = findViewById(R.id.saveButton);




        ImageView hamburgherMenu = findViewById(R.id.imageMenu);




       for(int i=0; i< intolleranceChoosen.size(); i++){
           switch (intolleranceChoosen.get(i)){

               case Costants.GLUTEN:
                   gluten.setChecked(true);
                   break;

               case Costants.EGG:
                   egg.setChecked(true);
                   break;

               case Costants.DAIRY:
                   dairy.setChecked(true);
                   break;

               case Costants.TREE_NUT:
                   treeNut.setChecked(true);
                   break;

               case Costants.PENAUT:
                   penaut.setChecked(true);
                   break;

               case Costants.SHELLFISH:
                   shellfish.setChecked(true);
                   break;

               case Costants.WHEAT:
                   wheat.setChecked(true);
                   break;

               case Costants.GRAIN:
                   grain.setChecked(true);
                   break;

               case Costants.SEAFOOD:
                   seafood.setChecked(true);
                   break;

               case Costants.SOY:
                   soy.setChecked(true);
                   break;

               case Costants.SESAME:
                   sesame.setChecked(true);
                   break;


               case Costants.SULFITE:
                   sulfite.setChecked(true);
                   break;
           }
       }




        for (int i = 0; i < preferencesChoosen.size(); i++) {


            switch (preferencesChoosen.get(i)) {
                case Costants.VEGAN:
                    vegan.setChecked(true);
                    break;

                case Costants.VEGETARIAN:
                    vegetarian.setChecked(true);
                    break;

                case Costants.PESCETARIAN:
                    pescetarian.setChecked(true);
                    break;

                case Costants.GLUTEN_FREE:
                    glutenFree.setChecked(true);
                    break;

                case Costants.PALEO:
                    paleo.setChecked(true);
                    break;

                case Costants.KETOGENIC:
                    ketogenic.setChecked(true);
                    break;
            }
        }


        // todo sistemare bug se mi loggo e torno sul alimentar preferences mi carica il
        // todo drawer da non loggato
        hamburgherMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(GravityCompat.START);



            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("alimentar", "premuto bottone save");

                // svuoto gli array list che memorizzano intolleranze e preferenze alimentari
                intolleranceChoosen.clear();
                preferencesChoosen.clear();
                diet = "";
                intollerances = "";

                // se gli elementi sono selezionati gli aggiungo agli array list

                if (gluten.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.GLUTEN);

                if (dairy.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.DAIRY);

                if (treeNut.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.TREE_NUT);

                if (egg.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.EGG);

                if (grain.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.GRAIN);

                if (penaut.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.PENAUT);

                if (seafood.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.SEAFOOD);

                if (sesame.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.SESAME);

                if (shellfish.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.SHELLFISH);

                if (soy.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.SOY);

                if (sulfite.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.SULFITE);

                if (wheat.isChecked())
                    intolleranceChoosen.add(alimentarPreferenceCostants.WHEAT);


                if (vegan.isChecked())
                    preferencesChoosen.add(alimentarPreferenceCostants.VEGAN);

                if (vegetarian.isChecked())
                    preferencesChoosen.add(alimentarPreferenceCostants.VEGETARIAN);

                if (pescetarian.isChecked())
                    preferencesChoosen.add(alimentarPreferenceCostants.PESCETARIAN);

                if (glutenFree.isChecked())
                    preferencesChoosen.add(alimentarPreferenceCostants.GLUTEN_FREE);

                if (ketogenic.isChecked())
                    preferencesChoosen.add(alimentarPreferenceCostants.KETOGENIC);

                if (paleo.isChecked())
                    preferencesChoosen.add(alimentarPreferenceCostants.PALEO);


                if (intolleranceChoosen.size() > 0) {
                    for (int i = 0; i < intolleranceChoosen.size() - 1; i++)
                        intollerances += intolleranceChoosen.get(i) + ",";

                    intollerances += intolleranceChoosen.get(intolleranceChoosen.size() - 1);

                }


                if (preferencesChoosen.size() > 0) {
                    for (int i = 0; i < preferencesChoosen.size() - 1; i++)
                        diet += preferencesChoosen.get(i) + ",";

                    diet += preferencesChoosen.get(preferencesChoosen.size() - 1);
                }

                preferenciesModified = true;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(alimentarPreferenceCostants.INTOLLERANCES, intollerances);
                editor.putString(alimentarPreferenceCostants.DIET, diet);
                editor.putBoolean(alimentarPreferenceCostants.PREFERENCES_MODIFIED, preferenciesModified);
                editor.apply();


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


            }


        });
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
                            finish();
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

                    }
                    return false;
                }
            });
        }
    }






}