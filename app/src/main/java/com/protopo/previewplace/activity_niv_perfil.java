package com.protopo.previewplace;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.itextpdf.awt.geom.CubicCurve2D;

import com.itextpdf.awt.geom.misc.RenderingHints;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import android.view.View.OnTouchListener;

public class activity_niv_perfil extends ActionBarActivity implements  OnTouchListener, menu_agregar_perfil.DialogListener,bn_pl_perfil.DialogListener,primer_bn_perfil.DialogListener,cadenamiento_perfil.DialogListener {
    Tabla tabla;
    TabHost TbH;
    int pl=1;
    boolean carga;
    static int punto_edit=0;
    static boolean edit,bn_pl;
    ArrayList<String[]> elementos = new ArrayList<String[]>();
    String archivoNombre, nombre_grafica="INSTITUTO TECNOLOGICO DE TEHUACAN";

    // Paso 1: añadir un unas instancias
    private  float mScale =  1f ;
    private ScaleGestureDetector mScaleDetector ;
    GestureDetector gestureDetector ;

    //Vectores con valores de la grafica
    //Number[] Datos1 = {0, 2, 8, 20, 40, 60, 80, 94, 100, 120, 137, 140, 148, 160, 180, 186.6, 192, 200, 220, 228.7, 234.6, 240, 242, 253, 256.2, 260, 280, 300, 320, 340, 360, 370, 380, 400, 405, 405, 420, 440, 460, 480, 500, 520, 540, 560, 580, 600, 613.2};
    //Number[] Datos2 = {100, 99.72, 97.29, 96.98, 96.68, 96.44, 95.99, 95.41, 94.89, 94.71, 94.7, 94.27, 93.51, 93.65, 93.69, 93.63, 92.77, 92.74, 92.74, 92.74, 91.85, 91.78, 91.44, 91.45, 90.99, 90.98, 90.94, 90.36, 89.87, 89.71, 89.07, 89.12, 88.62, 88.46, 88.46, 88.32, 87.36, 85.79, 85.47, 85.15, 84.87, 84.72, 84.16, 84.12, 84, 83.79, 82.82};

    //****************** DE MI GRAFICA ***********
    private XYPlot mySimpleXYPlot;
    private SimpleXYSeries[] series = null;
    private PointF minXY;
    private PointF maxXY;
    //  --------------   ESTADOS DE TOUCHES ----------
    static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    static final int TWO_FINGERS_DRAG = 2;
    int mode = NONE;

    PointF firstFinger;
    float distBetweenFingers;
    boolean stopThread = false;
    // -----------------------------------------------
    //****************** DE MI GRAFICA ***********
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
        tabla = new Tabla(0,getSupportFragmentManager(),this, (TableLayout) findViewById(R.id.tabla_perfil));
        Bundle extras = getIntent().getExtras();
        carga = extras.getBoolean("carga");
        archivoNombre = extras.getString("nombre");

        // Paso 2: crear una instancia de GestureDetector (este paso sea sholude lugar en onCreate ())
        gestureDetector =  new  GestureDetector(this ,new  GestureListener());

        // Animación para scalling
        mScaleDetector =  new  ScaleGestureDetector ( this ,  new  ScaleGestureDetector . SimpleOnScaleGestureListener ()
        {
            @Override
            public  boolean onScale ( ScaleGestureDetector detector )
            {
                float scale = 1.0f - detector.getScaleFactor();

                float prevScale = mScale;
                mScale += scale;

                if (mScale < 0.1f) // Minimum scale condition:
                    mScale = 0.1f;

                if (mScale > 1.0f) // Maximum scale condition:
                    mScale = 1.0f;
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                scaleAnimation.setDuration(0);
                scaleAnimation.setFillAfter(true);
                ScrollView layout2 = (ScrollView) findViewById(R.id.scrollPerfil);
                layout2.startAnimation(scaleAnimation);
                return true;
            }
        });

        if (carga) {
            cabecera();
        } //  SI ES NUEVO
        else {
            cargar();
        }  // LEYENDO ARCHIVO EXISTENTE

        //****************** DE MI GRAFICA ***********
        llamar_init();
        pestañas();




    }
    // step 3: override dispatchTouchEvent()
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

//step 4: add private class GestureListener

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // double tap fired.
            return true;
        }
    }

    //*******************************   G  R  A  F  I  C  A   ************************************
    /*public void inicializa_grafica(View view )
    {
        llamar_init();
    }
    */

    public void llamar_init()
    {
        mySimpleXYPlot = (XYPlot) findViewById(R.id.plot);
        mySimpleXYPlot.setOnTouchListener(this);
        mySimpleXYPlot.getGraphWidget().setTicksPerRangeLabel(2);
        mySimpleXYPlot.getGraphWidget().setTicksPerDomainLabel(2);
        mySimpleXYPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        mySimpleXYPlot.getGraphWidget().setRangeValueFormat(new DecimalFormat("#####"));
        mySimpleXYPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("#####.#"));
        mySimpleXYPlot.getGraphWidget().setRangeLabelSubTickExtension(25);
        mySimpleXYPlot.setRangeLabel("");
        mySimpleXYPlot.setDomainLabel("");
        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.NONE, null, null);


        series = new SimpleXYSeries[4];
        int scale = 1;
        for (int i = 0; i < 4; i++, scale *= 5) {
            series[i] = new SimpleXYSeries(nombre_grafica);
            populateSeries(series[i]);
        }                                                           // CONTORNO                              //RELLENO
        mySimpleXYPlot.addSeries(series[3], new LineAndPointFormatter(Color.rgb(100, 0, 0), null, Color.rgb(244, 164,96), null));
        mySimpleXYPlot.redraw();
        mySimpleXYPlot.calculateMinMaxVals();
        minXY = new PointF(mySimpleXYPlot.getCalculatedMinX().floatValue(), mySimpleXYPlot.getCalculatedMinY().floatValue());
        maxXY = new PointF(mySimpleXYPlot.getCalculatedMaxX().floatValue(), mySimpleXYPlot.getCalculatedMaxY().floatValue());

    }


    public void regresamela_como_estaba_plis()
    {
        minXY.x = series[0].getX(0).floatValue();
        maxXY.x = series[3].getX(series[3].size() - 1).floatValue();
        mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x, BoundaryMode.FIXED);
        mySimpleXYPlot.redraw();
    }

    private void populateSeries(SimpleXYSeries series)
    {
        for(int i = 0; i <  lista_double_x.size(); /* Datos1.length -1;*/ i++)
        {
            series.addLast(lista_double_x.get(i), lista_double_y.get(i) );
            //series.addLast( Datos1[i], Datos2[i]);
            //Toast.makeText(getApplicationContext(), "En la grafica X: "+ lista_double_x.get(i)+"    Y: "+ lista_double_y.get(i), Toast.LENGTH_SHORT).show();
        }
    }

    private void zoom(float scale)
    {
        float domainSpan = maxXY.x - minXY.x;
        float domainMidPoint = maxXY.x - domainSpan / 2.0f;
        float offset = domainSpan * scale / 2.0f;

        minXY.x = domainMidPoint - offset;
        maxXY.x = domainMidPoint + offset;

        minXY.x = Math.min(minXY.x, series[3].getX(series[3].size() - 3).floatValue());
        maxXY.x = Math.max(maxXY.x, series[0].getX(1).floatValue());
        clampToDomainBounds(domainSpan);
    }

    private void scroll(float pan)
    {
        float domainSpan = maxXY.x - minXY.x;
        float step = domainSpan / mySimpleXYPlot.getWidth();
        float offset = pan * step;
        minXY.x = minXY.x + offset;
        maxXY.x = maxXY.x + offset;
        clampToDomainBounds(domainSpan);
    }

    private void clampToDomainBounds(float domainSpan)
    {
        float leftBoundary = series[0].getX(0).floatValue();
        float rightBoundary = series[3].getX(series[3].size() - 1).floatValue();
        // enforce left scroll boundary:
        if (minXY.x < leftBoundary)
        {
            minXY.x = leftBoundary;
            maxXY.x = leftBoundary + domainSpan;
        }
        if (maxXY.x > series[3].getX(series[3].size() - 1).floatValue())
        {
            maxXY.x = rightBoundary;
            minXY.x = rightBoundary - domainSpan;
        }
    }

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.hypot(x, y);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        //Toast.makeText(getApplicationContext(),"COMO QUE SENTI ALGO....",Toast.LENGTH_SHORT).show();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // Start gesture
                firstFinger = new PointF(event.getX(), event.getY());
                mode = ONE_FINGER_DRAG;
                stopThread = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // second finger
                distBetweenFingers = spacing(event);
                // the distance check is done to avoid false alarms
                if (distBetweenFingers > 5f) {
                    mode = TWO_FINGERS_DRAG;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ONE_FINGER_DRAG) {
                    PointF oldFirstFinger = firstFinger;
                    firstFinger = new PointF(event.getX(), event.getY());
                    scroll(oldFirstFinger.x - firstFinger.x);
                    mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x,
                            BoundaryMode.FIXED);
                    mySimpleXYPlot.redraw();
                } else if (mode == TWO_FINGERS_DRAG) {
                    float oldDist = distBetweenFingers;
                    distBetweenFingers = spacing(event);
                    zoom(oldDist / distBetweenFingers);
                    mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x,
                            BoundaryMode.FIXED);
                    mySimpleXYPlot.redraw();

                }
                break;
        }
        return true;

    }

    //*******************************   G  R  A  F  I  C  A   ************************************


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void pestañas()
    {
        TbH = (TabHost) findViewById(R.id.tabHost); //llamamos al Tabhost
        TbH.setup();                                                         //lo activamos
        TabHost.TabSpec tab1 = TbH.newTabSpec("tab1");  //aspectos de cada Tab (pestaña)
        final TabHost.TabSpec tab2 = TbH.newTabSpec("tab2");
        tab1.setIndicator(getString(R.string.lbltabla));    //qué queremos que aparezca en las pestañas
        tab1.setContent(R.id.lnlTabla); //definimos el id de cada Tab (pestaña)
        tab2.setIndicator(getString(R.string.lblGrafica));
        tab2.setContent(R.id.lnlGrafica);
        TbH.addTab(tab1); //añadimos los tabs ya programados
        TbH.addTab(tab2);
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
                mySimpleXYPlot=null;
                llamar_init();
                regresamela_como_estaba_plis();

            }
        });
    }

    ArrayList<String> las_x = new ArrayList<String>();
    ArrayList<String> las_y = new ArrayList<String>();

    public void sacame_xy_plis()
    {
        //*********************inicialice *********
        las_x.clear();
        las_y.clear();
    try {
    if (elementos.size() > 1)
    {
        String vec_1[] = elementos.get(1);
        las_x.add("0");
        las_y.add(vec_1[4]);


        for (int i = 1; i < elementos.size(); i++)    // FOR DE TODOS QUE SACA TODOS LOS VALORES DE 'ELEMENTOS'
        {
            String kilometro = "", metros = "";
            Boolean bandera = true;
            String vec[] = elementos.get(i);


            String cadena_x = vec[0];
            if (Character.isDigit(cadena_x.charAt(0))) {
                for (int j = 0; j < cadena_x.length(); j++) {
                    if (cadena_x.charAt(j) == '+') {
                        bandera = false;
                    }

                    if (bandera) {
                        kilometro += cadena_x.charAt(j);
                    }

                    if (!bandera) {
                        if (cadena_x.charAt(j) == '+') {
                        } else {

                            metros += cadena_x.charAt(j);
                        }
                    }
                }

                String texto = "";
                texto += kilometro += metros;
                las_x.add(texto);
                las_y.add(vec[4]);
            }
        }
    }
        }catch (Exception Ex) {}
    }




    ArrayList<Double> lista_double_x = new ArrayList<Double>();
    ArrayList<Double> lista_double_y = new ArrayList<Double>();

    public  void mustrame_lasx()
    {
        for(int i =0; i<las_y.size(); i++ )
        {
            lista_double_x.add( Double.parseDouble(las_x.get(i)) );
            lista_double_y.add( Double.parseDouble(las_y.get(i)) );
        }
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
    //eventos de los botones flotantes
    public void fab_agregar_perfil(View view)
    {
        agregar();
    }
    public void fab2_eliminar_perfil(View view)
    {
        eliminar();
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
    // Estado de la memoria externa
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



    // Generar captura de la grafica
    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private Bitmap obtener_captura()
    {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.activity_niv_perfil, null); // activity_main is UI(xml) file we used in our Activity class. FrameLayout is root view of my UI(xml) file.
        root.setDrawingCacheEnabled(true);
        Bitmap bitmap = getBitmapFromView(this.getWindow().findViewById(R.id.plot)); // here give id of our root layout (here its my FrameLayout's id)
        root.setDrawingCacheEnabled(false);
        return bitmap;
    }
    //redimensionar imagen
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }
    //agregar contenido al pdf


    private void addContent(Document document)
    {
        try
        {

            Bitmap bitmap = obtener_captura();
            bitmap = redimensionarImagenMaximo(bitmap,530f,300f);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image imagen = Image.getInstance(stream.toByteArray());

            Bitmap bitmaplog = BitmapFactory.decodeResource(this.getResources(), R.drawable.encabezado);
            bitmaplog = redimensionarImagenMaximo(bitmaplog,530f,100f);
            ByteArrayOutputStream streamlog = new ByteArrayOutputStream();
            bitmaplog.compress(Bitmap.CompressFormat.JPEG, 100, streamlog);
            Image encabezado = Image.getInstance(streamlog.toByteArray());

            Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
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
            document.add(encabezado);
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph(getString(R.string.lblObra)+" "+getString(R.string.lblObraN),font));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(getString(R.string.lblLocalizacion)+" Tehuacán Puebla, México.",font));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(getString(R.string.lblTipoPdf)+" "+this.getTitle(),font));
            document.add(new Paragraph("______________________________________________________________________________"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("                                                 "+getString(R.string.lblPrevisualizacion),font2));
            document.add(new Paragraph("\n"));
            document.add(tabla);
            document.add(new Paragraph("\n"));
            document.add(imagen);
            } catch (Exception e) { e.printStackTrace();  }
    }



    //metodo para abrir un pdf con un intent
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
    //metodo que carga datos desde archivo


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

    //metodo que lee el archivo
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


    //metodo agregar fila de banco de nivel 1
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
        sacame_xy_plis();
        mustrame_lasx();
    }
    public void valores()
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
        } catch (Exception ex) { Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();  }
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

    private void agregar()
    {
        edit=false;
        DialogFragment nuevo = new menu_agregar_perfil();
        nuevo.show(getSupportFragmentManager(), "nuevo");
    }
    //menu superior ...(Tres puntitos)
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.itmAgregar)
        {
            agregar();
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

        String copia_m="", copia_kilo="";

        if(valor1 < 10)
        { copia_m  += "00" + valor1; }
        else if(valor1 < 100)
        { copia_m  += "0" + valor1; }
        else if(valor1 < 999)
        { copia_m  += valor1; }


        if(valor < 10)
        { copia_kilo  += "00" + valor; }
        else if(valor < 100)
        { copia_kilo  += "0" + valor; }
        else if(valor < 999)
        { copia_kilo  += valor; }


        String[] temp = new String[5];
        temp[0] = copia_kilo+"+"+copia_m;
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