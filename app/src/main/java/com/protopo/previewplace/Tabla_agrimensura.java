package com.protopo.previewplace;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.itextpdf.awt.geom.CubicCurve2D;

import java.util.ArrayList;

/**
 * Created by Francisco on 05/03/2016.
 */
public class Tabla_agrimensura {

    private TableLayout tabla;          // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas;  // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private FragmentManager supportFragmentManager;
    private int FILAS, COLUMNAS,padre;  // Filas y columnas de nuestra tabla
    ArrayList<String[]> elementos2 = new ArrayList<String[]>();
    public Tabla_agrimensura(int padre, FragmentManager supportFragmentManager, Activity actividad, TableLayout tabla)
    {
        this.actividad = actividad;
        this.tabla = tabla;
        rs = this.actividad.getResources();
        this.supportFragmentManager = supportFragmentManager;
        FILAS = COLUMNAS = 0;
        this.padre = padre;
        filas = new ArrayList<TableRow>();
    }
    /*
     * Añade la cabecera a la tabla
     * @param recursocabecera Recurso (array) donde se encuentra la cabecera de la tabla
     */


    public void agregarCabecera(int recursocabecera)
    {

        TableRow.LayoutParams layoutCelda;
        TableRow fila = new TableRow(actividad);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(layoutFila);

        String[] arraycabecera = rs.getStringArray(recursocabecera);
        COLUMNAS = arraycabecera.length;

        for(int i = 0; i < arraycabecera.length; i++)
        {
            TextView texto = new TextView(actividad);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(arraycabecera[i]), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setText(arraycabecera[i]);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            texto.setTextAppearance(actividad, R.style.estilo_celda);
            texto.setBackgroundResource(R.drawable.tabla_celda_cabecera);
            texto.setLayoutParams(layoutCelda);
            texto.setTextColor(Color.BLACK);

            fila.addView(texto);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }

    public void limpiar()
    {
        tabla.removeAllViews();
        elementos2.clear();
    }

    /**
     * Agrega una fila a la tabla
     * @param elementos Elementos de la fila
     */

    public void agregarFilaTabla(final String [] elementos)
    {
        elementos2.add(elementos);
        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);
        for(int i = 0; i< elementos.length; i++)
        {
            final TextView texto = new TextView(actividad);
            if(elementos[i] == null)
            {
                texto.setText(String.valueOf("---"));
            }
            else
            {
                texto.setText(String.valueOf(elementos[i]));
            }
            if(i==0)
            {
                texto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String a[]=null;
                        int punto_edit=0;
                        for(int i = 0;i<elementos2.size();i++)
                        {
                            a = elementos2.get(i);
                            if(a[0].equals(texto.getText().toString()))
                            {
                                punto_edit = i;
                                break;
                            }
                        }
                        switch (padre)
                        {
                            case 0 :
                                char []ini= a[0].toCharArray();
                                activity_niv_perfil.edit = true;
                                activity_niv_perfil.punto_edit=punto_edit;
                                if(ini[0]=='B'&&ini[2]=='1')
                                {
                                    DialogFragment banco = new primer_bn_perfil();
                                    banco.show(supportFragmentManager,"bn1");
                                }
                                if(ini[0]=='P')
                                {
                                    DialogFragment punto_liga = new bn_pl_perfil();
                                    punto_liga.show(supportFragmentManager,"punl");
                                    activity_niv_perfil.bn_pl=false;
                                }
                                if(Character.isDigit(ini[0]))
                                {
                                    DialogFragment cadenamiento = new cadenamiento_perfil();
                                    cadenamiento.show(supportFragmentManager,"cad");

                                }
                                if(ini[0]=='B'&&ini[2]=='2')
                                {
                                    DialogFragment banco2 = new bn_pl_perfil();
                                    banco2.show(supportFragmentManager,"bn2");
                                    activity_niv_perfil.bn_pl = true;
                                }
                                break;
                            case 1:
                                niv_dif_cont.edit = true;
                                niv_dif_cont.punto_edit=punto_edit;
                                if (punto_edit == 0) {
                                    DialogFragment bn1 = new primer_bn();
                                    bn1.show(supportFragmentManager, "bn1");
                                } else if ((punto_edit == elementos2.size() - 1) && (a[1] == null)) {
                                    DialogFragment bnf = new ultimo_bn();
                                    bnf.show(supportFragmentManager, "bnf");
                                }
                                else
                                {
                                    char ea[] = a[0].toCharArray();
                                    DialogFragment bnpl = new bn_pl();
                                    bnpl.show(supportFragmentManager, "bnpl");
                                    if (ea[0] == 'B')
                                        niv_dif_cont.bn_pl = true;
                                    else
                                        niv_dif_cont.bn_pl = false;
                                }

                                break;

                            default:

                                break;

                        }


                    }
                });


                texto.setBackgroundResource(R.drawable.tabla_celda_edit);

            }
            else
            {
                texto.setBackgroundResource(R.drawable.tabla_celda);
            }
            texto.setTextAppearance(actividad, R.style.estilo_celda);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(texto.getText().toString()), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setLayoutParams(layoutCelda);
            texto.setId(i);
            fila.addView(texto);
        }
        tabla.addView(fila);
        filas.add(fila);
        FILAS++;
    }

    private int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(35);
        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }


}
