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


public class MainActivity extends AppCompatActivity implements dialog_nombre_archivo.DialogListener , NavigationView.OnNavigationItemSelectedListener, msg_borrar.DialogListener,menu_lista.DialogListener,NivelacionDiferencial.PasoParametros
{
    String nombre_archivo;
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
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_nivelacion)
        {
            String ya_llego[] =filtrar_archivos(".nd").toArray(new String[0]);
            Bundle bundle = new Bundle();
            bundle.putStringArray("lista", ya_llego );
            NivelacionDiferencial fragInfo = new NivelacionDiferencial();
            fragInfo.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.content_frame,fragInfo).commit();
        }

        if (id == R.id.nav_nivelacion_perfil)
        {
            String ya_llego[] =filtrar_archivos(".nd").toArray(new String[0]);
            Bundle bundle = new Bundle();
            bundle.putStringArray("lista", ya_llego );
            NivelacionDiferencial fragInfo = new NivelacionDiferencial();
            fragInfo.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.content_frame,fragInfo).commit();
        }

        if (id == R.id.nav_configuracion)
        {
            Intent ajustes = new Intent(MainActivity.this, Ajustes.class );
            startActivity(ajustes);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void borrarOnClick(View view)
    {
        msg_borrar objt = new msg_borrar();
        objt.show(getSupportFragmentManager(),"Borrar");


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
    public void cancelar_btn(DialogFragment dialog) { }

    @Override
    public void si_btn_msg(DialogFragment dialog)
    {
     Toast.makeText(getApplicationContext(), "Le dio que si", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void no_btn_msg(DialogFragment dialog)
    {
        Toast.makeText(getApplicationContext(), "Le dio que NO", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClickListaDif(DialogFragment dialog, int arg)
    {
        Toast.makeText(getApplicationContext(),""+arg,Toast.LENGTH_SHORT).show();
    public void onClickListaDif(DialogFragment dialog, int arg) {
        switch (arg)
        {
            case 0:
                abrir_niv_dif(false,nombre_archivo);
                break;
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }


    }

    @Override
    public void pasoParametros(String datos) {
        nombre_archivo=datos;
    }
}
