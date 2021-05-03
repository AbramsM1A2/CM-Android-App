package com.example.myapplication.database.Card;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "card_table")
public class Card implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Integer deckId;
    private String frontText;
    private String backText;

    //----------------Algoritmo-----------------

    //repetitions - this is the number of times a user sees a flashcard. 0 means they haven't studied it yet, 1 means it is their first time, and so on. It is also referred to as n in some of the documentation.
    private Integer repetitions;

    //quality - also known as quality of assessment. This is how difficult (as defined by the user) a flashcard is. The scale is from 0 to 5.
    private Integer quality;

    //easiness - this is also referred to as the easiness factor or EFactor or EF. It is multiplier used to increase the "space" in spaced repetition. The range is from 1.3 to 2.5.
    private Double easiness;

    //interval - this is the length of time (in days) between repetitions. It is the "space" of spaced repetition.
    private Integer interval;

    //nextPractice - This is the date/time of when the flashcard comes due to review again.
    private Date nextPractice;

    public Card(String frontText, String backText, Integer deckId, Integer repetitions, Integer quality, Double easiness, Integer interval, Date nextPractice) {
        this.deckId = deckId;
        this.frontText = frontText;
        this.backText = backText;
        this.repetitions = repetitions;
        this.quality = quality;
        this.easiness = easiness;
        this.interval = interval;
        this.nextPractice = nextPractice;
    }

    protected Card(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            deckId = null;
        } else {
            deckId = in.readInt();
        }
        frontText = in.readString();
        backText = in.readString();
        if (in.readByte() == 0) {
            repetitions = null;
        } else {
            repetitions = in.readInt();
        }
        if (in.readByte() == 0) {
            quality = null;
        } else {
            quality = in.readInt();
        }
        if (in.readByte() == 0) {
            easiness = null;
        } else {
            easiness = in.readDouble();
        }
        if (in.readByte() == 0) {
            interval = null;
        } else {
            interval = in.readInt();
        }
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDeckId() {
        return deckId;
    }

    public String getFrontText() {
        return frontText;
    }

    public String getBackText() {
        return backText;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public Integer getQuality() {
        return quality;
    }

    public Double getEasiness() {
        return easiness;
    }

    public Integer getInterval() {
        return interval;
    }

    public Date getNextPractice() {
        return nextPractice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id.equals(card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Card{" +
                "Id=" + id +
                ", frontText='" + frontText + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (deckId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(deckId);
        }
        dest.writeString(frontText);
        dest.writeString(backText);
        if (repetitions == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(repetitions);
        }
        if (quality == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quality);
        }
        if (easiness == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(easiness);
        }
        if (interval == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(interval);
        }
    }
}
