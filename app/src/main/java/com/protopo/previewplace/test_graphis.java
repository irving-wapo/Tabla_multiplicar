package com.protopo.previewplace;
import java.text.DecimalFormat;
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


public class test_graphis extends Activity implements OnTouchListener
{
    private XYPlot mySimpleXYPlot;
    private Button resetButton;
    private SimpleXYSeries[] series = null;
    private PointF minXY;
    private PointF maxXY;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_graphis);
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

        mySimpleXYPlot = (XYPlot) findViewById(R.id.plot);
        mySimpleXYPlot.setOnTouchListener(this);
        mySimpleXYPlot.getGraphWidget().setTicksPerRangeLabel(2);
        mySimpleXYPlot.getGraphWidget().setTicksPerDomainLabel(2);
        mySimpleXYPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        mySimpleXYPlot.getGraphWidget().setRangeValueFormat( new DecimalFormat("#####"));
        mySimpleXYPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("#####.#"));
        mySimpleXYPlot.getGraphWidget().setRangeLabelSubTickExtension(25);
        mySimpleXYPlot.setRangeLabel("");
        mySimpleXYPlot.setDomainLabel("");
        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.NONE, null, null);


        series = new SimpleXYSeries[4];
        int scale = 1;
        for (int i = 0; i < 4; i++, scale *= 5)
        {
            series[i] = new SimpleXYSeries("INSTITUTO TECNOLOGICO DE TEHUACAN");
            populateSeries(series[i]);
        }                                                           // verde
        mySimpleXYPlot.addSeries(series[3], new LineAndPointFormatter(Color.rgb(0, 255, 0), null, Color.rgb(100, 0, 0), null));
        mySimpleXYPlot.redraw();
        mySimpleXYPlot.calculateMinMaxVals();
        minXY = new PointF(mySimpleXYPlot.getCalculatedMinX().floatValue(),mySimpleXYPlot.getCalculatedMinY().floatValue());
        maxXY = new PointF(mySimpleXYPlot.getCalculatedMaxX().floatValue(),mySimpleXYPlot.getCalculatedMaxY().floatValue());
    }

    private void populateSeries(SimpleXYSeries series)
    {
        Number[] Datos1 = {0, 2, 8, 20, 40, 60, 80, 94, 100, 120, 137, 140, 148, 160, 180, 186.6, 192, 200, 220, 228.7, 234.6, 240, 242, 253, 256.2, 260, 280, 300, 320, 340, 360, 370, 380, 400, 405, 405, 420, 440, 460, 480, 500, 520, 540, 560, 580, 600, 613.2};
        Number[] Datos2 = {100, 99.72, 97.29, 96.98, 96.68, 96.44, 95.99, 95.41, 94.89, 94.71, 94.7, 94.27, 93.51, 93.65, 93.69, 93.63, 92.77, 92.74, 92.74, 92.74, 91.85, 91.78, 91.44, 91.45, 90.99, 90.98, 90.94, 90.36, 89.87, 89.71, 89.07, 89.12, 88.62, 88.46, 88.46, 88.32, 87.36, 85.79, 85.47, 85.15, 84.87, 84.72, 84.16, 84.12, 84, 83.79, 82.82};

        for(int i = 0; i < Datos1.length -1; i++)
        {
           series.addLast( Datos1[i], Datos2[i]);
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

