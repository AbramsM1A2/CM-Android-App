package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CardsViewModel extends AndroidViewModel {

    private CardsRepository mRepository;

    private final LiveData<List<Questions>> mAllQuestions;

    public CardsViewModel (Application application) {
        super(application);
        mRepository = new CardsRepository(application);
        mAllQuestions = mRepository.getAllWords();
    }

    LiveData<List<Questions>> getAllWords() { return mAllQuestions; }

    public void insert(Questions question) { mRepository.insert(question); }
}
