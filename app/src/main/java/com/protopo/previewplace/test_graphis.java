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

        Number[] Datos2 = {0, 2, 8,20,40,60,80,94,100,120,137,140,148,160,180,186.6,192,200,220,228.7, 234.6, 240, 242,253,256.2,260,280,300,320,340,360,370,380,400,405,405,420,440,460,480,500,520,540,560,580,600,613.2};
        Number[] Datos1 = {100,99.72,97.29,96.98,96.68,96.44,95.99,95.41,94.89,94.71,94.7,94.27,93.51,93.65,93.69,93.63,92.77,92.74,92.74,92.74,91.85,91.78,91.44,91.45,90.99,90.98,90.94,90.36,89.87,89.71,89.07,89.12,88.62,88.46,88.46,88.32,87.36,85.79,85.47,85.15,84.87,84.72,84.16,84.12,84,83.79,82.82};

        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(Datos1),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Series1");

  /*      XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(Datos2),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Series2");
*/
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
        //myxyPlot.addSeries(series2, serie2Format);



    }
}






















