package com.protopo.previewplace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class c_verticales extends AppCompatActivity
{
    Tabla_agrimensura tabla_uno;
    TextView pantalla, txt_km, txt_mt, txt_cota, txt_pe, txt_pe_pri;
    Button boton, btn_grafica;
    ImageButton img_btn;
    ArrayList<Integer> lista_double_x = new ArrayList<Integer>();
    ArrayList<String> lista_double_y = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_verticales);
        txt_km    =  (TextView) findViewById(R.id.txt_km);
        txt_mt    =  (TextView) findViewById(R.id.txt_mt);
        txt_cota  =  (TextView) findViewById(R.id.txt_cota);
        txt_pe    =  (TextView) findViewById(R.id.txt_pe);
        txt_pe_pri=  (TextView) findViewById(R.id.txt_pe_pri);
        pantalla =  (TextView) findViewById(R.id.textView49);
        boton =  (Button) findViewById(R.id.button6);
        img_btn =  (ImageButton) findViewById(R.id.imageButton);
        tabla_uno = new Tabla_agrimensura(1, getSupportFragmentManager(), this, (TableLayout) findViewById(R.id.tabla_c_v));
        pantalla.setText("");
        img_btn.setVisibility(View.INVISIBLE);
    }

    int numero_estaciones=0; boolean estacion;
    public  void bton_cal(View view)
    {
        pantalla.setText("");
        double resta_pes=0, pe=0, pe_pri=0, metros=0;
        pe= Double.parseDouble(txt_pe.getText().toString());
        pe_pri= Double.parseDouble(txt_pe_pri.getText().toString());
        resta_pes = pe -pe_pri;

        int redondeo = Math.abs( Math.round( Float.parseFloat(""+resta_pes)));
        //pantalla.setText( pantalla.getText().toString() + "\nRedondeo abs: " +redondeo);
        //pantalla.setText("\nResta:  " + decimal(resta_pes));
        metros = Double.parseDouble(txt_mt.getText().toString());
        String metros_cad=""+ (int)metros;
        if(metros_cad.equals("0")) { metros_cad="000"; }


        if(metros_cad.length() == 3)
        {

            if ( (int)metros_cad.charAt(1) % 2 == 0)
            {
                // ******  ESTACION  CERRADA ***
                estacion = true;
                //pantalla.setText(pantalla.getText().toString() + "\nNUMERO PAR ");
            }
            else
            {
                 // ******  ESTACION  MEDIA ***
                 estacion = false;
                 //pantalla.setText(pantalla.getText().toString() + "\nNUMERO IMPAR ");
            }


            if( estacion == true && (redondeo %2 != 0) )
            {
                //PREGUNTAR OSCAR REDONDEO + 1 =  PAR
                numero_estaciones = redondeo +1 ;
            }

            else if( estacion == false && (redondeo %2 == 0) )
            {
                //volver IMPAR hacia arriba
                numero_estaciones = redondeo +1;
            }
            else
            {
                //  SI NO ENTRA EN NINGUNA DE LAS CONDICIONES SE QUEDA CON EL VALOR REDONDEADO
                numero_estaciones = redondeo;
            }

            pantalla.setText(pantalla.getText().toString() + "\nNumero de estaciones: " +numero_estaciones);
            int LCV= numero_estaciones * 20;
            pantalla.setText(pantalla.getText().toString() + "\nLCV: " +LCV);
            paso_dos( LCV);
        }
        else {  Toast.makeText(getApplicationContext(), "Verifique metros. Ejemplo 080", Toast.LENGTH_SHORT).show();  }
    }





    int KM_PCV =0;
    public void paso_dos(int LCV)
    {
        int  KM_PTV=0; String numero_cad="";

        numero_cad += txt_km.getText().toString() +""+ txt_mt.getText().toString();
        KM_PCV= (Integer.parseInt(numero_cad)) - (int)( 0.5 * LCV );
        pantalla.setText(pantalla.getText().toString() + "\nKM_PCV : " + KM_PCV);

        KM_PTV = KM_PCV +LCV;
        pantalla.setText(pantalla.getText().toString() + "\nKM PTV : " + KM_PTV);
        paso_tres(LCV);
    }



    double COTA_PCV =0, COTA_P=0, COTA_PTV=0;
    public void paso_tres(int LCV)
    {
        double  COTA_PIV=0, VALOR_P=0, VALOR_PE_PRi=0 ;

        COTA_PIV =Double.parseDouble(txt_cota.getText().toString());
        VALOR_P = Double.parseDouble(txt_pe.getText().toString());
        COTA_PCV = COTA_PIV - (0.5 * LCV * (VALOR_P/100));
        pantalla.setText(pantalla.getText().toString() + "\nCOTA PCV : " + deci_uno.format(COTA_PCV));


        VALOR_PE_PRi = Double.parseDouble(txt_pe_pri.getText().toString());
        COTA_PTV = COTA_PIV  + (0.5 * LCV * (VALOR_PE_PRi/100));
        pantalla.setText(pantalla.getText().toString() + "\nCOTA PTV : " + deci_uno.format(COTA_PTV));


        VALOR_P = Double.parseDouble(txt_pe.getText().toString());
        COTA_P = COTA_PIV  + (0.5 * LCV * (VALOR_P/100));
        pantalla.setText(pantalla.getText().toString() + "\nCOTA P : " + deci_uno.format(COTA_P));
        paso_cutro_tabla();
    }





    public void paso_cutro_tabla()
    {
        img_btn.setVisibility(View.VISIBLE);
        lista_double_y.clear();
        lista_double_x.clear();
        tabla_uno.limpiar();
        cabecera();
        double val_km_pcv= KM_PCV;
        double val_desnivel = (Double.parseDouble(txt_pe.getText().toString())   /5   );
        double val_cota_pcv= COTA_PCV;
        double  val_ka = (COTA_P - COTA_PTV)  * (-1 / (Math.pow(numero_estaciones,2)));
        String Kilometraje="";
        Kilometraje += txt_km.getText().toString() + txt_mt.getText().toString();
        Boolean ban=false;


        for (int i = 0; i <= numero_estaciones; i++)
        {
            if ((val_km_pcv > Double.parseDouble(Kilometraje)) && estacion == false && ban == false)
            {
                ban = true;
                double temporal_1=val_desnivel /2;
                double temporal = val_cota_pcv - temporal_1;

                double val_1=0, val_2=0, val_3;
                val_1 = temporal;
                val_2= Double.parseDouble(String.format("%.2f", val_ka * Math.pow((i - 0.5),2)));
                val_3= val_2 + val_1;
                String vec[] = {"", "" + (i - 0.5), "" + (int) (val_km_pcv - 10.0), ""+ temporal_1, ""+ val_1, ""+Math.pow((i - 0.5),2), ""+val_2,  "" + val_3};
                tabla_uno.agregarFilaTabla(vec);
                lista_double_x.add((int) (val_km_pcv - 10.0) );
                lista_double_y.add( ""+ val_3 );

                double temporal_nue = temporal + temporal_1;
                lista_double_x.add( (int) val_km_pcv);
                lista_double_y.add(  String.format("%.2f", val_cota_pcv + val_ka * Math.pow(i, 2)) );
                String vec2[] = {"", "" + i, "" + (int) val_km_pcv, "" + temporal_1, "" + temporal_nue, "" + Math.pow(i, 2), "" + String.format("%.2f", val_ka * Math.pow(i, 2)), "" + String.format("%.2f", val_cota_pcv + val_ka * Math.pow(i, 2))};
                tabla_uno.agregarFilaTabla(vec2);
                val_km_pcv += 20;
                val_cota_pcv += val_desnivel;
            }
            else
            {
                String vec[] = {"", "" + i, "" + (int) val_km_pcv, "" + val_desnivel, "" + val_cota_pcv, "" + Math.pow(i, 2), "" + String.format("%.2f", val_ka * Math.pow(i, 2)), "" + String.format("%.2f", val_cota_pcv + val_ka * Math.pow(i, 2))};
                tabla_uno.agregarFilaTabla(vec);
                lista_double_x.add( (int) val_km_pcv);
                lista_double_y.add(  String.format("%.2f", val_cota_pcv + val_ka * Math.pow(i, 2)) );
                val_km_pcv += 20;
                val_cota_pcv += val_desnivel;
            }
        }

    }

    public void graficar( View view)
    {
        Intent test = new Intent(getApplicationContext(), angel.class);
        test.putExtra("listaX", lista_double_x);
        test.putExtra("listaY", lista_double_y);
        startActivity(test);
    }

    public Double decimal(double val)
    {
      return Double.parseDouble( deci_uno.format(val));
    }
    DecimalFormat deci_uno = new DecimalFormat("0.0");
    DecimalFormat deci_dos = new DecimalFormat("0.00");
    private void cabecera()
    {
        tabla_uno.agregarCabecera(R.array.cabesera_c_vuno);
    }
}
