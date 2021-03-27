package com.example.myapplication.database.Card;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


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
}

