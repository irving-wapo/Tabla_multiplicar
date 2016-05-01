package com.protopo.previewplace;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements dialog_nombre_archivo.DialogListener , NavigationView.OnNavigationItemSelectedListener, msg_borrar.DialogListener,menu_lista.DialogListener,NivelacionDiferencial.PasoParametros
{
    String nombre_archivo;
    int fragment = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            {
                drawer.closeDrawer(GravityCompat.START);


            }
        else
            { super.onBackPressed();}
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        switch (fragment)
        {
            case 1:
                fragment_nivelacion_diferencial();
                break;
            case 2:
                break;
            default:
                break;
        }

        //Refresh your stuff here
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_nivelacion)
        {
            fragment = 1;
            fragment_nivelacion_diferencial();
        }

        if (id == R.id.nav_nivelacion_perfil)
        {

        }

        if (id == R.id.nav_configuracion)
        {
            fragment = 0;
            Intent ajustes = new Intent(MainActivity.this, Ajustes.class );
            startActivity(ajustes);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void fragment_nivelacion_diferencial() // metodo que llama al fragment Nivelacion Diferencial
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String ya_llego[] =filtrar_archivos(".nd").toArray(new String[0]);
        Bundle bundle = new Bundle();
        bundle.putStringArray("lista", ya_llego );
        NivelacionDiferencial fragInfo = new NivelacionDiferencial();
        fragInfo.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragInfo,"nivdif").commit();
    }
    public  ArrayList<String> filtrar_archivos(String extencion)
    {
        File listFile[] = getApplicationContext().getFilesDir().listFiles();
        ArrayList<String> lista_de_nombres = new ArrayList<String>();

        if (listFile != null && listFile.length > 0)  //SI SI TIENE ALGO EL DIRECTORIO
        {
            for (int i = 0; i < listFile.length; i++)  // 1- 10
            {
                if (listFile[i].getName().endsWith(extencion)/*|| listFile[i].getName().endsWith(".np")*/)
                {
                    lista_de_nombres.add(listFile[i].getName());
                }
            }
        }
        return lista_de_nombres;
    }


    public void nuevoOnClick(View view)
    {
        dialog_nombre_archivo mensaje = new dialog_nombre_archivo();
        mensaje.show(getSupportFragmentManager(),"Nuevo");
    }

    private void abrir_niv_dif(Boolean estado, String nombre)
    {
        String nombreArchivo =  nombre;
        try
        {
            if(nombreArchivo != null)
            {
                Intent diferencial = new Intent(MainActivity.this,niv_dif_cont.class);
                diferencial.putExtra("nombre",nombreArchivo);
                diferencial.putExtra("carga",estado);
                startActivity(diferencial);
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(getApplicationContext(),R.string.msjError_abrir,Toast.LENGTH_LONG).show();

        }
    }
    private void borrar(String nombre)
    {
        File f = new File(getApplicationContext().getFilesDir(), nombre);
        f.delete();
    }

    @Override
    public void aceptar_btn(DialogFragment dialog, String nombre)
    {
        boolean bandera = true;
        if(!filtrar_archivos(".nd").contains(nombre.concat(".nd")))
        {
            for(int i=0; i<nombre.length(); i++)
            {
                char letra = nombre.charAt(i);
                if( !Character.isDigit(letra)  &&  !Character.isLetter(letra) ) {  bandera = false; }
            }

            if(bandera)
            {
                abrir_niv_dif(true,nombre.concat(".nd"));
            }
            else {   Toast.makeText(getApplicationContext(), R.string.msjError_nombre, Toast.LENGTH_SHORT).show();  }
        }
        else
        {
            Toast.makeText(getApplicationContext(), R.string.msjError_nombre2, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void cancelar_btn(DialogFragment dialog) { } //evento cancelar del cuadro de dalogo nuevo archivo

    @Override
    public void si_btn_msg(DialogFragment dialog) // evento  aceptar del dialogo borrar
    {
        borrar(nombre_archivo);
        fragment_nivelacion_diferencial();
    }

    @Override
    public void no_btn_msg(DialogFragment dialog) // evento cancelar del boton borrar
    {

    }


    @Override
    public void onClickListaDif(DialogFragment dialog, int arg) {
        switch (arg)
        {
            case 0:
                abrir_niv_dif(false,nombre_archivo);
                break;
            case 1:
                msg_borrar objt = new msg_borrar();
                objt.show(getSupportFragmentManager(),"Borrar");
                break;
            default:
                break;
        }


    } // evento al seleccionar un elemento en la lista

    @Override
    public void pasoParametros(String datos) {
        nombre_archivo=datos;
    } // resibe nombre del archivo
}
