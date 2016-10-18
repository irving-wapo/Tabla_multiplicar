package com.protopo.previewplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class tabla_datos_agri extends AppCompatActivity {

    Button add;
    Tabla_agrimensura tabla, tabla_dos ;
    TextView distancia, grados, minutos, segundos, sumatoria_a_i_o;
    double val = 0; String senti="", angulo="";
    ArrayList<String> list_pos1 = new ArrayList<String>();
    ArrayList<String> list_pos2 = new ArrayList<String>();
    ArrayList<String> list_pos3 = new ArrayList<String>();
    ArrayList<String> list_pos4 = new ArrayList<String>();
    ArrayList<String> list_alfas  = new ArrayList<String>();
    ArrayList<String> list_distancias = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_datos_agri);

        tabla = new Tabla_agrimensura(1, getSupportFragmentManager(), this, (TableLayout) findViewById(R.id.tablita));
        tabla_dos = new Tabla_agrimensura(1, getSupportFragmentManager(), this, (TableLayout) findViewById(R.id.tableLayouttt));
        add = (Button) findViewById(R.id.btn_add);
        distancia = (TextView) findViewById(R.id.editText11_dis);
        grados = (TextView) findViewById(R.id.editText12_g);
        minutos = (TextView) findViewById(R.id.editText13_m);
        segundos = (TextView) findViewById(R.id.editText14_s);
        sumatoria_a_i_o = (TextView) findViewById(R.id.textView44);
        val = getIntent().getDoubleExtra("val_n", val);
        senti = getIntent().getStringExtra("sentido");
        angulo = getIntent().getStringExtra("angulo");
    }


    double alfa_cero=0, de_cero=0;
    public void  calcula_alfas()
    {
        if(senti.equals("SE"))
        { alfa_cero = (1.5* 3.1416)  + radianes( Double.parseDouble(angulo));  }

        if(senti.equals("SW"))
        { alfa_cero = (1.5* 3.1416)  - radianes( Double.parseDouble(angulo));  }

        if(senti.equals("NW"))
        { alfa_cero = (0.5* 3.1416)  + radianes( Double.parseDouble(angulo));  }

        if(senti.equals("NE"))
        { alfa_cero = (0.5* 3.1416)  - radianes( Double.parseDouble(angulo));  }

        list_alfas.add(""+alfa_cero);

        //Toast.makeText(getApplicationContext(), "alfa cero : "+ alfa_cero , Toast.LENGTH_LONG).show();

        de_cero= Double.parseDouble(list_pos3.get(0));
        list_distancias.add(""+de_cero);
       // Toast.makeText(getApplicationContext(), "'D' cero : "+ de_cero , Toast.LENGTH_LONG).show();
//        calcula_duno();
    }

    double de_uno=0;
    public void calcula_duno() {
        double parte_uno_uno = 0, parte_uno_dos = 0;
        parte_uno_uno = Math.pow(de_cero, 2) + Math.pow(Double.parseDouble(list_pos3.get(1)), 2);
        parte_uno_dos = 2 * de_cero * Double.parseDouble(list_pos3.get(1)) * Math.cos(Double.parseDouble(list_pos4.get(1)));
        de_uno = Math.sqrt(parte_uno_uno - parte_uno_dos);
        list_distancias.add("" + de_uno);
        // sumatoria_a_i_o.setText("hola:"+de_uno );
        Toast.makeText(getApplicationContext(), "De uno : " + de_uno, Toast.LENGTH_LONG).show();

        double alfa_uno = 0;
        double  valor_r=Math.pow(Double.parseDouble(list_pos3.get(1)), 2);
        double par_uno_uno = (Math.pow(Double.parseDouble(list_distancias.get(0)), 2) + Math.pow(Double.parseDouble(list_distancias.get(1)), 2) - valor_r );
        double par_uno_dos = 2 * Double.parseDouble(list_distancias.get(0)) * Double.parseDouble(list_distancias.get(1));
        alfa_uno = Double.parseDouble(list_alfas.get(0)) - Math.acos(parte_uno_uno / par_uno_dos);
        list_alfas.add(""+alfa_uno);
        Toast.makeText(getApplicationContext(), "alfa_uno:  "+ alfa_uno , Toast.LENGTH_LONG).show();


        double teta = Double.parseDouble( list_pos4.get(1)) + Double.parseDouble( list_pos4.get(2));

        double parte_uno_2 = 0, parte_dos_2 = 0, de_dos=0;
        parte_uno_2 = Math.pow(de_uno, 2) + Math.pow(Double.parseDouble(list_pos3.get(2)), 2);
        parte_dos_2 = 2 * de_uno * Double.parseDouble(list_pos3.get(2)) * Math.cos(teta - Double.parseDouble(list_alfas.get(1)) + Double.parseDouble(list_alfas.get(0))  - 3.1416 ) ;
        de_dos = Math.sqrt(parte_uno_2 - parte_dos_2);
        list_distancias.add("" + de_dos);
        // sumatoria_a_i_o.setText("hola:"+de_uno );
        Toast.makeText(getApplicationContext(), "De dos : " + de_dos, Toast.LENGTH_LONG).show();



        double alfa_dos = 0;
        double  valor_r_dos=Math.pow(Double.parseDouble(list_pos3.get(2)), 2);
        double par_dos_uno = (Math.pow(Double.parseDouble(list_distancias.get(1)), 2) + Math.pow(Double.parseDouble(list_distancias.get(2)), 2) - valor_r_dos );
        double par_dos_dos = 2 * Double.parseDouble(list_distancias.get(1)) * Double.parseDouble(list_distancias.get(2));
        alfa_dos = Double.parseDouble(list_alfas.get(1)) - Math.acos(par_dos_uno / par_dos_dos);
        list_alfas.add(""+alfa_dos);
        Toast.makeText(getApplicationContext(), "alfa_dos:  "+ alfa_dos , Toast.LENGTH_LONG).show();

        // comentarios


        teta  += Double.parseDouble(list_pos4.get(3));
        double parte_uno_3 = 0, parte_dos_3 = 0, de_tres=0;
        teta  += Double.parseDouble(list_pos3.get(3));
        parte_uno_3 = Math.pow(de_dos, 2) + Math.pow(Double.parseDouble(list_pos3.get(3)), 2);
        parte_dos_3 = 2 * de_dos * Double.parseDouble(list_pos3.get(3)) * Math.cos(teta - Double.parseDouble(list_alfas.get(2)) + Double.parseDouble(list_alfas.get(0))  - 3.1416*2 ) ;
        de_tres = Math.sqrt(parte_uno_3 - parte_dos_3);
        list_distancias.add("" + de_tres);
        // sumatoria_a_i_o.setText("hola:"+de_uno );
        Toast.makeText(getApplicationContext(), "De tres : " + de_tres, Toast.LENGTH_LONG).show();


        double alfa_tres = 0;
        double  valor_r_tres=Math.pow(Double.parseDouble(list_pos3.get(3)), 2);
        double par_tres_uno = (Math.pow(Double.parseDouble(list_distancias.get(2)), 2) + Math.pow(Double.parseDouble(list_distancias.get(3)), 2) - valor_r_tres );
        double par_tres_dos = 2 * Double.parseDouble(list_distancias.get(2)) * Double.parseDouble(list_distancias.get(3));
        alfa_tres = Double.parseDouble(list_alfas.get(2)) - Math.acos(par_tres_uno / par_tres_dos);
        list_alfas.add(""+alfa_tres);
        Toast.makeText(getApplicationContext(), "alfa tres:  "+ alfa_tres , Toast.LENGTH_LONG).show();



/*        double parte_uno_4 = 0, parte_dos_4 = 0, de_cuatro=0;
        teta  += Double.parseDouble(list_pos4.get(4));
        parte_uno_4 = Math.pow(de_tres, 2) + Math.pow(Double.parseDouble(list_pos3.get(4)), 2);
        parte_dos_4 = 2 * de_tres * Double.parseDouble(list_pos3.get(4)) * Math.cos(teta - Double.parseDouble(list_alfas.get(3)) + Double.parseDouble(list_alfas.get(0))  - 3.1416*3 ) ;
        de_cuatro = Math.sqrt(parte_uno_4 - parte_dos_4);
        list_distancias.add("" + de_cuatro);
        // sumatoria_a_i_o.setText("hola:"+de_uno );
        Toast.makeText(getApplicationContext(), "De cuatro : " + de_cuatro, Toast.LENGTH_LONG).show();


        double alfa_cuatro = 0;
        double  valor_r_cuatro=Math.pow(Double.parseDouble(list_pos3.get(4)), 2);
        double par_cuatro_uno = (Math.pow(Double.parseDouble(list_distancias.get(3)), 2) + Math.pow(Double.parseDouble(list_distancias.get(4)), 2) - valor_r_cuatro );
        double par_cuatro_dos = 2 * Double.parseDouble(list_distancias.get(3)) * Double.parseDouble(list_distancias.get(4));
        alfa_cuatro = Double.parseDouble(list_alfas.get(3)) - Math.acos(par_cuatro_uno / par_cuatro_dos);
        list_alfas.add(""+alfa_cuatro);
        Toast.makeText(getApplicationContext(), "alfa_cuatro:  "+ alfa_cuatro , Toast.LENGTH_LONG).show();

*/

        //for (int i = 2; i < list_pos1.size(); i++)
/*
        try {
            int i = 2;

            double alfa_i = 0;
            double parte_alfa_uno = Math.pow(Double.parseDouble(list_distancias.get(i - 2)), 2) + Math.pow(Double.parseDouble(list_distancias.get(i-1)), 2) - Math.pow(Double.parseDouble(list_pos3.get(i-1)), 2);
            double parte_alfa_dos = (2 * Double.parseDouble(list_distancias.get(i-1))) * (Double.parseDouble(list_distancias.get(i-2)) );


            Toast.makeText(getApplicationContext(), "Parte uno:  " + parte_alfa_uno, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Parte dos: " + parte_alfa_dos, Toast.LENGTH_LONG).show();


            alfa_i = Double.parseDouble(list_alfas.get(i - 1)) - Math.acos(parte_alfa_uno / parte_alfa_dos);

            Toast.makeText(getApplicationContext(), "Alfa dos: " + alfa_i, Toast.LENGTH_LONG).show();
        }catch (Exception Ex){            Toast.makeText(getApplicationContext(),Ex.toString(), Toast.LENGTH_LONG).show();}


*/

       //double tetas = 0;
        //tetas += Double.parseDouble(list_pos4.get(0));
        //tetas += Double.parseDouble(list_pos4.get(1));

       // try {
//            for (int i = 1; i < list_pos4.size(); i++) {

               // tetas += Double.parseDouble(list_pos4.get(i));

/*                double alfa_i = 0;
                double  val_temporal=Math.pow(Double.parseDouble(list_pos3.get(i - 1)), 2);
                double  val_temporal_2=Math.pow(Double.parseDouble(list_distancias.get(i - 1)), 2);


                double par_uno_i = Math.acos(val_temporal_2 + val_temporal );
                double parte_dos_i = 2 * Double.parseDouble(list_distancias.get(i - 1)) * Double.parseDouble(list_distancias.get(i));
                alfa_i = Double.parseDouble(list_distancias.get(i - 1)) - Math.acos(par_uno_i / parte_dos_i);

                Toast.makeText(getApplicationContext(), "temporal 1  " + i + ": " + val_temporal, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "temporal 2  " + i + ": " + val_temporal_2, Toast.LENGTH_LONG).show();


                Toast.makeText(getApplicationContext(), "Parte 1  " + i + ": " + par_uno_i, Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "Parte 2  " + i + ": " + parte_dos_i, Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "Alfa  " + i + ": " + alfa_i, Toast.LENGTH_LONG).show();
*/

/*
                double alfa_i = 0;
                double parte_alfa_uno = Math.pow(Double.parseDouble(list_distancias.get(i - 1)), 2) + Math.pow(Double.parseDouble(list_distancias.get(i)), 2) - Math.pow(Double.parseDouble(list_pos3.get(i)), 2);
                double parte_alfa_dos = (2 * Double.parseDouble(list_distancias.get(i))) * (Double.parseDouble(list_distancias.get(i - 1)));

                alfa_i = Double.parseDouble(list_alfas.get(i - 1)) - Math.acos(parte_alfa_uno / parte_alfa_dos);
                list_alfas.add(""+alfa_i);
                Toast.makeText(getApplicationContext(), "Alfa 1: " + alfa_i, Toast.LENGTH_LONG).show();

               double distancia_i = 0;
                double par_uno_i_d =   Math.pow(Double.parseDouble(list_distancias.get(i - 1)), 2) + Math.pow(Double.parseDouble(list_pos3.get(i)), 2);
                double parte_dos_d = tetas - (Double.parseDouble(list_alfas.get(i-1)) + (Double.parseDouble(list_alfas.get(0))) - (i -1)*3.1416 );

                distancia_i = Math.sqrt( par_uno_i_d  -  (2*Double.parseDouble(list_distancias.get(i-1)) *  Double.parseDouble(list_pos3.get(i)))  * Math.cos( parte_dos_d) );

                list_distancias.add(""+distancia_i);
                Toast.makeText(getApplicationContext(), "Distancia  " + i + ": " + distancia_i, Toast.LENGTH_LONG).show();
               }
        }catch(Exception Ex) {Toast.makeText(getApplicationContext(), "Ex: "+Ex.toString(), Toast.LENGTH_LONG).show();}
*/

    }

    private void cabecera()
    {
        tabla.agregarCabecera(R.array.cabesera_agri);
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
                cabecera_dos();
                calculos_a_i();
            }
            else
            {
                list_pos2.add("" + (contador + 1));
            }
            contador++;
        }
        mostar_valores();
        distancia.setText("");
        grados.setText("");
        //segundos.setText("");
       // minutos.setText("");
    }



    double sumatoria=0;
    public String calucla_sumatoria()
    {
        sumatoria=0;
        for(int i=0; i<list_pos4.size();i++)
        {
            sumatoria += Double.parseDouble(list_pos4.get(i));
        }

        calcula_alfas();
        return "Sumatoria: "+sumatoria;
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



    //******************************************

    private void cabecera_dos() {
        tabla_dos.agregarCabecera(R.array.cabesera_nueva);
    }

    public void calculos_a_i()
    {
        double ang_origi=0, part1=0, angulos_corre=0;
        for(int i=0; i<list_pos4.size(); i++)
        {
            ang_origi = Double.parseDouble(list_pos4.get(i));
            part1 = (((val -2) * 3.1416) - sumatoria) / val;

            angulos_corre = ang_origi + part1;
            String vec[]={""+angulos_corre};
            tabla_dos.agregarFilaTabla(vec);
        }
    }
}

