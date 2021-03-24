package com.example.myapplication.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardDao;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckDao;

import java.time.DateTimeException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Card.class, Deck.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract CardDao CardDao();
    public abstract DeckDao DeckDao();

    private static volatile RoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, "room_database")
                            .addMigrations(MIGRATION_1_2)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static androidx.room.RoomDatabase.Callback sRoomDatabaseCallback = new androidx.room.RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.

                //---------------------------Deck---------------------------
                DeckDao deckDao = INSTANCE.DeckDao();
                deckDao.deleteAll();

                Deck deck = new Deck("Inglés");
                deckDao.insert(deck);

                deck= new Deck("Español");
                deckDao.insert(deck);
                //---------------------------Card---------------------------
                CardDao carddao = INSTANCE.CardDao();
                carddao.deleteAll();

                Card card = new Card("Hello", "Hola", new Date(), 1);
                carddao.insert(card);

                card = new Card("Dog", "Perro", new Date(), 1);
                carddao.insert(card);
            });
        }
    };
    //No funciona, pero si reinstalas si ¯\_(ツ)_/¯
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE deck_table (DeckId INTEGER NOT NULL, name_text TEXT NOT NULL, PRIMARY KEY(DeckId))");
        }
    };
}