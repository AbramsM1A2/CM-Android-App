package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.bottomMenu.cardsTab.CardsTabFragment;
import com.example.myapplication.bottomMenu.settingsTab.SettingsFragment;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.bottomMenu.homeTab.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.concurrent.atomic.AtomicInteger;

import static com.example.myapplication.R.id.home_tab;

public class MainActivity extends AppCompatActivity implements HomeFragment.onFragmentInteraction {

    private CardViewModel mCardViewModel;

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
                showFragment(HomeFragment.newInstance());
                return true;
            } else if (itemId == R.id.cards_tab){
                showFragment(CardsTabFragment.newInstance(1));
                return true;
            } else if (itemId == R.id.statistics_tab) {
                //TODO
                return true;
            } else if (itemId == R.id.settings_tab) {
                showFragment(new SettingsFragment());
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
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        AtomicInteger deckSize = new AtomicInteger();
        mCardViewModel.getAllCardsWithThisId(dataItem.getDeckId()).observe(this, cards -> {
            deckSize.set(cards.size());
            Log.d("TAG", "onListClickListener: "+String.valueOf(deckSize.get()));
            if (deckSize.get() < 20){
                Toast.makeText(this, R.string.minimun_deck_size, Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(this, ReviewCardsActivity.class);
                intent.putExtra("selected_deck_id", String.valueOf(dataItem.getDeckId()));
                intent.putExtra("selected_deck_name", dataItem.getNameText());
                startActivity(intent);
            }
        });

    }

}