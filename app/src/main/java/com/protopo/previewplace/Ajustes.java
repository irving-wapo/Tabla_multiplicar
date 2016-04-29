package com.protopo.previewplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Ajustes extends AppCompatActivity  implements OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        spin();
    }
    private void spin()
    {
        //spinner
        Spinner spinner = (Spinner)findViewById(R.id.spinner);

        //spinner click listener
        spinner.setOnItemSelectedListener(this);

        List<String> temas = new ArrayList<String>();
        //valores de la lista
        temas.add(getString(R.string.item1));
        temas.add(getString(R.string.item2));
        //Adaptador para el spinner
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,temas);
        //estilo de down
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //agragando el adaptador al spinner
        spinner.setAdapter(adaptador);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
