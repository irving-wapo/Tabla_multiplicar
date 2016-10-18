package com.protopo.previewplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class acerca extends AppCompatActivity {

    ImageView img,  img2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca);
        img = (ImageView) findViewById(R.id.imageView2);
        img.setImageResource(R.mipmap.icono);

        img2 = (ImageView) findViewById(R.id.imageView3);
        img2.setImageResource(R.mipmap.acercaden);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.giro);
        img.startAnimation(animation);
        ///actualizando datos
    }
}
