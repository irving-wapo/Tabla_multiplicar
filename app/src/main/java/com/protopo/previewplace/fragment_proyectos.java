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
    String nombre_activi;

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
             nombre_activi =  this.getArguments().getString("nombre");

             nombre_icono = (TextView) miVista.findViewById(R.id.textView16);
             el_icono = (ImageView) miVista.findViewById(R.id.imageView4);


            if (nombre_activi.equals("n_dife") )
            {
                el_icono.setImageResource(R.drawable.niv_dif);
                nombre_icono.setText(String.valueOf( getString(R.string.lbl_fragment_1 )));
            }
            else  if (nombre_activi.equals("n_perfil") )
            {
                el_icono.setImageResource(R.drawable.perfil);
                nombre_icono.setText(String.valueOf( getString(R.string.lbl_fragment_2 )));
            }
            else  if (nombre_activi.equals("Curvas_nivel") )
            {
                el_icono.setImageResource(R.drawable.nivel);
                nombre_icono.setText(String.valueOf(getString(R.string.lbl_fragment_3 )));
                Toast.makeText(miVista.getContext(), "AREA EN DESARROLLO.",Toast.LENGTH_LONG).show();
            }
            else  if (nombre_activi.equals("Curvas_h") )
            {
                el_icono.setImageResource(R.drawable.horizontal);
                nombre_icono.setText(String.valueOf(getString(R.string.lbl_fragment_4 )));
                Toast.makeText(miVista.getContext(), "AREA EN DESARROLLO.",Toast.LENGTH_LONG).show();
            }
            else  if (nombre_activi.equals("Agri") )
            {
                el_icono.setImageResource(R.drawable.agrime);
                nombre_icono.setText(String.valueOf(getString(R.string.lbl_fragment_5 )));
                Toast.makeText(miVista.getContext(), "AREA EN DESARROLLO.",Toast.LENGTH_LONG).show();
            }
            else  if (nombre_activi.equals("Curvas_v") )
            {
                el_icono.setImageResource(R.drawable.verticales);
                nombre_icono.setText(String.valueOf(getString(R.string.lbl_fragment_6 )));
                Toast.makeText(miVista.getContext(), "AREA EN DESARROLLO.",Toast.LENGTH_LONG).show();
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
    return miVista;
    }

    private void abrir_niv_dif(Boolean estado, String nombre)
    {
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

}
