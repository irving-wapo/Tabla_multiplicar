package com.protopo.previewplace;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class inicio extends Activity{


    long starttime = 0;
    final android.os.Handler h = new android.os.Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            long millis = System.currentTimeMillis() - starttime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;

            if(seconds == 0)
            {
                //Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dos);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transparencia);
                img_inicio.startAnimation(animation);

            }

            if(seconds == 0)
            {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomzoom);
                label_inicio .startAnimation(animation);

            }
            if(seconds == 5 )
            {
                label_inicio.setTextSize(29);
            }

            if(seconds == 6)
            {
                btn_ini.setVisibility(View.VISIBLE);
            }

            if(seconds == 7)
            {
                btn_ini.setTextSize(17);
            }

            if(seconds == 9)
            {

            }
            seconds     = seconds % 60;
            //text.setText("timer" + String.format("%d:%02d", minutes, seconds));
            return false;
        }
    });


    class firstTask extends TimerTask
    {
        @Override
        public void run() {
            h.sendEmptyMessage(0);
        }
    }

    Timer timer = new Timer();


TextView label_inicio;  ImageView img_inicio; Button btn_ini;

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previewplace);

        img_inicio = (ImageView) findViewById(R.id.img_ini);
        img_inicio.setImageResource(R.mipmap.pplace);

        label_inicio = (TextView) findViewById(R.id.mensaje_ini);
        label_inicio.setTypeface(null, Typeface.BOLD);


        btn_ini = (Button)findViewById(R.id.btn_ini);
        btn_ini.setVisibility(View.INVISIBLE);

        starttime = System.currentTimeMillis();
        timer = new Timer();
        timer.schedule(new firstTask(), 0,500);


        btn_ini.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                b = (Button) v;

                if(b.getText().equals("Iniciar")){
                    Intent inten  = new Intent(getApplicationContext(), registro_usuarios.class );
                    startActivityForResult(inten ,0 );

                    b.setVisibility(View.INVISIBLE);
                    b.setText("Salir");
                    b.setVisibility(View.VISIBLE);






                }else{
                    timer.cancel();
                    timer.purge();
                        System.exit(0);
                //starttime = System.currentTimeMillis();
                //timer = new Timer();
                //timer.schedule(new firstTask(), 0,500);
                //b.setText("stop");
                }



            }
        });



        }
    }
