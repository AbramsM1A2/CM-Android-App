package com.example.myapplication;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.bottomMenu.cardsTab.CardsTabFragment;
import com.example.myapplication.bottomMenu.homeTab.HomeFragment;
import com.example.myapplication.bottomMenu.settingsTab.SettingsFragment;
import com.example.myapplication.bottomMenu.statisticTab.StatisticsFragment;
import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.myapplication.R.id.home_tab;

public class MainActivity extends AppCompatActivity {
    private String up_bar_string;
    private SharedPreferences sharedPreferences;
    private Integer currentSelection;
    private BottomNavigationView navigation;

    public static boolean flag = Boolean.FALSE;
    private ArrayList<PieEntry> pieChartValueSet;

    private StatisticsFragment statisticsfragment;
    private ArrayList<Deck> deckOrder;
    private ArrayList<ArrayList<BarEntry>> groupedBarEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Color del navigation bar
        //Menu inferior
        navigation = findViewById(R.id.bottom_navigation);
        navigation.bringToFront();
        currentSelection = home_tab;
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
        pieChartValueSet = new ArrayList<>();
        groupedBarEntries = new ArrayList<>();
        deckOrder = new ArrayList<>();

        DeckViewModel mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        CardViewModel mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        //TODO revisar bug del chart aqui

        mDeckViewModel.getAllDecks().observe(this, decks -> {

            System.out.println("----------------------------DECKVIDEMODEL START-------------------------------");
            for (Deck d : decks) {
                mCardViewModel.getAllCards().observe(this, cards -> {

                    System.out.println("FOR DECK: "+d.getNameText());
                    AtomicReference<Float> count = new AtomicReference<>(0f);
                    ArrayList<BarEntry> barChartValueSet = new ArrayList<>();
                    HashMap<Integer, Integer> countRepetitions = new HashMap<>();
                    System.out.println("-------------CARDVIEWMODEL START--------------");
                    for (Card c : cards) {
                        System.out.println("FOR CARD "+c.getDeckId());
                        if (d.getId().equals(c.getDeckId())) {
                            count.getAndSet(count.get() + 1f);

                            Integer i = countRepetitions.get(c.getRepetitions());
                            if (i == null) countRepetitions.put(c.getRepetitions(), 1);
                            else countRepetitions.put(c.getRepetitions(), i + 1);
                        }


                    }
                    System.out.println("CARD COUNT: "+count.get());
                    System.out.println("COUNT REPETITIONS: " + countRepetitions);


                    //PieChart
                    PieEntry p = new PieEntry(count.get(), d.getNameText());
                    insertPieEData(p);

                    //BarChart
                    System.out.println("--BAR CHART OPERATIONS--");
                    for (Map.Entry<Integer, Integer> k : countRepetitions.entrySet()
                    ) {
                        BarEntry b = new BarEntry(k.getKey(), k.getValue());
                        barChartValueSet.add(b);
                    }

                    deckOrder.add(d);
                    System.out.println("deck order"+deckOrder);
                    System.out.println("BARCHARTVALUE SET: "+barChartValueSet);
                    groupedBarEntries.add(barChartValueSet);
                    System.out.println("GROUPEDBARENTRIES: "+groupedBarEntries);

                    System.out.println("--BAR CHART END OPERATIONS--");

                    System.out.println("-------------CARDVIEWMODEL END--------------");
                });
            }
            System.out.println("----------------------------DECKVIDEMODEL END-------------------------------");
        });

        navigation.setOnNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == home_tab) {
                currentSelection = itemId;
                up_bar_string = getApplicationContext().getString(R.string.app_name);
                showFragment(HomeFragment.newInstance());
                setTheme();

                return true;
            } else if (itemId == R.id.cards_tab) {
                currentSelection = itemId;
                up_bar_string = getApplicationContext().getString(R.string.cards);
                showFragment(CardsTabFragment.newInstance(1));
                setTheme();

                return true;
            } else if (itemId == R.id.statistics_tab) {
                currentSelection = itemId;
                up_bar_string = getApplicationContext().getString(R.string.statistics);

                statisticsfragment = new StatisticsFragment();

                Bundle bundle = new Bundle();
                System.out.println("---------------STATISTICS BUNDLE START----------");
                bundle.putParcelableArrayList("pieChartData", pieChartValueSet);

                System.out.println("DECK ORDER: "+deckOrder);
                System.out.println("GROUPED Entries: "+groupedBarEntries);
                for (int i = 0; i < groupedBarEntries.size(); i++) {
                    bundle.putParcelableArrayList("barChartData"+i, (groupedBarEntries.get(i)));
                }
                bundle.putInt("barChartData", groupedBarEntries.size());

                bundle.putParcelableArrayList("decksOrder", deckOrder);
                statisticsfragment.setArguments(bundle);
                showFragment(statisticsfragment);
                setTheme();
                groupedBarEntries = new ArrayList<>();
                pieChartValueSet = new ArrayList<>();
                deckOrder = new ArrayList<>();

                System.out.println("---------------STATISTICS BUNDLE END----------");

                return true;
            } else if (itemId == R.id.settings_tab) {
                up_bar_string = getApplicationContext().getString(R.string.settings);
                currentSelection = itemId;
                showFragment(new SettingsFragment());
                setTheme();

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
        } else if (sharedPreferences.getBoolean("ajustes", Boolean.FALSE) == Boolean.TRUE) {
            navigation.setSelectedItemId(R.id.settings_tab);
            //up_bar_string=sharedPreferences.getString("UP_BAR_STRING", "");
            sharedPreferences = this.getSharedPreferences("PREFERENCIAS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("ajustes", Boolean.FALSE);
            editor.apply();
        }


    }

    /**
     * Inserta aquellos elementos nuevos o modificados
     *
     * @param p PieEntry
     */
    //TODO funciona a medias
    private void insertPieEData(PieEntry p) {
        boolean fullMatch = false;
        boolean partialMatch = false;
        Integer matchPosition = null;
        for (int i = 0; i < pieChartValueSet.size(); i++) {
            PieEntry value = pieChartValueSet.get(i);
            if (value.getLabel().equals(p.getLabel())) {
          /*      System.out.println("Son iguales en nombre");
                System.out.println("V: " + value.getLabel());
                System.out.println("P: " + p.getLabel());*/
                partialMatch = true;
                matchPosition = i;
                if (value.getValue() == p.getValue()) {
                  /*  System.out.println("Son iguales en todo");
                    System.out.println("V: " + value.getLabel() + "-" + value.getValue());
                    System.out.println("P: " + p.getLabel() + "-" + p.getValue());*/
                    fullMatch = true;
                }
            }
        }

        if (!fullMatch && partialMatch) {
            if (p.getValue() == 0) {
                pieChartValueSet.remove(matchPosition);
            } else {
                pieChartValueSet.set(matchPosition, p);
            }
        } else {
            pieChartValueSet.add(p);
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
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + up_bar_string + "</font>"));

        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            //oscuro
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue_200)));
            //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "caca"+"</font>"));
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + up_bar_string + "</font>"));

            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue_500)));
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

    //TODO intercambio de vistas
    public void onClickPieChart(View view) {
        statisticsfragment.changeChart(0);
    }

    public void onClickBarChart(View view) {
        statisticsfragment.changeChart(1);
    }
}