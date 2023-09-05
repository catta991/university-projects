package it.unimib.cookery.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import it.unimib.cookery.R;


public class FragmentPerDialogCreazioneRicetta extends Fragment {


    /* dichiaro le variabili a cui associerò i bottoni */
    private Button aggiungiIngrediente;
    private Button aggiungiStep;


    /* dichiaro una una variabile per ottenere la quantità dalla dialog e una per ottenere il testo dello step */
    private int quantità = 0;
    private String stepString = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_per_dialog_creazione_ricetta, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        /* -- inizio codice dialog aggiunta ingrediente --*/

        // bottone che serve solo per invocare la dialog per aggiungere l'ingrediente poi da cancellare
        aggiungiIngrediente = view.findViewById(R.id.buttonIngrediente);

        // listener per il bottone per creare la dialog per aggiungere un ingrediente, da cancellare
        aggiungiIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            // da cancellare onClick
            public void onClick(View v) {

                // crea una dialog
                Dialog ingredientDialog = new Dialog(getContext());

                // elimina il titolo dalla dialog che non serve
                ingredientDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                // permette l'uscita dalla dialog solo se si preme cancella
                ingredientDialog.setCancelable(false);

                // setta il layout che poi verrà mostrato nella dialog
                ingredientDialog.setContentView(R.layout.layout_ingredient_dialog);

                // creo e trovo l'oggetto textView nella dialog
                TextView ingredientName = ingredientDialog.findViewById(R.id.IngredientName);

                // setto il teso della dialog la stringa andrà poi sostituita col nome dell'ingrediente da aggiungere
                ingredientName.setText("nome ingrediente da modificare");

                // creo e trovo l'oggetto editText dove l'utente inserisce la quantità
                EditText editText = ingredientDialog.findViewById(R.id.IngredientEditText);

                Log.d("debug", "" + editText);

                // creo e trovo il bottone per aggiungere l'ingrediente
                Button addButton = ingredientDialog.findViewById(R.id.addIngredientButton);

                //listener per il bottone per aggiungere l'ingrediente
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // prendo la quantità scritta nel text edit se non è vuota la converto in stringa e poi in intero
                        // se è > 0 la salvo mentre se è = 0 mostro il toast
                        // l'edit text è già settata solo per accettare numeri interi positivi

                        if (!editText.getText().toString().equals("") && (Integer.parseInt(editText.getText().toString())) > 0) {
                            // salvo la quantità inserita
                            quantità = Integer.parseInt(editText.getText().toString());
                            // chiude la dialog
                            ingredientDialog.dismiss();
                        } else {
                            // stampa un toast di errore
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.invalid_quantity, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });


                // creo e ottengo l'oggetto per il bottono di delete
                Button deleteButton = ingredientDialog.findViewById(R.id.deleteIngredientButton);

                // listener del bottone di delete
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pr sicurezza setto la quantità a 0
                        quantità = 0;
                        // chiude la dialog
                        ingredientDialog.dismiss();

                    }
                });

                // mostra la dialog a schermo
                ingredientDialog.show();


            }
        });

        /* -- fine codice dialog aggiunta ingrediente -- */


        /* -- inizio codice dialog aggiunta step --*/

        aggiungiStep = view.findViewById(R.id.buttonStep);

        aggiungiStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // crea una dialog
                Dialog stepDialog = new Dialog(getContext());

                // elimina il titolo dalla dialog che non serve
                stepDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                // permette l'uscita dalla dialog solo se si preme cancella
                stepDialog.setCancelable(false);

                // setta il layout che poi verrà mostrato nella dialog
                stepDialog.setContentView(R.layout.layout_step_dialog);

                // creo e ottengo il riferimento all'edit text nel file layout
                EditText editTextStep = stepDialog.findViewById(R.id.StepEditText);

                // creo e ottengo il riferimento al bottone per aggiungere gli step
                Button addButton = stepDialog.findViewById(R.id.yesButton);

                // listener per il bottone per aggiungere lo step
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // controllo che quando viene premuto il bottone il contenuto scritto non sia vuoto
                        if (!editTextStep.getText().toString().equals("")) {

                            // salvo il contenuto dello step e chiudo la dialog
                            stepString = editTextStep.getText().toString();
                            stepDialog.dismiss();

                        } else {
                            // stampa un toast di errore
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.invalid_step, Snackbar.LENGTH_SHORT).show();

                        }

                    }
                });

                // creo e ottengo il bottone per cancellare lo step scritto
                Button deleteButton = stepDialog.findViewById(R.id.noButton);

                // listener del bottone per cancellare
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // per sicurezza setto la stringa a stringa vuota e chiudo la dialog
                        stepString = "";
                        stepDialog.dismiss();
                    }
                });

                // mostra la dialog a schermo
                stepDialog.show();
            }
        });


    }
}