package com.example.myapplication.database.Card;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.Date;
import java.util.List;

@Dao
public interface CardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Card card);

    @Query("DELETE FROM card_table")
    void deleteAll();

    @Update
    void update(Card card);

    @Delete
    void delete(Card card);

    @Query("DELETE FROM card_table WHERE deckId = :deckid")
    void deleteAllCardsFromDeckId(Integer deckid);

    @Query("SELECT * FROM card_table ORDER BY id ASC")
    LiveData<List<Card>> getAllCards();

    @Query("SELECT * FROM card_table WHERE id=:cardId")
    LiveData<Card> getCardById(Integer cardId);

    @Query("UPDATE card_table SET repetitions=:repetitions, quality=:quality, easiness=:easiness, interval=:interval, nextPractice=:nextPractice   WHERE id=:cardId")
    void updateCard(Integer cardId, Integer repetitions, Integer quality,
                    Double easiness,
                    Integer interval,
                    Date nextPractice);

    @Query("SELECT * FROM card_Table WHERE nextPractice <= :date AND deckId =:deckID ORDER BY nextPractice ASC LIMIT 20")
    LiveData<List<Card>> getOlderCards(Date date, Integer deckID);

    //Query para obtener el id de un mazo pasandole su nombre
    @Query("SELECT * FROM card_table WHERE deckId = :deckid ORDER BY frontText ASC")
    LiveData<List<Card>> getCardsWithId(Integer deckid);

    @Query("UPDATE card_table SET deckId =:deckID, frontText=:frontTEXT, backText=:backTEXT WHERE id=:cardID")
    void updateCardTextsAndDeck(Integer cardID,String frontTEXT, String backTEXT,Integer deckID);

}

