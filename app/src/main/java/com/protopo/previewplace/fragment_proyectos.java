package com.protopo.previewplace;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

public class fragment_proyectos extends Fragment
{
    MainActivity activityPrincipal;
    View miVista;
    ListView la_lista;
    PasoParametros pasadorDatos;

    ImageView el_icono;
    TextView nombre_icono;
    Boolean texto, icono;

    public interface PasoParametros
    {
        public void pasoParametros(String datos);
    }


    @Override
    public void onAttach(Activity a)
    {
        super.onAttach(a);
        pasadorDatos = (PasoParametros) a;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        miVista = inflater.inflate(R.layout.fragment_proyectos,container,false);
        try {

            // Defined Array values to show in ListView
            la_lista = (ListView) miVista.findViewById(R.id.lstProjectos);
            String[] valores =  this.getArguments().getStringArray("lista");
             texto =  this.getArguments().getBoolean("nombre");
             icono =  this.getArguments().getBoolean("imagen");

            nombre_icono = (TextView) miVista.findViewById(R.id.wapo);
            el_icono = (ImageView) miVista.findViewById(R.id.imageView4);

        String atras= nombre_icono.getText().toString();
                 // Toast.makeText(miVista.getContext(),atras +" DIFERENCIAL",Toast.LENGTH_LONG).show();


            if (texto && icono )
            {
                el_icono.setImageResource(R.drawable.niv_dif);

                nombre_icono.setText(String.valueOf("Nivelacion diferecial"));
                //Toast.makeText(miVista.getContext(),"viene de  niv DIFERENCIAL",Toast.LENGTH_LONG).show();
            }
            else
            {
                nombre_icono.setText(String.valueOf("Nivelacion de perfil"));
                //Toast.makeText(miVista.getContext(),"viene de  niv perfil",Toast.LENGTH_LONG).show();
            }


            ArrayAdapter<String> contenedor = new ArrayAdapter<String>(miVista.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, valores);
            la_lista.setAdapter(contenedor);

            la_lista.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int posicion, long id)
                {
                    String itemValue = (String) la_lista.getItemAtPosition(posicion);    // Click en un item de la lista
                    String extencion = "";
                    if (itemValue !=null)
                    {
                        StringTokenizer st = new StringTokenizer(itemValue,".");
                        itemValue = st.nextToken();
                        extencion = st.nextToken();
                    }

                    if(extencion.equals("nd"))
                        { abrir_niv_dif(false,itemValue); }

                    if(extencion.equals("np"))
                        { abrir_niv_perfil(false,itemValue); }
                }

            });

            la_lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
            {
                public boolean onItemLongClick(AdapterView<?> arg0, View v, int posicion, long arg3)
                {
                    String itemValue = (String) la_lista.getItemAtPosition(posicion);    // Click en un item de la lista
                    String extencion = "";
                    if (itemValue !=null)
                    {
                        StringTokenizer st = new StringTokenizer(itemValue,".");
                        itemValue = st.nextToken();
                    }
                    // ListView Clicked item index
                    menu_lista dialogFragment = new menu_lista();
                    pasadorDatos.pasoParametros(itemValue);
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "Menu lista");
                    return true;
                }
            });

        }catch (Exception e)  { Toast.makeText(miVista.getContext(),R.string.msjError_cargando,Toast.LENGTH_LONG).show(); }

        try {
           // String atras = nombre_icono.getText().toString();
        }catch (Exception Ex)
        {
            Toast.makeText(miVista.getContext(),Ex.toString() +" DIFERENCIAL",Toast.LENGTH_LONG).show();
        }
//            Toast.makeText(miVista.getContext(),atras +" DIFERENCIAL",Toast.LENGTH_LONG).show();
        return miVista;
    }

    private void abrir_niv_dif(Boolean estado, String nombre)
    {
        logos();
        String nombreArchivo =  nombre;
        try
        {
            if(nombreArchivo != null)
            {
                Intent diferencial = new Intent(miVista.getContext(),niv_dif_cont.class);
                diferencial.putExtra("nombre",nombreArchivo);
                diferencial.putExtra("carga",estado);
                startActivity(diferencial);
            }
        }
        catch(Exception ex) {Toast.makeText(miVista.getContext(),R.string.msjError_abrir,Toast.LENGTH_LONG).show(); }
    }

    private void abrir_niv_perfil(Boolean estado, String nombre)
    {
        logos();
        String nombreArchivo =  nombre;
        try
        {
            if(nombreArchivo != null)
            {
                Intent diferencial = new Intent(miVista.getContext(), activity_niv_perfil.class);
                diferencial.putExtra("nombre",nombreArchivo);
                diferencial.putExtra("carga",estado);
                startActivity(diferencial);
            }
        }
        catch(Exception ex) {Toast.makeText(miVista.getContext(),R.string.msjError_abrir,Toast.LENGTH_LONG).show(); }
    }

    public  void logos()
    {

//        String atras= nombre_icono.getText().toString();
  //      Toast.makeText(miVista.getContext(),atras +" DIFERENCIAL",Toast.LENGTH_LONG).show();
    }

}
