package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;
import android.util.Log;


import com.example.myapplication.bottomMenu.cardsTab.CardsTabFragment;

import com.example.myapplication.bottomMenu.settingsTab.SettingsFragment;

import com.example.myapplication.bottomMenu.homeTab.HomeFragment;
import com.example.myapplication.bottomMenu.statisticTab.StatisticsFragment;
import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.myapplication.R.id.home_tab;

public class MainActivity extends AppCompatActivity {
    private String up_bar_string;
    private SharedPreferences sharedPreferences;
    private Integer currentSelection;
    private BottomNavigationView navigation;

    public static boolean flag = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Color del navigation bar
        setTheme();
        //Menu inferior
        navigation = findViewById(R.id.bottom_navigation);
        navigation.bringToFront();
        currentSelection= home_tab;
        //PREFERENCIAS
        sharedPreferences = this.getSharedPreferences("PREFERENCIAS", MODE_PRIVATE);
        //tema
        SharedPreferences.Editor edit = sharedPreferences.edit();//PARA EDITAR LAS PREFERENCIAS
        if (sharedPreferences.getBoolean("theme", false) == Boolean.FALSE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);//SEGUIR EL TEMA DEL SISTEMA
        } else {
            if (!flag) {//SI ES LA PRIMERA VEZ QUE SE INICIA Y EL BOTÓN ESTÁ ACTIVADO, SE INVIERTE EL TEMA
                int nightModeFlags = this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        }

        //Statistics data chart
        ArrayList<PieEntry> valueSet = new ArrayList<>();
        DeckViewModel mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        CardViewModel mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        //TODO revisar bug del chart aqui
        mDeckViewModel.getAllDecks().observe(this,decks -> {
            valueSet.clear();
            for (Deck d:decks) {
                AtomicReference<Float> count= new AtomicReference<>(0f);
                mCardViewModel.getAllCards().observe(this, cards -> {
                    for (Card c: cards) {
                        if (d.getId().equals(c.getDeckId())){
                            count.getAndSet((float) (count.get() + 1));
                        }
                    }
                    valueSet.add(new PieEntry(count.get(), d.getNameText()));
                });
            }

        });

        navigation.setOnNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == home_tab) {
                currentSelection=itemId;
                System.out.println(R.string.app_name);
                up_bar_string = getApplicationContext().getString(R.string.app_name);
                showFragment(HomeFragment.newInstance());
                getSupportActionBar().setTitle(up_bar_string);
                return true;
            } else if (itemId == R.id.cards_tab) {
                currentSelection=itemId;
                up_bar_string = getApplicationContext().getString(R.string.cards);
                showFragment(CardsTabFragment.newInstance(1));
                getSupportActionBar().setTitle(up_bar_string);
                return true;
            } else if (itemId == R.id.statistics_tab) {
                //TODO statistics tab
                currentSelection=itemId;
                up_bar_string = getApplicationContext().getString(R.string.statistics);
                getSupportActionBar().setTitle(up_bar_string);
                StatisticsFragment fragment = new StatisticsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("valuesSet",valueSet);
                fragment.setArguments(bundle);
                showFragment(fragment);
                return true;
            } else if (itemId == R.id.settings_tab) {
                up_bar_string = getApplicationContext().getString(R.string.settings);
                currentSelection=itemId;
                showFragment(new SettingsFragment());
                getSupportActionBar().setTitle(up_bar_string);
                return true;
            }

            return false;
        });
        if (!flag) {
            navigation.setSelectedItemId(home_tab);
            flag = true;
        } else if (sharedPreferences.getBoolean("activar_home", false) == Boolean.TRUE) {//CUANDO VUELVE DE ABOUT US O CONTACT US
            navigation.setSelectedItemId(home_tab);
            edit.putBoolean("activar_home", Boolean.FALSE);
            edit.apply();
            this.recreate();
        }



    }

    /**
     * Maneja la visualizacion de los diferentes fragments del menu
     *
     * @param frg fragmento seleccionado
     */
    private void showFragment(Fragment frg) {
        getSupportFragmentManager()
                .beginTransaction()
                //.setCustomAnimations(R.anim.bottom_nav_enter, R.anim.bottom_nav_exit)
                .replace(R.id.container, frg)
                .commit();

    }

    private void setTheme() {

        int nightModeFlags = this.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            //claro
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue_700)));
            //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + up_bar_string + "</font>"));

        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            //oscuro
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue_200)));
            //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + up_bar_string + "</font>"));


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        navigation.setSelectedItemId(currentSelection);
        System.out.println("ACITIVTY ONRESUME");
    }

    @Override
    public void onPause() {
        super.onPause();

        System.out.println("ACITIVTY ONPAUSE");
    }
}