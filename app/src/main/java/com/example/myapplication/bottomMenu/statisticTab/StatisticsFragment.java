package com.example.myapplication.bottomMenu.statisticTab;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.myapplication.R;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import java.util.ArrayList;
import java.util.List;


public class StatisticsFragment extends Fragment {

    private PieData pieData;
    private BarData barData;
    private View v;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    private void getPieData() {
        assert getArguments() != null;
        List<PieEntry> pieChartValueSet = getArguments().getParcelableArrayList("pieChartData");
        PieDataSet pieDataSet = new PieDataSet(pieChartValueSet, this.getResources().getString(R.string.statisticsDataSetName));
        pieDataSet.setColors(setThemeColors().get(1));
        pieDataSet.setDrawValues(false);
        //pieDataSet.setValueTextSize(60f); //value text size
        //pieDataSet.setValueTextColor(setThemeColors().get(0)[0]);
        System.out.println("values PIECHART: " + pieChartValueSet);
        pieData = new PieData(pieDataSet);
    }

    private void getBarData() {
        assert getArguments() != null;
        List<BarEntry> barChartValueSet = getArguments().getParcelableArrayList("barChartData");
        BarDataSet barDataSet = new BarDataSet(barChartValueSet, "Usage"); //TODO poner como r string
        System.out.println("values BARCHART: " + barChartValueSet);
        barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f); // set custom bar width
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO controlar que cuando no hay cartas en el mazo / no hay mazos, la vista muestre algo util
        getPieData();
        getBarData();

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

    private void setBarChart(BarChart chart) {
        chart.setData(barData);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
    }

    private void setPieChart(PieChart chart) {
        Description desc = new Description();
        desc.setText(this.getResources().getString(R.string.statisticsCardsByDeck));
        desc.setTextColor(setThemeColors().get(2)[0]);

        chart.setData(pieData);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_statistics, container, false);

        PieChart pieChart = v.findViewById(R.id.pieChart);
        setPieChart(pieChart);

        BarChart barChart = v.findViewById(R.id.barChart);
        setBarChart(barChart);
        //TODO cambiar el nombre de los botones del layout
        return v;

    }

    public void changeChart(int i){
        RelativeLayout pieChartLayout = v.findViewById(R.id.pieChartLayout);
        RelativeLayout barChartLayout = v.findViewById(R.id.barChartLayout);
        if (i==0){
            barChartLayout.setVisibility(View.GONE);
            pieChartLayout.setVisibility(View.VISIBLE);
        }else{
            barChartLayout.setVisibility(View.VISIBLE);
            pieChartLayout.setVisibility(View.GONE);
        }

    }
}