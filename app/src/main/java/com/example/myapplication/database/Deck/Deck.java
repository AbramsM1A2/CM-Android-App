package com.example.myapplication.database.Deck;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "deck_table")
public class Deck {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String nameText;

    private Date nextPractice;

    public Deck(String nameText, Date nextPractice) {
        this.nameText = nameText;
        this.nextPractice = nextPractice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameText() {
        return nameText;
    }

    public Date getNextPractice() {
        return nextPractice;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "nameText='" + nameText + '\'' +
                ", nextPractice=" + nextPractice +
                '}';
    }
}
