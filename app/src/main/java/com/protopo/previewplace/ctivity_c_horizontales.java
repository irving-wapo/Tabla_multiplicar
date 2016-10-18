package com.protopo.previewplace;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ctivity_c_horizontales extends AppCompatActivity
{
    Tabla_horizontales tabla;
    ArrayList<String[]> elementos = new ArrayList<String[]>();
    TabHost TbH;
    TextView res1;
    EditText Grados,  Minutos, Segundos, Valor_g, Valor_pi_km, Valor_pi_m;
    DecimalFormat decimales = new DecimalFormat("0.0000");
    Double var_G =0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_horizontales);
        res1 =(TextView) findViewById(R.id.label_res1 );
        Grados = (EditText) findViewById(R.id.txtGrados);
        Minutos = (EditText) findViewById(R.id.txtMinutos);
        Segundos = (EditText) findViewById(R.id.txtsegundos);
        Valor_g = (EditText) findViewById(R.id.txt_valor_g);
        Valor_pi_km = (EditText) findViewById(R.id.txt_pi_km);
        Valor_pi_m = (EditText) findViewById(R.id.txt_pi_m);

        pestañas();
        tabla = new Tabla_horizontales(1,getSupportFragmentManager(),this, (TableLayout) findViewById(R.id.tabla_datos));
        cabecera();
    }

    private void pestañas()
    {
            TbH = (TabHost) findViewById(R.id.tabHost2); //llamamos al Tabhost
            TbH.setup();                                                         //lo activamos
            TabHost.TabSpec tab1 = TbH.newTabSpec("tab1");  //aspectos de cada Tab (pestaña)
            TabHost.TabSpec tab2 = TbH.newTabSpec("tab2");  //aspectos de cada Tab (pestaña)
            final TabHost.TabSpec tab3 = TbH.newTabSpec("tab3");
            tab1.setIndicator("Datos");    //qué queremos que aparezca en las pestañas
            tab1.setContent(R.id.lnlDatos); //definimos el id de cada Tab (pestaña)
            tab2.setIndicator("Tabla");
            tab2.setContent(R.id.lnlTabla);
            tab3.setIndicator("Grafica");
            tab3.setContent(R.id.lnlGrafica);

            TbH.addTab(tab1); //añadimos los tabs ya programados
            TbH.addTab(tab2);
            TbH.addTab(tab3);

        TextView tv = (TextView) TbH.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#ffffff"));
        TbH.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {

            @Override
            public void onTabChanged(String tabId) {

                for (int i = 0; i < TbH.getTabWidget().getChildCount(); i++) {
                    TextView tv = (TextView) TbH.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#000000"));

                }
                TextView tv = (TextView) TbH.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTextColor(Color.parseColor("#ffffff"));


            }
        });
        TbH.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
               // Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
               // Toast.makeText(getApplicationContext(),"AndroidTabsDemo, Pulsada pestaña: " + tabId ,Toast.LENGTH_LONG).show();

                if(tabId.equals("tab3"))
                {
                    Intent i = new Intent(getApplicationContext(), grafica_horizontales.class );
                    i.putExtra("lista", lista_envio);
                    i.putExtra("ST",ST);
                    startActivity(i);
                }

            }
        });

    }






    //*****************************   C A L C U L O S  *****************************

    Double grados_c=0.0;
    public void calcular_valores(View view)
    {
        String imprimir="\n";
        imprimir += calcular_grados();
        imprimir += calcula_valor_c();
        imprimir += calculos_funciones();
        res1.setText( imprimir );
        carga_contenido();
    }

    public String calcular_grados()
    {
        Double  segundos_c=0.0, minutos_c =0.0;
        segundos_c = Double.parseDouble(Segundos.getText().toString())  / 60;
        minutos_c = Double.parseDouble(Minutos.getText().toString())  + segundos_c;
        minutos_c = minutos_c / 60;
        grados_c = Double.parseDouble(Grados.getText().toString()) + minutos_c;
        return Grados.getText().toString() +"° "+ Minutos.getText().toString()+"' " + Segundos.getText().toString()+"''  = " +decimales.format(radianes(grados_c))+" radianes\n\n";
    }

    public double radianes(double valor)
    {
        return Math.toRadians(valor);
    }

    public double grados(double valor)
    {
        return Math.toDegrees(valor);
    }

    public void calcular_valor_g() { var_G= radianes (Double.parseDouble(Valor_g.getText().toString())); }

    int var_C =0;
    public String calcula_valor_c ()
    {
        calcular_valor_g();
        if(var_G <= 10){var_C =  20;}
        else
        if(var_G > 10  && var_G <= 20){var_C = 10;}
        else
        if(var_G > 20  && var_G <= 40){var_C =  5;}
        return "C = " + var_C +"\n\n";
    }

    double PC=0.0, PT=0.0, Deflexion=0.0, ST=0.0;
    public String calculos_funciones()
    {
        double R=0.0, LC=0.0,  p_i=0.0; String cadena=""; ST=0.0;


        R=var_C/2;
        R= R/Math.sin(var_G/2);
        cadena += "R = "+R +"\n\n";

        ST = R * Math.tan( radianes(grados_c) /2 );
        cadena += "ST = "+ST +"\n\n";

        LC = (20*(radianes(grados_c)) / var_G );
        cadena += "LC = "+LC +"\n\n";

        p_i= Double.parseDouble(Valor_pi_km.getText().toString()+"000");
        p_i += Double.parseDouble(Valor_pi_m.getText().toString());

        PC = p_i - ST;
        cadena += "PC = "+PC+"\n\n";

        PT = PC + LC;
        cadena += "PT = "+PT+"\n\n";

        Deflexion = (radianes(grados_c)/2) / (LC);
        cadena += "Deflexion = "+ grados(Deflexion)+" radianes = ";

        cadena += objetn_g_m_s(grados(Deflexion));

        //Toast.makeText(getApplicationContext(),"Valor de R: "+R,Toast.LENGTH_LONG).show();
        return cadena;
    }

    public String objetn_g_m_s(Double deflex)
    {
        int d_Grados=0, d_Minutos=0; double minutos_decimal=0.0, d_Segundos=0;

        d_Grados = p_entera(deflex);
        minutos_decimal = p_decimal(deflex ) *60 ;
        d_Minutos =p_entera(minutos_decimal);
        d_Segundos = p_decimal(minutos_decimal) *60;

        String res = d_Grados+"° "+ d_Minutos+"' "+d_Segundos+"''";
        return res;
    }

    public int p_entera(double valor){return (int)valor; }

    public double p_decimal(double valor){ int p_ent= (int)valor; return  valor - p_ent; }








    //*****************************   T A B L A  *****************************


    private void cabecera() { tabla.agregarCabecera(R.array.PROBANDO_WAPO);}

    ArrayList  <Double[]> a_list = new ArrayList<Double[]>();
    String valor="";


    public  void carga_contenido()
    {
        //******  INICIALIZA E INSERTA PRIMER VALOR ************
        tabla.limpiar();
        a_list.clear();
        lista_envio.clear();
        cabecera();
        String vec[]={""+PC, "---","---"}; valor="";
        Double vecc[] = {PC, 0.0};
        a_list.add(   vecc );
        tabla.agregarFilaTabla(vec);


        //********** VERIFICA VALOR DE INICIO Y COMPARA   ************
        valor += String.valueOf((int)PC).charAt(String.valueOf((int)PC).length()-2 );
        valor += String.valueOf((int)PC).charAt(String.valueOf((int)PC).length()-1 );
        int temporal = (int) PC;
        temporal -= Integer.parseInt(valor);

        if( Integer.parseInt(valor) >20)
        {
            if( Integer.parseInt(valor)>40)
            {
                if( Integer.parseInt(valor)>60)
                {
                    if( Integer.parseInt(valor)>80)
                    {
                        if( Integer.parseInt(valor)>99){}
                        else { temporal+=100; }
                    }
                    else { temporal+=80; }
                }
                else { temporal+=60; }
            }
            else { temporal+=40; }
        }
        else { temporal+=20; }


        double def_par=0.0, def_total=0.0;
        def_par =  (temporal - PC) * Deflexion;
        def_total = def_par;
        String vec1[]={""+temporal,  ""+objetn_g_m_s(grados(def_par)), ""+objetn_g_m_s(grados(def_total)) };
        Double vecc1[]={Double.parseDouble(""+temporal),def_total};
        a_list.add(vecc1);
        tabla.agregarFilaTabla(vec1);


        //**********  INGRESA TERCERO Y DEMAS VALORES ************
        int anterior = temporal;
        def_par = (   (temporal += 20) - anterior   ) * Deflexion;
        def_total += def_par;
        String vec2[]={""+temporal,  ""+objetn_g_m_s(grados(def_par)), ""+objetn_g_m_s(grados(def_total)) };
        Double vecc2[]={Double.parseDouble(""+temporal),def_total};
        a_list.add(vecc2);
        tabla.agregarFilaTabla(vec2);

        while ( temporal < PT-20 )
        {
            anterior = temporal;
            def_par = (   (temporal += 20) - anterior   ) * Deflexion;
            def_total += def_par;
            String vec3[]={""+temporal,  ""+objetn_g_m_s(grados(def_par)), ""+objetn_g_m_s(grados(def_total)) };
            Double vecc10[]={Double.parseDouble(""+temporal),def_total};
            a_list.add(vecc10);
            tabla.agregarFilaTabla(vec3);
        }


        //**********  INGRESA ULTIMO VALOR ************
        anterior = temporal;
        def_par = (   PT  - anterior   ) * Deflexion;
        def_total += def_par;
        String vec4[]={""+PT,  ""+objetn_g_m_s(grados(def_par)), ""+objetn_g_m_s(grados(def_total)) };
        Double vecc4[]={Double.parseDouble(""+PT),def_total};
        a_list.add(vecc4);
        tabla.agregarFilaTabla(vec4);
        muestra_valores();
    }


    ArrayList<Double[]> lista_envio = new ArrayList<Double[]>();
    public void muestra_valores ()
    {
        int valor=0, valor_resta=0;
        for(int i=0; i<a_list.size(); i++)
        {
            Double[] vector = a_list.get(i);

                if (i == 0)
                {
                    valor =  (int)   ((double)vector[0]);
                    int decenas = valor % 100;
                    valor_resta = ((int) ((double)vector[0]))   -  decenas;
                }


            Double val_x =   vector[0] - valor_resta;
            Double val_y =   val_x *  Math.sin( vector[1]);

            Double[] valores={val_x, val_y };
            lista_envio.add(valores);
        }

    }
}
