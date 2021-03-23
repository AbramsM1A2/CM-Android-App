package com.example.myapplication.dummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyDeck {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyCard> ITEMS = new ArrayList<DummyCard>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyCard> ITEM_MAP = new HashMap<String, DummyCard>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        String originalWord,translatedWord;
        String[] strsOrig = new String[]{"Aceituna","Aguacate","Calabaza","Cebolla","Espinaca","Gallina",
                "Gelatina","Harina","Huevo","Lechuga","Limon","Mandarina","Naranja","Patata","Queso"};
        String[] strsTrans = new String[]{"Olive","Avocado","Pumpkin","Onion","Spinach","Chicken",
                "Jelly","Flour","Egg","Lettuce","Lemon","Tangerine","Orange","Potato","Cheese"};
        List<String> original =  Arrays.asList(strsOrig);
        List<String> translated =  Arrays.asList(strsTrans);
        //List<ArrayList<String>> deck = new ArrayList<ArrayList <String>>();
        //deck.add(new ArrayList<String>("Patata"));

        for(int i=0;i<=14; i++){
            originalWord = original.get(i);
            translatedWord = translated.get(i);
            addItem(createDummyCard(i+1,originalWord,translatedWord));
        }

    }

    private static void addItem(DummyCard item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
    private static DummyCard createDummyCard(int position,String original,String translated) {
        return new DummyCard(String.valueOf(position), original, translated);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyCard {
        public final String id;
        public final String front;
        public final String back;

        public DummyCard(String id, String front, String back) {
            this.id = id;
            this.front = front;
            this.back = back;
        }

        @Override
        public String toString() {
            return "Front: '" +this.front + "', Back: '" + this.back+"'";
        }
    }
}