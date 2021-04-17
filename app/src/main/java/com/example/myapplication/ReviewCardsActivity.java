package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReviewCardsActivity extends AppCompatActivity implements View.OnClickListener {

    private CardViewModel mCardViewModel;

    private List<Card> cardList;
    private Card card;
    private Button frontext;
    private Button backtext;
    private int pos;
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
        AtomicBoolean initialState = new AtomicBoolean(true);
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        AtomicBoolean control = new AtomicBoolean(false);

        mCardViewModel.getAllOlderCards(new Date(), currentDeckId).observe(this, cards -> {
            if (initialState.get()) {
                for (Card c : cards) {
                    System.out.println(c);
                    cardList.add(c);
                }
                control.set(true);
            }
            if (control.get()) {
                mCardViewModel.getAllCardsWithThisId(currentDeckId).observe(this, moreCards -> {
                    if (initialState.get()) {
                        if (moreCards.size() < 20) {
                            for (Card c : moreCards) {
                                if (!cardList.contains(c)) {
                                    cardList.add(c);
                                }
                            }

                            cardList.addAll(moreCards);
                        } else {
                            Collections.shuffle(moreCards);
                            for (int i = 0; i < 15; i++) {
                                if (!cardList.contains(moreCards.get(i))) {
                                    cardList.add(moreCards.get(i));
                                }
                            }

                        }
                    }
                    if (initialState.get()) {
                        card = cardList.get(0);
                        updateCardView(card);
                        pos=0;
                        initialState.set(false);
                        System.out.println("----initialState-----");
                        System.out.println("Current cardList: ");
                        for (Card c : cardList) {
                            System.out.println(c.getFrontText());
                        }
                        System.out.println("----END-----");
                    }

                });
            }

        });

    }


    /**
     * Se encarga de mostrar la siguiente carta
     */
    private void nextCard() {//TODO: bucle :(
        System.out.println("----NextCard-----");
        System.out.println("Current card: " + card.getFrontText());
        System.out.println("CardList size: " + cardList.size());
        System.out.println("Current Card Position: " + pos);

        pos = pos +1;
        if (pos != cardList.size()) {
            card = cardList.get(pos);
            System.out.println("Next card: " + card.getFrontText());
            updateCardView(card);
        } else {
            System.out.println("****************FINISH******************");
            //Cierra la activity cuando ya no hay mas cartas
            ReviewCardsActivity.this.finish();
        }
        System.out.println("----END-----");
    }

    /**
     * Se encarda de mostrar la carta al usuario
     *
     * @param card la carta a actualizar
     */
    private void updateCardView(Card card) {
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

    @Override
    public void onClick(View view) {
        System.out.println("----onCLick-----");
        int id = view.getId();
        if (id == R.id.buttonFrontText) {
            showUIAnswerAndButtons();
        } else if (id == R.id.buttonAgain) {
            cardList.add(card);
            viewHandler();
        } else if (id == R.id.buttonHard) { //3 dias
            Date updatedDate = setNewDatebyDays(card.getDueDate(), 3);
            System.out.println("Current card: " + card.getFrontText()+" date: " + card.getDueDate());
            System.out.println("Sum 3 days to the date: " + updatedDate);
            mCardViewModel.updateCardsDueDate(updatedDate, card.getCardId());
            System.out.println("card: " + card.getFrontText()+" Updateddate: " + card.getDueDate());
            viewHandler();

        } else if (id == R.id.buttonGood) { //10 dias
            Date updatedDate = setNewDatebyDays(card.getDueDate(), 10);
            System.out.println("Current card: " + card.getFrontText()+" date: " + card.getDueDate());
            System.out.println("Sum 10 days to the date: " + updatedDate);
            mCardViewModel.updateCardsDueDate(updatedDate, card.getCardId());
            viewHandler();

        } else if (id == R.id.buttonEasy) {//20 dias
            Date updatedDate = setNewDatebyDays(card.getDueDate(), 20);
            System.out.println("Current card: " + card.getFrontText()+" date: " + card.getDueDate());
            System.out.println("Sum 20 days to the date: " + updatedDate);
            mCardViewModel.updateCardsDueDate(updatedDate, card.getCardId());
            viewHandler();
        }
        System.out.println("----END-----");
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