package com.example.myapplication.database.Card;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.database.Repository;

import java.util.List;

public class CardsViewModel extends AndroidViewModel {

    private Repository mRepository;

    private final LiveData<List<Card>> mAllQuestions;

    public CardsViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllQuestions = mRepository.getAllCards();
    }

    public LiveData<List<Card>> getAllCards() { return mAllQuestions; }

    public void insert(Card card) { mRepository.insert(card); }
}
