package com.protopo.previewplace;
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
import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements dialog_nombre_archivo.DialogListener , NavigationView.OnNavigationItemSelectedListener, msg_borrar.DialogListener,menu_lista.DialogListener,fragment_proyectos.PasoParametros
{
    String nombre_archivo, bandera_archivo ="";
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
            { drawer.closeDrawer(GravityCompat.START); }
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
                fragment_nivelacion_perfil();
                break;
            default:
                break;
        }
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
            fragment = 2;
            fragment_nivelacion_perfil();
        }


        if(id == R.id.nav_curvas_horizontales )
        {
            Intent c_hori = new Intent (MainActivity.this, ctivity_c_horizontales.class);
            startActivity(c_hori);
           // String[] a= {};
            //carga_fragment(a, "Curvas_h");
        }

        if(id == R.id.nav_curvas_verticales )
        {   String[] a= {};
            carga_fragment(a, "Curvas_v");
        }


        if(id == R.id.nav_curvas_nivel )
        {   String[] a= {};
            carga_fragment(a, "Curvas_nivel");
        }

        if(id == R.id.nav_agrimensura )
        {   String[] a= {};
            carga_fragment(a, "Agri");

            Intent agri = new Intent (MainActivity.this, Agrimensura.class);
            startActivity(agri);
        }


        if(id == R.id.nav_comentarios)
        {
            Intent ayuda = new Intent (MainActivity.this, Ayuda_comentarios.class);
            startActivity(ayuda);
        }

        if(id == R.id.nav_Acerca)
        {
            Intent acer = new Intent (MainActivity.this, acerca.class);
            startActivity(acer);
        }

        if (id == R.id.nav_configuracion)
        {
            fragment = 0;
            Intent ajustes = new Intent(MainActivity.this, Ajustes.class );
            startActivity(ajustes);
        }

        if(id == R.id.nav_salir)
        {
            Intent registro = new Intent ( getApplicationContext(), registro_usuarios.class);
            startActivity(registro);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fragment_nivelacion_diferencial() // metodo que llama al fragment Nivelacion Diferencial
    {

        String ya_llego[] =filtrar_archivos(".nd").toArray(new String[0]);
        carga_fragment(ya_llego, "n_dife");
        bandera_archivo =".nd";
    }

    public void carga_fragment(String [] ya_llego, String im)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putStringArray("lista", ya_llego );
        bundle.putString("nombre", im );
        fragment_proyectos fragInfo = new fragment_proyectos();
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragInfo).commit();
        fragInfo.setArguments(bundle);
    }

    public void fragment_nivelacion_perfil()
    {
        String ya_llego[] =filtrar_archivos(".np").toArray(new String[0]);
        carga_fragment(ya_llego, "n_perfil");
        bandera_archivo =".np";
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
            catch(Exception ex) { Toast.makeText(getApplicationContext(),R.string.msjError_abrir,Toast.LENGTH_LONG).show(); }
    }

    private void abrir_niv_perfil(Boolean estado, String nombre)
    {
        String nombreArchivo =  nombre;
        try
        {
            if(nombreArchivo != null)
            {
                Intent diferencial2 = new Intent(MainActivity.this, activity_niv_perfil.class);
                diferencial2.putExtra("nombre",nombreArchivo);
                diferencial2.putExtra("carga",estado);
                startActivity(diferencial2);
            }
        }
        catch(Exception ex) { Toast.makeText(getApplicationContext(),R.string.msjError_abrir,Toast.LENGTH_LONG).show(); }
    }

    private void borrar(String nombre)
    {
        File f = null;
        if(bandera_archivo.equals(".nd") )
        {
            f = new File(getApplicationContext().getFilesDir(), nombre.concat(".nd"));
        }

        if(bandera_archivo.equals(".np") )
        {
            f = new File(getApplicationContext().getFilesDir(), nombre.concat(".np"));
        }

        f.delete();
    }

    public boolean verifica_nombre( String nombre)
    {
        boolean bandera = false,   caracteres = true;
        for(int i=0; i<nombre.length(); i++)
        {
            char letra = nombre.charAt(i);
            if( !Character.isDigit(letra)  &&  !Character.isLetter(letra) ) {  caracteres = false; }
        }

        if((!filtrar_archivos(".nd").contains(nombre.concat(".nd")) || !filtrar_archivos(".np").contains(nombre.concat(".np")) )  && caracteres) //COMPARA CON LOS ARCHIVOS EXISTENTES
        {
            bandera = true;
        }
        else  { bandera = false; Toast.makeText(getApplicationContext(), R.string.msjError_nombre2, Toast.LENGTH_SHORT).show(); }
        return  bandera;

    }

    @Override
    public void aceptar_btn(DialogFragment dialog, String nombre)
    {
        if (verifica_nombre(nombre))
        {
            if(bandera_archivo.equals(".nd") )
            {  abrir_niv_dif(true,nombre); }

            if(bandera_archivo.equals(".np") )
            {  abrir_niv_perfil(true,nombre); }
        }
    }

    @Override
    public void cancelar_btn(DialogFragment dialog) { } //evento cancelar del cuadro de dalogo nuevo archivo

    @Override
    public void si_btn_msg(DialogFragment dialog) // evento  aceptar del dialogo borrar
    {
        borrar(nombre_archivo);
        onResume();
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
                if(bandera_archivo.equals(".nd") )
                    abrir_niv_dif(false,nombre_archivo);
                if(bandera_archivo.equals(".np") )
                    abrir_niv_perfil(false,nombre_archivo);
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
