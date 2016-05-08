package com.protopo.previewplace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import java.util.ArrayList;

public class activity_niv_perfil extends AppCompatActivity {
    Tabla tabla;
    ArrayList<String[]> elementos = new ArrayList<String[]>();
    String archivoNombre;
    Boolean carga;

    //carga menu de opciones
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

    public void btn_cerrar_perf(View view) {
        onBackPressed();
    }

    private void cabecera()// cabecera de la tabla
    {
        tabla.agregarCabecera(R.array.cabecera_tabla_perfil);
    }



}