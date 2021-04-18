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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Card.class, Deck.class}, version = 1, exportSchema = false)
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
                            .addCallback(sRoomDatabaseCallback) //Rellena la BD
                            .build(); //Se inicializa la BD en la APP
                }
            }
        }
        return INSTANCE;
    }

    private static androidx.room.RoomDatabase.Callback sRoomDatabaseCallback = new androidx.room.RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Si quieres mantener los datos durante los reinicios de la aplicación,
            // comenta el siguiente bloque (TODO: Borrar cuando la app este mas madura)
            databaseWriteExecutor.execute(() -> {
                //Rellena la base de datos en segundo plano.
                // Si quieres empezar con más elementos, sólo tienes que añadirlos debajo.

                //---------------------------Deck---------------------------
                DeckDao deckDao = INSTANCE.DeckDao();
                deckDao.deleteAll();

                Deck deck = new Deck("Inglés");
                deckDao.insert(deck);

                deck = new Deck("Español");
                deckDao.insert(deck);
                //---------------------------Card---------------------------
                CardDao carddao = INSTANCE.CardDao();
                carddao.deleteAll();

                //change date with: new GregorianCalendar(2021, Calendar.FEBRUARY, 11).getTime()

                //-----Mazo 1 (ingles)
                Card card = new Card("Hello", "Hola", 1, 0, null, 2.5, 0, new Date());
                carddao.insert(card);

                card = new Card("Dog", "Perro", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Olive", "Aceituna", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);


                card = new Card("Onion", "Cebolla", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Limon", "Lemon", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Egg", "Huevo", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Potato", "Patata", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("friendship", "amistad", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("blood", "sangre", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("medicine", "medicina", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("week", "semana", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("president", "presidente", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("worker", "trabajador", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("device", "dispositivo", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("phone", "telefono", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("satisfaction", "satisfaccion", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("marriage", "matrimonio", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("truth", "verdad", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("ad", "publicidad", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("weakness", "debilidad", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("cool", "guay", 1, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                //------Mazo 2(español)
                card = new Card("Hola", "Hello", 2, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Perro", "Dog", 2, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Aceituna", "Olive", 2, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Cebolla", "Onion", 2, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Limon", "Lemon", 2, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Huevo", "Egg", 2, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);

                card = new Card("Patata", "Potato", 2, 0, null, 2.5, 0,  new Date());
                carddao.insert(card);
            });
        }
    };
    //Cualquier modificación de la BD requiere actualizar la version y eso resulta en una migracion
    // que hay que indicar de forma explicita.

    //No funciona, pero si reinstalas si ¯\_(ツ)_/¯
    /*static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE deck_table (DeckId INTEGER NOT NULL, name_text TEXT NOT NULL, PRIMARY KEY(DeckId))");
        }
    };*/

}