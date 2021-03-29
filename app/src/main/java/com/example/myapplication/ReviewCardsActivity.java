package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReviewCardsActivity extends AppCompatActivity implements View.OnClickListener {

    private DeckViewModel mDeckViewModel;
    private CardViewModel mCardViewModel;
    private Integer deckId;
    private List<Card> cardList;
    private Card card;
    private Button frontext;
    private Button backtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_cards);

        //TODO pasar los datos del deck mas eficiente, y asi ahorrarme la consulta
        Intent intent = getIntent();
        String deckName = intent.getStringExtra("message_key");

        frontext = findViewById(R.id.buttonFrontText);
        backtext = findViewById(R.id.buttonBackText);

        //TODO: Poner nombre del mazo?
        TextView textView = findViewById(R.id.deckName);
        textView.setText(deckName);

        //Se obtiene de la BD el ID del mazo a partir del mazo seleccionado en la lista de mazos
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        mDeckViewModel.getAllDecks().observe(this, decks -> {
            for (Deck s : decks) {
                Log.d("decktag", s.getNameText());
                if (s.getNameText().equals(deckName)) {
                    deckId = s.getDeckId();
                    Log.d("tag", "id");
                    break;
                }
            }
        });

        //Se obtienen las cartas de la BD a partir del mazo seleccionado
        cardList = new ArrayList<>();
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        mCardViewModel.getAllCards().observe(this, cards -> {
            for (Card s : cards) {
                //TODO: algoritmo
                //DE x cartas coge 5 due y fecha anterior
                // populate db con mas cartas y fechas random
                // numero fijo de cartas (ajuste de usuario)
                //solo un repaso al dia
                if (s.getDeckId().equals(deckId)) {
                    cardList.add(s);
                    Log.d("CardList", s.getFrontText());
                }
            }
            if (!cardList.isEmpty()) {
                card=cardList.get(0);
                updateCard(card);
                Log.d("FirstCard",card.getFrontText());
            }
        });


    }

    /**
     * Se encarga de mostrar la siguiente carta
     */
    private void nextCard() {
        int pos = cardList.indexOf(card);
        if (pos != cardList.size()-1){
        card = cardList.get(pos + 1);
        Log.d("nextCard",card.getFrontText());
        updateCard(card);
        }else{
            //Cierra la activity cuando ya no hay mas cartas
            ReviewCardsActivity.this.finish();
        }
    }

    /**
     * Se encarda de mostrar la carta al usuario
     * @param card
     */
    private void updateCard(Card card) {
        Log.d("updateCard",card.getFrontText());
        frontext.setText(card.getFrontText());
        backtext.setText(card.getBackText());
    }

    //TODO: Botones algoritmo
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonFrontText) {
            showUIAnswerAndButtons();
        } else if (id == R.id.buttonAgain) {
            hideUIAnswerAndButtons(); //meterlo de nuevo en cardList o salir al dia siguiente
            nextCard();
        } else if (id == R.id.buttonHard) { //3 dias
            // do something for button 2 click
        } else if (id == R.id.buttonGood) { //10 dias
            // do something for button 2 click
        } else if (id == R.id.buttonEasy) {//20 dias
            // do something for button 2 click
        }
    }

    /**
     * Se encarga de controlar la visibilidad de los elementos para el usuario
     */
    private void hideUIAnswerAndButtons() {
        Button backText = findViewById(R.id.buttonBackText);
        backText.setVisibility(View.INVISIBLE);

        Button again = findViewById(R.id.buttonAgain);
        again.setVisibility(View.INVISIBLE);

        Button hard = findViewById(R.id.buttonHard);
        hard.setVisibility(View.INVISIBLE);

        Button good = findViewById(R.id.buttonGood);
        good.setVisibility(View.INVISIBLE);

        Button easy = findViewById(R.id.buttonEasy);
        easy.setVisibility(View.INVISIBLE);
    }

    /**
     * Se encarga de controlar la visibilidad de los elementos para el usuario
     */
    private void showUIAnswerAndButtons() {
        Button backText = findViewById(R.id.buttonBackText);
        backText.setVisibility(View.VISIBLE);

        Button again = findViewById(R.id.buttonAgain);
        again.setVisibility(View.VISIBLE);

        Button hard = findViewById(R.id.buttonHard);
        hard.setVisibility(View.VISIBLE);

        Button good = findViewById(R.id.buttonGood);
        good.setVisibility(View.VISIBLE);

        Button easy = findViewById(R.id.buttonEasy);
        easy.setVisibility(View.VISIBLE);
    }



}