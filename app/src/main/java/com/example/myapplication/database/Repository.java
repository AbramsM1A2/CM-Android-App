package com.example.myapplication.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardDao;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckDao;

import java.util.Date;
import java.util.List;

public class Repository {

    private final CardDao mCardDao;
    private final LiveData<List<Card>> mAllCards;

    private final DeckDao mDeckDao;
    private final LiveData<List<Deck>> mAllDecks;

    private LiveData<List<Card>> mOnly5OldCards;

    public Repository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);

        mCardDao = db.CardDao();
        mAllCards = mCardDao.getAllCards();

        mDeckDao = db.DeckDao();
        mAllDecks = mDeckDao.getDecksById();
    }


    //------------------------------------Card---------------------------------------------

    // ROOM ejecuta todas las consultas en un hilo separado.
    // El LiveData observado notificará al observador cuando los datos hayan cambiado.
    public LiveData<List<Card>> getAllCards() {
        return mAllCards;
    }

    public LiveData<List<Card>> getAllCardsWithThisId(Integer idOfDeck) {
        return mCardDao.getCardsWithId(idOfDeck);
    }

    public LiveData<List<Card>> getAllOlderCards(Date date, Integer deckID) {
        return mCardDao.getOlderCards(date, deckID);
    }

    public LiveData<Card> getCardById(Integer cardId) {
        return mCardDao.getCardById(cardId);
    }
    public LiveData<List<Card>> get5OldCards(Date date, Integer idOfDeck){
        mOnly5OldCards = mCardDao.getOlderCards(date,idOfDeck);
        return mOnly5OldCards;
    }

    // Debes llamar a esto en un hilo no-UI o tu aplicación lanzará una excepción. ROOM asegura
    // que no estás haciendo ninguna operación de larga duración en el hilo principal, bloqueando la UI.
    public void insert(Card card) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mCardDao.insert(card));
    }


    public void updateCard(Integer cardId, Integer repetitions, Integer quality,
                           Double easiness,
                           Integer interval,
                           Date nextPractice) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mCardDao.updateCard(cardId, repetitions, quality, easiness, interval, nextPractice));
    }

    public void deleteAllCardsFromDeckId(Integer deckid){mCardDao.deleteAllCardsFromDeckId(deckid);
    }


    //------------------------------------Deck---------------------------------------------

    public LiveData<List<Deck>> getAllDecks() {
        return mAllDecks;
    }

    public void insert(Deck deck) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mDeckDao.insert(deck));
    }

    public void updateDeckNameById(String new_name_text,Integer deckID){
        mDeckDao.updateDeckNameById(new_name_text,deckID);
    };

    public void deleteDeckGivingDeckId(Integer deckid){
        mDeckDao.deleteDeckGivingDeckId(deckid);
    }


}
