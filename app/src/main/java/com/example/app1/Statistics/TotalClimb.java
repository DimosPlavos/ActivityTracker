package com.example.app1.Statistics;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app1.R;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class TotalClimb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.totalclimb);


        String argument1 = getIntent().getStringExtra("climb_arg");
        String argument2 = getIntent().getStringExtra("mesos_oros_climb_arg");
        Log.d("GpxResults", "Argument Mesos Oros Climb: " + Float.parseFloat(argument2)+ "kai" +argument1);

         float value1= Float.parseFloat(argument1);
         float value2 = Float.parseFloat(argument2);

        // Create the data entries for the two values
//        float value1 = 5f;
//        float value2 = 30f;
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, value1));
        entries.add(new BarEntry(1, value2));

        // Create a dataset with the entries
        BarDataSet dataSet = new BarDataSet(entries, "Comparison");

        // Set custom colors for the columns
        int lightBlue = Color.parseColor("#90CAF9");
        int lightPurple = Color.parseColor("#B39DDB");
        dataSet.setColors(lightBlue, lightPurple);

        // Create a BarData object with the dataset
        BarData data = new BarData(dataSet);

        // Get the BarChart view from the layout
        BarChart chart = findViewById(R.id.chart_climb);

        // Customize the chart appearance
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);

        // Hide the values on the x-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // Set the interval between x-axis labels
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Set the labels for each column
                if (value == 0) {
                    return "YOU";
                } else if (value == 1) {
                    return "TOTAL";
                } else {
                    return "";
                }
            }
        });

        // Adjust the bar width
        data.setBarWidth(0.5f); // Set the desired width for the bars (0.0f - 1.0f)


        // Set the data to the chart and refresh
        chart.setData(data);
        chart.invalidate();
    }
}


