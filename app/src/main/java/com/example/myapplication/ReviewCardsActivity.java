package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReviewCardsActivity extends AppCompatActivity implements View.OnClickListener {

    private DeckViewModel mDeckViewModel;
    private CardViewModel mCardViewModel;
    private LifecycleOwner lc;
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

        lc = this;
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        //TODO: algoritmo
        //DE x cartas coge 5 due y fecha anterior
        // populate db con mas cartas y fechas random
        // numero fijo de cartas (ajuste de usuario)
        //solo un repaso al dia

        //Se obtienen las cartas de la BD a partir del mazo seleccionado
        cardList = new ArrayList<>();
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        //selectCards();

        //TODO: hacer metodos en el viewmodel y no aqui
        //https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-4-saving-user-data/lesson-10-storing-data-with-room/10-1-c-room-livedata-viewmodel/10-1-c-room-livedata-viewmodel.html#viewmodel
        new DBTask().execute(new DBRunnable() {
            @Override
            public void executeDBTask() {
                //Se obtiene de la BD el ID del mazo a partir del mazo seleccionado en la lista de mazos
                mDeckViewModel.getAllDecks().observe(lc, decks -> {
                    for (Deck s : decks) {
                        Log.d("decktag", s.getNameText());
                        if (s.getNameText().equals(deckName)) {
                            deckId = s.getDeckId();
                            Log.d("tag", "id");
                            break;
                        }
                    }
                });

                //Se obtiene de la BD las cartas con fecha inferior a la de hoy
                mCardViewModel.getAllOlderCards(new Date(), deckId).observe(lc, olderCards -> {
                    cardList.addAll(olderCards);
                });
                //Se obtiene de la BD las cartas restantes
                mCardViewModel.getAllCards().observe(lc, cards -> {
                    while (cardList.size() < 20) {
                        for (Card s : cards) {
                            if (s.getDeckId().equals(deckId) && !cardList.contains(s)) {
                                cardList.add(s);
                                Log.d("CardList", s.getFrontText());
                            }
                        }
                    }
                });
            }

            @Override
            public void postExecuteDBTask() {
                // run your post execute code here
                updateCard(cardList.get(0));
                Log.d("FirstCard", card.getFrontText());
            }
        });

        Log.d("CardList", cardList.toString());
    }

    /**
     * Se encarga de mostrar la siguiente carta
     */
    private void nextCard() {
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

    /**
     * Controla la interfaz de la activity
     */
    private void viewHandler() {
        hideUIAnswerAndButtons();
        nextCard();
    }

    //TODO: Botones algoritmo
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

    public interface DBRunnable {
        public void executeDBTask();
        public void postExecuteDBTask();
    }
    private class DBTask extends AsyncTask<DBRunnable, Void, DBRunnable> {
        @Override
        protected DBRunnable doInBackground(DBRunnable...runnables) {
            runnables[0].executeDBTask();
            return runnables[0];
        }

        @Override
        protected void onPostExecute(DBRunnable runnable) {
            runnable.postExecuteDBTask();
        }
    }
}