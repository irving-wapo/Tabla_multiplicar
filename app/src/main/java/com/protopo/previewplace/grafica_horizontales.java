package com.protopo.previewplace;
import android.app.*;
import android.graphics.*;
import android.os.*;

import com.androidplot.xy.*;

import java.util.*;

public class grafica_horizontales extends Activity {

    private XYPlot plot; double val_ST=0;
    ArrayList<Double[]> listDouble = new ArrayList<Double[]>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica_horizontales);
        listDouble = (ArrayList<Double[]>) getIntent().getSerializableExtra("lista");
        val_ST =  getIntent().getDoubleExtra ("ST", val_ST);

        // initialize our XYPlot reference:);
        plot = (XYPlot) findViewById(R.id.plot);
        plot.setDomainStep(XYStepMode.INCREMENT_BY_PIXELS, 100);                  //DIVISION DE CUADRICULA
        plot.setRangeStep(XYStepMode.INCREMENT_BY_PIXELS, 20);                  //DE FONDO
        plot.centerOnDomainOrigin(0);                                         //ORIENTAR GRAFICO X
        plot.centerOnRangeOrigin(100);                                          //EJEMPLO 8,-8 Y

        plot.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
        plot.setDomainBoundaries(90, 100, BoundaryMode.GROW);                  //IMAGEN COMPLETA
        plot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);       //COLOR DEL MARCO DE FONDO
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);   //COLOR DEL FONDO DEL PANEL

        // Domain
        plot.getGraphWidget().setDomainLabelPaint(null);                        //ELIMINA VALORES DE X
        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 5);                     //DIVISION DE LA CUADRICULA
        //plot.setDomainValueFormat(new DecimalFormat("50"));
        //plot.getGraphWidget().setDomainOriginLinePaint(null);
        //Remove legend
        //plot.getLayoutManager().s .remove (plot.getLegendWidget());
        //plot.getLayoutManager().remove(plot.getDomainLabelWidget());
        //plot.getLayoutManager().remove(plot.getRangeLabelWidget());
        //plot.getLayoutManager().remove(plot.getTitleWidget());
        //plot.getLegendWidget().setSize(new SizeMetrics(400, SizeLayoutType.FILL, 80, SizeLayoutType.FILL));
        //plot.getLegendWidget().position(25, XLayoutStyle.ABSOLUTE_FROM_LEFT, 100, YLayoutStyle.ABSOLUTE_FROM_TOP, AnchorPosition.LEFT_BOTTOM);
        plot.getLegendWidget().setPadding(100, 1, 1, 1);

        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAlpha(200);
        plot.getLegendWidget().setBackgroundPaint(bgPaint);


        plot.addSeries(serie1(), estilo("puntos"));
        plot.addSeries(serie4(), estilo("punto"));
        plot.addSeries(serie2(), estilo("lineas"));
        //plot.addSeries(serie3(), estilo("margen"));

    }


    public  LineAndPointFormatter estilo(String nombre)
    {
       LineAndPointFormatter regresa = null;

       switch (nombre)
        {
            case "puntos":
                            {LineAndPointFormatter formato_puntos = new LineAndPointFormatter(
                             Color.rgb(0,0 , 0),                   // line color
                             Color.rgb(51,102 , 0),                // point color
                             Color.TRANSPARENT,                    // fill color (none)
                             new PointLabelFormatter(Color.TRANSPARENT)); regresa = formato_puntos;} break;


            case "lineas":
                            {LineAndPointFormatter formato_lineas = new LineAndPointFormatter(
                             Color.rgb(161, 158, 158),
                             null,
                             null, null);
                             regresa = formato_lineas;}break;

            case "margen":
                            {LineAndPointFormatter formato_margen = new LineAndPointFormatter(
                             Color.RED, null, null, null); regresa = formato_margen;} break;

            case "punto":
                            {LineAndPointFormatter formato_margen = new LineAndPointFormatter(
                            null, Color.RED, null, null); regresa = formato_margen;} break;
        }
        return regresa;
    }

    protected XYSeries serie1()
    {
        List<Number> xVals = new ArrayList<>();
        List<Number> yVals = new ArrayList<>();

        for(int i=0; i<listDouble.size(); i++)
        {
            Double[] obj_temporal =  listDouble.get(i);
            Double val_x = obj_temporal[0];
            Double val_y = obj_temporal[1];

            xVals.add(val_x);
            yVals.add(val_y);
            //Toast.makeText(getApplicationContext(),"Valor de x: "+ val_x +" Valor de y: "+ val_y  +". Caja "+i  ,Toast.LENGTH_LONG).show();
        }
        return new SimpleXYSeries(xVals, yVals, "Tabla de Datos");
    }

    public XYSeries serie2()
    {
        List<Number> xVals = new ArrayList<>();
        List<Number> yVals = new ArrayList<>();

        xVals.add(punto_pc[0] - 20);    yVals.add(punto_pc[1]);
        xVals.add(punto_pi[0] + 20);    yVals.add(punto_pi[1]);
        xVals.add(punto_pi[0]);         yVals.add(punto_pi[1]);

        xVals.add(punto_pi[0] -5);      yVals.add(punto_pi[1]-5);
        xVals.add(punto_pi[0]);         yVals.add(punto_pi[1]);


        xVals.add(punto_pt[0]);         yVals.add(punto_pt[1]);
        xVals.add(punto_pt[0]+5);       yVals.add(punto_pt[1]+5);

        /*  double x =10 * Math.cos( punto_pt[0]);
            double y =10 * Math.sin( punto_pt[1]);
            xVals.add( punto_pi[0] + x);       yVals.add( punto_pi[1] - y );
        */
        return new SimpleXYSeries(xVals, yVals, "rango");
    }


    public XYSeries serie3()
    {
        List<Number> xVals = new ArrayList<>();
        List<Number> yVals = new ArrayList<>();

 /*       xVals.add(0);   yVals.add(-120);
        xVals.add(0);   yVals.add(10);
        xVals.add(220); yVals.add(10);              //ABAJO
        xVals.add(220); yVals.add(-120);
        xVals.add(0);   yVals.add(-120);
   */     return new SimpleXYSeries(xVals, yVals, "margen");
    }

    Double[] punto_pc =new  Double[2];
    Double[] punto_pi = new  Double[2];
    Double[] punto_pt = new  Double[2];

    public XYSeries serie4()
    {
        punto_pc = listDouble.get(0);
        punto_pt = listDouble.get(listDouble.size()-1 );

        List<Number> x = new ArrayList<>();
        List<Number> y = new ArrayList<>();

        punto_pi[0] = punto_pc[0] + val_ST;
        punto_pi[1] = punto_pc[1];

        x.add(punto_pc[0]);          y.add(punto_pc[1]);
        x.add(punto_pi[0]);          y.add(punto_pi[1]);
        x.add(punto_pt[0]);          y.add(punto_pt[1]);
     return new SimpleXYSeries(x, y, "Pi");
    }
}
