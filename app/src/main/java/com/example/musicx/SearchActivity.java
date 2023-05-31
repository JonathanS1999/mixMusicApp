package com.example.musicx;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicx.background.MusicBackground;
import com.example.musicx.stylesFonts.SearchSpannables;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.musicx.AlbumDetailsAdapter.albumFilesTarget;
import static com.example.musicx.MainActivity.musicFilesGeneral;
import static com.example.musicx.MainActivity.musicFilesfavoritos;
import static com.example.musicx.MainActivity.musicFilesrecientes;
import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener  {

    private SearchAdapter searchAdapter;
    private ImageView setBackground ;
    private LinearLayout linearLayout;
    private ArrayList<MusicFiles> musicSearches ;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setBackground = findViewById(R.id.backgroundSearchActivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setPadding(0,0,0,0);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Busqueda");
        RecyclerView recyclerViewS = findViewById(R.id.recyclerSE);
        SearchView searchView = findViewById(R.id.searchElements);
        linearLayout = findViewById(R.id.linearYoutube);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
        searchView.onActionViewExpanded();
        recyclerViewS.setHasFixedSize(true);
        inicializerSongsSearching();
        searchAdapter = new SearchAdapter(this, this, musicSearches );
        recyclerViewS.setAdapter(searchAdapter);
        recyclerViewS.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        title = findViewById(R.id.searchTitle);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this , WebViewYoutube.class);
                intent.putExtra("url",title.getText());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicBackground.setBackground(setBackground,this);
    }


    private void inicializerSongsSearching(){
        int idSongs = getIntent().getIntExtra("search", 0);
        switch (idSongs) {
            case 1:
                musicSearches = musicFilesrecientes;
                break;
            case 2:
                musicSearches = musicFilesfavoritos;
                break;
            case 3:
                musicSearches = albumFilesTarget;
                break;
            case 4:
                musicSearches = nowPlayerSongs;
                break;
            default:
                musicSearches = musicFilesGeneral;
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            String userInput = newText.toLowerCase();
            ArrayList<MusicFiles> myFiles = new ArrayList<>();
            ArrayList<SearchSpannables> spannables= new ArrayList<>();
            for (MusicFiles song : musicSearches) {
                if (song.getTitle().toLowerCase().contains(userInput)) {
                    myFiles.add(song);
                    SpannableString spannable= new SpannableString(song.getTitle());
                    SpannableString spannableB=new SpannableString(song.getArtist());

                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(238,59,5));
                    spannable.setSpan(colorSpan,song.getTitle().toLowerCase().indexOf(userInput),song.getTitle().toLowerCase().indexOf(userInput)+userInput.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (song.getArtist().toLowerCase().contains(userInput)){
                        ForegroundColorSpan colorSpanB= new ForegroundColorSpan(Color.rgb(238,59,5));
                        spannableB.setSpan(colorSpanB,song.getArtist().toLowerCase().indexOf(userInput),song.getArtist().toLowerCase().indexOf(userInput)+userInput.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    spannables.add(new SearchSpannables(spannable,spannableB));
                }else if (song.getArtist().toLowerCase().contains(userInput)) {
                    myFiles.add(song);
                    SpannableString spannable= new SpannableString(song.getArtist());
                    SpannableString spannableB=new SpannableString(song.getTitle());

                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(238,59,5));
                    spannable.setSpan(colorSpan,song.getArtist().toLowerCase().indexOf(userInput),song.getArtist().toLowerCase().indexOf(userInput)+userInput.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (song.getTitle().toLowerCase().contains(userInput)){
                        ForegroundColorSpan colorSpanB= new ForegroundColorSpan(Color.rgb(238,59,5));
                        spannableB.setSpan(colorSpanB,song.getTitle().toLowerCase().indexOf(userInput),song.getTitle().toLowerCase().indexOf(userInput)+userInput.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    spannables.add(new SearchSpannables(spannableB,spannable));

                }
            }
            if (searchAdapter != null) {
                    if (userInput.equals("")){
                        searchAdapter.setSpannable(null);
                        searchAdapter.updateList(musicSearches);
                    }else{
                        searchAdapter.setSpannable(spannables);
                        searchAdapter.updateList(myFiles);
                    }
            }
            linearLayout.setVisibility(View.VISIBLE);
            if (title!= null) {
                title.setText(newText);
            }
        } catch (Exception e) {
            Toast.makeText(this, e + " error 72", Toast.LENGTH_LONG).show();
        }
        return true;
    }

}