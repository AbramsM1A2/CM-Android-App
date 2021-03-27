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
//TODO: guardar en la BD el ultimo deck seleccionado para que no se pierda cuando la app muere?
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
        TextView textView = findViewById(R.id.deckName);
        textView.setText(deckName);

        //Get deck ID
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

        //Get cards
        cardList = new ArrayList<>();
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        mCardViewModel.getAllCards().observe(this, cards -> {
            for (Card s : cards) {
                //Implementar logica de paso de cartas y algoritmo

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

    private void nextCard() {
        int pos = cardList.indexOf(card);
        if (pos != cardList.size()-1){
        card = cardList.get(pos + 1);
        Log.d("nextCard",card.getFrontText());
        updateCard(card);
        }else{
            ReviewCardsActivity.this.finish(); //Cierra la activity
        }
    }

    private void updateCard(Card card) {
        Log.d("updateCard",card.getFrontText());
        frontext.setText(card.getFrontText());
        backtext.setText(card.getBackText());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonFrontText) {
            showUIAnswerAndButtons();
        } else if (id == R.id.buttonAgain) {
            hideUIAnswerAndButtons();
            nextCard();
        } else if (id == R.id.buttonHard) {
            // do something for button 2 click
        } else if (id == R.id.buttonGood) {
            // do something for button 2 click
        } else if (id == R.id.buttonEasy) {
            // do something for button 2 click
        }
    }

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

    //FULL SCREEN MODE - TODO: implement View.OnSystemUiVisibilityChangeListener & Implement onWindowFocusChanged() & Implement a GestureDetector that detects onSingleTapUp(MotionEvent)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}