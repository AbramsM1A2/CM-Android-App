package com.example.myapplication.database.Card;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "card_table")
public class Card {
    private static final AtomicInteger count = new AtomicInteger(0);

    @PrimaryKey
    @NonNull
    private Integer cardId;

    @NonNull
    @ColumnInfo(name = "deckId")
    private Integer deckId;

    @NonNull
    @ColumnInfo(name = "front_text")
    private String frontText;

    @NonNull
    @ColumnInfo(name = "back_text")
    private String backText;

    //Algoritmo

    //repetitions - this is the number of times a user sees a flashcard. 0 means they haven't studied it yet, 1 means it is their first time, and so on. It is also referred to as n in some of the documentation.
    @NonNull
    @ColumnInfo(name = "repetitions")
    private Integer repetitions;

    //quality - also known as quality of assessment. This is how difficult (as defined by the user) a flashcard is. The scale is from 0 to 5.

    @ColumnInfo(name = "quality")
    private Integer quality;

    //easiness - this is also referred to as the easiness factor or EFactor or EF. It is multiplier used to increase the "space" in spaced repetition. The range is from 1.3 to 2.5.
    @NonNull
    @ColumnInfo(name = "easiness")
    private Double easiness;

    //interval - this is the length of time (in days) between repetitions. It is the "space" of spaced repetition.
    @NonNull
    @ColumnInfo(name = "interval")
    private Integer interval;

    //nextPractice - This is the date/time of when the flashcard comes due to review again.

    @ColumnInfo(name = "nextPractice")
    private Date nextPractice;


    public Card(@NonNull String frontText, @NonNull String backText, @NonNull Integer deckId,
                @NonNull Integer repetitions,
                Integer quality,
                @NonNull Double easiness,
                @NonNull Integer interval,
                Date nextPractice) {
        count.incrementAndGet(); //CardId Increment
        this.frontText = frontText;
        this.backText = backText;
        this.deckId = deckId;
        this.nextPractice = nextPractice;
        this.repetitions = repetitions;
        this.quality = quality;
        this.easiness = easiness;
        this.interval = interval;
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
    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(@NonNull Integer deckId) {
        this.deckId = deckId;
    }

    @NonNull
    public Date getNextPractice() {
        return nextPractice;
    }

    public void setNextPractice(@NonNull Date nextPractice) {
        this.nextPractice = nextPractice;
    }

    @NonNull
    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(@NonNull Integer repetitions) {
        this.repetitions = repetitions;
    }

    @NonNull
    public Integer getQuality() {
        return quality;
    }

    public void setQuality(@NonNull Integer quality) {
        this.quality = quality;
    }

    @NonNull
    public Double getEasiness() {
        return easiness;
    }

    public void setEasiness(@NonNull Double easiness) {
        this.easiness = easiness;
    }

    @NonNull
    public Integer getInterval() {
        return interval;
    }

    public void setInterval(@NonNull Integer interval) {
        this.interval = interval;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardId.equals(card.cardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", frontText='" + frontText + '\'' +
                '}';
    }
}
