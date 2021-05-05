package com.example.myapplication.bottomMenu.statisticTab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {


    private PieData data;


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

        //establecemos los datos
        data = new PieData(getDataSet());

    }


    private PieDataSet getDataSet() {

        List<PieEntry> valueSet1 = new ArrayList<>();
        ///TODO cambiar labels por getString(R.string....)
        PieEntry v1e1 = new PieEntry(25.0f, "Inglés");
        valueSet1.add(v1e1);
        PieEntry v1e2 = new PieEntry(30.0f, "Español");
        valueSet1.add(v1e2);
        PieEntry v1e3 = new PieEntry(10.0f, "Suajili");
        valueSet1.add(v1e3);

        PieDataSet pieDataSet1 = new PieDataSet(valueSet1, "Mazos");

        pieDataSet1.setColors(setThemeColors().get(1));
        pieDataSet1.setValueTextSize(60f); //value text size
        pieDataSet1.setValueTextColor(setThemeColors().get(0)[0]);
        return pieDataSet1;
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

        PieChart chart = (PieChart) v.findViewById(R.id.chart);

        Description desc = new Description();
        desc.setText("Cartas por mazo"); //TODO poner como strings.xml
        chart.setData(data);
        chart.setEntryLabelTextSize(16f);
        chart.setDescription(desc);
        chart.setDrawEntryLabels(true); //etiquetas
        //chart.setUsePercentValues(true); //transforma los valores en porcentajes
        chart.notifyDataSetChanged(); //TODO datos dinamicos
        chart.animateXY(700, 700);
        chart.invalidate(); // recarga el grafico

        return v;

    }
}