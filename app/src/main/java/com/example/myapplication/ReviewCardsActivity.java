package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
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
    private SharedPreferences sharedPreferences;
    private List<Card> cardList;
    private Card card;
    private Button frontText;
    private Button backText;
    private int pos;
    private DeckViewModel mDeckViewModel;
    private int currentDeckId;
    private View againButton;
    private View hardButton;
    private View goodButton;
    private View easyButton;


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
        frontText = findViewById(R.id.buttonFrontText);
        backText = findViewById(R.id.buttonBackText);

        //Se inicializan los botones
        againButton = findViewById(R.id.buttonAgain);
        hardButton = findViewById(R.id.buttonHard);
        goodButton = findViewById(R.id.buttonGood);
        easyButton = findViewById(R.id.buttonEasy);

        //Se actualiza el color de los botones en base al tema
        setTheme();

        //Se obtienen las cartas de la BD a partir del mazo seleccionado
        cardList = new ArrayList<>();
        AtomicBoolean initialState = new AtomicBoolean(true);
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);

        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);


        sharedPreferences = this.getSharedPreferences("PREFERENCIAS", MODE_PRIVATE);

        String userLimitPref = sharedPreferences.getString("daily_cards","20"); //TODO definir por el sharedpreference
        int userLimit = 20;

        switch (userLimitPref) {
            case "5":
                userLimit = 5;
                break;
            case "10":
                userLimit = 10;
                break;
            case "15":
                userLimit = 15;
                break;
            case "20":
                userLimit = 20;
                break;
            case "25":
                userLimit = 25;
                break;
        }

        mCardViewModel.getAllOlderCards(new Date(), currentDeckId, userLimit).observe(this, cards -> {

            if (initialState.get()) {
                cardList.addAll(cards);

                card = cardList.get(0);
                pos = 0;
                updateCardView(card);

                initialState.set(false);
            }
        });
    }

    /**
     * Cambia el color de los botones en base al tema actual
     */
    private void setTheme() {

        int nightModeFlags = this.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            //claro
            frontText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_blue_700)));
            backText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_700)));
            againButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_700)));
            hardButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_700)));
            goodButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.brown_700)));
            easyButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_700)));
        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            //oscuro
            frontText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_blue_200)));
            backText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_200)));
            againButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_200)));
            hardButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_200)));
            goodButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.brown_200)));
            easyButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_200)));
        }
    }

    //TODO: comprobar que se guarda el estado del juego cuando pausas en BD
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("Current_position", pos);
        savedInstanceState.putInt("CardId", card.getId());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        pos = savedInstanceState.getInt("Current_position");
        int cardId = savedInstanceState.getInt("CardId");
        mCardViewModel.getCardById(cardId).observe(this, c -> card = c);
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
            Date date = setNewDatebyDays(new Date(), 1);
            mDeckViewModel.updateDeckDate(currentDeckId, date);
            //Cierra la activity cuando ya no hay mas cartas
            this.finish();
        }
    }

    /**
     * Se encarda de mostrar la carta al usuario
     *
     * @param card la carta a actualizar
     */
    private void updateCardView(Card card) {
        frontText.setText(card.getFrontText());
        backText.setText(card.getBackText());
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