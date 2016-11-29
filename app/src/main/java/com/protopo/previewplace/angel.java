package com.protopo.previewplace;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class angel extends Activity implements OnTouchListener
{
    private XYPlot mySimpleXYPlot;
    private Button resetButton;
    private SimpleXYSeries[] series = null;
    private PointF minXY;
    private PointF maxXY;

    ArrayList<Integer> lista_X = new ArrayList<Integer>();
    ArrayList<String> lista_Y = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.angel);
        lista_X = (ArrayList<Integer>) getIntent().getSerializableExtra("listaX");
        lista_Y = (ArrayList<String>) getIntent().getSerializableExtra("listaY");

        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                minXY.x = series[0].getX(0).floatValue();
                maxXY.x = series[3].getX(series[3].size() - 1).floatValue();
                mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x, BoundaryMode.FIXED);
                mySimpleXYPlot.redraw();
            }
        });
        inicializa_grafica();
    }

    public void inicializa_grafica() {

        mySimpleXYPlot = (XYPlot) findViewById(R.id.plot);
        mySimpleXYPlot.setOnTouchListener(this);
        mySimpleXYPlot.getGraphWidget().setTicksPerRangeLabel(2);
        mySimpleXYPlot.getGraphWidget().setTicksPerDomainLabel(2);
        mySimpleXYPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        mySimpleXYPlot.getGraphWidget().setRangeValueFormat(new DecimalFormat("#####"));
        mySimpleXYPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("#####.#"));
        mySimpleXYPlot.getGraphWidget().setRangeLabelSubTickExtension(25);
        mySimpleXYPlot.setRangeLabel("");
        mySimpleXYPlot.setDomainLabel("");
        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.NONE, null, null);

        mySimpleXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 2);                      //DIVISION DE LA CUADRICULA

        series = new SimpleXYSeries[4];
        int scale = 1;
        for (int i = 0; i < 4; i++, scale *= 5) {
            series[i] = new SimpleXYSeries("PREVIEWPLACE");
            populateSeries(series[i]);
        }                                                           // verde

        mySimpleXYPlot.addSeries(series[3],
                new LineAndPointFormatter(
                        Color.rgb(0, 0, 0),
                        Color.rgb(255, 255, 255),
                        Color.rgb(100, 0, 0),
                        null));



        mySimpleXYPlot.redraw();
        mySimpleXYPlot.calculateMinMaxVals();
        minXY = new PointF(mySimpleXYPlot.getCalculatedMinX().floatValue(), mySimpleXYPlot.getCalculatedMinY().floatValue());
        maxXY = new PointF(mySimpleXYPlot.getCalculatedMaxX().floatValue(), mySimpleXYPlot.getCalculatedMaxY().floatValue());
    }
    private void populateSeries(SimpleXYSeries series)
    {

        for(int i = 0; i < lista_X.size(); i++)
        {
           series.addLast( lista_X.get(i), Double.parseDouble(lista_Y.get(i)) );
        }
    }

    private void zoom(float scale)
    {
        float domainSpan = maxXY.x - minXY.x;
        float domainMidPoint = maxXY.x - domainSpan / 2.0f;
        float offset = domainSpan * scale / 2.0f;

        minXY.x = domainMidPoint - offset;
        maxXY.x = domainMidPoint + offset;

        minXY.x = Math.min(minXY.x, series[3].getX(series[3].size() - 3).floatValue());
        maxXY.x = Math.max(maxXY.x, series[0].getX(1).floatValue());
        clampToDomainBounds(domainSpan);
    }

    private void scroll(float pan)
    {
        float domainSpan = maxXY.x - minXY.x;
        float step = domainSpan / mySimpleXYPlot.getWidth();
        float offset = pan * step;
        minXY.x = minXY.x + offset;
        maxXY.x = maxXY.x + offset;
        clampToDomainBounds(domainSpan);
    }

    private void clampToDomainBounds(float domainSpan)
    {
        float leftBoundary = series[0].getX(0).floatValue();
        float rightBoundary = series[3].getX(series[3].size() - 1).floatValue();
        // enforce left scroll boundary:
        if (minXY.x < leftBoundary)
        {
            minXY.x = leftBoundary;
            maxXY.x = leftBoundary + domainSpan;
        }
        if (maxXY.x > series[3].getX(series[3].size() - 1).floatValue())
        {
            maxXY.x = rightBoundary;
            minXY.x = rightBoundary - domainSpan;
        }
    }

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.hypot(x, y);
    }


    //  ************   ESTADOS DE TOUCHES ***********
    static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    static final int TWO_FINGERS_DRAG = 2;
    int mode = NONE;

    PointF firstFinger;
    float distBetweenFingers;
    boolean stopThread = false;
    //  *********************************************
    @Override
    public boolean onTouch(View arg0, MotionEvent event)
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // Start gesture
                firstFinger = new PointF(event.getX(), event.getY());
                mode = ONE_FINGER_DRAG;
                stopThread = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // second finger
                distBetweenFingers = spacing(event);
                // the distance check is done to avoid false alarms
                if (distBetweenFingers > 5f) {
                    mode = TWO_FINGERS_DRAG;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ONE_FINGER_DRAG) {
                    PointF oldFirstFinger = firstFinger;
                    firstFinger = new PointF(event.getX(), event.getY());
                    scroll(oldFirstFinger.x - firstFinger.x);
                    mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x,
                            BoundaryMode.FIXED);
                    mySimpleXYPlot.redraw();

                } else if (mode == TWO_FINGERS_DRAG) {
                    float oldDist = distBetweenFingers;
                    distBetweenFingers = spacing(event);
                    zoom(oldDist / distBetweenFingers);
                    mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x,
                            BoundaryMode.FIXED);
                    mySimpleXYPlot.redraw();
                }
                break;
        }
        return true;
    }


}

