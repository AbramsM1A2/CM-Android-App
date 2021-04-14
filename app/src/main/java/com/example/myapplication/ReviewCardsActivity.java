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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ReviewCardsActivity extends AppCompatActivity implements View.OnClickListener {

    private CardViewModel mCardViewModel;

    private List<Card> cardList;
    private Card card;
    private Button frontext;
    private Button backtext;

    private int currentDeckId;
    private String currentDeckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_cards);

        Intent intent = getIntent();
        currentDeckId = Integer.parseInt(intent.getStringExtra("selected_deck_id"));
        currentDeckName = intent.getStringExtra("selected_deck_name");

        //Se actualiza el nombre de la barra superior con el mazo actual
        getSupportActionBar().setTitle(currentDeckName);

        //Se inicializan el anverso y el reverso
        frontext = findViewById(R.id.buttonFrontText);
        backtext = findViewById(R.id.buttonBackText);


        //TODO: algoritmo
        //DE x cartas coge 5 due y fecha anterior
        // populate db con mas cartas y fechas random
        // numero fijo de cartas (ajuste de usuario)
        //solo un repaso al dia

        //Se obtienen las cartas de la BD a partir del mazo seleccionado

        cardList = new ArrayList<>();
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);

        mCardViewModel.getAllOlderCards(new Date(), currentDeckId).observe(this, cards -> {
            for (Card c : cards) {
                System.out.println(c);
                cardList.add(c);
            }
            System.out.println(cardList);
            mCardViewModel.getAllCardsWithThisId(currentDeckId).observe(this, moreCards -> {
                if (moreCards.size()<20){
                    cardList.addAll(moreCards);
                }else {
                    Collections.shuffle(moreCards);
                    int end=15;
                    while (cardList.size() < 19) {
                        for (int i = 0; i < end; i++) {
                            if (!cardList.contains(moreCards.get(i))) {
                                cardList.add(moreCards.get(i));
                            }
                        }
                    }
                }
            });
            card=cardList.get(0);
            updateCard(card);
        });

    }


    /**
     * Se encarga de mostrar la siguiente carta
     */
    private void nextCard() {//TODO: creo que cuando se le da again esto hace bucle
        int pos = cardList.indexOf(card);
        if (pos != cardList.size() - 1) {
            card = cardList.get(pos + 1);
            Log.d("nextCard", card.getFrontText());
            updateCard(card);
        } else {
            //Cierra la activity cuando ya no hay mas cartas
            ReviewCardsActivity.this.finish();
        }
    }

    /**
     * Se encarda de mostrar la carta al usuario
     *
     * @param card la carta a actualizar
     */
    private void updateCard(Card card) {
        Log.d("updateCard", card.getFrontText());
        frontext.setText(card.getFrontText());
        backtext.setText(card.getBackText());
    }

    /**
     * Permite incrementar o decrementar una fecha
     *
     * @param date una fecha
     * @param days numero de dias a actualizar
     * @return fecha actualizada
     */
    private Date setNewDatebyDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //un número negativo disminuiría los días
        return cal.getTime();
    }


    //TODO: Botones algoritmo, comprobar updates de la BD
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonFrontText) {
            showUIAnswerAndButtons();
        } else if (id == R.id.buttonAgain) {
            cardList.add(card);
            viewHandler();
        } else if (id == R.id.buttonHard) { //3 dias
            mCardViewModel.updateCardsDueDate(setNewDatebyDays(card.getDueDate(), 3), card.getCardId());
            viewHandler();

        } else if (id == R.id.buttonGood) { //10 dias
            mCardViewModel.updateCardsDueDate(setNewDatebyDays(card.getDueDate(), 10), card.getCardId());
            viewHandler();

        } else if (id == R.id.buttonEasy) {//20 dias
            mCardViewModel.updateCardsDueDate(setNewDatebyDays(card.getDueDate(), 20), card.getCardId());
            viewHandler();
        }
    }

    /**
     * Controla la interfaz de la activity
     */
    private void viewHandler() {
        hideUIAnswerAndButtons();
        nextCard();
    }

    /**
     * Controlar la visibilidad de los elementos para el usuario, concretamente los oculta
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
     * Controlar la visibilidad de los elementos para el usuario, concretamente los muestra
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