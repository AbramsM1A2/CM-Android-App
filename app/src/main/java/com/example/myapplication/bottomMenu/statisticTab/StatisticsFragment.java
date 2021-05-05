package com.example.myapplication.bottomMenu.statisticTab;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;


import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {


    private PieData data;
    private PieDataSet pieDataSet;
    private PieChart chart;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<PieEntry> valueSet = getArguments().getParcelableArrayList("valuesSet");
        System.out.println("values: "+valueSet);
        pieDataSet = new PieDataSet(valueSet, this.getResources().getString(R.string.statisticsDataSetName));

        pieDataSet.setColors(setThemeColors().get(1));
        pieDataSet.setValueTextSize(60f); //value text size
        pieDataSet.setValueTextColor(setThemeColors().get(0)[0]);
        data = new PieData(pieDataSet);
//TODO controlar que cuando no hay cartas en el mazo / no hay mazos, la vista muestre algo util

    }

    private List<int[]> setThemeColors() {
        List<int[]> res = new ArrayList<>();

        int nightModeFlags = this.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            //claro
            int[] pieTextColor = new int[]{getResources().getColor(R.color.white)};
            int[] pieColors = new int[]{
                    getResources().getColor(R.color.red_700),
                    getResources().getColor(R.color.orange_700),
                    getResources().getColor(R.color.brown_700),
                    getResources().getColor(R.color.green_700)
            };
            res.add(pieTextColor);
            res.add(pieColors);

        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            //oscuro

            int[] pieTextColor = new int[]{getResources().getColor(R.color.black)};
            int[] pieColors = new int[]{
                    getResources().getColor(R.color.red_200),
                    getResources().getColor(R.color.orange_200),
                    getResources().getColor(R.color.brown_200),
                    getResources().getColor(R.color.green_200)
            };
            res.add(pieTextColor);
            res.add(pieColors);
        }

        return res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);

        chart = (PieChart) v.findViewById(R.id.chart);

        Description desc = new Description();
        desc.setText(this.getResources().getString(R.string.statisticsCardsByDeck));

        chart.setData(data);
        chart.setEntryLabelTextSize(16f);
        chart.setDescription(desc);
        chart.setDrawEntryLabels(true); //etiquetas
        //chart.setUsePercentValues(true); //transforma los valores en porcentajes
        chart.animateXY(700, 700);
        chart.invalidate(); // recarga el grafico
        chart.notifyDataSetChanged(); //TODO datos dinamicos funciona?
        return v;

    }
}