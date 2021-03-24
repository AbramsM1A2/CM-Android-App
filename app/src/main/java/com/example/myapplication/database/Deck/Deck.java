package com.example.myapplication.database.Deck;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "deck_table")
public class Deck {
    private static final AtomicInteger count = new AtomicInteger(0);

    @PrimaryKey
    @NonNull
    private Integer DeckId;
    @NonNull
    @ColumnInfo(name = "name_text")
    private String nameText;

    public Deck(@NonNull String nameText) {
        count.incrementAndGet(); //DeckId Increment
        this.nameText = nameText;
    }

    @NonNull
    public Integer getDeckId() {
        return DeckId;
    }

    public void setDeckId(@NonNull Integer deckId) {
        this.DeckId = deckId;
    }

    @NonNull
    public String getNameText() {
        return nameText;
    }

    public void setNameText(@NonNull String nameText) {
        this.nameText = nameText;
    }
}
