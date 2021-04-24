package com.example.myapplication.database.Deck;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.database.Repository;

import java.util.Date;
import java.util.List;

public class DeckViewModel extends AndroidViewModel {
    private final Repository mRepository;

    private final LiveData<List<Deck>> mAllDecks;

    public DeckViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllDecks = mRepository.getAllDecks();
    }

    public LiveData<List<Deck>> getAllDecks() {
        return mAllDecks;
    }


    public void insert(Deck deck) {
        mRepository.insertDeck(deck);
    }

    public void update(Deck deck){
        mRepository.updateDeck(deck);
    }

    public void delete(Deck deck){
        mRepository.deleteDeck(deck);
    }
    public void deleteAll(){
        mRepository.deleteAllDecks();
    }

    public List<Deck> getDecksCurrentDate(Date date) {
        return mRepository.getDecksWithCurrentDate(date);
    }

    public void updateDeckDate(Integer deckId, Date date) {
        mRepository.updateDeckDate(deckId, date);
    }
}
