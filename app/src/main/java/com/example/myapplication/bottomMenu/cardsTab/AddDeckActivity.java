package com.example.myapplication.bottomMenu.cardsTab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddDeckActivity extends AppCompatActivity {
    private DeckViewModel mDeckViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO HACER QUE SE MODIFIQUE LA ACTION BAR Y PONER UNA MANERA DE VOLVER ATRAS (BASICAMENTE UN BOTON QUE CIERRE LA ACTIVIDAD)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deck);
        getSupportActionBar().setTitle(getApplicationContext().getString(R.string.fab_add_deck));

        //Viewmodel de BD
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        //Botones y UI
        final Button add_deck_button = (Button) findViewById(R.id.buttonAddDeck);
        EditText mEditTextDeckName = (EditText) findViewById(R.id.editTextDeckName);
        add_deck_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //TODO AÑADIR UN ALERTDIALOG PARA QUE SE AVISE ANTES DE AÑADIR UN MAZO
                String deckName = mEditTextDeckName.getText().toString();
                System.out.println(deckName);
                //TODO AÑADIR QUE SE HAGA ALGUNA COMPROBACION DEL STRING CON EL QUE NOMBRAMOS AL MAZO
                // TIPO QUE NO PUEDA SER UN STRING VACIO
                Deck deck = new Deck(deckName);
                mDeckViewModel.insert(deck);
                System.out.println("Creo que se ha insertado el deck");
            }
        });
    }
}