package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;

public class ReviewCardsActivity extends AppCompatActivity {

    private DeckViewModel mDeckViewModel;
    private CardViewModel mCardViewModel;
    private Integer deckId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_cards);

        Intent intent = getIntent();
        String deckName = intent.getStringExtra("message_key");

        Button frontext = findViewById(R.id.buttonFrontText);
        Button backtext = findViewById(R.id.buttonBackText);

        //TODO: borrar
        TextView textView = findViewById(R.id.textView);
        textView.setText(deckName);

        //Get deck ID
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        mDeckViewModel.getAllDecks().observe(this, decks -> {
            for (Deck s : decks) {
                if (s.getNameText().equals(deckName)) {
                    deckId = s.getDeckId();
                    break;
                }
            }
        });

        //Get cards
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        mCardViewModel.getAllCards().observe(this, cards -> {
            for (Card s : cards) {
                frontext.setText(s.getFrontText());
                backtext.setText(s.getBackText());
            }
        });
    }
}