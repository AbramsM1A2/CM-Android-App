package com.example.myapplication.bottomMenu.cardsTab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddCardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DeckViewModel mDeckViewModel;
    private CardViewModel mCardViewModel;
    private String mMsg_snack_add_card;

    //TODO AÑADIR LAS ACCIONES DEL BOTON, ALGUNA COMPROBACION EN LOS STRINGS Y LA ACCION DE LA BASE DE DATOS ADEMAS DEL ALERT DIALOG

    //AÑADIR TAMBIEN LOS AVANCES HECHOS EN ADD DECK
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

        //Viewmodel de BD
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        mDeckViewModel.getAllDecks().observe(this, decks -> {
            for (Deck s : decks) {
                //Podriamos poner solo una estructura de datos
                adapter.add(s.getNameText());
            }
        });

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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