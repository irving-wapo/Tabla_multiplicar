package com.protopo.previewplace;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.VideoView;

public class Ayuda_comentarios extends AppCompatActivity
{
    TabHost TbH;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda_comentarios);
        pestañas();
    }

    public void botonaso(View view)
    {
       VideoView     mividio = (VideoView) findViewById(R.id.videoView3);
        // String uriPath = "android.resource://com.example.toyo.playvideo/" + R.raw.movie;
        String uriPath = "rtsp://r17---sn-q4f7snsl.googlevideo.com/Cj0LENy73wIaNAmk3cJBg-iaXhMYDSANFC1q4DhXMOCoAUIASARg48_D5NbgqJhXigELM3l4VlViU0MtWncM/69CC1F15406BE6B3F18928F0B5CB9FA3DE85223D.83E71F61F1F33B41F2E792ED23B8A98BC7CDD514/yt6/1/video.3gp";
        Uri uri2 = Uri.parse(uriPath);
        mividio.setVideoURI(uri2);
        mividio.requestFocus();
        mividio.start();
    }

    public void paralo_hay(View view)
    {
        VideoView     mividio = (VideoView) findViewById(R.id.videoView3);
        // String uriPath = "android.resource://com.example.toyo.playvideo/" + R.raw.movie;
        String uriPath = "rtsp://r17---sn-q4f7snsl.googlevideo.com/Cj0LENy73wIaNAmk3cJBg-iaXhMYDSANFC1q4DhXMOCoAUIASARg48_D5NbgqJhXigELM3l4VlViU0MtWncM/69CC1F15406BE6B3F18928F0B5CB9FA3DE85223D.83E71F61F1F33B41F2E792ED23B8A98BC7CDD514/yt6/1/video.3gp";
        Uri uri2 = Uri.parse(uriPath);
        mividio.setVideoURI(uri2);
        mividio.requestFocus();
        mividio.stopPlayback();
    }


    private void pestañas()
    {
        TbH = (TabHost) findViewById(R.id.mi_tab);
        TbH.setup();

        TabHost.TabSpec tab1 = TbH.newTabSpec("tab1");
        tab1.setIndicator(getString(R.string.lbl_acerca_uno ));
        tab1.setContent(R.id.preguntas);

        TabHost.TabSpec tab2 = TbH.newTabSpec("tab2");
        tab2.setIndicator(getString(R.string.lbl_acerca_dos ));
        tab2.setContent(R.id.vidio);

        TbH.addTab(tab1);
        TbH.addTab(tab2);
    }
}
