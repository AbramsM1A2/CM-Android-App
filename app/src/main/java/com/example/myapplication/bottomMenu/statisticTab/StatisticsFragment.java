package com.example.myapplication.bottomMenu.statisticTab;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;



import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import java.util.ArrayList;
import java.util.List;


public class StatisticsFragment extends Fragment {

    private PieData data;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<PieEntry> valueSet = getArguments().getParcelableArrayList("valuesSet");
        System.out.println("values: "+valueSet);
        PieDataSet pieDataSet = new PieDataSet(valueSet, this.getResources().getString(R.string.statisticsDataSetName));

        pieDataSet.setColors(setThemeColors().get(1));
        //pieDataSet.setValueTextSize(60f); //value text size
        //pieDataSet.setValueTextColor(setThemeColors().get(0)[0]);
        pieDataSet.setDrawValues(false);
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
            int[] themeColor = new int[]{getResources().getColor(R.color.black)};
            res.add(pieTextColor);
            res.add(pieColors);
            res.add(themeColor);

        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            //oscuro

            int[] pieTextColor = new int[]{getResources().getColor(R.color.black)};
            int[] pieColors = new int[]{//TODO: Por cada mazo debe haber un color
                    getResources().getColor(R.color.red_200),
                    getResources().getColor(R.color.orange_200),
                    getResources().getColor(R.color.brown_200),
                    getResources().getColor(R.color.green_200)
            };
            int[] themeColor = new int[]{getResources().getColor(R.color.white)};
            res.add(pieTextColor);
            res.add(pieColors);
            res.add(themeColor);
        }

        return res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);

        PieChart chart = (PieChart) v.findViewById(R.id.chart);

        Description desc = new Description();
        desc.setText(this.getResources().getString(R.string.statisticsCardsByDeck));
        desc.setTextColor(setThemeColors().get(2)[0]);

        chart.setData(data);
        chart.setDescription(desc);

        chart.setNoDataTextColor(setThemeColors().get(0)[0]);
        chart.setNoDataText(this.getResources().getString(R.string.chartData));

        Legend legend = chart.getLegend();
        legend.setTextColor(setThemeColors().get(2)[0]);
        chart.setHoleColor(setThemeColors().get(0)[0]);

        //etiquetas
        chart.setEntryLabelTextSize(16f);
        chart.setDrawEntryLabels(true);
        chart.setEntryLabelColor(setThemeColors().get(0)[0]);


        //chart.setUsePercentValues(true); //transforma los valores en porcentajes
        chart.animateXY(700, 700);
        chart.invalidate(); // recarga el grafico
        chart.notifyDataSetChanged(); //TODO datos dinamicos funciona?

        return v;

    }
}