package com.protopo.previewplace;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
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
    TextView nombre_icono, descripcion;
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
             descripcion =  (TextView) miVista.findViewById(R.id.textView8);

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
                c_nivel();
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
                agrimensura();
              //  Toast.makeText(miVista.getContext(), "AREA EN DESARROLLO.",Toast.LENGTH_LONG).show();
            }
            else  if (nombre_activi.equals("Curvas_v") )
            {
                el_icono.setImageResource(R.drawable.verticales);
                nombre_icono.setText(String.valueOf(getString(R.string.lbl_fragment_6 )));
               c_verticales();
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

            Animation animation = AnimationUtils.loadAnimation(miVista.getContext() , R.anim.diagonal);
            Animation animation2 = AnimationUtils.loadAnimation(miVista.getContext() , R.anim.diagonal);
            Animation animation4 = AnimationUtils.loadAnimation(miVista.getContext() , R.anim.transparencia_corto);

            el_icono.startAnimation(animation);
            nombre_icono.startAnimation(animation2);
            la_lista.startAnimation(animation4);
            descripcion.startAnimation(animation4);

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

    public void c_nivel()
    {
        descripcion.setText("Una curva de nivel es aquella línea que en un mapa une todos los puntos que tienen igualdad de condiciones y de altitud. Las curvas de nivel suelen imprimirse en los mapas en color siena para el terreno y en azul para los glaciares y las profundidades marinas. \n\nAcualmente esta funcion se encuentra en desarrollo.");
    }

    public void c_verticales()
    {
        descripcion.setText("Las curvas verticales son curvas que se diseñan cuando se interceptan dos tangentes, en forma vertical, de un tramo de carretera. Con el fin de suavizar la intersección de dos tangentes, por medio de curvas verticales, se crea un cambio gradual entre las tangentes, de este modo se genera una transición, entre una pendiente y otra, cómoda para la vía del usuario.\n\nAcualmente esta funcion se encuentra en desarrollo.");
    }

    public void agrimensura()
    {
        descripcion.setText("La agrimensura es esencial para establecer los límites de la tierra y, de este modo, determinar la propiedad de la misma. Para esto se miden las distancias, los ángulos y las alturas con diferentes instrumentos. Estas actividades pueden ayudar a delimitar la propiedad privada y las divisiones políticas de un terreno. \n\nAcualmente esta funcion se encuentra en desarrollo.");
    }


}
