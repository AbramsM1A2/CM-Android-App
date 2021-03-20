package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "questions_table")
public class Questions {
    private static final AtomicInteger count = new AtomicInteger(0);

    @PrimaryKey
    private Integer question_id;
    @NonNull
    @ColumnInfo(name = "question_text")
    private String text;
    //@ColumnInfo(name = "question_image")
    //private String mWord;
//TODO: incrementar IDS


    public Questions(@NonNull String text) {
        this.text = text;
        setQuestion_id(count.incrementAndGet());
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }
}
