package com.example.myapplication.database.Card;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.database.Repository;

import java.util.Date;
import java.util.List;

public class CardViewModel extends AndroidViewModel {

    private final Repository mRepository;

    private final LiveData<List<Card>> mAllCards;

    public CardViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllCards = mRepository.getAllCards();
    }

    public LiveData<List<Card>> getAllCards() {
        return mAllCards;
    }

    public LiveData<List<Card>> getAllOlderCards(Date date, Integer deckID, Integer userLimit) {
        return mRepository.getAllOlderCards(date, deckID, userLimit);
    }

    public LiveData<List<Card>> getAllCardsWithThisId(Integer id) {
        return mRepository.getAllCardsWithThisId(id);
    }

    public LiveData<Card> getCardById(Integer cardId) {
        return mRepository.getCardById(cardId);
    }

    public void insert(Card card) {
        mRepository.insertCard(card);
    }

    public void updateCard(Integer cardId, Integer repetitions, Integer quality,
                           Double easiness,
                           Integer interval,
                           Date nextPractice) {
        mRepository.updateCardAlgorithm(cardId, repetitions, quality, easiness, interval, nextPractice);
    }

    public void update(Card card) {
        mRepository.updateCard(card);
    }

    public void deleteCard(Card card) {
        mRepository.deleteCard(card);
    }

    public void deleteAllCards() {
        mRepository.deleteAllCards();
    }

    public void deleteAllCardsFromDeckId(Integer deckid) {
        mRepository.deleteAllCardsFromDeckId(deckid);
    }

    public void updateCardTextsAndDeck(Integer cardID,String frontTEXT, String backTEXT,Integer deckID) {
        mRepository.updateCardTextsAndDeck(cardID, frontTEXT, backTEXT, deckID);
    }

}
