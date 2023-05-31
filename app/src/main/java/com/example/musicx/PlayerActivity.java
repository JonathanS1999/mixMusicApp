package com.example.musicx;

import static com.example.musicx.AlbumDetailsAdapter.albumFilesTarget;
import static com.example.musicx.MainActivity.musicFilesGeneral;
import static com.example.musicx.MainActivity.musicFilesfavoritos;
import static com.example.musicx.MainActivity.musicFilesrecientes;
import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;
import static com.example.musicx.NowPlayingFragment.playerSmall;
import static com.example.musicx.SearchAdapter.mFilesSearchView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.background.MusicBackground;
import com.example.musicx.controlInicService.InicServiceMusic;
import com.example.musicx.converTimes.ConvertTime;
import com.example.musicx.custom.AdaptaBleFramePlayerCard;
import com.example.musicx.custom.ViewCardImg;
import com.example.musicx.image.ImageSong;
import com.example.musicx.playControls.PlayMusicControls;
import com.example.musicx.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class PlayerActivity extends AppCompatActivity implements ActionPlaying , ServiceConnection ,
        ListPlayerAdapter.ListPlayerAdaperInterface , FragmentPlayerCardInterface ,
        Fragment_Player_list.FragmentListPlayerInterface , AlertBottomSeeDialog.OnSpeedChangeListener,
        MenuPlayerFragment.OnMenuPlayer , View.OnClickListener , InicServiceMusic.OnIniciMusicServiceListener {

 // variables para mostrar detalles de la cancion
 private TextView song_name , artist_name , duration_played , duration_total , numCanciones ;

 //variables de controles
 private ImageView  nextBtn , prevBtn  , shuffleBtn , repeatBtn  ;
 private ImageView playPauseBtn;
 private SeekBar seekBar;
 private ImageView setBackgroud , bgGradient , imageCard;
 private ImageView imgPort8 = null;
 private LinearLayout frameCircle;

private int elementSelected=0;
private  ArrayList < MusicFiles > listSongs;
private  MusicService musicService ;
private SectionsPagerAdapter sectionsPagerAdapter ;
private ViewPager2 viewPager2 ;
private LinearLayout linearPuntos ;
private boolean booleanSavedInstaceState=false;

private Toolbar toolbar;
private PlayMusicControls playMusicControls;
private InicServiceMusic inicServiceMusic;
private ConvertTime convertTime;
private boolean barPress;
private ListPlayerAdapter playerAdapter;
private RecyclerView recyclerView;
private SeekBar seekBarSpeed;
private Context context;
private  AdaptableBackground adaptableBackground;
private SettingsPlayingPreferences settings;
private boolean setDefaultBackground;
private String path=null;
private Handler handler =null;


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState ){
        super. onSaveInstanceState( outState );
        outState. putInt("position", musicService.getPosition() );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        toolbar = findViewById(R.id.toolbal_player);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setPadding(0,0,0,0);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        booleanSavedInstaceState= savedInstanceState != null; // si hay alguna pocision gurdada

        if(playerSmall != null ) playerSmall = null;
        handler=new Handler(getMainLooper());
        settings= new SettingsPlayingPreferences(this);

        try {
            initViews();
            updateSongs();
            convertTime = new ConvertTime();
            context = this;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                updateViewPager();
                viewPager2.setCurrentItem(2, true);
            }

        }catch (Exception e){
            Toast.makeText(this,e+" player Activ 114 ",Toast.LENGTH_LONG).show();
        }

    }


    private void updateSongs(){
        elementSelected= getIntent().getIntExtra("elemtSelected", -1);
        switch (elementSelected){
            case 1 :  listSongs = musicFilesrecientes;
                break;
            case 2:  listSongs = musicFilesfavoritos ;
                break;
            case 3:  listSongs = albumFilesTarget;
                break;
            case 4 : listSongs = nowPlayerSongs;
                break;
            case 5: listSongs = mFilesSearchView;
                break;
            case 12: // listSongs = musicFilesElementSelected;
                break;
            default:  listSongs = musicFilesGeneral;
                break;
        }
    }

   public void updateViewPager() {
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(sectionsPagerAdapter);
        linearPuntos = findViewById(R.id.linearPuntos);
       if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT) {
           agregarIndicadorPuntos(0);
           viewPager2.registerOnPageChangeCallback(viewListener2);
       }
   }

    private void initViews (){
        setBackgroud = findViewById(R.id.backgroundActivityPlayer);
        viewPager2 = findViewById(R.id.viewpager_player);

        if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) {
            updateViewPager();
            viewPager2.setCurrentItem(1);
            viewPager2.setUserInputEnabled(false);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if(fm.findFragmentByTag("fmRight")==null){
                ft.add(R.id.containerRight , new FragmentPlayerCard_L(),"fmRight").commit();
            }
            }

        inicRelativeCard();
    }

    @Override
    public void updatePager(){
        viewPager2.setCurrentItem(0);
    }

    @Override
    public void setFrameCircle(LinearLayout frameCircle) {
        this.frameCircle=frameCircle;
    }


    private  void inicRelativeCard() {
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            RelativeLayout relativeLayoutCard = findViewById(R.id.card);
            FrameLayout relativeLayoutplayer = findViewById(R.id.containerRight);
            int ancho;
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                ancho = (int) (displayMetrics.widthPixels / displayMetrics.density);
            }else {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                ancho = displayMetrics.widthPixels;
            }
            RelativeLayout.LayoutParams paramsA = new RelativeLayout.LayoutParams(ancho / 2, RelativeLayout.LayoutParams.MATCH_PARENT);
            paramsA.addRule(RelativeLayout.ALIGN_PARENT_LEFT ,RelativeLayout.TRUE);
            paramsA.addRule(RelativeLayout.BELOW,toolbar.getId());
            relativeLayoutCard.setLayoutParams(paramsA);

            RelativeLayout.LayoutParams paramsB= new RelativeLayout.LayoutParams(ancho / 2, RelativeLayout.LayoutParams.MATCH_PARENT);
            paramsB.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsB.addRule(RelativeLayout.RIGHT_OF, relativeLayoutCard.getId());
            paramsB.addRule(RelativeLayout.BELOW,toolbar.getId());
            relativeLayoutplayer.setLayoutParams(paramsB);
        }
    }

    private void agregarIndicadorPuntos(int pos ) {
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            TextView[] puntosSlide = new TextView[2];
            linearPuntos.removeAllViews();
            for (int i = 0; i < puntosSlide.length; i++) {
                puntosSlide[i] = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                params.setMargins(2, 0, 2, 0);
                puntosSlide[i].setLayoutParams(params);
                puntosSlide[i].setBackground(ContextCompat.getDrawable(this, R.drawable.incators_points_a));
                linearPuntos.addView(puntosSlide[i]);
            }
            puntosSlide[pos].setBackground(ContextCompat.getDrawable(this, R.drawable.indicators_points_b));
        }
    }

    ViewPager2.OnPageChangeCallback viewListener2 = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            agregarIndicadorPuntos(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

private void inicializerSeekBar(){//verificar llamadas a este metodo

    try {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration_played.setText(convertTime.getMilliSecondMinutHors(musicService.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                barPress = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (musicService.getClickCompletado() == 0) {
                    musicService.seekTo(seekBar.getProgress());
                }
                barPress = false;
            }
        });
    }catch (Exception e){
        Toast.makeText(getApplicationContext(),e+" 243 ",Toast.LENGTH_LONG).show();
    }
}

    @Override
    public void setMaxSeekBar(int maxSeekBar) {
    try {
        seekBar.setMax(maxSeekBar);
    } catch(Exception e){
        Toast.makeText(this, e+" 337",Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    public void setSeekBarProgres(int progres) {
            try{
                if (! barPress ) {
                    seekBar.setProgress(progres);  // actualiza la barra de progreso
                }
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), e+" 337",Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onClick( View view ){
        int id = view.getId();
        if(playMusicControls != null) playMusicControls.onClick(id);
        }

    @Override
    public void updateControlRandom(){
     if (playMusicControls != null) playMusicControls.updateControlRandom();
    }

    @Override
    public void updateControlRepeat( ){
    if(playMusicControls != null) playMusicControls.updateControlRepeat();
    }


    @Override
    protected void onResume() {// me vinculo con el servicio
        super.onResume();
        //new MusicBackground(setBackgroud, this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
            musicService = myBinder.getService();
            inicOnService();
        }


        private void inicOnService(){
    try{
            inicializerSeekBar();
            musicService.setCallBack(this);
            musicService.getSettingsPreferences();
            playMusicControls.setMusicService(musicService);
            if(inicServiceMusic == null) {
                inicServiceMusic = new InicServiceMusic(this, getIntent(), musicService, elementSelected,
                        booleanSavedInstaceState, listSongs, this, 1, convertTime);
                inicServiceMusic.setSeekBar(seekBar);
                inicServiceMusic.iniciarVistaReproduccion();
            }else{
                seekBar.setMax(musicService.getDuration());
                updateInfoGeneral();
            }

            if(recyclerView != null && playerAdapter != null) {
                    recyclerView.scrollToPosition(musicService.getPosition());
                    musicService.setListPlayerAdapter(playerAdapter);
                    recyclerView =null;
                    playerAdapter = null;
                }

            if(seekBarSpeed != null){
                seekBarSpeed.setProgress((int) (musicService.getSpeedMusic() * 100));
            }

            playMusicControls.isFavorite();

            updateControlRepeat();
        }catch (Exception e){
            Toast.makeText(this," "+e.getMessage() +"play music 304 ",Toast.LENGTH_SHORT).show();
        }
    }


@Override
    public void updateInfoGeneral(){

            try {

                Toast.makeText(this,settings.getMyThemePlayer()+"",Toast.LENGTH_SHORT).show();

                if(settings.getMyThemePlayer() > 4 ){
                imgPort8 = imageCard;
                imageCard.setImageDrawable(null);

                // imagen y degradado de fondo ( activity )
                bgGradient = findViewById(R.id.player_gradient);
                imageCard = findViewById(R.id.playercard);
                }else{ // si el tema establecido es menor que 4 , se tomará el color gradiente
                       // por defecto o el que el usuario haya establecido
                   ( (ImageView)findViewById(R.id.player_gradient) ).setImageDrawable(null);
                    ( (ImageView)findViewById(R.id.playercard) ).setImageDrawable(null);
                }

                if(settings.getMyThemePlayer() != 12){
                playMusicControls.setImageCard(imageCard);
                }
                Toast.makeText(getApplicationContext()," estamos actualizando",Toast.LENGTH_SHORT).show();

                String string = String.format(Locale.US,"%s", musicService.get(musicService.getPosition()).getTitle().replace(".mp3", "").replace(".wav", ""));
                song_name.setText(string);
                artist_name.setText(musicService.get(musicService.getPosition()).getArtist());
                song_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                artist_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                song_name.setSelected(true);
                artist_name.setSelected(true);
                duration_total.setText(convertTime.getMilliSecondMinutHors(Long.parseLong(musicService.get(musicService.getPosition()).getDuration())));
                string = String.format(Locale.US,"%d / %d ",(musicService.getPosition() + 1),  musicService.sizeMusics());
                numCanciones.setText(string);

                if(settings.getMyThemePlayer() != 11 && settings.getMyThemePlayer() != 12){
                    playMusicControls.actualizarPortada('A');

                    if(imgPort8 != null){
                        playMusicControls.getSettingsTheme(3);
                        playMusicControls.setImageCard(imgPort8);
                        playMusicControls.actualizarPortada('A');
                        playMusicControls.getSettingsTheme(0);
                        imageCard=imgPort8;
                        imgPort8=null;
                    }
                }

                if(musicService.isPlaying()){
                    playPauseBtn.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_pause));
                }else{
                    seekBar.setProgress(musicService.getCurrentPosition());
                    playPauseBtn.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_play));
                }


                if (settings.getMyThemePlayer() >=3 ) {

                    if (adaptableBackground == null) {
                        adaptableBackground = new AdaptableBackground(
                                this,
                                setBackgroud,
                                bgGradient,
                                song_name,
                                artist_name,
                                duration_played,
                                duration_total,
                                numCanciones, settings.getMyThemePlayer());
                        adaptableBackground.setPosition(musicService.get(musicService.getPosition()));
                    }

                    // ten en cuenta la siguiente linea
                   // Glide.with(context).load(uri).placeholder(drawable).override(20,20).into(imageBackground);
                    String pathMusic=musicService.get(musicService.getPosition()).getPicturePath();
                    if(settings.getMyThemePlayer() ==11){
                        if(path== null || !path.equals(pathMusic)) {
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(musicService.get(musicService.getPosition()).getPicturePath());
                            byte[] picture = retriever.getEmbeddedPicture();
                            retriever.release();
                            if (picture != null) {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(picture,
                                        0, picture.length);
                                Drawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeByteArray(picture,
                                        0, picture.length));
                                Glide.with(context).load(bitmap).placeholder(drawable).override(5,5).into(setBackgroud);
                                path = pathMusic;
                            }else{
                                MusicBackground.setBackground(setBackgroud,getApplicationContext());
                            }
                        }
                    }else if (settings.getMyThemePlayer() ==10){
                        // adapta el color del fondo con la portada de la imagen
                        adaptableBackground.inicFondo();
                    }


                    /*if (musicBackground == null )
                        musicBackground = new MusicBackground(setBackgroud,this);

                     */
                }else{

                    if( !setDefaultBackground ){
                        MusicBackground.setBackground(setBackgroud,this);
                        setDefaultBackground=true;
                    }

                }

                if(frameCircle != null){
                    if (frameCircle.getChildCount() > 0) frameCircle.removeAllViews();

                    if(settings.getMyThemePlayer() == 10 || settings.getMyThemePlayer() == 11){
                      new AdaptaBleFramePlayerCard(this,settings,frameCircle,musicService,0);
                    }else if(settings.getMyThemePlayer() == 12){
                        byte[] picture= ImageSong.getBitmapPicture(musicService.get(musicService.getPosition()).getPicturePath());
                        Bitmap bitmap = BitmapFactory.decodeByteArray(picture,0,picture.length);
                        frameCircle.addView(new ViewCardImg(this,bitmap));
                    }

                }

            }catch (Exception exception){
                Toast.makeText(context,exception+" 291 info general",Toast.LENGTH_SHORT).show();
            }
    }


    @Override
    public void onServiceDisconnected(ComponentName name) {
    musicService =null;
    Toast.makeText(this, "service disconnect", Toast.LENGTH_LONG).show();
    }


    @Override
    public void setFavoriteMusic() {
    try {
        playMusicControls.setFavoriteMusic();
    }catch(Exception e){
        Toast.makeText(this, e+" 395",Toast.LENGTH_SHORT).show();
    }
    }


    @Override
    public void updateViewPlayPause( int resource) {
    if(playPauseBtn != null)
        playPauseBtn.setImageResource(resource);
    }


    @Override
    public void setPositionPlayer(int position) {
    try {
        musicService.setPosition(position);
        musicService.btn_nextClicked();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            viewPager2.setCurrentItem(2, true);
    }catch (Exception e){
        Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    public void itemRemove(int position) {
        if(musicService !=null) musicService.itemRemove(position);
    }

    @Override
    public int getPositionPlayer() {
    return getPositionForList();
    }

    @Override
    public int getPositionForList() {
        if (musicService == null) return 0;
        if(musicService.sizeMusics() <=0 ) return 0;
        return musicService.getPosition();
    }

    @Override
    public void setPlayerListAdapter(ListPlayerAdapter adapter, RecyclerView recyclerView) {
    try{
        if(musicService != null) musicService.setListPlayerAdapter(adapter);
        else playerAdapter=adapter;
        this.recyclerView=recyclerView;
    }catch(Exception e){
        Toast.makeText(this, e+" 463",Toast.LENGTH_SHORT).show();
    }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player_action_bar ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        if(item.getItemId() ==R.id.searchp ){
                intent = new Intent(this , SearchActivity.class);
                intent.putExtra("search",4);
                startActivity(intent);
        }else if( item.getItemId()==R.id.ecualizerp){
           Toast.makeText(this," ecualizer ",Toast.LENGTH_LONG).show();
        }
        return true;
    }
    @Override
    public void showMyMenu(){
        String string = String.format(Locale.US,"%s", musicService.get(musicService.getPosition()).getTitle().replace(".mp3", "").replace(".wav", ""));
        MenuPlayerFragment menuPlayer = new MenuPlayerFragment(string);
        menuPlayer.show(getSupportFragmentManager(), "Menu");
    }

    @Override
    public void onClickPlayerCard(View v) {
        onClick(v);
    }

    @Override
    public void inicializer(TextView song_name, TextView artist_name, TextView duration_played,
                            TextView duration_total, TextView numCanciones, ImageView nextBtn,
                            ImageView prevBtn, ImageView shuffleBtn, ImageView repeatBtn,
                            ImageView playPauseBtn, SeekBar seekBar , ImageView imageMusic,
                            ImageView favorite , ImageView bgGradient ) {
    try {
        this.song_name = song_name;
        this.artist_name = artist_name;
        this.duration_played = duration_played;
        this.duration_total = duration_total;
        this.numCanciones = numCanciones;
        this.nextBtn = nextBtn;
        this.prevBtn = prevBtn;
        this.shuffleBtn = shuffleBtn;
        this.repeatBtn = repeatBtn;
        this.playPauseBtn = playPauseBtn;
        this.seekBar = seekBar;
        this.bgGradient = bgGradient;
        imageCard=imageMusic;

        if (musicService == null) {
            playMusicControls = new PlayMusicControls(this);
            playMusicControls.inicializerControlRepeats(repeatBtn, shuffleBtn);
           // playMusicControls.setImageCard(imageMusic);
            playMusicControls.setImageFavorte(favorite);

            Intent intent = new Intent(this, MusicService.class);
            bindService(intent, this, BIND_AUTO_CREATE);

        } else {
            playMusicControls.inicializerControlRepeats(repeatBtn, shuffleBtn);
            //playMusicControls.setImageCard(imageMusic);
            playMusicControls.setImageFavorte(favorite);
            inicOnService();
        }
    }catch (Exception e){
        Toast.makeText(this,e+" ",Toast.LENGTH_LONG).show();
    }
    }

    @Override
    public void showAlertDialogBottom(){
        AlertBottomSeeDialog menuSpeed = new AlertBottomSeeDialog();
        menuSpeed.show(getSupportFragmentManager(),"Menu Speed");
    }

    @Override
    public void setSpeed(int change) {
        musicService.changeSpeedMusic((float)change/100);
    }

    @Override
    public float getSpeed() {
    if(musicService == null){
        return -1;
    }
        return musicService.getSpeedMusic();
    }

    @Override
    public void setSeekBarSpeed(SeekBar seekBarSpeed){
    this.seekBarSpeed =seekBarSpeed;
    }

    @Override
    public void delete() {
        musicService.deleteMusic(this);
    }

    @Override
    public void share() {

    }
}