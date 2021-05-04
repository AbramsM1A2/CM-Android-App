package com.example.myapplication.database.Deck;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "deck_table")
public class Deck implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String nameText;

    private Date nextPractice;

    public Deck(String nameText, Date nextPractice) {
        this.nameText = nameText;
        this.nextPractice = nextPractice;
    }

    protected Deck(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        nameText = in.readString();
    }

    public static final Creator<Deck> CREATOR = new Creator<Deck>() {
        @Override
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(nameText);
        if (nextPractice == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(nextPractice.getTime());
        }
    }
}
