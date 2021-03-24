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

public class ReviewCardsActivity extends AppCompatActivity implements View.OnClickListener {

    private DeckViewModel mDeckViewModel;
    private CardViewModel mCardViewModel;
    private Integer deckId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_cards);
        Log.d("intetn","pressed");
        Intent intent = getIntent();
        String deckName = intent.getStringExtra("message_key");

        Button frontext = findViewById(R.id.buttonFrontText);
        Button backtext = findViewById(R.id.buttonBackText);
        //backtext.setVisibility(View.INVISIBLE);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonFrontText) {
            Log.d("bb","pressed");
            Button b = findViewById(R.id.buttonBackText);
            b.setVisibility(View.VISIBLE);
        } else if (id == R.id.button3) {
            // do something for button 2 click
        }else if (id == R.id.button4) {
            // do something for button 2 click
        }else if (id == R.id.button5) {
            // do something for button 2 click
        }else if (id == R.id.button6) {
            // do something for button 2 click
        }
    }

}