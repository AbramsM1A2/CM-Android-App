package com.example.myapplication.database.Deck;

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
public interface DeckDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Deck deck);

    @Update
    void update(Deck deck);

    @Delete
    void delete(Deck deck);

    @Query("DELETE FROM deck_table")
    void deleteAll();

    @Query("SELECT * FROM deck_table ORDER BY id ASC")
    LiveData<List<Deck>> getAllDecks();


    //Custom methods
    @Query("SELECT * FROM deck_table WHERE nextPractice <= :date  ORDER BY nextPractice ASC")
    List<Deck> getDecksWithCurrentDate(Date date);

    @Query("UPDATE deck_table SET nextPractice=:date WHERE id=:deckId")
    void updateDeckDate(Integer deckId, Date date);

}
