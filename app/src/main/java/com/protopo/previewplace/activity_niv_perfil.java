package com.protopo.previewplace;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class activity_niv_perfil extends AppCompatActivity {
    int opc = 0, bn = 2, pl = 1;
    boolean bn_pl, edit;
    Tabla tabla;
    ArrayList<String[]> elementos = new ArrayList<String[]>();
    int punto_edit = 0;
    TextView sumatoria_p;
    TextView sumatoria_n;
    TextView Desnivel;
    String archivoNombre;
    Boolean carga;


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

    //*********************************************    A R C H I V O S    **********************************************************



    public void btn_cerrar_perf(View view) {
        onBackPressed();
    }


    private void cabecera()// cabecera de la tabla
    {
        tabla.agregarCabecera(R.array.cabecera_tabla);
    }
    //*********************************************    A R C H I V O S    **********************************************************


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
}


