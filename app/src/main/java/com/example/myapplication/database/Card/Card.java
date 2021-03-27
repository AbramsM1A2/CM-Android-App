package com.example.myapplication.database.Card;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "card_table")
public class Card {
    private static final AtomicInteger count = new AtomicInteger(0);
    //TODO: autoincrement aqui o en SQLite(esto no se como se hace, tal vez con una migracion)?
    @PrimaryKey
    @NonNull
    private Integer cardId;
    @NonNull
    @ColumnInfo(name = "front_text")
    private String frontText;

    @NonNull
    @ColumnInfo(name = "back_text")
    private String backText;

    @NonNull
    @ColumnInfo(name = "due_date")
    private Date dueDate;

    @NonNull
    @ColumnInfo(name = "deckId")
    private Integer deckId;


    public Card(@NonNull String frontText, @NonNull String backText, @NonNull Date dueDate, @NonNull Integer deckId) {
        count.incrementAndGet(); //CardId Increment
        this.frontText = frontText;
        this.backText = backText;
        this.dueDate = dueDate;
        this.deckId = deckId;
    }

    @NonNull
    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(@NonNull Integer cardId) {
        this.cardId = cardId;
    }

    @NonNull
    public String getFrontText() {
        return frontText;
    }

    public void setFrontText(@NonNull String frontText) {
        this.frontText = frontText;
    }

    @NonNull
    public String getBackText() {
        return backText;
    }

    public void setBackText(@NonNull String backText) {
        this.backText = backText;
    }

    @NonNull
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(@NonNull Date dueDate) {
        this.dueDate = dueDate;
    }

    @NonNull
    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(@NonNull Integer deckId) {
        this.deckId = deckId;
    }
}
