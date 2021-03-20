package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionsDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Questions word);

    @Query("DELETE FROM questions_table")
    void deleteAll();

    @Query("SELECT * FROM questions_table ORDER BY question_text ASC")
    LiveData<List<Questions>> getAlphabetizedWords();
}

