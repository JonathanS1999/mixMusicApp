package com.example.musicx;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicx.background.MusicBackground;

import java.util.Objects;

import static com.example.musicx.MainActivity.musicFilesfavoritos;
import static com.example.musicx.MainActivity.musicFilesrecientes;


public class ActivityRecientes extends AppCompatActivity  {

    private RecienteAdapter musicAdapter;
    private RecyclerView recyclerView;

    public ActivityRecientes(){
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recientes);

        // my toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_recientes);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setTitle("Recientes");
        recyclerView= findViewById(R.id.recyclerView_reciente);
        recyclerView.setHasFixedSize(true);
        if ( musicFilesrecientes != null) {
            musicAdapter = new RecienteAdapter(this, musicFilesrecientes, musicFilesfavoritos);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                    false));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ImageView view= findViewById(R.id.background_recientes);
        MusicBackground.setBackground(view ,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent =  new Intent(this,SearchActivity.class);
        intent.putExtra("search", 1);
        startActivity(intent);
        return true;
    }
}