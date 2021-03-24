package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        Intent intent = getIntent();
        String deckName = intent.getStringExtra("message_key");

        frontext = findViewById(R.id.buttonFrontText);
        backtext = findViewById(R.id.buttonBackText);
        //backtext.setVisibility(View.INVISIBLE);
        //TODO: borrar
        TextView textView = findViewById(R.id.textView);
        textView.setText(deckName);

        //Get deck ID
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        mDeckViewModel.getAllDecks().observe(this, decks -> {
            for (Deck s : decks) {
                Log.d("decktag",s.getNameText());
                if (s.getNameText().equals(deckName)) {
                    deckId = s.getDeckId();
                    Log.d("tag","id");
                    break;
                }
            }
        });

        //Get cards
        cardList = new ArrayList<>();
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        mCardViewModel.getAllCards().observe(this, cards -> {
            for (Card s : cards) {
                //Implementar logica de paso de cartas y algoritmo

                if (s.getDeckId().equals(deckId)) {
                    cardList.add(s);
                    Log.d("cardtag",s.getFrontText());
                }
            }
            if (!cardList.isEmpty()) {
                updateCard(cardList.get(0));
            }
        });


    }
    private void nextCard(){
        int pos = cardList.indexOf(card);
        card = cardList.get(pos+1);
        updateCard(card);
    }
    private void updateCard(Card card){
        frontext.setText(card.getFrontText());
        backtext.setText(card.getBackText());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonFrontText) {
            Button b = findViewById(R.id.buttonBackText);
            b.setVisibility(View.VISIBLE);
        } else if (id == R.id.buttonAgain) {
            Button b = findViewById(R.id.buttonBackText);
            b.setVisibility(View.INVISIBLE);
            nextCard();
        }else if (id == R.id.buttonHard) {
            // do something for button 2 click
        }else if (id == R.id.buttonGood) {
            // do something for button 2 click
        }else if (id == R.id.buttonEasy) {
            // do something for button 2 click
        }
    }

}