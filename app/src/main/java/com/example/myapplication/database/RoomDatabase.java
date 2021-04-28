package com.example.myapplication.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardDao;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckDao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Database(entities = {Card.class, Deck.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static volatile RoomDatabase INSTANCE;

    //Interfaces
    public abstract CardDao CardDao();

    public abstract DeckDao DeckDao();

    public static synchronized RoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDatabase.class, "room_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(sRoomDatabaseCallback) //Rellena la BD
                    .build(); //Se inicializa la BD en la APP
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PupulateDBAsyncTask(INSTANCE).execute();
        }
    };

    private static class PupulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private final DeckDao deckDao;
        private final CardDao cardDao;

        private PupulateDBAsyncTask(RoomDatabase db) {
            deckDao = db.DeckDao();
            cardDao = db.CardDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //---------------------------Deck---------------------------
            Deck deck = new Deck("Inglés", new GregorianCalendar(2021, Calendar.MARCH, 10).getTime());
            deckDao.insert(deck);

            deck = new Deck("Español", new GregorianCalendar(2021, Calendar.FEBRUARY, 11).getTime());
            deckDao.insert(deck);

            deck = new Deck("Suajili", new GregorianCalendar(2021, Calendar.MAY, 21).getTime());
            deckDao.insert(deck);

            //---------------------------Card---------------------------
            //change date with: new GregorianCalendar(2021, Calendar.FEBRUARY, 11).getTime()

            //-----Mazo 1 (ingles)
            Card card = new Card("Hello", "Hola", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Dog", "Perro", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Olive", "Aceituna", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);


            card = new Card("Onion", "Cebolla", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Limon", "Lemon", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Egg", "Huevo", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Potato", "Patata", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("friendship", "amistad", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("blood", "sangre", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("medicine", "medicina", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("week", "semana", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("president", "presidente", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("worker", "trabajador", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("device", "dispositivo", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("phone", "telefono", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("satisfaction", "satisfaccion", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("marriage", "matrimonio", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("truth", "verdad", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("ad", "publicidad", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("weakness", "debilidad", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("cool", "guay", 1, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            //------Mazo 2(español)
            card = new Card("Hola", "Hello", 2, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Perro", "Dog", 2, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Aceituna", "Olive", 2, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Cebolla", "Onion", 2, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Limon", "Lemon", 2, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Huevo", "Egg", 2, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);

            card = new Card("Patata", "Potato", 2, 0, null, 2.5, 0, new Date());
            cardDao.insert(card);
            return null;
        }
    }
    //Source: https://www.youtube.com/playlist?list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118
}