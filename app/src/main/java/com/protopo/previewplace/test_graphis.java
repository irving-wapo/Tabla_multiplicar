package com.protopo.previewplace;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.LineAndPointFormatter;

import java.util.Arrays;



public class test_graphis extends AppCompatActivity {
private XYPlot myxyPlot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_graphis);

        myxyPlot = (XYPlot) findViewById(R.id.myXYPlot_vista);

        Number[] Datos1 = {1, 10,5,9,7,4};
        Number[] Datos2 = {3,6,2,8,4,50};

        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(Datos1),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Series1");

        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(Datos2),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Series2");

        LineAndPointFormatter serie1Format = new LineAndPointFormatter(
                Color.rgb(0,200,0),
                Color.rgb(0,100,0),
                Color.rgb(150,190,150), null
        );
        myxyPlot.addSeries(series1, serie1Format);



        LineAndPointFormatter serie2Format = new LineAndPointFormatter(
                Color.rgb(0,0,200),
                Color.rgb(0,0,100),
                Color.rgb(150,150,190), null
        );
        myxyPlot.addSeries(series2, serie2Format);



    }
}






















