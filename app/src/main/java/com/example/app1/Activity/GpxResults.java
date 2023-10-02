package com.example.app1.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app1.R;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GpxResults extends AppCompatActivity {

    TextView distance,speed,time,climb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpxresults);

        distance = (TextView) findViewById(R.id.distance_result);
        speed = (TextView) findViewById(R.id.speed_result);
        time = (TextView) findViewById(R.id.time_result);
        climb = (TextView) findViewById(R.id.climb_result);

        String argument1 = getIntent().getStringExtra("distance_arg");
        String argument2 = getIntent().getStringExtra("speed_arg");
        String argument3 = getIntent().getStringExtra("time_arg");
        String argument4 = getIntent().getStringExtra("climb_arg");

        distance.setText(argument1 + " km. ");
        speed.setText(argument2 + " km/h. ");
        time.setText(argument3 + " mins. ");
        climb.setText(argument4 + " m. ");
    }
}