package com.protopo.previewplace;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class activity_niv_perfil extends AppCompatActivity implements menu_agregar_perfil.DialogListener,bn_pl_perfil.DialogListener,primer_bn_perfil.DialogListener,cadenamiento_perfil.DialogListener {
    Tabla tabla;
    boolean bn_pl,edit;
    ArrayList<String[]> elementos = new ArrayList<String[]>();
    String archivoNombre;
    Boolean carga;

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
        tabla.limpiar();
        cabecera();
        for (int i = 0; i < elementos.size(); i++) {
            tabla.agregarFilaTabla(elementos.get(i));
        }
    }
    //menu superior ...(Tres puntitos)
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.itmAgregar)
        {
            edit=true;
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
                bn_pl=true;
                break;
            case 3:
                DialogFragment pl = new bn_pl_perfil();
                pl.show(getSupportFragmentManager(), "bnf");
                bn_pl=false;
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    //Dialogo de datos punto de liga y banco de nivel final
    @Override
    public void bn_plPositiveClick(DialogFragment dialog, Double valor) {

    }

    @Override
    public void bn_plNegativeClick(DialogFragment dialog) {

    }


    //Dialogo de datos del primer banco de nivel
    @Override
    public void primerPositiveClick(DialogFragment dialog, Double valor, Double valor1) {
        String[] temp = new String[5];
        temp[0] = "BN1";
        temp[1] = String.valueOf(valor);
        temp[2] = String.valueOf(valor+valor1);
        temp[3] = null;
        temp[4] = String.valueOf(valor1);
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

    }

    @Override
    public void cadenamientoNegativeClick(DialogFragment dialog) {

    }
}