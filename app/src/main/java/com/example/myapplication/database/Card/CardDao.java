package com.example.myapplication.database.Card;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.Date;
import java.util.List;

@Dao
public interface CardDao {

    // permitie la inserción de la misma carta varias veces pasando una
    // estrategia de resolución de conflictos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Card card);

    @Query("DELETE FROM card_table")
    void deleteAll();

    @Query("SELECT * FROM card_table ORDER BY cardId ASC")
    LiveData<List<Card>> getCardsbyId();

    //Query para obtener el id de un mazo pasandole su nombre
    @Query("SELECT * FROM card_table WHERE deckId = :deckid ORDER BY front_text ASC")
    LiveData<List<Card>>getCardsWithId(Integer deckid);

    @Query("SELECT * FROM card_Table WHERE due_date <= :date AND deckId =:deckID ORDER BY due_date DESC LIMIT 5")
    LiveData<List<Card>> getOlderCards(Date date, Integer deckID);
}

