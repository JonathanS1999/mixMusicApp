package com.example.musicx;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicx.AboutApp.AboutThisApp;
import com.example.musicx.Fragments.MainFragment;
import com.example.musicx.SearchSongs.SearchSongs;
import com.example.musicx.background.MusicBackground;
import com.example.musicx.custom.ActivityItemsCustom;
import com.example.musicx.image.ImageSong;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;
import java.util.ArrayList;

import static com.example.musicx.MusicService.MUSIC_LAST_PLAYED;
import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        SongsFragment.OnSongsListener , NowPlayingFragment.OnNowPlayerListener ,
      OnItemRemove , AlbumFragment.OnAlbumListener , ArtistFragment.OnArtistListener,
        FoldersFragment.OnFoldersListener , OnShowButtonList , SearchSongs.OnFinishSearchAudio {

    public static  boolean SHOW_MINI_PLAYER  ;

    //music files
    public static ArrayList<MusicFiles> musicFilesGeneral ;
    public static ArrayList<MusicFiles> musicFilesrecientes;
    public static ArrayList<MusicFiles> musicFilesfavoritos;
    //lista actual de reproduccion

    //Name file card music
    public static final String MUSIC_FILE_LAST_PLAYED = "LAST_PLAYED";

    // key shared preferences  card music
    public static final String MUSIC_FILE ="STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST_NAME";
    public static final String SONG_NAME = "SONG_NAME";
    public static final String POSITION_NAME="POSITION_NAME";

    //Name File  preferences themes
    public static final String BACKGROND_THEME ="BACKGROUND_THEME";
    public static final String THEME_CUSTOM_BACKGROUND = "THEME_CUSTOM_BACKGROUND";

    //preferences sort order
    public static String MY_SORT_PREF="Sort_Order";

    //key valriables themes
    public static final String BACKGROND ="BACKGROUND" ;

    //variables Now player fragment
    public static String ARTIST_TO_FRAG = null;
    public static String SONG_NAME_TO_FRAG = null;
    public static String PATH_TO_FRAG = null;
    public static String POSITION_TO_FRAG =null;

    private DrawerLayout drawerLayout;
    private ImageView setBackgroud;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    // variables para fragments
   private FragmentManager fragmentManager;
   private FragmentTransaction fragmentTransaction;

    //update now player
    private ImageView card_Np;
    private TextView title_NP;
    private TextView artist_NP;
    private TextView num_NP;
    private ProgressBar progressBar_NP;

    private MainFragment mainFragment;

    private OnUpdateSongs updateSongs;

    //Adapters
    private ArtistAdapter artistAdapter;
    private MusicAdapter musicAdapter;
    private FoldersAdapter foldersAdapter;
    private AlbumsAdapter albumsAdapter;
    private MusicFiles currentfileNowPlayer;
    private int currentPositionNowPlayer =-20;
    private boolean statePlaying;
    private Menu menu;

    private final ActivityResultLauncher<String> requestPermissions = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted ->{
                if( isGranted ){
                    readSong();
                }
            }
    );

    public static final Object object = new Object();
    private static final Object objectSort = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_drawer);
        setSupportActionBar(toolbar);
        //my app bar layout
        AppBarLayout appBarLayout = findViewById(R.id.appbarlayoutmain);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }

        drawerLayout = findViewById(R.id.drawer);
        setBackgroud= findViewById(R.id.backgroundMainActivity);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar, R.string.open, R.string.close
                );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();



        permission();

        // cargar fragment principal
        if(savedInstanceState == null) {
            //inicializar fragments
            mainFragment = new MainFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.containerF, mainFragment,"fragA");
            fragmentTransaction.commit();
        }else{
            fragmentManager = getSupportFragmentManager();
            mainFragment = (MainFragment) fragmentManager.findFragmentByTag("fragA");
        }

    }

    private void setBackgrondTheme(){
        try {
            SharedPreferences preferences = getSharedPreferences(BACKGROND_THEME, MODE_PRIVATE);
            if (preferences.getString(THEME_CUSTOM_BACKGROUND, null) != null) {
                String path = preferences.getString(THEME_CUSTOM_BACKGROUND, null);
                try {
                    Uri uri = Uri.parse(path);
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Drawable drawable = Drawable.createFromStream(inputStream, uri.toString());
                    setBackgroud.setImageDrawable(drawable);
                    ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.backgroundDrawerHeader);
                    imageView.setImageDrawable(drawable);
                } catch (Exception e) {
                    setBackgrondColor();
                    Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
                }

            } else {
                setBackgrondColor();
            }
        }catch (Exception exce){
            Toast.makeText(this, exce+" main 170",Toast.LENGTH_SHORT).show();
        }
    }


    private void setBackgrondColor(){
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.backgroundDrawerHeader);
        MusicBackground.setBackground(setBackgroud, this);
        MusicBackground.setBackground(imageView, this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.home) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerF, mainFragment);
            fragmentTransaction.commit();
        }else if(item.getItemId() ==R.id.temas_drawer) {
            Intent intent = new Intent(this, ActivityItemsCustom.class);
            startActivity(intent);
        }else if(item.getItemId() ==R.id.setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.modeConductor){
            Intent intent = new Intent(this , ActivityModeConductor.class);
                    int pos = Integer.parseInt(POSITION_TO_FRAG );
            if ( currentPositionNowPlayer >= 0  ) pos =currentPositionNowPlayer;
            intent.putExtra("position", pos).putExtra("elemtSelected",4);
            startActivity(intent);
        }else if( item.getItemId() == R.id.about){
            Intent intent = new Intent(this , AboutThisApp.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu_action_bar ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.search ) {
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra("search", 0);
                startActivity(intent);
        }else if(item.getItemId() == R.id.settings) {

        }else if ( item.getItemId() == R.id.themesApp){
                 intent = new Intent(this , ThemesActivity.class);
                startActivity(intent);
        } else if ( item.getItemId()== R.id.sortDate){
            saveSharedPereferencesSort("sortDate");
            updateOrder();
    }else if ( item.getItemId()== R.id.sortNameAZ){
    saveSharedPereferencesSort("sortNameAZ");
    updateOrder();
    }else if ( item.getItemId()== R.id.sortNameZA){
            saveSharedPereferencesSort("sortNameZA");
            updateOrder();
    }else if ( item.getItemId()== R.id.sortSizeAs){
            saveSharedPereferencesSort("sortSizeAs");
            updateOrder();
    }else if ( item.getItemId()== R.id.sortSizeDes){
            saveSharedPereferencesSort("sortSizeDes");
            updateOrder();
    }else if ( item.getItemId()== R.id.sortDateU){
            saveSharedPereferencesSort("sortDateU");
            updateOrder();
        }
        return true;
    }

    private void saveSharedPereferencesSort(String modeOrder){
        SharedPreferences.Editor editor = getSharedPreferences(MY_SORT_PREF,MODE_PRIVATE).edit();
        editor.putString("sorting",modeOrder);
        editor.apply();
    }

    private void updateOrder(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (objectSort ) {
                    try {
                        if (musicFilesGeneral != null) musicFilesGeneral.clear();
                        SearchSongs mySongs = new SearchSongs(MainActivity.this);
                        mySongs.getAllAudio(MainActivity.this);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, e+" 292 ",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        ).start();



    }

    private void deletePreferencesMusic(){
        SharedPreferences preferences = getSharedPreferences(MUSIC_FILE_LAST_PLAYED, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            SharedPreferences preferences = getSharedPreferences(MUSIC_FILE_LAST_PLAYED, MODE_PRIVATE);
            String path = preferences.getString(MUSIC_FILE, null);
            String artist = preferences.getString(ARTIST_NAME, null);
            String song_name = preferences.getString(SONG_NAME, null);
            String position_name = preferences.getString(POSITION_NAME, null);

            if (path != null) {
                SHOW_MINI_PLAYER = true;
                PATH_TO_FRAG = path;
                ARTIST_TO_FRAG = artist;
                SONG_NAME_TO_FRAG = song_name;
                POSITION_TO_FRAG = position_name;
            } else {
                SHOW_MINI_PLAYER = false;
                PATH_TO_FRAG = "";
                ARTIST_TO_FRAG = "";
                SONG_NAME_TO_FRAG = "";
                POSITION_TO_FRAG = "0";
            }
            setBackgrondTheme();
        }catch (Exception e){
            Toast.makeText(this,e+" main 279",Toast.LENGTH_SHORT).show();
        }
    }

     private int busq=0;

    @Override
    public void updateSongs() {
        try {
            new Thread(new Runnable() {// inicia un hilo
                @Override
                public void run() {
                    synchronized (object){ // evita que se sobreescriba y haya conflicto

            MainActivity.this.runOnUiThread(new Runnable() {// hilo principal
                @Override
                public void run() {
                    if (nowPlayerSongs == null && musicFilesGeneral != null) {
                        if (card_Np != null) {
                            Thread threadSearch = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    nowPlayerSongs = new ArrayList<>();
                                    if (SHOW_MINI_PLAYER && PATH_TO_FRAG != null) {
                                        for (busq = 0; busq < musicFilesGeneral.size(); busq++) {
                                            if (musicFilesGeneral.get(busq).getPatch().equals(PATH_TO_FRAG)) {
                                                nowPlayerSongs.addAll(musicFilesGeneral);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        /*
                                                        byte[] art = nowPlayerSongs.get(busq).getPicture();
                                                        if (art != null) {
                                                            Bitmap port = BitmapFactory.decodeByteArray(nowPlayerSongs.get(busq).getPicture(),
                                                                    0, nowPlayerSongs.get(busq).getPicture().length);
                                                            card_Np.setImageBitmap(port);
                                                        } else {
                                                            card_Np.setImageResource(R.drawable.play2);
                                                        }
                                                         */
                                                        ImageSong.setImage(card_Np,MainActivity.this,nowPlayerSongs.get(busq).getPicturePath());
                                                        title_NP.setText(nowPlayerSongs.get(busq).getTitle());
                                                        artist_NP.setText(nowPlayerSongs.get(busq).getArtist());
                                                        num_NP.setText(String.valueOf(busq + 1));
                                                        progressBar_NP.setMax(100);
                                                        progressBar_NP.setProgress(2);
                                                        POSITION_TO_FRAG = String.valueOf(busq);
                                                        Toast.makeText(getApplicationContext(), "si entra", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                break;
                                            }
                                        }
                                    } else {
                                        nowPlayerSongs.addAll(musicFilesGeneral);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    /*
                                                    byte[] art = nowPlayerSongs.get(0).getPicture();
                                                    if (art != null) {
                                                        Bitmap port = BitmapFactory.decodeByteArray(nowPlayerSongs.get(0).getPicture(),
                                                                0, nowPlayerSongs.get(0).getPicture().length);
                                                        card_Np.setImageBitmap(port);
                                                    } else {
                                                        card_Np.setImageResource(R.drawable.play2);
                                                    }*/
                                                    ImageSong.setImage(card_Np,MainActivity.this,nowPlayerSongs.get(0).getPicturePath());

                                                    title_NP.setText(nowPlayerSongs.get(0).getTitle());
                                                    artist_NP.setText(nowPlayerSongs.get(0).getArtist());
                                                    num_NP.setText("1");
                                                    progressBar_NP.setMax(100);
                                                    progressBar_NP.setProgress(0);
                                                    POSITION_TO_FRAG = String.valueOf(0);
                                                }catch (Exception e){
                                                    Toast.makeText(MainActivity.this,e+"",Toast.LENGTH_LONG).show();
                                                }
                                                //Toast.makeText(getApplicationContext(), "si entran2", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                            threadSearch.start();
                        }
                    }
                    //Toast.makeText(getApplicationContext(), "si pasa por aqui " + nowPlayerSongs + " " + SHOW_MINI_PLAYER + " " + PATH_TO_FRAG + " " + card_Np, Toast.LENGTH_SHORT).show();
                }
            });

                    }
                }
            }).start();
        }catch (Exception exception){
            Toast.makeText(this , exception +" updt 304", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setOnUpdate(OnUpdateSongs update) {
        updateSongs=update;
    }

    @Override
    public boolean permissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void removePreferences() {
        //deletePreferencesMusic();
    }

    @Override
    public void songsAdapter(MusicAdapter adapter) {
        musicAdapter =adapter;
    }


    @Override
    public ArrayList<MusicFiles> getMusicFilesGen() {
        return musicFilesGeneral;
    }


    @Override
    public void updateViews(ImageView card, TextView title, TextView artist, TextView num,  ProgressBar progressBar, ArrayList<MusicFiles> nowPlayerSongs ) {
        card_Np = card;
        title_NP= title;
        artist_NP= artist;
        num_NP= num;
        progressBar_NP =progressBar;
        updateSongs();
    }

    @Override
    public void setMusicFileActual(MusicFiles fil) {
        currentfileNowPlayer= fil;
    }

    @Override
    public void setCurrentPosition(int currentPosition) {
        currentPositionNowPlayer =currentPosition;
    }

    @Override
    public void setStatePlaying(boolean statePlaying) {
        this.statePlaying=statePlaying;
    }


    private void permission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            readSong();
        }
    }


    public void readSong(){
        if( musicFilesGeneral == null) {
            SearchSongs mySongs = new SearchSongs(this);
            mySongs.getAllAudio(this);

            /*if(updateSongs != null){
                updateSongs.update();
            }else{
                //deletePreferencesMusic();
            }
             */
        }/*else{
            if(updateSongs != null){
                updateSongs.update();
            }

        }
       updateSongs();*/

    }

    @Override
    public void itemRemove(MusicFiles fil) {
        //elimina este item en los adaptadores
        if( musicAdapter != null) musicAdapter.updateItemRemoved(fil);
        if( albumsAdapter != null) albumsAdapter.updateItemRemove(fil);
        if(artistAdapter != null) artistAdapter.updateItemRemove(fil);
        if(foldersAdapter != null) foldersAdapter.updateItemRemove(fil);
        if(nowPlayerSongs != null) nowPlayerSongs.remove(fil);
        if(musicFilesfavoritos != null) musicFilesfavoritos.remove(fil);
        if(musicFilesrecientes != null) musicFilesrecientes.remove(fil);
        if(musicFilesGeneral != null) musicFilesGeneral.remove(fil);
        if(currentfileNowPlayer == fil){
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("ActionName", "removeFil");
            startService(intent);
        }else{
            if(nowPlayerSongs != null) {
                int i =0;
                for (MusicFiles musicFiles : nowPlayerSongs) {
                    if( musicFiles == currentfileNowPlayer){
                        /*byte[] art = nowPlayerSongs.get(i).getPicture();
                        if (art != null) {
                            Bitmap port = BitmapFactory.decodeByteArray(nowPlayerSongs.get(i).getPicture(),
                                    0, nowPlayerSongs.get(i).getPicture().length);
                            card_Np.setImageBitmap(port);
                        } else {
                            card_Np.setImageResource(R.drawable.play2);
                        }*/
                        ImageSong.setImage(card_Np,MainActivity.this,nowPlayerSongs.get(i).getPicturePath());

                        title_NP.setText(nowPlayerSongs.get(i).getTitle());
                        artist_NP.setText(nowPlayerSongs.get(i).getArtist());
                        num_NP.setText(String.valueOf(i + 1));
                        if (!statePlaying) {
                            progressBar_NP.setMax(100);
                            progressBar_NP.setProgress(0);
                        }
                        SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE)
                                .edit();
                        editor.putString(POSITION_NAME, i + "");
                        editor.putString(MUSIC_FILE, nowPlayerSongs.get(i).getPatch());// editar ruta del archivo
                        editor.putString(SONG_NAME, nowPlayerSongs.get(i).getTitle()); // editar mobre de la cancion
                        editor.putString(ARTIST_NAME, nowPlayerSongs.get(i).getArtist()); // editar mobre del artista
                        editor.apply();
                Intent intent = new Intent(this, MusicService.class);
                intent.putExtra("ActionName", "upDatePosition").putExtra("newPosition",i);
                startService(intent);
                POSITION_TO_FRAG=String.valueOf(i);
                break;
                    }
                    i++;
                }
            }
        }
    }

    @Override
    public void albumAdapter(AlbumsAdapter adapter) {
        albumsAdapter =adapter;
    }

    @Override
    public void artistAdapter(ArtistAdapter adapter) {
        artistAdapter = adapter;
    }

    @Override
    public void onFoldersAdaper(FoldersAdapter adapter) {
        foldersAdapter=adapter;
    }

    @Override
    public void showButton(boolean show) {
        FloatingActionButton btn = findViewById(R.id.floatingButton);
        if(show){
            btn.setVisibility(View.VISIBLE);
        } else{
            btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAudio(ArrayList<MusicFiles> tempAudioList){
        musicFilesGeneral = tempAudioList;

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
        try {

            //if (updateSongs != null) updateSongs.update();
            if (musicAdapter != null)
                musicAdapter.updateList(musicFilesGeneral);
            if (albumsAdapter != null)
                albumsAdapter.updateList(musicFilesGeneral);
            if (artistAdapter != null)
                artistAdapter.updateList(musicFilesGeneral);
            if (foldersAdapter != null)
                foldersAdapter.updateList(musicFilesGeneral);

            updateSongs();

        }catch (Exception e){
            Toast.makeText(MainActivity.this," 621 -- "+e,Toast.LENGTH_LONG).show();
        }
            }
        });
    }

}