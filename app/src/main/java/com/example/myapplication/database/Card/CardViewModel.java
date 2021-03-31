package com.example.myapplication.database.Card;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.database.Repository;

import java.util.List;

public class CardViewModel extends AndroidViewModel {

    private Repository mRepository;

    private final LiveData<List<Card>> mAllCards;

    public CardViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllCards = mRepository.getAllCards();
    }

    public LiveData<List<Card>> getAllCards() { return mAllCards; }

    public LiveData<List<Card>> getAllCardsWithThisId(Integer id){
        return mRepository.getAllCardsWithThisId(id);
    }
    public void insert(Card card) { mRepository.insert(card); }
}
