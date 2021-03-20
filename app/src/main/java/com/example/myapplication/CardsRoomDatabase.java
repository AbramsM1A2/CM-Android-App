package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Questions.class}, version = 1, exportSchema = false)
public abstract class CardsRoomDatabase extends RoomDatabase {

    public abstract QuestionsDao questionDao();

    private static volatile CardsRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CardsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CardsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CardsRoomDatabase.class, "cards_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                QuestionsDao dao = INSTANCE.questionDao();
                dao.deleteAll();

                Questions question = new Questions("Hello");
                dao.insert(question);
                question = new Questions("World");
                dao.insert(question);
            });
        }
    };
}