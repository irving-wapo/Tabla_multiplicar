package com.protopo.previewplace;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.TextField;

public class registro_usuarios extends AppCompatActivity
{
    public  TextView label;
    public EditText usuario, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);

        label = (TextView) findViewById(R.id.textView9);
        usuario= (EditText) findViewById(R.id.editText2);
        password= (EditText) findViewById(R.id.editText);
        consultame_id_android();
    }

    public void btn_registro_click(View view) 
    {
        String name="", pass="";
        name= usuario.getText().toString();
        pass= password.getText().toString();

//        if(name.equals("admin") && pass.equals("1234"))
        {
            Intent inicio = new Intent(registro_usuarios.this, MainActivity.class );
            startActivity(inicio);
            Toast.makeText(getApplicationContext(),"Bienvenido "+name, Toast.LENGTH_SHORT).show();
        }
    //    else
        {
  //          Toast.makeText(getApplicationContext(),"El nombre de usuario o contraseña son incorrectos.", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultame_id_android()
    {
        String id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        label.setText(String.valueOf("DISPOSITIVO: "+id ));

    }
}
