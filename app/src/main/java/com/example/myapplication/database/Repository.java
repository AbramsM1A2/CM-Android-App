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

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public Repository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);

        mCardDao = db.CardDao();
        mAllCards = mCardDao.getCardsbyId();

        mDeckDao = db.DeckDao();
        mAllDecks = mDeckDao.getDecksById();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Card>> getAllCards() {
        return mAllCards;
    }

    public LiveData<List<Deck>> getAllDecks() {
        return mAllDecks;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
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