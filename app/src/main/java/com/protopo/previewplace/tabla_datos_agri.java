package com.protopo.previewplace;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class tabla_datos_agri extends AppCompatActivity implements menu_agregar_dif.DialogListener
{

    Button add;
    Tabla_agrimensura tabla, tabla_dos, tabla_tres ;
    TextView distancia, grados, minutos, segundos, sumatoria_a_i_o, provisional, titulo1, titulo2;
    double val = 0; String senti="", angulo="";
    ArrayList<String> list_pos1 = new ArrayList<String>();
    ArrayList<String> list_pos2 = new ArrayList<String>();
    ArrayList<String> list_pos3 = new ArrayList<String>();
    ArrayList<String> list_pos4 = new ArrayList<String>();
    ArrayList<String> list_alfas  = new ArrayList<String>();
    ArrayList<String> list_distancias = new ArrayList<String>();
    ArrayList<Double> list_X  = new ArrayList<Double>();
    ArrayList<Double> list_Y = new ArrayList<Double>();
    ArrayList<Double> list_Rx  = new ArrayList<Double>();
    ArrayList<Double> list_Ry = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_datos_agri);

        tabla = new Tabla_agrimensura(1, getSupportFragmentManager(), this, (TableLayout) findViewById(R.id.tablta));
        tabla_dos = new Tabla_agrimensura(1, getSupportFragmentManager(), this, (TableLayout) findViewById(R.id.tableLayouttt));
        tabla_tres = new Tabla_agrimensura(1, getSupportFragmentManager(), this, (TableLayout) findViewById(R.id.tableLayouttt1));
        add = (Button) findViewById(R.id.btn_add);
        distancia = (TextView) findViewById(R.id.editText11_dis);
        grados = (TextView) findViewById(R.id.editText12_g);
        minutos = (TextView) findViewById(R.id.editText13_m);
        segundos = (TextView) findViewById(R.id.editText14_s);
        sumatoria_a_i_o = (TextView) findViewById(R.id.textView44);
        provisional =     (TextView) findViewById(R.id.provi);
        titulo1 =     (TextView) findViewById(R.id.titulo1);
        titulo2 =     (TextView) findViewById(R.id.titulo2);
        val = getIntent().getDoubleExtra("val_n", val);
        senti = getIntent().getStringExtra("sentido");
        angulo = getIntent().getStringExtra("angulo");
        distancia.setText("106"); grados.setText("88");  minutos.setText("12"); segundos.setText("0");

        Toast.makeText(getApplicationContext(), "hola mundo" ,Toast.LENGTH_LONG).show();
    }



    int contador = 1;
    public void btn_add(View view)
    {
        if (contador <= val)
        {
            list_pos1.add("" + contador);
            list_pos3.add("" + distancia.getText().toString());
            calcular_grados();

            if(contador  == val)
            {
                list_pos2.add("1");
                sumatoria_a_i_o.setText(calucla_sumatoria());
                calculos_a_i();
            }
            else
                { list_pos2.add("" + (contador + 1)); }
            if(contador ==1 )  { distancia.setText("85"); grados.setText("93");  minutos.setText("30"); segundos.setText("0"); }
            if(contador ==2 )  { distancia.setText("51.8"); grados.setText("100");  minutos.setText("0"); segundos.setText("0"); }
            if(contador ==3 )  { distancia.setText("71.5"); grados.setText("132");  minutos.setText("15"); segundos.setText("0"); }
            if(contador ==4 )  { distancia.setText("57.3"); grados.setText("126");  minutos.setText("15"); segundos.setText("0"); }
            contador++;
            mostar_valores();
        }
    }

    private void cabecera()
    {
        tabla.agregarCabecera(R.array.cabesera_agri);
    }
    private void cabecera_tres()
    {
        tabla_tres.agregarCabecera(R.array.cabecera_rx);
    }

    double sumatoria=0;
    public String calucla_sumatoria()
    {
        String cadena="\nAngulo interno inicial: \n";
        sumatoria=0;
        for(int i=0; i<list_pos4.size();i++)
        {
            if(i== list_pos4.size()-1 )
                { cadena += decimales.format( Double.parseDouble(list_pos4.get(i)));}
            else
                { cadena += decimales.format( Double.parseDouble(list_pos4.get(i))) + " + ";}

            sumatoria += Double.parseDouble(list_pos4.get(i));
        }
        calcula_alfas();
        cadena += "\n\nSuma de angulos: "+sumatoria +"\n\n\n";
        return cadena;
    }



    double alfa_cero=0, de_cero=0;
    public void  calcula_alfas()
    {
        if(senti.equals("SE"))  { alfa_cero = (1.5* 3.1416)  +  ( Double.parseDouble(angulo));  }
        if(senti.equals("SW"))  { alfa_cero = (1.5* 3.1416)  -  ( Double.parseDouble(angulo));  }
        if(senti.equals("NW"))  { alfa_cero = (0.5* 3.1416)  +  ( Double.parseDouble(angulo));  }
        if(senti.equals("NE"))  { alfa_cero = (0.5* 3.1416)  -  ( Double.parseDouble(angulo));  }

        de_cero= Double.parseDouble(list_pos3.get(0));
        list_alfas.add(""+alfa_cero);
        list_distancias.add(""+de_cero);
        cabecera_dos();

        //------------------------  D   I   S   T   A   N   C   I   A       1 --------------------------------
        double var_1 = 0, var_2 = 0, de_uno=0;
        var_1 = Math.pow(de_cero, 2) + Math.pow(Double.parseDouble(list_pos3.get(1)), 2);
        var_2 = 2 * de_cero * Double.parseDouble(list_pos3.get(1)) * Math.cos(Double.parseDouble(list_pos4.get(1)));
        de_uno = Math.sqrt(var_1 - var_2);
        list_distancias.add("" + de_uno);


        //----------------------------------------  A   L   F   A   1 -----------------------------------------
        double alfa_uno = 0;
        double valor_r= Math.pow(Double.parseDouble(list_pos3.get(1)), 2);
        double var_3  = (Math.pow(Double.parseDouble(list_distancias.get(0)), 2) + Math.pow(Double.parseDouble(list_distancias.get(1)), 2) - valor_r );
        double var_4  = 2 * Double.parseDouble(list_distancias.get(0)) * Double.parseDouble(list_distancias.get(1));
        // signo = (3*Math.pi/2-tetaCompensado(1))Math.abs(3*Math.pi/2-tetaCompensado(1))
        // alfa_uno = Double.parseDouble(list_alfas.get(0)) - signo*Math.acos(var_3 / var_4);

        alfa_uno = Double.parseDouble(list_alfas.get(0)) - Math.acos(var_3 / var_4);
        list_alfas.add(""+alfa_uno);
        double teta = Double.parseDouble(list_pos4.get(1));



        //**************  F  O  R    ******************
        for(int i=2; i<val; i++)
        {
            double var_5_tem = 0, var_6_tem = 0, var_7_tem=0, de_tem = 0;
            teta = teta + Double.parseDouble(list_pos4.get(i));
            var_5_tem = Math.pow(Double.parseDouble(list_distancias.get(i-1)),2)   +  Math.pow(Double.parseDouble(list_pos3.get(i)),  2);
            var_6_tem = teta - Double.parseDouble(list_alfas.get(i-1)) + Double.parseDouble(list_alfas.get(0)) - (i-1)*3.1416;
            var_7_tem = 2 * Double.parseDouble(list_distancias.get(i-1)) * Double.parseDouble(list_pos3.get(i)) * Math.cos(var_6_tem);
            de_tem = Math.sqrt(var_5_tem - var_7_tem);
            list_distancias.add(""+de_tem);


            double alfa_dos = 0;
            double valor_r_dos = Math.pow(Double.parseDouble(list_pos3.get(i)), 2);
            double par_dos_uno = (Math.pow(Double.parseDouble(list_distancias.get(i-1)), 2) + Math.pow(Double.parseDouble(list_distancias.get(i)), 2) - valor_r_dos);
            double par_dos_dos = 2 * Double.parseDouble(list_distancias.get(i-1)) * Double.parseDouble(list_distancias.get(i));
            // signo = (3*Math.pi/2-tetaCompensado(i))Math.abs(3*Math.pi/2-tetaCompensado(i))
            // alfa_dos = Double.parseDouble(list_alfas.get(i-1)) - signo*Math.acos(par_dos_uno / par_dos_dos);
            alfa_dos = Double.parseDouble(list_alfas.get(i-1)) - Math.acos(par_dos_uno / par_dos_dos);
            list_alfas.add("" + alfa_dos);
        }
        titulo1.setText("\n\nCalculo de alfa's y delta's\n");
        titulo2.setText("\n\nDatos para graficar y Correccion de radios:\n");
        mostar_alfas();
        calcula_xy();
    }

    public void calcula_xy()
    {
        String resultado=""; double valor=0, contador_rx=0, contador_ry=0;

        for(int i=0; i<list_distancias.size(); i++)
        {
            valor = Double.parseDouble(list_distancias.get(i)) * Math.cos(Double.parseDouble(list_alfas.get(i)));   list_X.add(valor);
            valor = Double.parseDouble(list_distancias.get(i)) * Math.sin(Double.parseDouble(list_alfas.get(i)));   list_Y.add(valor);
        }

        for(int i=0; i<list_X.size(); i++)
        {
            if(i==0)
                {   list_Rx.add(list_X.get(0));     list_Ry.add(list_Y.get(0));   }
            else
                {   list_Rx.add(list_X.get(i) - list_X.get(i - 1));     list_Ry.add(list_Y.get(i) - list_Y.get(i - 1)); }
            contador_rx += list_Rx.get(i);  contador_ry += list_Ry.get(i);
        }
        resultado += "\nSuma Rx: "+contador_rx;
        resultado += "\nSuma Ry: "+contador_ry;
        //
        provisional.setText( provisional.getText().toString() + resultado);
        cabecera_tres();
          muestra_tabla_xy();

    }



    //***********************************************************************************

    private void cabecera_dos() { tabla_dos.agregarCabecera(R.array.cabecera_nueva);  }

    public void calculos_a_i()
    {
        double ang_origi = 0, part1 = 0, angulos_corre = 0, contador = 0;  String cadena_salida ="\nAngulos internos compensados:\n";
        for (int i = 0; i < list_pos4.size(); i++)
        {
            ang_origi = Double.parseDouble(list_pos4.get(i));
            part1 = (((val - 2) * 3.1416) - sumatoria) / val;
            angulos_corre = ang_origi + part1;
            contador += angulos_corre;

            if (i == list_pos4.size() - 1)
               { cadena_salida += decimales.format(angulos_corre); }
            else
               { cadena_salida += decimales.format(angulos_corre) + " + "; }
        }
        //String vec[]={""+angulos_corre}; tabla_dos.agregarFilaTabla(vec);
        cadena_salida += "\n\nSumatoria de angulos compensados: "+contador+"\n\n\n";
        sumatoria_a_i_o.setText(sumatoria_a_i_o.getText().toString() +"" + cadena_salida);
    }







    //----------------------------------------------------------------------------------------------------------------
    public void muestra_tabla_xy()
    {
        for (int i = 0; i < list_X.size(); i++)
        {
            String vec[] = {"", "" +decimales.format(list_X.get(i)), "" + decimales.format(list_Y.get(i)),  "" +  decimales.format(list_Rx.get(i)), "" + decimales.format(list_Ry.get(i)) };
            tabla_tres.agregarFilaTabla(vec);
        }
    }


    public void mostar_alfas()
    {
        tabla.limpiar(); cabecera();
        for (int i = 0; i < list_distancias.size(); i++)
        {
            String vec[] = {"" +decimales.format(Double.parseDouble(list_alfas.get(i))), "" + decimales.format(Double.parseDouble(list_distancias.get(i))) };
            tabla_dos.agregarFilaTabla(vec);
        }
    }

    public void mostar_valores()
    {
        tabla.limpiar(); cabecera();
        for (int i = 0; i < list_pos1.size(); i++)
        {
            String mus = "";
            mus = ""+ grados(Double.parseDouble(list_pos4.get(i)));
            String vec[] = {"" + list_pos1.get(i), "" + list_pos2.get(i), "" + list_pos3.get(i), "" + decimales.format(Double.parseDouble(mus)) +"°"  };// valor="";
            tabla.agregarFilaTabla(vec);
        }
    }


    DecimalFormat decimales = new DecimalFormat("0.0000");
    Double grados_c=0.0;
    public String calcular_grados()
    {
        Double  segundos_c=0.0, minutos_c =0.0;
        segundos_c = Double.parseDouble(segundos.getText().toString())  / 60;
        minutos_c = Double.parseDouble(minutos.getText().toString())  + segundos_c;
        minutos_c = minutos_c / 60;
        grados_c =  Double.parseDouble(grados.getText().toString()) + minutos_c;
        list_pos4.add(""+radianes(grados_c));
        return grados.getText().toString() +"° "+ minutos.getText().toString()+"' " + segundos.getText().toString()+"''  = " /*+decimales.format(radianes(grados_c))+" radianes\n\n"*/;
    }

    public double radianes(double valor)
    {
        return Math.toRadians(valor);
    }

    public double grados(double valor)
    {
        return Math.toDegrees(valor);
    }

    @Override
    public void onSingleChoiceItems(DialogFragment dialog, int arg) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
    //provisional.setText( provisional.getText().toString() + " ==  ALFA UNO: " +alfa_uno);


}

