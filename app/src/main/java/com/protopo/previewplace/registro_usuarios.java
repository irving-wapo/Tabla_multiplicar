package com.protopo.previewplace;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class registro_usuarios extends AppCompatActivity
{
    private  TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);

        label = (TextView) findViewById(R.id.textView9);
        consultame_id_android();
    }

    public void btn_registro_click(View view) 
    {

        Intent inicio = new Intent(registro_usuarios.this, MainActivity.class );
        startActivity(inicio);
        //this.onResume();
        //super.finishActivity(1);
        //Toast.makeText(getApplicationContext(), "En unos momentos recibira un MSG  de conformacion por favor espere un momento...", Toast.LENGTH_SHORT).show();
    }

    public void consultame_id_android()
    {
        String id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        label.setText(String.valueOf("DISPOSITIVO: "+id ));

    }
}
