package com.example.myapplication.bottomMenu.cardsTab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DeckViewModel mDeckViewModel;
    private CardViewModel mCardViewModel;
    private String mMsg_snack_edit_card;
    private Card card;
    private Integer mCardId;
    private String mfrontText;
    private String mBackText;
    private Integer mCardDeckId;
    private String mDeckSelected;
    private Map<String, Integer> mDecksByName;
    private Map<Integer, String> mDecksById;

    private Button edit_card_button;
    private Button delete_card_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        //INTENT EXTRA STUFF
        if(getIntent().hasExtra("selected_card")){
            card = getIntent().getParcelableExtra("selected_card");
            mCardId = card.getId();
            mfrontText = card.getFrontText();
            mBackText = card.getBackText();
            mCardDeckId = card.getDeckId();
        }

        //ACTION BAR
        getSupportActionBar().setTitle(getApplicationContext().getString(R.string.edit_card));
        //Para tener el boton de hacia atras
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Msg to the user when he edits the card
        mMsg_snack_edit_card = getApplicationContext().getString(R.string.msg_snack_edit_card);


        //-----------Spinner-----------------------
        Spinner spinner = (Spinner) findViewById(R.id.spinnerDecks);
        spinner.setOnItemSelectedListener(this);

        //Data for the Spinner
        List<String> values = new ArrayList<String>();

        // Create an ArrayAdapter using the string and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, values);

        //Iniciar el diccionario
        mDecksByName = new HashMap<String, Integer>();
        mDecksById = new HashMap<Integer, String>();

        //Viewmodel de BD
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        mDeckViewModel.getAllDecks().observe(this, decks -> {
            values.clear();
            for (Deck s : decks) {
                mDecksByName.put(s.getNameText(), s.getId());
                mDecksById.put(s.getId(),s.getNameText());
                adapter.add(s.getNameText());

            }
            int deckPositionInSpinner = adapter.getPosition(mDecksById.get(mCardDeckId));
            spinner.setSelection(deckPositionInSpinner);

        });

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Boton
        edit_card_button = (Button) findViewById(R.id.buttonEditCard);
        delete_card_button = (Button) findViewById(R.id.buttonDeleteCard);

        //Buscamos los EditText
        EditText mEditTextFront = (EditText) findViewById(R.id.editTextEditFront);
        EditText mEditTextBack = (EditText) findViewById(R.id.editTextEditBack);

        //Rellenamos los diferentes objetos con los datos de la carta
        //Front y back
        mEditTextFront.setText(mfrontText);
        mEditTextBack.setText(mBackText);

        //Listener del boton
        edit_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos los editText a strings
                String Front = mEditTextFront.getText().toString();
                String Back = mEditTextBack.getText().toString();

                //El id del deck elegido
                Integer deckId = mDecksByName.get(mDeckSelected);
                if (deckId == null) {
                    //En el caso de que se deje en blanco el nombre del mazo, es decir, que no exista ningun mazo
                    Toast.makeText(getApplicationContext(), R.string.msg_toast_noSelectedMaze, Toast.LENGTH_LONG).show();
                } else if (Front.isEmpty() || Back.isEmpty()) {
                    //En el caso de que se deje en blanco el el front o el back de una carta
                    Toast.makeText(getApplicationContext(), R.string.msg_toast_card_noText, Toast.LENGTH_LONG).show();
                } else {
                    AsyncTask.execute(() -> mCardViewModel.updateCardTextsAndDeck(mCardId,Front,Back,deckId));
                    finish();
                    Snackbar.make(v, mMsg_snack_edit_card, Snackbar.LENGTH_SHORT).show();

                }

            }
        });

        delete_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Msgs to user in alert dialog
                String msg_to_user = getApplicationContext().getString(R.string.msg_delete_deck) + " " + mfrontText+"-"+mBackText;
                String title_to_user = getApplicationContext().getString(R.string.delete_card) + "?";

                new AlertDialog.Builder(v.getContext())
                        .setTitle(title_to_user)
                        .setMessage(msg_to_user)
                        .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCardViewModel.deleteCard(card);
                                finish();
                            }

                        })
                        .setNegativeButton(R.string.btn_cancel, null)
                        .show();

            }
        });

        setTheme();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mDeckSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean onSupportNavigateUp() {
        //Metodo para el boton de atras del actionbar
        finish();
        return true;
    }
    private void setTheme() {

        int nightModeFlags = this.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            //claro
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cp_light_blue_700)));
            edit_card_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_blue_700)));
            delete_card_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_700)));

        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            //oscuro
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cp_light_blue_200)));
            edit_card_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_blue_200)));
            delete_card_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_200)));

        }
    }
}