package com.example.myapplication.database.Deck;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Repository;

import java.util.List;

public class DeckViewModel extends AndroidViewModel {
    private Repository mRepository;

    private final LiveData<List<Deck>> mAllDecks;

    public DeckViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllDecks = mRepository.getAllDecks();
    }

    public LiveData<List<Deck>> getAllDecks() { return mAllDecks; }

    public void insert(Deck deck) { mRepository.insert(deck); }
}
