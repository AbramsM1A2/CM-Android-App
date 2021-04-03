package com.example.myapplication.database.Card;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.database.Repository;

import java.util.Date;
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
    public LiveData<List<Card>> getAllOlderCards(Date date, Integer deckID){return mRepository.getAllOlderCards(date, deckID);}

    public void insert(Card card) { mRepository.insert(card); }

    public void updateCardsDueDate(Date date, Integer cardId){mRepository.updateCardsDueDate(date,cardId);}
}
