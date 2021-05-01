package com.example.myapplication.database.Deck;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface DeckDao {

    // permitie la inserción del mismo mazo varias veces pasando una
    // estrategia de resolución de conflictos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Deck deck);

    @Query("DELETE FROM deck_table")
    void deleteAll();

    @Query("DELETE FROM deck_table WHERE DeckId = :deckid")
    void deleteDeckGivingDeckId(Integer deckid);

    @Query("SELECT * FROM deck_table ORDER BY DeckId ASC")
    LiveData<List<Deck>> getDecksById();

    @Query("UPDATE deck_table SET name_text =:new_name_text WHERE DeckId =:deckID ")
    void updateDeckNameById(String new_name_text,Integer deckID);


}
