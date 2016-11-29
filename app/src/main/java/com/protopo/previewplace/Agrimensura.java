package com.protopo.previewplace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Agrimensura extends AppCompatActivity
{


    Tabla_agrimensura tabla;
    Button continuar;
    Tabla_agrimensura tabla_agri_11;
    EditText rumbo_txt, val_ene, ang_int, dist, hola_NE;
    TextView pantalla;
    Button btn_ok;
    int n = 0;
    double tetaGrad[], tetaRad[], tetaCor[], r[], rAcc[], tetaAcc[], alfa[], d[], x[], y[];
    double perimetro = 0, tetaSuma = 0,   rumbo, signo, prteDosA, prteDosD, error, errorX, errorY, area = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        rumbo = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrimensura);
        tabla = new Tabla_agrimensura(1, getSupportFragmentManager(), this, (TableLayout) findViewById(R.id.tablta));

        pantalla = (TextView) findViewById(R.id.salida);
        ang_int = (EditText) findViewById(R.id.txt3);
        hola_NE = (EditText) findViewById(R.id.hola);
        dist = (EditText) findViewById(R.id.txt4);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        rumbo_txt = (EditText) findViewById(R.id.txt1);
        val_ene = (EditText) findViewById(R.id.txt2);
        rumbo = Double.parseDouble(rumbo_txt.getText().toString());
        rumbo = rumbo * Math.PI / 180;
        n = Integer.parseInt(val_ene.getText().toString());
        pantalla.setText("");
    }


    public void bnt_ok(View view)
    {
        tetaGrad = new double[n];
        tetaRad = new double[n];
        tetaCor = new double[n];
        r = new double[n];
        rAcc = new double[n];
        tetaAcc = new double[n];
        alfa = new double[n];
        d = new double[n];
        x = new double[n + 1];
        y = new double[n + 1];
        pantalla.setText("\nMatrices creadas correctamente... ");
    }


    ArrayList<Double> list_X = new ArrayList<Double>();
    ArrayList<Double> list_Y = new ArrayList<Double>();

    int contador =0;
    public void bnt_add(View view)
    {
        if(contador == 0) {ang_int.setText("93.5"); dist.setText("85");}
        if(contador == 1) {ang_int.setText("100"); dist.setText("51.8");}
        if(contador == 2) {ang_int.setText("132.5"); dist.setText("71.5");}
        if(contador == 3) {ang_int.setText("126.25"); dist.setText("57.30");}

            if (list_X.size() < n)
            {
                list_X.add(Double.parseDouble(ang_int.getText().toString()));
                list_Y.add(Double.parseDouble(dist.getText().toString()));

            }
            else
            {
                //Toast.makeText(getApplicationContext(), "Lista llena. ", Toast.LENGTH_LONG).show();
                pantalla.setText("");
                String envio = "" + hola_NE.getText().toString();
                llama_metodo(envio);
            }
        contador++;
        muestra_valores();
    }



    public void muestra_valores()
    {
        for (int i = 0; i < list_X.size(); i++)
        {
            String vec[] = {"", "",""  };
           // tabla_tres.agregarFilaTabla(vec);
        }

    }

    //private void cabecera_dos() { tabla_dos.agregarCabecera(R.array.cabecera_nueva);  }

    public void llama_metodo(String valorRumbo)
    {
        pantalla.setText("\nMatrices creadas correctamente... ");
        pantalla.setText(pantalla.getText().toString()+ "\nSentido establecido: "+valorRumbo);

        switch (valorRumbo)
        {
            case "NE":
            {
                alfa[0] = Math.PI / 2 - rumbo; // Primer valor de alfa para rumbo NE
            } break;

            case "NW":
            {
                alfa[0] = Math.PI / 2 + rumbo; // Primer valor de alfa para rumbo NW
            }
                break;

            case "SW":
            {
                alfa[0] = 3 * Math.PI / 2 - rumbo; // Primer valor de alfa para rumbo SW
                pantalla.setText("El rumbo ingresado es SW");
            }
                break;

            case "SE":
            {
                alfa[0] = 3 * Math.PI / 2 + rumbo; // Primer valor de alfa para rumbo SE
            }
                break;

            default:
            {   pantalla.setText("La opción elegida es incorrecta.");}
        }



                for (int i = 0; i < n; i++)
                {
                    tetaGrad[i] = list_X.get(i);
                    tetaRad[i] = tetaGrad[i] * Math.PI / 180;
                    double entera = Math.round(tetaRad[i]);
                    double decimal = tetaRad[i] - entera;
                    decimal = decimal * 10000;
                    decimal = Math.round(decimal);
                    decimal = decimal / 10000;
                    tetaRad[i] = entera + decimal;
                    tetaSuma += tetaRad[i];
                    r[i] = list_Y.get(i);
                    perimetro += r[i];
                }


                for (int i = 0; i < n; i++)
                {
                    tetaCor[i] = tetaRad[i] + ((n - 2) * Math.PI - tetaSuma) / n;
                }

                rAcc[0] = r[0];
                rAcc[1] = r[1] + rAcc[0];
                tetaAcc[0] = 0;
                tetaAcc[1] = tetaAcc[0] + tetaCor[1];
                d[0] = r[0];

                prteDosD = tetaAcc[1];
                d[1] = Math.sqrt(Math.pow(d[0], 2) + Math.pow(r[1], 2) - 2 * d[0] * r[1] * Math.cos(prteDosD));
                double val1 = tetaCor[1];
                double val2 = 3 * Math.PI / 2;

                if (val1 == val2)
                {
                    alfa[1] = alfa[0];
                }
                else
                {
                    signo = (3 * Math.PI / 2 - tetaCor[1]) / Math.abs(3 * Math.PI / 2 - tetaCor[1]);
                    prteDosA = (Math.pow(d[0], 2) + Math.pow(d[1], 2) - Math.pow(r[1], 2)) / (2 * d[0] * d[1]);
                    alfa[1] = alfa[0] - signo * Math.acos(prteDosA);
                }


                for (int i = 2; i < n; i++)
                {
                    rAcc[i] = r[i] + rAcc[i - 1];
                    tetaAcc[i] = tetaAcc[i - 1] + tetaCor[i];
                    prteDosD = tetaAcc[i] - alfa[i - 1] + alfa[0] - (i - 1) * Math.PI;
                    d[i] = Math.sqrt(Math.pow(d[i - 1], 2) + Math.pow(r[i], 2) - 2 * d[i - 1] * r[i] * Math.cos(prteDosD)); // aplicando la fórmula de d

                    val1 = tetaCor[i];
                    if (val1 == val2)
                    {
                        alfa[i] = alfa[i - 1];
                    }
                    else
                    {
                        signo = (3 * Math.PI / 2 - tetaCor[i]) / Math.abs(3 * Math.PI / 2 - tetaCor[i]);
                        prteDosA = (Math.pow(d[i - 1], 2) + Math.pow(d[i], 2) - Math.pow(r[i], 2)) / (2 * d[i - 1] * d[i]);
                        alfa[i] = alfa[i - 1] - signo * Math.acos(prteDosA);
                    }

                }

                error = d[n - 1];
                errorX = error * Math.cos(alfa[n - 1]);
                errorY = error * Math.sin(alfa[n - 1]);

                for (int i = 1; i < n + 1; i++)
                {
                    x[0] = 0;
                    y[0] = 0;
                    x[i] = d[i - 1] * Math.cos(alfa[i - 1]) - errorX * rAcc[i - 1] / perimetro;
                    y[i] = d[i - 1] * Math.sin(alfa[i - 1]) - errorY * rAcc[i - 1] / perimetro;
                }

                for (int i = 0; i < n; i++)
                {
                    area += (x[i] * y[i + 1] - x[i + 1] * y[i]) / 2;
                }
                area = Math.abs(area); // Valor final del área de la poligonal

                // --------------------- Salida de datos ------------------------//
                // --------------------- Salida de datos ------------------------//


                pantalla.setText(pantalla.getText().toString() + "\n\nAbscisas para graficar: ");
                for (double fila : x)
                {
                    pantalla.setText(pantalla.getText().toString() +"\n"+ fila + "    ");
                }

                pantalla.setText(pantalla.getText().toString() + "\n\nOrdenadas para graficar");
                for (double fila : y)
                {
                    pantalla.setText(pantalla.getText().toString() +"\n"+fila + " ");
                }
                pantalla.setText(pantalla.getText().toString() + "\n\nÁrea = " + area);
        }


    }
