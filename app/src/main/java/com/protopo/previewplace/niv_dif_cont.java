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

public class niv_dif_cont extends AppCompatActivity implements menu_agregar_dif.DialogListener, primer_bn.DialogListener, bn_pl.DialogListener, ultimo_bn.DialogListener {
    int  bn = 2, pl = 1;
    static boolean bn_pl, edit;
    Tabla tabla;
    ArrayList<String[]> elementos = new ArrayList<String[]>();
    static int punto_edit = 0;
    TextView sumatoria_p;
    TextView sumatoria_n;
    TextView Desnivel;
    String archivoNombre;
    Boolean carga;


    @Override
    protected void onCreate(Bundle savedInstanceState)     //al abrir la app "LOAD"
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niv_dif_cont);
        tabla = new Tabla(1,getSupportFragmentManager(), this, (TableLayout) findViewById(R.id.tabla));
        sumatoria_p = (TextView) findViewById(R.id.lblSumatoria_p);
        sumatoria_n = (TextView) findViewById(R.id.lblSumatoria_n);
        Desnivel = (TextView) findViewById(R.id.lblDesnivel);
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

    //*********************************************    A R C H I V O S    **********************************************************


    // enrruta para leer "archivo.nd"

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void cargar() {
        String nombre_arch = archivoNombre;
        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput(nombre_arch.concat(".nd"))));

            leeFichero(fin);
            fin.close();
            tabla.limpiar();
            cabecera();
            for (int i = 0; i < elementos.size(); i++) {
                tabla.agregarFilaTabla(elementos.get(i));
            }
            calculos();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), R.string.msjError_sistema, Toast.LENGTH_SHORT).show();
        }
    }

    public void pdfOnClick(View view)  //prepara generar nombre.pdf
    {
        String pdf = archivoNombre.concat(".pdf");
        if (generar_pdf(pdf)) {
            abrir_pdf(pdf);
        }
    }

    public void compartirOnClick(View view)  //   prepara path de pdf
    {
        String pdf = archivoNombre.concat(".pdf");
        if (generar_pdf(pdf)) {
            compartir_pdf(pdf);
        }
    }

    // cabecera de la tabla
    private void cabecera() {
        tabla.agregarCabecera(R.array.cabecera_tabla_diferencial);
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


    public void guardar() //guarda l archivo   "arrchivo.nd"
    {
        //-------------------------- LEE TODA LA TABLA -----------------------
        String nombresito=archivoNombre;
        String texto="";
        for(int i=0; i<elementos.size(); i++ )
        {
            String vec [] = elementos.get(i);
            texto += vec[0] + "," + vec[1] + "," +vec [2];
            texto+= "\n";
        }

        //-------------------------- CREA ARCHIVO Y LO GUARDA -----------------------
        try
        {
            OutputStreamWriter fout= new OutputStreamWriter(openFileOutput( nombresito.concat(".nd"), MODE_PRIVATE));
            fout.write(texto);
            fout.close();
            Toast.makeText(getApplicationContext(),R.string.msjGuardar,Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)  { Toast.makeText(getApplicationContext(),R.string.msjError_sistema,Toast.LENGTH_SHORT).show();}
    }

    public void btn_guardar(View view) {
        guardar();
    }

    public void btn_cerrar(View view){
        onBackPressed();
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
                Toast.makeText(getApplicationContext(), R.string.msjPdf, Toast.LENGTH_SHORT).show();
                ret = false;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.msjMemoria, Toast.LENGTH_SHORT).show();
        }
        return ret;

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

    public void leeFichero(BufferedReader br) throws IOException
    {
        //Leemos el fichero
        String linea=br.readLine();
        int bn_c=1,pl_c=1;
        if(linea==null)
            bn_c=2;
        while(linea!=null){
            String temp[] = new String[3];
            StringTokenizer st = new StringTokenizer(linea,",");
            int i = 0;
            while (st.hasMoreTokens())
            {
                temp[i]=st.nextToken();
                char a[] = temp[i].toCharArray();
                if(temp[i].equals("null"))
                    temp[i]=null;
                if(a[0]=='B')
                    bn_c++;
                else if (a[0]=='P')
                    pl_c++;
                i++;
            }
            elementos.add(temp);
            linea=br.readLine();
        }
        bn=bn_c;
        pl=pl_c;
    }
    //*********************************************    A R C H I V O S    **********************************************************


    private void addContent(Document document)
    {
        try
        {
            PdfPTable tabla = new PdfPTable(3);
            tabla.addCell(getString(R.string.lblpv));
            tabla.addCell(getString(R.string.lblmas));
            tabla.addCell(getString(R.string.lblmenos));
            for (int i = 0; i < elementos.size(); i++)
            {
                String a[] = elementos.get(i);
                for(int j=0;j<3;j++)
                {

                    if(a[j]==null)
                        tabla.addCell("");
                    else
                        tabla.addCell(""+a[j]);
                }
            }

            document.add(tabla);
            document.add(new Paragraph(getString(R.string.ndResultadop) + sumatoria_p.getText()));
            document.add(new Paragraph(getString(R.string.ndResultadon) + sumatoria_n.getText()));
            document.add(new Paragraph(getString(R.string.ndDesnivel) + Desnivel.getText()));

        } catch (DocumentException e) { e.printStackTrace();  }
    }


    // Formateo de numero a dos decimales
    public String dos(double numero) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(numero);
    }


    // agregar fila de banco inicial
    private void filaBN1(String[] temp) {
        if (elementos.isEmpty())
            elementos.add(0, temp);
        else
            Toast.makeText(getApplicationContext(), R.string.msgBN1, Toast.LENGTH_SHORT).show();
        tabla.limpiar();
        cabecera();
        for (int i = 0; i < elementos.size(); i++) {
            tabla.agregarFilaTabla(elementos.get(i));
        }
        calculos();
    }

    // agregar banco de nivel intermedio o punto de liga
    private void filabn_pl(String[] temp)
    {
        try {
            String[] bnf = elementos.get(elementos.size() - 1);
            if (!elementos.isEmpty() && bnf[1] != null) {
                String[] comp = elementos.get(elementos.size() - 1);
                char[] ult = comp[0].toCharArray();
                char[] alt = temp[0].toCharArray();
                if (ult[0] == alt[0] && ult[0] == 'B') {
                    Toast.makeText(getApplicationContext(), R.string.msgJuntos, Toast.LENGTH_SHORT).show();
                    bn--;
                } else
                    elementos.add(temp);
            } else
                Toast.makeText(getApplicationContext(), R.string.msgFaltaBN1, Toast.LENGTH_SHORT).show();
            tabla.limpiar();
            cabecera();
            for (int i = 0; i < elementos.size(); i++) {
                tabla.agregarFilaTabla(elementos.get(i));
            }
            calculos();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), R.string.msgFaltaBN1, Toast.LENGTH_SHORT).show();
        }

    }

    //banco de nivel final
    private void bn_final(String[] temp)
    {
        try
        {
            String[] bnf = elementos.get(elementos.size() - 1);
            char[] ult = bnf[0].toCharArray();

            if (!(elementos.isEmpty()) && (ult[0] != 'B') && (bnf[1] != null))
                elementos.add(elementos.size(), temp);
            else
                Toast.makeText(getApplicationContext(), R.string.msgBNF, Toast.LENGTH_SHORT).show();
            tabla.limpiar();
            cabecera();
            for (int i = 0; i < elementos.size(); i++) {
                tabla.agregarFilaTabla(elementos.get(i));
            }
            calculos();
        }
        catch (Exception ex) { Toast.makeText(getApplicationContext(), R.string.msgBNF, Toast.LENGTH_SHORT).show(); }
    }

    //carga menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_superior, menu);
        return true;
    }


    //evento click p0sitivo del dialogo de punto de liga o banco de nivel intermedio
    @Override
    public void bn_plPositiveClick(DialogFragment dialog, Double atras, Double adelante)
    {
        if (bn_pl == true)
        {
            String[] temp = new String[3];
            temp[1] = String.valueOf(atras);
            temp[2] = String.valueOf(adelante);
            if (edit) {
                String tt[] = elementos.get(punto_edit);
                temp[0] = tt[0];
                actualizar(temp);
            } else {
                temp[0] = "BN" + bn;
                filabn_pl(temp);
                bn++;
            }
        }
        else
        {
            String[] temp = new String[3];
            temp[1] = String.valueOf(atras);
            temp[2] = String.valueOf(adelante);
            if (edit) {
                String tt[] = elementos.get(punto_edit);
                temp[0] = tt[0];
                actualizar(temp);
            } else {
                temp[0] = "PL" + pl;
                filabn_pl(temp);
                pl++;
            }
        }

    }


    //evento click negativo del dialogo de punto de liga o banco de nivel intermedio
    @Override
    public void bn_plNegativeClick(DialogFragment dialog) { }

    //evento itemSelect del menu superior
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.itmAgregar)
        {
            edit = false;
            DialogFragment nuevo = new menu_agregar_dif();
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

    //dialogo de edicion (Lista de puntos). editar la tabla
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
                                        String a[] = elementos.get(punto_edit);
                                        if (punto_edit == 0) {
                                            DialogFragment bn1 = new primer_bn();
                                            bn1.show(getSupportFragmentManager(), "bn1");
                                        } else if ((punto_edit == elementos.size() - 1) && (a[1] == null)) {

                                            DialogFragment bnf = new ultimo_bn();
                                            bnf.show(getSupportFragmentManager(), "bnf");
                                        }
                                        else
                                        {
                                            char ea[] = puntos[punto_edit].toCharArray();
                                            DialogFragment bnpl = new bn_pl();
                                            bnpl.show(getSupportFragmentManager(), "bnpl");
                                            if (ea[0] == 'B')
                                                bn_pl = true;
                                            else
                                                bn_pl = false;
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

    private void calculos()
    {
        try {
            Double positivo = 0.0, negativo = 0.0, des = 0.0;
            for (int i = 0; i < elementos.size(); i++)
            {
                String a[] = elementos.get(i);
                if (a[1] != null)
                    positivo += Double.valueOf(a[1]);
                if (a[2] != null)
                    negativo += Double.valueOf(a[2]);
            }
            des = negativo - positivo;
            sumatoria_p.setText(String.valueOf(dos(positivo)));
            sumatoria_n.setText(String.valueOf(dos(negativo)));
            Desnivel.setText(String.valueOf(dos(des)));
        } catch (Exception e) {  e.printStackTrace();  }
    }

    //metodo que edita los datos
    private void actualizar(String act[])   //desntro de la tabla
    {
        try
        {
            elementos.set(punto_edit, act);
            tabla.limpiar();
            cabecera();
            for (int i = 0; i < elementos.size(); i++) {
                tabla.agregarFilaTabla(elementos.get(i));
            }
            calculos();
            Toast.makeText(getApplicationContext(), R.string.Actualizar, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) { Toast.makeText(getApplicationContext(), R.string.erActualizar, Toast.LENGTH_SHORT).show();  }
    }

    private void eliminar() //celda de la lista
    {
        try
        {
            String temp[] = elementos.get(elementos.size()-1);
            if(temp[0]!="BN1")
            {
                char a[] = temp[0].toCharArray();
                if(a[0]=='B')
                    bn--;
                else if(a[0]=='P')
                    pl--;
            }
            elementos.remove(elementos.size() - 1);
            tabla.limpiar();
            cabecera();
            for (int i = 0; i < elementos.size(); i++) {
                tabla.agregarFilaTabla(elementos.get(i));
            }
            calculos();
            Toast.makeText(getApplicationContext(), R.string.msjEliminarT, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) { Toast.makeText(getApplicationContext(), R.string.msjEliminarF, Toast.LENGTH_SHORT).show();  }
    }

    //boton positivo del primer banco de nivel
    @Override
    public void primerPositiveClick(DialogFragment dialog, Double valor)
    {
        String[] temp = new String[3];
        temp[0] = "BN1";
        temp[1] = String.valueOf(valor);
        temp[2] = null;
        if (edit)
            actualizar(temp);
        else
            filaBN1(temp);
    }

    // boton negativo del primer banco de nivel
    @Override
    public void primerNegativeClick(DialogFragment dialog) { }
    // boton negativo del dialogo agregar
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { }

    //evento de seleccion del dialogo agregar
    @Override
    public void onSingleChoiceItems(DialogFragment dialog, int arg) {
        switch (arg)
        {
            case 0:
                DialogFragment bn1 = new primer_bn();
                bn1.show(getSupportFragmentManager(), "bn1");
                break;
            case 1:
                DialogFragment bnpl = new bn_pl();
                bnpl.show(getSupportFragmentManager(), "bnpl");
                bn_pl = true;
                break;
            case 2:
                DialogFragment bnf = new ultimo_bn();
                bnf.show(getSupportFragmentManager(), "bnf");

                break;
            case 3:
                DialogFragment pl = new bn_pl();
                pl.show(getSupportFragmentManager(), "pl");
                bn_pl = false;
                break;
        }
    }

    //evento clic en boton positivo de Banco de nivel final
    public void ultimoPositiveClick(DialogFragment dialog, Double valor)
    {
        String[] temp = new String[3];
        temp[0] = "BN" + bn;
        temp[1] = null;
        temp[2] = String.valueOf(valor);
        if (edit)
            actualizar(temp);
        else
            bn_final(temp);
    }

    @Override
    public void onStart()
    {
        super.onStart();

    }

    @Override
    public void onStop()
    {
        super.onStop();

    }


    public void ultimoNegativeClick(DialogFragment dialog) { }     //  vento clic boton negativo de banco de nivel final


}
