package com.example.myapplication.cardstab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardItems {
    private Integer id;
    private String front;
    private String back;

    public CardItems(Integer id, String front, String back) {
        this.id = id;
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

    public static ArrayList<CardItems> createCardList() {
        ArrayList<CardItems> cardItemsList = new ArrayList<CardItems>();
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
            cardItemsList.add(new CardItems(i+1,originalWord,translatedWord));
        }
        return cardItemsList;
    }
    public String toString() {
        return "Front: '" +this.front + "', Back: '" + this.back+"'";
    }

}
