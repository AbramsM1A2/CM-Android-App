package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.example.myapplication.bottomMenu.cardsTab.CardsTabFragment;
import com.example.myapplication.bottomMenu.settingsTab.SettingsFragment;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.bottomMenu.homeTab.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import static com.example.myapplication.R.id.home_tab;

public class MainActivity extends AppCompatActivity implements HomeFragment.onFragmentInteraction {
    private String up_bar_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Menu inferior
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.bringToFront();
        navigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == home_tab) {
                System.out.println(R.string.app_name);
                up_bar_string = getApplicationContext().getString(R.string.app_name);
                showFragment(HomeFragment.newInstance());
                getSupportActionBar().setTitle(up_bar_string);
                return true;
            } else if (itemId == R.id.cards_tab){
                up_bar_string = getApplicationContext().getString(R.string.cards);
                showFragment(CardsTabFragment.newInstance(1));
                getSupportActionBar().setTitle(up_bar_string);
                return true;
            } else if (itemId == R.id.statistics_tab) {
                up_bar_string = getApplicationContext().getString(R.string.statistics);
                getSupportActionBar().setTitle(up_bar_string);
                //TODO
                return true;
            } else if (itemId == R.id.settings_tab) {
                up_bar_string = getApplicationContext().getString(R.string.settings);
                showFragment(new SettingsFragment());
                getSupportActionBar().setTitle(up_bar_string);
                return true;
            }
            return false;
        });
        navigation.setSelectedItemId(home_tab);
    }

    /**
     * Maneja la visualizacion de los diferentes fragments del menu
     * @param frg
     */
    private void showFragment(Fragment frg) {
        getSupportFragmentManager()
                .beginTransaction()
                //.setCustomAnimations(R.anim.bottom_nav_enter, R.anim.bottom_nav_exit)
                .replace(R.id.container, frg)
                .commit();

    }
    /**
     * Inicia el repaso de cartas a partir del mazo seleccionado
     * @param dataItem
     */
    @Override
    public void onListClickListener(Deck dataItem) {
        Intent intent = new Intent(this, ReviewCardsActivity.class);
        intent.putExtra("message_key", dataItem.getNameText());
        startActivity(intent);
    }

}