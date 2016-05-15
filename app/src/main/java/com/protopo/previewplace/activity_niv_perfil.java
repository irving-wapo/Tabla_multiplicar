package com.protopo.previewplace;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TabHost;
import android.widget.TableLayout;
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

public class activity_niv_perfil extends ActionBarActivity implements menu_agregar_perfil.DialogListener,bn_pl_perfil.DialogListener,primer_bn_perfil.DialogListener,cadenamiento_perfil.DialogListener {
    Tabla tabla;
    TabHost TbH;
    int pl=1,punto_edit=0;
    boolean bn_pl,edit,carga;
    ArrayList<String[]> elementos = new ArrayList<String[]>();
    String archivoNombre;

    //Metodos de la clase
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
        pestañas();
        tabla = new Tabla(this, (TableLayout) findViewById(R.id.tabla_perfil));
        Bundle extras = getIntent().getExtras();
        carga = extras.getBoolean("carga");
        archivoNombre = extras.getString("nombre");

        if (carga) {
            cabecera();
        } //  SI ES NUEVO
        else {
            cargar();
        }  // LEYENDO ARCHIVO EXISTENTE


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void pestañas()
    {
        TbH = (TabHost) findViewById(R.id.tabHost); //llamamos al Tabhost
        TbH.setup();                                                         //lo activamos

        TabHost.TabSpec tab1 = TbH.newTabSpec("tab1");  //aspectos de cada Tab (pestaña)
        TabHost.TabSpec tab2 = TbH.newTabSpec("tab2");

        tab1.setIndicator("UNO");    //qué queremos que aparezca en las pestañas
        tab1.setContent(R.id.lnlTabla); //definimos el id de cada Tab (pestaña)

        tab2.setIndicator("DOS");
        tab2.setContent(R.id.lnlGrafica);

        TbH.addTab(tab1); //añadimos los tabs ya programados
        TbH.addTab(tab2);
    }
    //Metodos de operaciones
    private void guardar() //guarda l archivo   "archivo.np"
    {
        //-------------------------- LEE TODA LA TABLA -----------------------
        String nombresito=archivoNombre;
        String texto="";
        for(int i=0; i<elementos.size(); i++ )
        {
            String vec [] = elementos.get(i);
            texto += vec[0] + "," + vec[1] + "," +vec [2]+ "," + vec[3] + "," +vec [4];
            texto += "\n";
        }
        //-------------------------- CREA ARCHIVO Y LO GUARDA -----------------------
        try
        {
            OutputStreamWriter fout= new OutputStreamWriter(openFileOutput( nombresito.concat(".np"), MODE_PRIVATE));
            fout.write(texto);
            fout.close();
            Toast.makeText(getApplicationContext(),R.string.msjGuardar,Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)  { Toast.makeText(getApplicationContext(),R.string.msjError_sistema,Toast.LENGTH_SHORT).show();}
    }

    public void btn_guardar_perfil(View view) {
        guardar();
    }
    public void pdfOnClick_perfil(View view)  //prepara generar nombre.pdf
    {
        String pdf = archivoNombre.concat(".pdf");
        if (generar_pdf(pdf)) {
            abrir_pdf(pdf);
        }
    }
    public void compartirOnClick_perfil(View view)  //   prepara path de pdf
    {
        String pdf = archivoNombre.concat(".pdf");
        if (generar_pdf(pdf)) {
            compartir_pdf(pdf);
        }
    }
    private void compartir_pdf(String path) {
        if (estado()) {
            try {
                File ruta_sd = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                File f = new File(ruta_sd.getAbsolutePath(), path);
                Uri targetUri = Uri.fromFile(f);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("application/pdf");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, targetUri);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.msjEnviar)));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.msjSPDF, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.msjMemoria, Toast.LENGTH_SHORT).show();
        }
    }
    private boolean generar_pdf(String dir) {
        boolean ret = false;
        if (estado()) {
            try {
                Document document = new Document();
                File ruta_sd = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                File f = new File(ruta_sd.getAbsolutePath(), dir);
                FileOutputStream outputStream = new FileOutputStream(f);
                PdfWriter.getInstance(document, outputStream);
                document.open();
                addContent(document);
                document.close();
                ret = true;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                ret = false;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.msjMemoria, Toast.LENGTH_SHORT).show();
        }
        return ret;

    }
    private boolean estado() {
        boolean r = false;


        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            r = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            r = false;
        } else {
            r = false;
        }
        return r;
    }
    private void addContent(Document document)
    {
        try
        {
            PdfPTable tabla = new PdfPTable(5);
            tabla.addCell(getString(R.string.lblpv));
            tabla.addCell(getString(R.string.lblmas));
            tabla.addCell(getString(R.string.lblAltura));
            tabla.addCell(getString(R.string.lblmenos));
            tabla.addCell(getString(R.string.lblCota));
            for (int i = 0; i < elementos.size(); i++)
            {
                String a[] = elementos.get(i);
                for(int j=0;j<5;j++)
                {

                    if(a[j]==null)
                        tabla.addCell("");
                    else
                        tabla.addCell(""+a[j]);
                }

            }
            document.add(tabla);
            // en caso necesario de tener que agregar texto
            /*
            document.add(new Paragraph(getString(R.string.ndResultadop) + sumatoria_p.getText()));
            document.add(new Paragraph(getString(R.string.ndResultadon) + sumatoria_n.getText()));
            document.add(new Paragraph(getString(R.string.ndDesnivel) + Desnivel.getText()));
            */

        } catch (Exception e) { e.printStackTrace();  }
    }
    private void abrir_pdf(String path) {
        if (estado()) {
            try {
                File ruta_sd = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                File f = new File(ruta_sd.getAbsolutePath(), path);
                Uri targetUri = Uri.fromFile(f);
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(targetUri, "application/pdf");
                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.msjOPDF, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.msjMemoria, Toast.LENGTH_SHORT).show();
        }
    }
    private void cargar()
    {
        String nombre_arch = archivoNombre;
        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput(nombre_arch.concat(".np"))));

            leeFichero(fin);
            fin.close();
            calculos();
            tabla.limpiar();
            cabecera();
            for (int i = 0; i < elementos.size(); i++) {
                tabla.agregarFilaTabla(elementos.get(i));
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), R.string.msjError_sistema, Toast.LENGTH_SHORT).show();
        }
    }
    public void leeFichero(BufferedReader br) throws IOException
    {
        //Leemos el fichero
        String linea=br.readLine();
        int pl_c=1;
        while(linea!=null){
            String temp[] = new String[5];
            StringTokenizer st = new StringTokenizer(linea,",");
            int i = 0;
            while (st.hasMoreTokens())
            {
                temp[i]=st.nextToken();
                char a[] = temp[i].toCharArray();
                if(temp[i].equals("null"))
                    temp[i]=null;
                if (a[0]=='P')
                    pl_c++;
                i++;
            }
            elementos.add(temp);
            linea=br.readLine();
        }
        pl=pl_c;
    }
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
                if (ult[0] == alt[0] || ult[0] == 'P' || ult[0] == 'B') {
                    Toast.makeText(getApplicationContext(), R.string.msgJuntos1, Toast.LENGTH_SHORT).show();
                } else
                {
                    elementos.add(temp);
                    if(!bn_pl)
                        pl++;
                }

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
                if(Character.isDigit(ini[0]))
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
    private void valores()
    {
        try
        {
            punto_edit = 0;
            final String[] puntos = new String[elementos.size()];
            for (int i = 0; i < elementos.size(); i++) {
                String[] val = elementos.get(i);
                puntos[i] = val[0];
            }

            NumberPicker myNumberPicker = new NumberPicker(this);
            myNumberPicker.setMinValue(0);
            myNumberPicker.setMaxValue(puntos.length - 1);
            myNumberPicker.setDisplayedValues(puntos);
            NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) { punto_edit = newVal;}
            };
            myNumberPicker.setOnValueChangedListener(myValChangedListener);
            new AlertDialog.Builder(this).setView(myNumberPicker)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    try
                                    {
                                        String [] temp = elementos.get(punto_edit);
                                        char []ini= temp[0].toCharArray();
                                        if(ini[0]=='B'&&ini[2]=='1')
                                        {
                                            DialogFragment banco = new primer_bn_perfil();
                                            banco.show(getSupportFragmentManager(),"bn1");
                                        }
                                        if(ini[0]=='P')
                                        {
                                            DialogFragment punto_liga = new bn_pl_perfil();
                                            punto_liga.show(getSupportFragmentManager(),"punl");
                                            bn_pl=false;
                                        }
                                        if(Character.isDigit(ini[0]))
                                        {
                                            DialogFragment cadenamiento = new cadenamiento_perfil();
                                            cadenamiento.show(getSupportFragmentManager(),"cad");

                                        }
                                        if(ini[0]=='B'&&ini[2]=='2')
                                        {
                                           DialogFragment banco2 = new bn_pl_perfil();
                                           banco2.show(getSupportFragmentManager(),"bn2");
                                           bn_pl = true;
                                        }
                                    } catch (Exception ex) { }
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) { } }).show();
        } catch (Exception ex) {  Toast.makeText(getApplicationContext(), R.string.msgEditar, Toast.LENGTH_SHORT).show();   }
    }
    //metodo que edita los datos
    private void actualizar(String act[])   //desntro de la tabla
    {
        try
        {
            elementos.set(punto_edit, act);
            tabla.limpiar();
            cabecera();
            calculos();
            for (int i = 0; i < elementos.size(); i++) {
                tabla.agregarFilaTabla(elementos.get(i));
            }
            Toast.makeText(getApplicationContext(), R.string.Actualizar, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) { Toast.makeText(getApplicationContext(), R.string.erActualizar, Toast.LENGTH_SHORT).show();  }
    }
    private void eliminar() //celda de la lista
    {
        try
        {
            String temp[] = elementos.get(elementos.size()-1);
            char []ini= temp[0].toCharArray();
            if(ini[0]=='P')
            {
                pl--;
            }
            elementos.remove(elementos.size() - 1);
            tabla.limpiar();
            cabecera();
            calculos();
            for (int i = 0; i < elementos.size(); i++) {
                tabla.agregarFilaTabla(elementos.get(i));
            }
            Toast.makeText(getApplicationContext(), R.string.msjEliminarT, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) { Toast.makeText(getApplicationContext(), R.string.msjEliminarF, Toast.LENGTH_SHORT).show();  }
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
            edit = true;
            valores();
            return true;
        } else if (id == R.id.itmEliminar) {
            eliminar();
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
                String tt[] = elementos.get(punto_edit);
                temp[0] = tt[0];
                actualizar(temp);
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
                String tt[] = elementos.get(punto_edit);
                temp[0] = tt[0];
                actualizar(temp);
            } else {
                temp[0] = "PL" + pl;
                filabn_pl(temp);

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
            actualizar(temp);
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
            actualizar(temp);
        }
        else
            filaCadenamiento(temp);

    }
    @Override
    public void cadenamientoNegativeClick(DialogFragment dialog) {

    }
}