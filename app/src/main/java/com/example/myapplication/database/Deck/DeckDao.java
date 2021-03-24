package com.example.myapplication.database.Deck;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface DeckDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Deck deck);

    @Query("DELETE FROM deck_table")
    void deleteAll();

    @Query("SELECT * FROM deck_table ORDER BY DeckId ASC")
    LiveData<List<Deck>> getDecksById();
}
