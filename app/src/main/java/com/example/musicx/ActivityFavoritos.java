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

import java.util.ArrayList;
import java.util.Objects;

import static com.example.musicx.MainActivity.musicFilesfavoritos;

public class ActivityFavoritos extends AppCompatActivity  {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        // my toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_favorites);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setTitle("Favoritos");

        RecyclerView recyclerView = findViewById(R.id.recyclerView_favoritos);
        recyclerView.setHasFixedSize(true);

        if ( musicFilesfavoritos != null) {
            FavoritoAdapter musicAdapter = new FavoritoAdapter(this, musicFilesfavoritos);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                    false));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView view= findViewById(R.id.backgroundFav);
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
        startActivity(intent);
        return true;
    }
}