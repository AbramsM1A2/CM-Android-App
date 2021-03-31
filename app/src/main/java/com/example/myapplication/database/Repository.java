package com.example.myapplication.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardDao;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckDao;

import java.util.List;

public class Repository {

    private CardDao mCardDao;
    private LiveData<List<Card>> mAllCards;

    private DeckDao mDeckDao;
    private LiveData<List<Deck>> mAllDecks;

    private LiveData<List<Card>> mAllCardsWithThisId;

    public Repository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);

        mCardDao = db.CardDao();
        mAllCards = mCardDao.getCardsbyId();

        mDeckDao = db.DeckDao();
        mAllDecks = mDeckDao.getDecksById();



    }

    // ROOM ejecuta todas las consultas en un hilo separado.
    // El LiveData observado notificará al observador cuando los datos hayan cambiado.
    public LiveData<List<Card>> getAllCards() {
        return mAllCards;
    }

    public LiveData<List<Deck>> getAllDecks() {
        return mAllDecks;
    }

    public LiveData<List<Card>> getAllCardsWithThisId(Integer idOfDeck){
        mAllCardsWithThisId = mCardDao.getCardsWithId(idOfDeck);
        return mAllCardsWithThisId;
    }

    // Debes llamar a esto en un hilo no-UI o tu aplicación lanzará una excepción. ROOM asegura
    // que no estás haciendo ninguna operación de larga duración en el hilo principal, bloqueando la UI.
    public void insert(Card card) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mCardDao.insert(card);
        });
    }

    public void insert(Deck deck) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mDeckDao.insert(deck);
        });
    }
}
