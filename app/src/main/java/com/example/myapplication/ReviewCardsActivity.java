package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.DeckViewModel;


import java.util.ArrayList;
import java.util.Calendar;

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
    private DeckViewModel mDeckViewModel;
    private int currentDeckId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_cards);

        Intent intent = getIntent();
        currentDeckId = Integer.parseInt(intent.getStringExtra("selected_deck_id"));
        String currentDeckName = intent.getStringExtra("selected_deck_name");

        //Se actualiza el nombre de la barra superior con el mazo actual
        getSupportActionBar().setTitle(currentDeckName);

        //Se inicializan el anverso y el reverso
        frontext = findViewById(R.id.buttonFrontText);
        backtext = findViewById(R.id.buttonBackText);

        //Se obtienen las cartas de la BD a partir del mazo seleccionado
        cardList = new ArrayList<>();
        AtomicBoolean initialState = new AtomicBoolean(true);
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);

        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        mCardViewModel.getAllOlderCards(new Date(), currentDeckId).observe(this, cards -> {

            if (initialState.get()) {
                for (Card c : cards) {
                    cardList.add(c);
                }

                card = cardList.get(0);
                pos = 0;
                updateCardView(card);

                initialState.set(false);
            }
        });
    }

    //TODO: comprobar que funciona
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("Current_position", pos);
        savedInstanceState.putInt("CardId", card.getId());
    }

    //TODO: comprobar que funciona
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        pos = savedInstanceState.getInt("Current_position");
        int cardId = savedInstanceState.getInt("CardId");
        mCardViewModel.getCardById(cardId).observe(this, c -> {
            card = c;
        });
    }

    /**
     * Algoritmo de flashcards
     *
     * @param card    una carta
     * @param quality la calidad del aprendizaje
     */
    private void calculateSuperMemo2Algorithm(Card card, Integer quality) {

        // recuperar los valores almacenados (valores por defecto si las tarjetas son nuevas)
        int repetitions = card.getRepetitions();
        double easiness = card.getEasiness();
        int interval = card.getInterval();

        // factor de facilidad
        easiness = (Double) Math.max(1.3, easiness + 0.1 - (5.0 - quality) * (0.08 + (5.0 - quality) * 0.02));


        if (quality >= 3) {
            if (repetitions == 0) {
                interval = 1;
            } else if (repetitions == 1) {
                interval = 6;
            } else {
                interval = (int) Math.round(interval * easiness);
            }
            repetitions += 1;
            easiness = easiness + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
        } else {
            repetitions = 0;
            interval = 1;
        }

        if (easiness < 1.3) {
            easiness = 1.3;
        }

        // actualizamos la fecha de la siguiente practica
        Date nextPractice = setNewDatebyDays(card.getNextPractice(), interval);

        // Actualizamos la carta en la BD
        mCardViewModel.updateCard(card.getId(), repetitions, quality, easiness, interval, nextPractice);


        //Source: https://www.skoumal.com/en/how-does-the-learning-algorithm-in-the-flashcard-app-vocabulary-miner-work/
        //Source: https://github.com/thyagoluciano/sm2
        //source: https://www.javaer101.com/en/article/458455.html
    }

    /**
     * Se encarga de mostrar la siguiente carta
     */
    private void nextCard() {
        pos = pos + 1;
        if (pos != cardList.size()) {
            card = cardList.get(pos);
            updateCardView(card);
        } else {
            //TODO: Actualizar la fecha de practia del mazo

            Date date = setNewDatebyDays(new Date(), 1);
            mDeckViewModel.updateDeckDate(currentDeckId, date);
            //Cierra la activity cuando ya no hay mas cartas
            ReviewCardsActivity.this.finish();

        }
    }

    /**
     * Se encarda de mostrar la carta al usuario
     *
     * @param card la carta a actualizar
     */
    private void updateCardView(Card card) {
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
        int id = view.getId();
        if (id == R.id.buttonFrontText) {
            showUIAnswerAndButtons();
        } else if (id == R.id.buttonAgain) {
            Integer quality = 0;
            calculateSuperMemo2Algorithm(card, quality);
            viewHandler();
        } else if (id == R.id.buttonHard) { //3 dias
            Integer quality = 2;
            calculateSuperMemo2Algorithm(card, quality);
            viewHandler();

        } else if (id == R.id.buttonGood) { //10 dias
            Integer quality = 3;
            calculateSuperMemo2Algorithm(card, quality);
            viewHandler();

        } else if (id == R.id.buttonEasy) {//20 dias
            Integer quality = 5;
            calculateSuperMemo2Algorithm(card, quality);
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