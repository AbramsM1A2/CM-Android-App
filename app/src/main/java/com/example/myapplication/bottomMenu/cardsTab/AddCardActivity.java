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

    //TODO HACER QUE EL SPINNER MUESTRE LOS NOMBRES DEL MISMO TAMAÑO QUE LAS DEMAS LETRAS

    //TODO CREO QUE EL TAMAÑO DEL SPINNER ES UN PELIN MAS PEQUEÑO QUE EL TEXTVIEW DE AL LADO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        getSupportActionBar().setTitle(getApplicationContext().getString(R.string.fab_add_card));


    //-----------Spinner-----------------------
    Spinner spinner = (Spinner) findViewById(R.id.spinnerDecks);
    spinner.setOnItemSelectedListener(this);

    //Data for the Spinner
    List<String> values = new ArrayList<String>();

    // Create an ArrayAdapter using the string and a default spinner layout
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);

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
}