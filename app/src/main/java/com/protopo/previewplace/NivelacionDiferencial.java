package com.protopo.previewplace;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;
public class NivelacionDiferencial extends Fragment
{
    MainActivity activityPrincipal;
    View miVista;
    ListView la_lista;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        miVista = inflater.inflate(R.layout.nivelacion_diferencial,container,false);
        try {


            // Defined Array values to show in ListView

            la_lista = (ListView) miVista.findViewById(R.id.lstProjectos);


            String[] valores =  this.getArguments().getStringArray("lista");

            ArrayAdapter<String> contenedor = new ArrayAdapter<String>(miVista.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, valores);
            la_lista.setAdapter(contenedor);

            la_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int posicion, long id) {

                    int itemPosition = posicion;                                         // ListView Clicked item index
                    String itemValue = (String) la_lista.getItemAtPosition(posicion);    // ListView Clicked item value
                    abrir_niv_dif(false,itemValue);
                }

            }


            );
            la_lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View v, int posicion, long arg3) {
                    // ListView Clicked item index
                    String itemValue = (String) la_lista.getItemAtPosition(posicion);
                    Toast.makeText(miVista.getContext(),"le dio en: "+itemValue,Toast.LENGTH_SHORT).show();


                    menu_lista dialogFragment = new menu_lista();
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "Menu lista");
                    return true;
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(miVista.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
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
        catch(Exception ex)
        {

        }
    }



}
