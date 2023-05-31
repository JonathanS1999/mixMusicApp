package com.example.musicx;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicx.background.MusicBackground;

public class SplashScreem extends AppCompatActivity {
   private  ImageView imagen  ;
    private Thread hilo ;
    private ImageView setBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screem);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        imagen = findViewById (R. id. iconp);
        imagen. setImageResource(R.drawable.iconp);
        setBackground=findViewById(R.id.backgroundSplashScreem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicBackground.setBackground(setBackground,this);
        hilo = new  Thread (){
            @Override
            public void run() {
                try {
                    sleep ( 800);
                    Intent intencion = new Intent( getApplicationContext (), MainActivity. class);
                    startActivity( intencion);
                    finish();
                }catch (InterruptedException e ){
                    Toast.makeText(getApplicationContext(), e+"splasyScreem ",Toast.LENGTH_LONG).show();
                }
            }
        };
        hilo. start();
    }
}