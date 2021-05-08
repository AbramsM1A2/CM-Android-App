package com.example.myapplication.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardDao;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository {

    private final CardDao mCardDao;
    private final LiveData<List<Card>> mAllCards;

    private final DeckDao mDeckDao;
    private final LiveData<List<Deck>> mAllDecks;

    public Repository(Application application) {
        RoomDatabase db = RoomDatabase.getInstance(application);

        mCardDao = db.CardDao();
        mAllCards = mCardDao.getAllCards();

        mDeckDao = db.DeckDao();
        mAllDecks = mDeckDao.getAllDecks();
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

    public LiveData<List<Card>> getAllOlderCards(Date date, Integer deckID, Integer userLimit) {
        return mCardDao.getOlderCards(date, deckID, userLimit);
    }

    public LiveData<Card> getCardById(Integer cardId) {
        return mCardDao.getCardById(cardId);
    }

    public void deleteCard(Card card) {
        new DeleteCardAsyncTask(mCardDao).execute(card);
    }

    public void deleteAllCards() {
        new DeleteAllCardsAsyncTask(mCardDao).execute();
    }

    // Debes llamar a esto en un hilo no-UI o tu aplicación lanzará una excepción. ROOM asegura
    // que no estás haciendo ninguna operación de larga duración en el hilo principal, bloqueando la UI.
    public void insertCard(Card card) {
        new InsertCardAsyncTask(mCardDao).execute(card);
    }


    public void updateCard(Card card) {
        new UpdateCardAsyncTask(mCardDao).execute(card);
    }

    public void updateCardAlgorithm(Integer cardId, Integer repetitions, Integer quality,
                                    Double easiness,
                                    Integer interval,
                                    Date nextPractice) {

        new UpdateCardAlgorithmAsyncTask(mCardDao).execute(new UpdateCardParams(cardId, repetitions, quality, easiness, interval, nextPractice));

    }

    private static class UpdateCardParams {
        Integer cardId;
        Integer repetitions;
        Integer quality;
        Double easiness;
        Integer interval;
        Date nextPractice;

        public UpdateCardParams(Integer cardId, Integer repetitions, Integer quality, Double easiness, Integer interval, Date nextPractice) {
            this.cardId = cardId;
            this.repetitions = repetitions;
            this.quality = quality;
            this.easiness = easiness;
            this.interval = interval;
            this.nextPractice = nextPractice;
        }
    }

    //---------------------------Card AsyncTasks----------------------------
    private static class InsertCardAsyncTask extends AsyncTask<Card, Void, Void> {

        private CardDao cardDao;

        private InsertCardAsyncTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            cardDao.insert(cards[0]);
            return null;
        }
    }

    private static class UpdateCardAsyncTask extends AsyncTask<Card, Void, Void> {

        private CardDao cardDao;

        private UpdateCardAsyncTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            cardDao.update(cards[0]);
            return null;
        }
    }

    private static class UpdateCardAlgorithmAsyncTask extends AsyncTask<UpdateCardParams, Void, Void> {

        private CardDao cardDao;

        private UpdateCardAlgorithmAsyncTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(UpdateCardParams... params) {
            System.out.println("PARAMS: " + params);
            cardDao.updateCard(params[0].cardId, params[0].repetitions, params[0].quality, params[0].easiness, params[0].interval, params[0].nextPractice);
            return null;
        }
    }

    private static class DeleteCardAsyncTask extends AsyncTask<Card, Void, Void> {

        private CardDao cardDao;

        private DeleteCardAsyncTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            cardDao.delete(cards[0]);
            return null;
        }
    }

    private static class DeleteAllCardsAsyncTask extends AsyncTask<Void, Void, Void> {

        private CardDao cardDao;

        private DeleteAllCardsAsyncTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cardDao.deleteAll();
            return null;
        }
    }

    public void deleteAllCardsFromDeckId(Integer deckid) {
        mCardDao.deleteAllCardsFromDeckId(deckid);
    }

    public void updateCardTextsAndDeck(Integer cardID, String frontTEXT, String backTEXT, Integer deckID) {
        mCardDao.updateCardTextsAndDeck(cardID, frontTEXT, backTEXT, deckID);
    }


    //------------------------------------Deck---------------------------------------------

    public void insertDeck(Deck deck) {
        new InsertDeckAsyncTask(mDeckDao).execute(deck);
    }

    public void updateDeck(Deck deck) {
        new UpdateDeckAsyncTask(mDeckDao).execute(deck);
    }

    public void deleteDeck(Deck deck) {
        new DeleteDeckAsyncTask(mDeckDao).execute(deck);
    }

    public void deleteAllDecks() {
        new DeleteAllDecksAsyncTask(mDeckDao).execute();
    }

    public LiveData<List<Deck>> getAllDecks() {
        return mAllDecks;
    }


    public LiveData<List<Deck>> getDecksWithCurrentDate(Date date) {
        return mDeckDao.getDecksWithCurrentDate(date);
    }


    public void updateDeckDate(Integer deckId, Date date) {
        new updateDeckDateAsyncTask(mDeckDao).execute(new UpdateDeckParams(deckId, date));
    }

    private static class UpdateDeckParams {
        int id;
        Date date;

        UpdateDeckParams(int id, Date date) {
            this.id = id;
            this.date = date;
        }
    }

    //---------------------------Deck AsyncTasks----------------------------
    private static class InsertDeckAsyncTask extends AsyncTask<Deck, Void, Void> {

        private DeckDao deckDao;

        private InsertDeckAsyncTask(DeckDao deckDao) {
            this.deckDao = deckDao;
        }

        @Override
        protected Void doInBackground(Deck... decks) {
            deckDao.insert(decks[0]);
            return null;
        }
    }

    private static class UpdateDeckAsyncTask extends AsyncTask<Deck, Void, Void> {

        private DeckDao deckDao;

        private UpdateDeckAsyncTask(DeckDao deckDao) {
            this.deckDao = deckDao;
        }

        @Override
        protected Void doInBackground(Deck... decks) {
            deckDao.update(decks[0]);
            return null;
        }
    }

    private static class DeleteDeckAsyncTask extends AsyncTask<Deck, Void, Void> {

        private DeckDao deckDao;

        private DeleteDeckAsyncTask(DeckDao deckDao) {
            this.deckDao = deckDao;
        }

        @Override
        protected Void doInBackground(Deck... decks) {
            deckDao.delete(decks[0]);
            return null;
        }
    }

    private static class DeleteAllDecksAsyncTask extends AsyncTask<Void, Void, Void> {

        private DeckDao deckDao;

        private DeleteAllDecksAsyncTask(DeckDao deckDao) {
            this.deckDao = deckDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            deckDao.deleteAll();
            return null;
        }
    }

    private static class updateDeckDateAsyncTask extends AsyncTask<UpdateDeckParams, Void, Void> {

        private DeckDao deckDao;

        private updateDeckDateAsyncTask(DeckDao deckDao) {
            this.deckDao = deckDao;
        }

        @Override
        protected Void doInBackground(UpdateDeckParams... deckParams) {
            deckDao.updateDeckDate(deckParams[0].id, deckParams[0].date);
            return null;
        }

    }

    public void updateDeckNameById(String new_name_text, Integer deckID) {
        mDeckDao.updateDeckNameById(new_name_text, deckID);
    }

    public void deleteDeckGivingDeckId(Integer deckid) {
        mDeckDao.deleteDeckGivingDeckId(deckid);
    }


}
