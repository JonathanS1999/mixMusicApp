package com.example.musicx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicx.background.MusicBackground;
import com.example.musicx.image.ImageSong;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static com.example.musicx.MainActivity.musicFilesfavoritos;
import static com.example.musicx.MainActivity.musicFilesGeneral;
import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;

public class AlbumDetails extends AppCompatActivity {

private RecyclerView recyclerView;
private ImageView albumPhoto;
private String albumName = "-";
private TextView titleSongs, cantSongs;
private ArrayList <MusicFiles> albumSongs ;
private AlbumDetailsAdapter albumDetailsAdapter;
private ImageView setBackground;
private RelativeLayout relativeLayout;
private AppBarLayout appBarLayout;
private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        // my toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_album_details);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         appBarLayout = findViewById(R.id.appbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        setBackground = findViewById(R.id.backgroundAlbumDetails);
        relativeLayout = findViewById(R.id.relativeLayout);
        recyclerView = findViewById(R.id.reciclerViewAlbum);
        albumPhoto = findViewById(R.id.albumMoto);
        titleSongs = findViewById(R.id.titleSong);
        cantSongs = findViewById(R.id.cantSong);
        initAppBar();
        Objects.requireNonNull(getSupportActionBar()).setTitle(albumName);

    }

    private void initAppBar(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                try{
                if(Math.abs(verticalOffset)== appBarLayout.getTotalScrollRange()){
                    collapsingToolbarLayout.setTitle(albumName);
                    relativeLayout.setVisibility(View.GONE);
                }else if ( verticalOffset ==0){
                    collapsingToolbarLayout.setTitle("");
                    relativeLayout.setVisibility(View.VISIBLE);
                }else{
                    collapsingToolbarLayout.setTitle(albumName);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                }catch (Exception e ){
                    Toast.makeText(AlbumDetails.this, e+" album details ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MusicBackground.setBackground(setBackground, this);
         inicializerAlbumsOrArtist();
    }

    private void inicializerAlbumsOrArtist(){

    try {


        //obtener si type ( comando artista o albums )
        int type = getIntent().getIntExtra("type", 0);
        //nombre de artista o album
        albumName = getIntent().getStringExtra("albumName");
        if (albumSongs != null) {
            albumSongs.clear();
        }
            albumSongs = new ArrayList<>();

        //condicional : buscar por artista o por album
        if (type == 1) {//artista

            for (int i = 0; i < musicFilesGeneral.size(); i++) {
                if (albumName.equals(musicFilesGeneral.get(i).getArtist())) {
                    albumSongs.add(musicFilesGeneral.get(i));
                }
            }

        } else {//album

            for (int i = 0; i < musicFilesGeneral.size(); i++) {
                if (albumName.equals(musicFilesGeneral.get(i).getAlbum())) {
                    albumSongs.add(musicFilesGeneral.get(i));
                }
            }
        }

        byte[] image = null; // arreglo de bytes
        // buscar si este album tiene portada
        for (MusicFiles musicFilesX : albumSongs) {
            image = ImageSong.getBitmapPicture( musicFilesX.getPicturePath());
            if (image != null) break;
        }

        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    image, 0, image.length);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            drawable.setCornerRadius(12);
            albumPhoto.setImageDrawable(drawable);

        } else {
            albumPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play2));
        }


        if (albumSongs.size() > 0 ) {
            albumDetailsAdapter = new AlbumDetailsAdapter(this, albumSongs);
            recyclerView.setAdapter(albumDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }

        String string = String.format(Locale.US, "%d canciones ", albumSongs.size());
        titleSongs.setText(albumName);
        cantSongs.setText(string);

    }catch (Exception e ){
        Toast.makeText(this, e+" album details ",Toast.LENGTH_LONG).show();
    }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
            if(item.getItemId() == R.id.search ) {
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra("search", 3);
                startActivity(intent);
            }else if( item.getItemId() ==  R.id.settings ){
                Toast.makeText(this," configuracion ",Toast.LENGTH_LONG).show();
            }else if( item.getItemId() ==  R.id.themesApp){
                Toast.makeText(this," temas ",Toast.LENGTH_LONG).show();
            }
        return true;
    }

}
