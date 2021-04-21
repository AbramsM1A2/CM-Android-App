package com.example.myapplication.bottomMenu.cardsTab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

public class AddCardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DeckViewModel mDeckViewModel;
    private CardViewModel mCardViewModel;
    private String mMsg_snack_add_card;
    private String deckName;
    //Diccionario con mazos y sus IDs;
    private Map<String, Integer> mDecksByName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        getSupportActionBar().setTitle(getApplicationContext().getString(R.string.fab_add_card));
        //Para tener el boton de hacia atras
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Msg to the user when he adds a deck to the database
        mMsg_snack_add_card = getApplicationContext().getString(R.string.msg_snack_add_card);


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
            for (Deck s : decks) {
                //Podriamos poner solo una estructura de datos
                mDecksByName.put(s.getNameText(),s.getDeckId());
                adapter.add(s.getNameText());
            }
        });

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Boton
        final Button add_card_button = (Button) findViewById(R.id.buttonAddCard);

        //Buscamos los EditText
        EditText mEditTextFront = (EditText) findViewById(R.id.editTextAddFront);
        EditText mEditTextBack = (EditText) findViewById(R.id.editTextAddBack);



        //Listener del boton
        add_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos los editText a strings
                String Front = mEditTextFront.getText().toString();
                String Back = mEditTextBack.getText().toString();

                //El id del deck elegido
                Integer deckId = mDecksByName.get(deckName);
                if (deckId == null){
                    //En el caso de que se deje en blanco el nombre del mazo, es decir, que no exista ningun mazo
                    Toast.makeText(getApplicationContext(),R.string.msg_toast_noMaze,Toast.LENGTH_LONG).show();
                }
                else if(Front.isEmpty() || Back.isEmpty()){
                    //En el caso de que se deje en blanco el el front o el back de una carta
                    Toast.makeText(getApplicationContext(),R.string.msg_toast_card_noText,Toast.LENGTH_LONG).show();
                }
                else{
                    Card card = new Card(Front, Back, deckId,0,null,2.5,0, new Date());
                    mCardViewModel.insert(card);
                    Snackbar.make(v, mMsg_snack_add_card,Snackbar.LENGTH_SHORT).show();

                    //Para vaciar los campos despues de añadir la carta
                    mEditTextFront.getText().clear();
                    mEditTextBack.getText().clear();

                }

            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        deckName = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public boolean onSupportNavigateUp() {
        //Metodo para el boton de atras del actionbar
        finish();
        return true;
    }
}