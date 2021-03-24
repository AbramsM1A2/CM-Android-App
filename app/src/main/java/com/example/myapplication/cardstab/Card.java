package com.example.myapplication.cardstab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Card {
    private Integer id;
    private String front;
    private String back;

    public Card(Integer id, String front, String back) {
        this.front = front;
        this.back = back;
    }

    public Integer getId() {
        return id;
    }
    public String getFront() {
        return front;
    }
    public String getBack() {
        return back;
    }

    public static ArrayList<Card> createCardList() {
        ArrayList<Card> cardList = new ArrayList<Card>();
        String originalWord,translatedWord;
        String[] strsOrig = new String[]{"Aceituna","Aguacate","Calabaza","Cebolla","Espinaca","Gallina",
                "Gelatina","Harina","Huevo","Lechuga","Limon","Mandarina","Naranja","Patata","Queso"};
        String[] strsTrans = new String[]{"Olive","Avocado","Pumpkin","Onion","Spinach","Chicken",
                "Jelly","Flour","Egg","Lettuce","Lemon","Tangerine","Orange","Potato","Cheese"};
        List<String> original =  Arrays.asList(strsOrig);
        List<String> translated =  Arrays.asList(strsTrans);

        for(int i=0;i<=14; i++){
            originalWord = original.get(i);
            translatedWord = translated.get(i);
            cardList.add(new Card(i+1,originalWord,translatedWord));
        }
        return cardList;
    }
}
