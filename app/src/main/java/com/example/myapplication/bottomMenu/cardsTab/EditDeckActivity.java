package com.example.myapplication.bottomMenu.cardsTab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditDeckActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DeckViewModel mDeckViewModel;
    private CardViewModel mCardViewModel;
    private String deckName;
    private String mMsg_snack_edit_deck;
    private String mMsg_snack_delete_deck;
    //Diccionario con mazos y sus IDs;
    private Map<String, Integer> mDecksByName;
    //EditTexts
    EditText mEditTextDeckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);
        getSupportActionBar().setTitle(getApplicationContext().getString(R.string.fab_edit_deck));
        //Para tener el boton de hacia atras
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Msg to the user when he edits a deck
        mMsg_snack_edit_deck = getApplicationContext().getString(R.string.msg_snack_edit_deck);
        //Msg to the user when he deletes a deck
        mMsg_snack_delete_deck = getApplicationContext().getString(R.string.msg_snack_deleted_deck);

        //-----------Spinner-----------------------
        Spinner spinner = (Spinner) findViewById(R.id.spinnerDecks);
        spinner.setOnItemSelectedListener(this);

        //Data for the Spinner
        List<String> values = new ArrayList<String>();

        // Create an ArrayAdapter using the string and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, values);

        //Iniciar el diccionario
        mDecksByName = new HashMap<String, Integer>();

        //Viewmodel de BD
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        mDeckViewModel.getAllDecks().observe(this, decks -> {
            values.clear();
            for (Deck s : decks) {
                //Podriamos poner solo una estructura de datos
                mDecksByName.put(s.getNameText(), s.getId());
                adapter.add(s.getNameText());
            }
        });

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Botones y UI
        final Button edit_deck_button = (Button) findViewById(R.id.buttonEditDeck);
        final Button delete_deck_button = (Button) findViewById(R.id.buttonDeleteDeck);
        mEditTextDeckName = (EditText) findViewById(R.id.editTextDeckName);

        edit_deck_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String editTextDeckName = mEditTextDeckName.getText().toString();
                //Para que no se añadan nombres en blanco
                if (!deckName.isEmpty()) {

                    //Msgs to user in alert dialog
                    String msg_to_user = getApplicationContext().getString(R.string.msg_edit_deck) + " " + deckName + getApplicationContext().getString(R.string.to) + editTextDeckName;
                    String title_to_user = getApplicationContext().getString(R.string.edit_deck) + "?";

                    new AlertDialog.Builder(v.getContext())
                            .setTitle(title_to_user)
                            .setMessage(msg_to_user)
                            .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Si no funciona la view v poner mejor findViewById(android.R.id.content)
                                    //El id del deck elegido
                                    Integer deckId = mDecksByName.get(deckName);
                                    //Estas son las opciones disponibles: https://stackoverflow.com/questions/59607324/error-cannot-access-database-on-the-main-thread-since-it-may-potentially-lock-t
                                    AsyncTask.execute(() -> mDeckViewModel.updateDeckNameById(editTextDeckName, deckId));
                                    Snackbar.make(v, mMsg_snack_edit_deck, Snackbar.LENGTH_LONG).show();

                                }
                            })
                            .setNegativeButton(R.string.btn_cancel, null)
                            .show();

                } else {
                    //En el caso de que se deje en blanco el nombre del mazo
                    Toast.makeText(getApplicationContext(), R.string.msg_toast_empty, Toast.LENGTH_LONG).show();
                }
            }
        });

        delete_deck_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!deckName.isEmpty()) {

                    //Msgs to user in alert dialog
                    String msg_to_user = getApplicationContext().getString(R.string.msg_delete_deck) + " " + deckName;
                    String title_to_user = getApplicationContext().getString(R.string.delete_deck) + "?";

                    new AlertDialog.Builder(v.getContext())
                            .setTitle(title_to_user)
                            .setMessage(msg_to_user)
                            .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Integer deckId = mDecksByName.get(deckName);
                                    AsyncTask.execute(() -> mCardViewModel.deleteAllCardsFromDeckId(deckId));
                                    AsyncTask.execute(() -> mDeckViewModel.deleteDeckGivingDeckId(deckId));
                                    Snackbar.make(v, mMsg_snack_delete_deck, Snackbar.LENGTH_LONG).show();
                                    spinner.setSelection(0);
                                }

                            })
                            .setNegativeButton(R.string.btn_cancel, null)
                            .show();

                } else {
                    //En el caso de que se deje en blanco el nombre del mazo
                    Toast.makeText(getApplicationContext(), R.string.msg_toast_noMaze_to_delete, Toast.LENGTH_LONG).show();
                }
            }

        });


    }

    public boolean onSupportNavigateUp() {
        //Metodo para el boton de atras del actionbar
        finish();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        deckName = parent.getItemAtPosition(position).toString();
        mEditTextDeckName.setText(deckName);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}