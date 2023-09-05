package it.unimib.cookery.ui;


import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;

import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


import it.unimib.cookery.R;


import it.unimib.cookery.adapters.IngredientChipAdapterPantry;
import it.unimib.cookery.adapters.SearchChipAdapter;
import it.unimib.cookery.models.IngredientApi;
import it.unimib.cookery.models.IngredientPantry;

import it.unimib.cookery.repository.DatabasePantryRepository;
import it.unimib.cookery.utils.ResponseCallbackDb;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class PantryFragment extends Fragment implements ResponseCallbackDb {

    // private static IngredientChipAdapter ingredientPantryAdapter;
    private SearchChipAdapter searchChipAdapter = new SearchChipAdapter();
    //buttom sheet
    private BottomSheetBehavior bottomSheetBehavior;
    private Button btnClose;
    private SearchView searchView;
    private int quantità = 0;

    private View root;
    static private DatabasePantryRepository db;
    static private IngredientPantry ingredientAdd;
    private List<IngredientPantry> list;
    static private List<IngredientPantry> pantry = new ArrayList();
    static private List<IngredientPantry> fridge = new ArrayList();
    static private List<IngredientPantry> freezer = new ArrayList();

    private static RecyclerView rvPantry;
    private static RecyclerView rvFridge;
    private static RecyclerView rvFreezer;
    private RecyclerView rcvIngredientSearch;
    private List<IngredientApi> ingredientApiSearch = new ArrayList();

    private static IngredientChipAdapterPantry ingredientPantryAdapter = new IngredientChipAdapterPantry();
    private static IngredientChipAdapterPantry ingredientFridgeAdapter = new IngredientChipAdapterPantry();
    private static IngredientChipAdapterPantry ingredientFreezeAdapter = new IngredientChipAdapterPantry();

    private LinearLayout designBottomSheet;
    //test ricerca e aggiunta di ricetta
    private IngredientApi primo;
    private IngredientApi secondo;
    private IngredientApi terzo;
    private View idLayout;
    public PantryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_pantry, container, false);
        db = new DatabasePantryRepository(requireActivity().getApplication(), this);
        db.readAllIngredientPantry();

        FlexboxLayoutManager flexboxLayoutManagerPantry = new FlexboxLayoutManager(getContext());
        rvPantry = root.findViewById(R.id.rcv_pantry);
        rvPantry.setLayoutManager(flexboxLayoutManagerPantry);
        rvPantry.setFocusable(false);
        rvPantry.setNestedScrollingEnabled(false);

        FlexboxLayoutManager flexboxLayoutManagerFridgee = new FlexboxLayoutManager(getContext());
        rvFridge = root.findViewById(R.id.rcv_fridge);
        rvFridge.setLayoutManager(flexboxLayoutManagerFridgee);
        rvFridge.setFocusable(false);
        rvFridge.setNestedScrollingEnabled(false);

        FlexboxLayoutManager flexboxLayoutManagerFreezer = new FlexboxLayoutManager(getContext());
        rvFreezer = root.findViewById(R.id.rcv_freezer);
        rvFreezer.setLayoutManager(flexboxLayoutManagerFreezer);
        rvFreezer.setFocusable(false);
        rvFreezer.setNestedScrollingEnabled(false);


        //Ricerca dell'ingrediente in basso
        FlexboxLayoutManager flexboxLayoutManagerSearch = new FlexboxLayoutManager(getContext());
        rcvIngredientSearch = root.findViewById(R.id.rcv_ingredient_search);
        rcvIngredientSearch.setLayoutManager(flexboxLayoutManagerSearch);
        rcvIngredientSearch.setFocusable(false);
        rcvIngredientSearch.setNestedScrollingEnabled(false);

        rvPantry.setAdapter(ingredientPantryAdapter);
        rvFridge.setAdapter(ingredientFridgeAdapter);
        rvFreezer.setAdapter(ingredientFreezeAdapter);
        rcvIngredientSearch.setAdapter(searchChipAdapter);

        searchView = root.findViewById(R.id.sv_ingredient);

        designBottomSheet = root.findViewById(R.id.design_bottom_sheet);
        LinearLayout linearLayout = root.findViewById(R.id.design_bottom_sheet);

        bottomSheetBehavior = BottomSheetBehavior.from(designBottomSheet);
        bottomSheetBehavior.setDraggable(false);
        btnClose = root.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                closeKeyboard();
                searchView.clearFocus();
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                rvPantry.removeAllViews();

            }
        });

        //al focus della  search box espande la bottom sheet
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        //al press della tastiera
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 3) {
                    db.readIngredientApi(newText);
                    Log.d("test ", "cosa scrivo: " + newText);
                }else{
                    createSearchChips(null);
                }
                return false;
            }
        });

        createSearchChips(ingredientApiSearch);
        designBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
            }
        });

        idLayout = root.findViewById(R.id.container_pantry);

        idLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "click");
                cleanX();
            }
        });
        return root;
    }
    public static void cleanX(){
        rvPantry.removeAllViews();
        rvFridge.removeAllViews();
        rvFreezer.removeAllViews();
    }
    private void closeKeyboard() {
        // this will give us the view which is currently focus in this layout
        View view = this.getActivity().getCurrentFocus();
        // if nothing is currently focus then this will protect the app from crash
        if (view != null) {
            // now assign the system service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void savedb(IngredientPantry ingredientPantry) {
        int  idDb = pantry.size() + fridge.size() +freezer.size() + 2;
        db.create(ingredientPantry);
        Log.d("test",""+ idDb);
        ingredientPantry.setIdIngredient(idDb);
        if (ingredientPantry.pantryId == 1) {
            pantry.add(ingredientPantry);
            ingredientPantryAdapter.setData(pantry);
        } else if (ingredientPantry.pantryId == 2) {
            fridge.add(ingredientPantry);
            ingredientFridgeAdapter.setData(fridge);
        } else if (ingredientPantry.pantryId == 3) {
            freezer.add(ingredientPantry);
            ingredientFreezeAdapter.setData(freezer);
        }
    }

    public static void updateQuantity(IngredientPantry ingredientPantry){
        db.update(ingredientPantry);
    }

    @Override
    public void onResponse(Object obj) {
        if (obj != null) {
            if (obj instanceof List) {
                list = (List) obj;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pantry.clear();
                        fridge.clear();
                        freezer.clear();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).pantryId == 1) {
                                pantry.add(list.get(i));
                            } else if (list.get(i).pantryId == 2) {
                                fridge.add(list.get(i));
                            } else if (list.get(i).pantryId == 3) {
                                freezer.add(list.get(i));
                            }
                        }
                        createPantry(pantry);
                        createFridge(fridge);
                        createFreeze(freezer);
                    }
                });
            }
        }
    }
    @Override
    public void onResponsePantry(Object obj) {
    }
    @Override
    public void onResponseSearchIngredient(Object obj) {
        if (obj != null) {
            if (obj instanceof List) {
                List<IngredientApi> listIngredient = (List) obj;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createSearchChips(listIngredient);
                    }
                });
            }
        }
    }
    public static void deleteIngredient(IngredientPantry ingredientPantry){
        //Log.d("test","delete");
        db.delete(ingredientPantry);
        //db.readAllIngredientPantry();
    }
    private void createPantry(List<IngredientPantry> listData) {
        ingredientPantryAdapter.setData(listData);
    }
    private void createFridge(List<IngredientPantry> listData) {
        ingredientFridgeAdapter.setData(listData);
    }
    private void createFreeze(List<IngredientPantry> listData) {
        ingredientFreezeAdapter.setData(listData);
    }
    private void createSearchChips(List listData) {
        searchChipAdapter.setData(listData);
    }
    @Override
    public void onFailure(String errorMessage) {
    }
}