package com.example.musicx;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicx.background.MusicBackground;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.musicx.MainActivity.musicFilesGeneral;

public class FoldersActivity extends AppCompatActivity {

private RecyclerView recyclerView;
private ImageView setBackground;
private String folderName;
private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);
        toolbar = findViewById(R.id.toolbar);
        setBackground = findViewById(R.id.backgroundActivityFolders);
        recyclerView = findViewById(R.id.recycler_MIF);
        folderName = getIntent().getStringExtra("nameFolder");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setTitle(folderName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<MusicFiles> foldersMusics = new ArrayList<>();
        for ( int i =0 ; i < musicFilesGeneral.size() ;i++){
            File nameFolder = new File(musicFilesGeneral.get(i).getPatch());
            if ( folderName .equals(Objects.requireNonNull(nameFolder.getParentFile()).getName()) ){
                foldersMusics.add(musicFilesGeneral.get(i));
            }
        }
        if ( foldersMusics.size() >0  ){
            AlbumDetailsAdapter albumDetailsAdapter = new AlbumDetailsAdapter(this, foldersMusics);
            recyclerView.setAdapter(albumDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this  , RecyclerView.VERTICAL , false));

        }
        MusicBackground.setBackground(setBackground,this);
    }


}