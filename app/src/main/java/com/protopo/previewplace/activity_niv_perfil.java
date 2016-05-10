package com.protopo.previewplace;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class activity_niv_perfil extends AppCompatActivity implements menu_agregar_perfil.DialogListener,bn_pl_perfil.DialogListener,primer_bn_perfil.DialogListener,cadenamiento_perfil.DialogListener {
    Tabla tabla;
    int pl=1;
    boolean bn_pl,edit,carga;
    ArrayList<String[]> elementos = new ArrayList<String[]>();
    String archivoNombre;


    //carga menu de opciones
    //Metodos de la clase
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_superior, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)     //al abrir la app "LOAD"
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niv_perfil);
        tabla = new Tabla(this, (TableLayout) findViewById(R.id.tabla_perfil));
        Bundle extras = getIntent().getExtras();
        carga = extras.getBoolean("carga");
        archivoNombre = extras.getString("nombre");
        if (carga) {
            cabecera();
        } //  SI ES NUEVO
        else {
            //cargar();
        }  // LEYENDO ARCHIVO EXISTENTE
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    //--------------------------------------------------------------------------

    //Metodos de operaciones
    private void filaBN1(String [] temp)
    {
        if(elementos.isEmpty())
            elementos.add(0,temp);
        else
            Toast.makeText(getApplicationContext(), R.string.msgBN1, Toast.LENGTH_SHORT).show();
        calculos();
        tabla.limpiar();
        cabecera();
        for (int i = 0; i < elementos.size(); i++) {
            tabla.agregarFilaTabla(elementos.get(i));
        }
    }
    //Metodo agregar cadenamiento
    private void filaCadenamiento(String [] temp)
    {
        try {
            String [] tempbn = elementos.get(elementos.size()-1);
            if (!elementos.isEmpty()&&tempbn[0]!="BN2") {
                elementos.add(temp);
                calculos();
                tabla.limpiar();
                cabecera();
                for (int i = 0; i < elementos.size(); i++) {
                    tabla.agregarFilaTabla(elementos.get(i));
                }
            } else
                Toast.makeText(getApplicationContext(), R.string.msgFaltaBN1, Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), R.string.msgFaltaBN1, Toast.LENGTH_SHORT).show();
        }

    }
    // agregar banco de nivel final o punto de liga
    private void filabn_pl(String[] temp)
    {
        try {
            String[] bnf = elementos.get(elementos.size() - 1);
            if (!elementos.isEmpty() && bnf[0] != "BN1") {
                String[] comp = elementos.get(elementos.size() - 1);
                char[] ult = comp[0].toCharArray();
                char[] alt = temp[0].toCharArray();
                if (ult[0] == alt[0] && ult[0] == 'P') {
                    Toast.makeText(getApplicationContext(), R.string.msgJuntos1, Toast.LENGTH_SHORT).show();
                } else
                    elementos.add(temp);
            } else
                Toast.makeText(getApplicationContext(), R.string.msgFaltaBN1, Toast.LENGTH_SHORT).show();
            calculos();
            tabla.limpiar();
            cabecera();
            for (int i = 0; i < elementos.size(); i++) {
                tabla.agregarFilaTabla(elementos.get(i));
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), R.string.msgFaltaBN1, Toast.LENGTH_SHORT).show();
        }

    }
    public void calculos()
    {
        double altura=0,cota=0;
        try
        {
            for(int i = 0; i<elementos.size();i++)
            {
                String [] temp = elementos.get(i);
                char []ini= temp[0].toCharArray();
                if(ini[0]=='B'&&ini[2]=='1')
                {
                    altura=Double.valueOf(temp[1])+Double.valueOf(temp[4]);
                    temp[2]=dos(altura);
                    elementos.set(i,temp);
                }
                if(ini[0]=='P')
                {
                    altura=cota+Double.valueOf(temp[1]);
                    temp[2]=dos(altura);
                    elementos.set(i,temp);
                }
                if(ini[0]=='0')
                {

                    cota=altura-Double.valueOf(temp[3]);
                    temp[4]=dos(cota);
                    elementos.set(i,temp);
                }
                if(ini[0]=='B'&&ini[2]=='2')
                {
                    altura=cota+Double.valueOf(temp[1]);
                    temp[2]=dos(altura);
                    temp[4]=dos(cota);
                    elementos.set(i,temp);
                }
            }
        }catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),R.string.msjError_tipos,Toast.LENGTH_SHORT).show();
        }

    }
    // Formateo de numero a dos decimales
    public String dos(double numero) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(numero);
    }

    //menu superior ...(Tres puntitos)
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.itmAgregar)
        {
            edit=false;
            DialogFragment nuevo = new menu_agregar_perfil();
            nuevo.show(getSupportFragmentManager(), "nuevo");
            return true;
        } else if (id == R.id.itmEditar) {

            return true;
        } else if (id == R.id.itmEliminar) {


            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void btn_cerrar_perf(View view) {
        onBackPressed();
    }
    private void cabecera()// cabecera de la tabla
    {
        tabla.agregarCabecera(R.array.cabecera_tabla_perfil);
    }


    //Implementacion de metodos de los dialogos

    //Dialogo de menu de opciones
    @Override
    public void onSingleChoiceItems(DialogFragment dialog, int arg) {
        switch (arg)
        {
            case 0:
                DialogFragment bn1 = new primer_bn_perfil();
                bn1.show(getSupportFragmentManager(), "bn1");
                break;
            case 1:
                DialogFragment bnpl = new cadenamiento_perfil();
                bnpl.show(getSupportFragmentManager(), "bnpl");
                break;
            case 2:
                DialogFragment bnf = new bn_pl_perfil();
                bnf.show(getSupportFragmentManager(), "pl");
                bn_pl=false;
                break;
            case 3:
                DialogFragment pl = new bn_pl_perfil();
                pl.show(getSupportFragmentManager(), "bnf");
                bn_pl=true;
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    //Dialogo de datos punto de liga y banco de nivel final
    @Override
    public void bn_plPositiveClick(DialogFragment dialog, Double valor) {
        if (bn_pl == true)
        {
            String[] temp = new String[5];
            temp[1] = dos(valor);
            if (edit) {
                //String tt[] = elementos.get(punto_edit);
                //temp[0] = tt[0];
                //actualizar(temp);
            } else {
                temp[0] = "BN2";
                filabn_pl(temp);

            }
        }
        else
        {
            String[] temp = new String[5];
            temp[1] = dos(valor);
            if (edit) {
                //String tt[] = elementos.get(punto_edit);
                //temp[0] = tt[0];
                //actualizar(temp);
            } else {
                temp[0] = "PL" + pl;
                filabn_pl(temp);
                pl++;
            }
        }
    }
    @Override
    public void bn_plNegativeClick(DialogFragment dialog) {

    }
    //Dialogo de datos del primer banco de nivel
    @Override
    public void primerPositiveClick(DialogFragment dialog, Double valor, Double valor1) {
        String[] temp = new String[5];
        temp[0] = "BN1";
        temp[1] = dos(valor);
        temp[2] = null;
        temp[3] = null;
        temp[4] = dos(valor1);
        if (edit)
        {
            //actualizar(temp);
        }
        else
            filaBN1(temp);
    }
    @Override
    public void primerNegativeClick(DialogFragment dialog) {

    }
    //Dialogo de datos al agregar un punto de cadenamiento
    @Override
    public void cadenamientoPositiveClick(DialogFragment dialog, int valor, int valor1, double valor2) {
        String[] temp = new String[5];
        temp[0] = valor+"+"+valor1;
        temp[1] = null;
        temp[2] = null;
        temp[3] = dos(valor2);
        temp[4] = null;
        if (edit)
        {
            //actualizar(temp);
        }
        else
            filaCadenamiento(temp);

    }
    @Override
    public void cadenamientoNegativeClick(DialogFragment dialog) {

    }
}