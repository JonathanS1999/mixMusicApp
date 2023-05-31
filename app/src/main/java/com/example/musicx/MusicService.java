package com.example.musicx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.image.ImageSong;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.example.musicx.ApplicationClass.ACTION_NEXT;
import static com.example.musicx.ApplicationClass.ACTION_PLAY;
import static com.example.musicx.ApplicationClass.ACTION_PREVIOUS;
import static com.example.musicx.ApplicationClass.CHANNEL_ID_2;

import static com.example.musicx.MainActivity.musicFilesfavoritos;
import static com.example.musicx.MainActivity.musicFilesrecientes;
import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;
import static com.example.musicx.MainActivity.musicFilesGeneral;
import static com.example.musicx.NowPlayingFragment.playerSmall;
import static java.lang.Thread.sleep;

public class MusicService  extends Service  implements AudioManager.OnAudioFocusChangeListener {

    private final IBinder mBinder = new MyBinder();
    private  MediaPlayer mediaPlayer ;
    private ArrayList<MusicFiles> musicFilesService ;
    private ActionPlaying  actionPlaying = null;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE ="STORED_MUSIC";
    private Context context;

    private Notification notification;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder nBuilder;


    public static String ARTIST_NAME = "ARTIST_NAME";
    public static String SONG_NAME = "SONG_NAME";
    public static String POSITION_NAME="POSITION_NAME";

    private AudioManager audioManager;
    private  float controlVolumeAudio =-1;
    private  float speed=-1f;
    private int timeCurrent=0;
    private  MediaPlayer mpDesvanecer ,mpDesvanecer_A ;
    private Thread threadDesvanecer, threadDesvanecersig,threadDesvanecer_A, threadDesvanecersig_B  ;
    private Thread threadProcess;
    private Thread timerCount;
    private ListPlayerAdapter listPlayerAdapter=null;
    private char TIPO_REPRODUCTOR = 'b';
    private char FADE_TYPE='b';
    private  int REPETIR =3 ;
    private int clickCompletado =0 ;
    private int position = -1  ;
    private int elementSelected =0;
    private int seekToStartPref=0;
    private int seekToEndPref  =0;
    private String stringException="";
    private MediaSessionCompat mediaSessionCompat;

    // variable para canciones aleatorias
    private  Random cancionAleat = new Random();
    private boolean random = false;

    //varable controls
    String CONFG_CONTROLS="CONFIG_CONTROLS";
    String MODE_RANDOM="MODE_RANDOM";
    String MODE_PLAYING="MODE_PLAYING";

    private Thread showProgressNotification;

    private AlertDialog.Builder  alerta;
    private AlertDialog titulo ;
    private Handler handler , handlerNextMediaPlayer, handlerException , handlerNotifi;
    private Runnable runnable , runnableNextMediaPlayer , runnableException , runnableNotifi;
    public static final Object desvanecerMp = new Object();
    private static final Object object_Des_A = new Object();
    private static final Object object_Des_B= new Object();
    private static final Object objectAddRec = new Object();
    private static final Object objectMediaplayerVolume = new Object();


    //Intents
    private Intent intent , prevIntent, pauseIntent,nextIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        inicializerMyRunnable();
        handler = new Handler(getMainLooper());
        handlerNextMediaPlayer=new Handler(getMainLooper());
        handlerException= new Handler(getMainLooper());
        handlerNotifi = new Handler(getMainLooper());
        context = this;

        mediaSessionCompat = new MediaSessionCompat(getApplicationContext(), "j");
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        getSettingsPreferences();

        /*
        nBuilder =new NotificationCompat.Builder(context, CHANNEL_ID_2);

        nBuilder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                 .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        */

        intent = new Intent(context, PlayerActivity.class);
        prevIntent = new Intent(context, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        pauseIntent = new Intent(context, NotificationReceiver.class).setAction(ACTION_PLAY);
        nextIntent = new Intent(context, NotificationReceiver.class).setAction(ACTION_NEXT);

    }

    public void getSettingsPreferences(){
        SettingsPlayingPreferences preferences = new SettingsPlayingPreferences(this);
        TIPO_REPRODUCTOR = Objects.requireNonNull(preferences.getTypePlayer()).charAt(0);
        FADE_TYPE = Objects.requireNonNull(preferences.getTypeFade()).charAt(0);
        seekToStartPref = preferences.getTimeStart();
        seekToEndPref = preferences.getTimeFinish();
    }

    private void inicializerMyRunnable(){

        runnableException = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, stringException,Toast.LENGTH_LONG).show();
            }
        };

        runnableNotifi = new Runnable() {
            @Override
            public void run() {
                try {
                    nBuilder.setProgress(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition(), false);
                }catch (Exception e){
                    stringException = e+" 152 ms";
                    handlerException.post(runnableException);
                }
            }
        };

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if( musicFilesService != null &&  musicFilesService.size()>0) {
                        if (mediaPlayer.isPlaying()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if ( position >= 0) {
                                        if (musicFilesrecientes != null) {
                                            if (!musicFilesrecientes.contains(musicFilesService.get(position))) {
                                                synchronized (objectAddRec) {
                                                    musicFilesrecientes.add(musicFilesService.get(position));
                                                }
                                            }
                                        } else {
                                            synchronized (objectAddRec) {
                                                musicFilesrecientes = new ArrayList<>();
                                                musicFilesrecientes.add(musicFilesService.get(position));
                                            }
                                        }
                                    }
                                }
                            }).start();

                            if (actionPlaying != null) {
                                actionPlaying.updateInfoGeneral();
                                actionPlaying.setMaxSeekBar(mediaPlayer.getDuration());
                                actionPlaying.updateViewPlayPause(R.drawable.ic_pause);
                            }
                            actualizarVistaPequenaDeReproduccion();
                            showNotification(R.drawable.ic_pause);
                        } else {
                                showNotification(R.drawable.ic_play);
                        }
    }else{
        Toast.makeText(context,"No hay archivos para reproducir 140 MS",Toast.LENGTH_SHORT).show();
    }

}catch (Exception exception){
    Toast.makeText(context,exception+" runnable 116",Toast.LENGTH_SHORT).show();
}
            }
        };

        runnableNextMediaPlayer = new Runnable() {
            @Override
            public void run() {

                try {
                    if (threadProcess != null) {
                        if (threadProcess.isAlive()) threadProcess.interrupt();
                    }
                    createMediaPlayer(position);
                    if (mediaPlayer.getDuration() < 7000) {
                        mediaPlayer.start();
                    } else {
                        mediaPlayer.setVolume(0.0f, 0.0f);
                        mediaPlayer.start();
                        desvanecerMediaPlayerEntante();
                    }
                    inicializarBarraDeProgreso();
                    threadProcess.start();
                }catch (Exception e){
                    stringException = " 196 "+ e;
                    handlerException.post(runnableException);
                }
            }
        };
    }

    private void prepareDesvaMusic(){
        timeCurrent =0;
       timerCount =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(6000);
                    timeCurrent = 6000;
                }catch (InterruptedException e){
                  timeCurrent=0;
                }
            }
        });
      timerCount.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
   public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    if ( mediaPlayer != null){
                        if( mediaPlayer.isPlaying())pauseMusic();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    playMusic();
                    break;
            }
    }

    public class MyBinder extends Binder{
       public MusicService getService (){
            return MusicService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            String actionName = intent.getStringExtra("ActionName");
            getSettingsPreferences();
            if (actionName != null) {
                inicializerData(intent);
                switch (actionName) {
                    case "playPause":
                        btn_play_pauseClicked();
                        break;
                    case "next":
                        btn_nextClickedNotificacion();
                        break;
                    case "previous":
                        btn_prevClickedNotificacion();
                        break;
                    case "MINIPLAYER":
                        if(actionPlaying != null) actionPlaying =null;
                        actualizarVistaPequenaDeReproduccion();
                        break;
                    case "removeFil":
                        try{
                            //verificar
                        musicFilesService = new ArrayList<>();
                        musicFilesService.addAll(nowPlayerSongs);
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {//verificar linea
                                btn_nextClicked();
                            } else {
                                mediaPlayer.release();
                                if (musicFilesService != null && musicFilesService.size() > 0) {
                                    position = position % sizeMusics();
                                    createMediaPlayer(position);
                                    actualizarVistaPequenaDeReproduccion();
                                    showNotification(R.drawable.ic_play);
                                } else {
                                    musicFilesService = new ArrayList<>();
                                    nowPlayerSongs.addAll(musicFilesGeneral);
                                    musicFilesService = nowPlayerSongs;
                                    position = 0;
                                    if (musicFilesService.size() > 0) {
                                        createMediaPlayer(position);
                                        actualizarVistaPequenaDeReproduccion();
                                        showNotification(R.drawable.ic_play);
                                    } else {
                                        Toast.makeText(this, "No hay archivos para reproducir ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else if (musicFilesService != null) {
                            if (musicFilesService.size() > 0) {
                                position = position % sizeMusics();
                                createMediaPlayer(position);
                                actualizarVistaPequenaDeReproduccion();
                            } else {
                                Toast.makeText(this, "No hay archivos para reproducir ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        }catch(Exception e ){
                            Toast.makeText(this,e+" music service 233",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "upDatePosition":
                        try{
                        if (musicFilesService != null && musicFilesService.size() > 0) {
                            if (intent.getIntExtra("newPosition", -20) >= 0) {
                                position = intent.getIntExtra("newPosition", -20);
                            }
                            if (mediaPlayer != null) {
                                if (mediaPlayer.isPlaying()) {
                                    showNotification(R.drawable.ic_pause);
                                } else {
                                    showNotification(R.drawable.ic_play);
                                }
                            }
                        } else {
                            nowPlayerSongs =  new ArrayList<>();
                            nowPlayerSongs.addAll(musicFilesGeneral);
                            musicFilesService = nowPlayerSongs;
                            position = 0;
                            if (musicFilesService.size() > 0) {
                                if (mediaPlayer != null) {
                                    if (mediaPlayer.isPlaying()) {
                                        showNotification(R.drawable.ic_pause);
                                    } else {
                                        showNotification(R.drawable.ic_play);
                                    }
                                }
                            } else {
                                Toast.makeText(this, "No hay archivos para reproducir", Toast.LENGTH_SHORT).show();
                            }
                        }
                        }catch(Exception e ){
                            Toast.makeText(this,e+" music service 264 update posigion",Toast.LENGTH_SHORT).show();
                        }
                }
            }
        }catch(Exception e ){
            Toast.makeText(this,e+" music service 268",Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    private void inicializerData(Intent intent) {
        if (nowPlayerSongs != null){
            if (musicFilesService == null) {
                musicFilesService = new ArrayList<>();
                //se agregan referencias todos los items nowPlayer
                musicFilesService.addAll(nowPlayerSongs);
                elementSelected = 4;
            }
        if (position < 0) {
            position = intent.getIntExtra("Position", -1);
        }

    }else{
            Toast.makeText(this,"sounds no initials 314",Toast.LENGTH_SHORT).show();
        }
    }

    public void setListPlayerAdapter(ListPlayerAdapter listPlayerAdapter) {
        if(this.listPlayerAdapter != listPlayerAdapter) {
            this.listPlayerAdapter = listPlayerAdapter;
            this.listPlayerAdapter.updateList(nowPlayerSongs);
        }
    }

   public boolean isPlaying(){
        if(mediaPlayer != null) return mediaPlayer.isPlaying();
        return false;
    }

    public char getTypePlay(){
        return TIPO_REPRODUCTOR;
    }

    public void setLooping (boolean looping){
        if(mediaPlayer != null) mediaPlayer.setLooping(looping);
    }

   public int getDuration(){
        if( mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }else{
            return 0;
        }
    }

    //llamado al arrastrar el seekBar
   public void seekTo( int seekPosition ){
        try {
            if (FADE_TYPE == 'a') {
                mediaPlayer.seekTo(seekPosition);
                if (!(mediaPlayer.isPlaying())) {// si esta en pausa
                    mediaPlayer.start();
                    inicializarBarraDeProgreso();
                    threadProcess.start();
                }

            } else {
                clickCompletado++;
                if (!(mediaPlayer.isPlaying())) { // esta en pausa
                    mediaPlayer.release();
                    mediaPlayer = null;
                } else {
                    desvanecer_A();
                }
                inicializerSeekTrack(seekPosition);
            }
        }catch (Exception e){
            Toast.makeText(this, e+" musicService 361",Toast.LENGTH_SHORT).show();
        }
    }

    private void inicializerSeekTrack(int seekPosition){
                createMediaPlayer(position);
                mediaPlayer.seekTo(seekPosition);
                if (mediaPlayer.getDuration() < 7000) {//si esta en modo desvanecer y el audio es muy corto
                    mediaPlayer.start();
                } else {
                    mediaPlayer.setVolume(0.0f, 0.0f);
                    mediaPlayer.start();
                    desvanecer_B();
                }
                inicializarBarraDeProgreso();
                threadProcess.start();
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int p) {
        if (p >= 0) position = p;
    }

    public void setCategoryMusic( int elementSelected){
        if (elementSelected>=0) this.elementSelected= elementSelected;
    }

    public int getCurrentPosition (){
            if (mediaPlayer != null) {
                return mediaPlayer.getCurrentPosition();
            } else {
                return 0;
            }
    }

    public void updateMusics(  ArrayList<MusicFiles> listSongs ){
        if( listSongs != null){
            if ( musicFilesService != null) musicFilesService.clear();
            musicFilesService = new ArrayList<>();
            musicFilesService.addAll(listSongs) ;
            nowPlayerSongs = musicFilesService;
        }
    }


   private  void createMediaPlayer(int position){
            Uri uri = Uri.parse(musicFilesService.get(position).getPatch());
            SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE)
                    .edit();
            editor.putString(POSITION_NAME, position + "");
            editor.putString(MUSIC_FILE, uri.toString());// editar ruta del archivo
            editor.putString(SONG_NAME, musicFilesService.get(position).getTitle()); // editar mobre de la cancion
            editor.putString(ARTIST_NAME, musicFilesService.get(position).getArtist()); // editar mobre del artista
            editor.apply();
            mediaPlayer = MediaPlayer.create(context, uri);
            if (speed > 0) {
                changeSpeedMusic(speed);
            }
            if((mediaPlayer.getDuration() - seekToStartPref - seekToEndPref) >  (seekToStartPref + seekToEndPref)) {
                mediaPlayer.seekTo(seekToStartPref);
            }
    }

    public int getClickCompletado() {
        return clickCompletado;
    }

    public void setRandom(boolean random){
       this.random= random;
   }
   public boolean getRandom(){return random;}

  public MediaPlayer getMediaPlayer(){
        if(mediaPlayer != null) return mediaPlayer;
        return null;
    }

 public void setCallBack(ActionPlaying actionPlaying){
        this.actionPlaying = actionPlaying;
 }

 public MusicFiles get ( int position){
        return musicFilesService.get(position);
 }

 public void setRepeat( int repeat){
        REPETIR= repeat;
 }

 public int sizeMusics( ){
        if( musicFilesService == null ) return 0;
        return musicFilesService.size();
 }
//Focus music  ....Deprecated
  private void focusGain(){
       audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
      if (controlVolumeAudio < 0){
           controlVolumeAudio =(float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / 15;
       }
   }

   private void finishedClick(){// verificar esta linea
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clickCompletado =0;
            }
        }).start();
   }

   private void abandomAudio(){
        audioManager.abandonAudioFocus(this);
   }

   float getVolumen() {
       return (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / 15;
   }

   float getControlAudio(){
        return controlVolumeAudio;
   }

   void setControlVolumeAudio(float  v){
        controlVolumeAudio =v;
   }

   void changeSpeedMusic( float speed ) {
       if (mediaPlayer != null){
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
           }
       }
        this.speed= speed;
   }

   float getSpeedMusic(){
        return speed;
   }

    void inicializarBarraDeProgreso () {
        threadProcess = new Thread (){
            @Override
            public void run() {
                finishedClick();
                    try {
                        if (musicFilesService != null && musicFilesService.size() > 0) {
                            handler.post(runnable);
                            if ( TIPO_REPRODUCTOR == 'a') {
                                if (!(REPETIR == 3 && (position + 1) == sizeMusics() )||  !(REPETIR == 0)) {
                                    cancionCompetada();
                                }
                                while (mediaPlayer.isPlaying() ) {  // Mientras se está reproduciendo
                                    try {
                                        if (actionPlaying != null)
                                            actionPlaying.setSeekBarProgres(mediaPlayer.getCurrentPosition());
                                        if (playerSmall != null)
                                            playerSmall.progress(mediaPlayer.getCurrentPosition());
                                    sleep(300);
                                        handlerNotifi.post(runnableNotifi);
                                    }catch (InterruptedException exce){
                                        break;
                                    }
                                }//while         si el usuario pausa la música sal del proces

                            } else {

                                if (mediaPlayer.getDuration() <= 8000) { // Si el audio demora muy poco tiempo
                                    miniCancionCompetada();
                                    while (mediaPlayer.isPlaying() ) {  // Mientras se está reproduciendo
                                        try{
                                        if (actionPlaying != null)
                                            actionPlaying.setSeekBarProgres(mediaPlayer.getCurrentPosition());
                                        if (playerSmall != null)
                                            playerSmall.progress(mediaPlayer.getCurrentPosition());

                                        sleep(300);
                                        // actualize seekBar view
                                        handlerNotifi.post(runnableNotifi);
                                }catch (InterruptedException exce){
                                    break;
                                }
                                    }//while         si el usuario pausa la música sal del proceso

                                } else {     // si es un audio normal > 7000
                                    prepareDesvaMusic();
                                    while (mediaPlayer.isPlaying()) {  // Mientras se está reproduciendo
                                        try{
                                        if (actionPlaying != null)
                                            actionPlaying.setSeekBarProgres(mediaPlayer.getCurrentPosition());
                                        if (playerSmall != null)
                                            playerSmall.progress(mediaPlayer.getCurrentPosition());
                                        sleep(300);
                                       // handlerNotifi.post(runnableNotifi);
                                        if (mediaPlayer.getCurrentPosition() > (mediaPlayer.getDuration() - 8000)) {
                                            if (REPETIR == 3 && (position + 1) == sizeMusics() || REPETIR == 0) {
                                                while (mediaPlayer.isPlaying()) {  // Mientras se está reproduciendo
                                                    try{
                                                    if (actionPlaying != null)
                                                        actionPlaying.setSeekBarProgres(mediaPlayer.getCurrentPosition());
                                                    if (playerSmall != null)
                                                        playerSmall.progress(mediaPlayer.getCurrentPosition());
                                                    sleep(300);
                                                    handlerNotifi.post(runnableNotifi);
                                                }catch (InterruptedException exce){
                                                    break;
                                                }
                                                }
                                                handler.post(runnable);//verificar esta linea
                                                break;
                                            } else {
                                                siguienteCancion_RepDesva ();   // siguiente canción, automáticamente
                                            }
                                        }
                                    }catch (InterruptedException exce){
                                        break;
                                    }
                                    }//while         si el usuario pausa la música sal del proceso
                                }// if  else -> ( mp. getDuration() < 7000)
                            }
                            if (actionPlaying != null) {
                                actionPlaying.updateViewPlayPause(R.drawable.ic_play);
                            }
                            if (playerSmall != null) {
                                playerSmall.statePlaying(false);
                            }
                        }
                    } catch (Exception exception) {
                stringException = " 630 "+exception;
                handlerException.post(runnableException);
                    }

            }

        };
    }

    private void miniCancionCompetada (  ){
           mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    if (threadProcess != null) {
                        if (threadProcess.isAlive()) threadProcess.interrupt();
                    }
                    mp.stop();
                    mp.release();
                    siguienteCancion_RepDesva ();
                }
            });
    }

    private void siguienteCancion_RepDesva (){
        if ( clickCompletado == 0 ) {
            if (threadDesvanecersig != null) {
                if (threadDesvanecersig.isAlive()) threadDesvanecersig.interrupt();
            }
            clickCompletado++;
            try {
                if (mediaPlayer != null) {
                    desvanescerMediaPlayerAnterior();
                }
                if (musicFilesService != null && musicFilesService.size() > 0) {
                    if (REPETIR == 1 || REPETIR == 3) {
                        if (random) {
                            if (sizeMusics() > 0) {
                                songs_Random();
                            } else {
                                position = (position + 1) % sizeMusics();
                            }
                        } else {
                            position = (position + 1) % sizeMusics();
                        }
                    }
                    handlerNextMediaPlayer.post(runnableNextMediaPlayer);
                } else {// verificar linea talvez demas
                    clickCompletado = 0;
                    position = 0;
                }
            } catch (Exception e) {
                stringException = " 630 "+e;
                handlerException.post(runnableException);
            }
        }

    }


    public void cancionCompetada ( ){
           mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    if (threadProcess != null) {
                        if (threadProcess.isAlive()) threadProcess.interrupt();
                    }
                    siguienteCancion ();
                }
            });
    }

    public void siguienteCancion (){

        try{
            mediaPlayer.stop();
            mediaPlayer.release();
            if (REPETIR == 1 || REPETIR == 3) {
                if (random) {
                    if (sizeMusics() > 0) {
                        songs_Random();
                    } else {
                        position = (position + 1) % sizeMusics();
                    }
                } else {
                    position = (position + 1) % sizeMusics();
                }
            }
            createMediaPlayer(position );
            mediaPlayer.start();
            inicializarBarraDeProgreso();
            threadProcess.start();

        }catch ( Exception e ){
            //Toast.makeText(getApplicationContext (), e+" ", Toast.LENGTH_LONG ). show ();
            //aux9=e+" 805 ";

        }


    }



    public void  desvanecer_A ( ){

            if (mpDesvanecer_A == null) {
                mpDesvanecer_A = mediaPlayer;
                threadDesvanecer_A = new Thread() {
                    @Override
                    public void run() {
                            float vol = getControlAudio();
                            MediaPlayer mP = mpDesvanecer_A;
                            try {
                                for (float i = vol; i >= 0.0; i -= 0.005) {
                                    if (mP.isPlaying()) {
                                        sleep((int) (55 - (20 * vol)));
                                        mP.setVolume(i, i);
                                    }else {
                                        break;
                                    }
                                }
                                mP.stop();
                                mP.release();
                                synchronized (object_Des_A) {
                                    mpDesvanecer_A=null;
                                }
                            } catch (Exception exe) {
                                //para capturar la excepcion
                            }
                    }//run
                };

                threadDesvanecer_A.start();


            } else {  //if ( mpDesvanecer == null )     else->      significa que el hilo aún está ejecutandose
                threadDesvanecer_A = new Thread() {

                    @Override
                    public void run() {
                        mpDesvanecer_A.pause();
                        mpDesvanecer_A = mediaPlayer;
                        float vol = getControlAudio();
                        MediaPlayer mP = mpDesvanecer_A;
                        try {
                            for (float i = vol; i >= 0.0; i -= 0.005) {
                                    if(mP.isPlaying()) {
                                        sleep((int) (55 - (20 * vol)));
                                        mP.setVolume(i, i);
                                    }else{
                                        break;
                                    }
                            }
                            mP.stop();
                            mP.release();
                            synchronized (object_Des_A) {
                                mpDesvanecer_A=null;
                            }
                        } catch (Exception exe) {
                            stringException = " 841 "+exe;
                            handlerException.post(runnableException);
                        }
                    }//run
                };
                threadDesvanecer_A.start();
            }
    }


    public void  desvanecer_B ( ){
            try {

                if (mediaPlayer != null) {

                    if (mediaPlayer.getDuration() > 7000) {

                        threadDesvanecersig_B = new Thread() {

                            @Override
                            public void run() {

                                // audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

                                float vol = getVolumen();

                                for (float i = 0.0f; i <= vol; i += 0.005) {
                                    try {
                                        sleep((int) (55 - (20 * vol)));
                                        synchronized (object_Des_B) {
                                            mediaPlayer.setVolume(i, i);
                                            setControlVolumeAudio(i);
                                        }
                                    } catch (InterruptedException e) {
                                        break;
                                    }

                                }//for( float  i =0.0f ; i<=vol ;i +=0.020  )

                            }//run
                        };

                        threadDesvanecersig_B.start();


                    }// if(mp.getDuration () > 7000)
                }
            } catch (Exception e) {
                //   Toast.makeText(getApplicationContext (),e +"  1139 error  ", Toast.LENGTH_LONG ). show ();
            }
    }


    private void desvanescerMediaPlayerAnterior(){
            try {

                if (mediaPlayer != null) {// necesario

                    if (mpDesvanecer == null) {  // significa que ya el hilo terminó su trabajo o aun no empieza más no está ejecutándose

                        mpDesvanecer = mediaPlayer;

                        threadDesvanecer = new Thread() {
                            @Override
                            public void run() {
                                final float vol = getControlAudio();
                                MediaPlayer mp = mpDesvanecer;
                                try {
                                    float i;
                                    for ( i = vol; i >= 0.0; i -= 0.020) {
                                            if(mp.isPlaying()) {
                                                sleep((int) (550 - (200 * vol)));
                                                mp.setVolume(i, i);
                                            }else{
                                                break;
                                            }
                                    }//for( float  i = vol ; i>= 0.0;i -=0.020)
                                    mp.stop();
                                    mp.release();
                                   // if(i<=0.0) {
                                        synchronized (desvanecerMp) {
                                            mpDesvanecer = null;
                                        }
                                   // }
                                } catch (Exception excep) {
                                    stringException = " 924 "+excep;
                                    handlerException.post(runnableException);
                                }
                            }//run
                        };

                        threadDesvanecer.start();

                    } else {  //if ( mpDesvanecer == null )     else->      significa que el hilo aún está ejecutandose

                        threadDesvanecer = new Thread() {

                            @Override
                            public void run() {
                                try{
                                    try {
                                        mpDesvanecer.pause();
                                    }catch (Exception ep){
                                        stringException = " 938 "+ep;
                                        handlerException.post(runnableException);
                                    }
                                mpDesvanecer = mediaPlayer;
                                final float vol = getControlAudio();
                                MediaPlayer mp = mpDesvanecer;
                                    try {
                                    float i;
                                    for ( i = vol; i >= 0.0; i -= 0.020) {
                                                if (mp.isPlaying()) {
                                                    sleep((int) (550 - (200 * vol)));
                                                    mp.setVolume(i, i);
                                                }else {
                                                    break;
                                                }
                                        }//for( float  i = vol ; i>= 0.0;i -=0.020)
                                        mp.stop();
                                        mp.release();
                                       //if(i<=0.0) {
                                           synchronized (desvanecerMp) {
                                               mpDesvanecer = null;
                                           }
                                       //}
                                } catch (Exception excep) {
                                        stringException = " 965 "+excep;
                                        handlerException.post(runnableException);
                                }
                                } catch (Exception exc) {
                                    stringException = " 951 "+exc;
                                    handlerException.post(runnableException);
                                }
                            }//run
                        };

                        threadDesvanecer.start();
                    }


                }//if(mp != null)

                else {   // Aquí cada vez que se ingresa a la actividad
                    Toast.makeText(getApplicationContext(), "  bien hecho ", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                //  Toast.makeText(getApplicationContext (), e+"  1094 error  ", Toast.LENGTH_LONG ). show ();
            }
    }



    public void desvanecerMediaPlayerEntante (){
        try {

            if(mediaPlayer!= null){

                if(mediaPlayer.getDuration () > 7000){
                    threadDesvanecersig = new Thread (){
                        @Override
                        public void run() {
                                //obtener el volumen actual del dispositivo
                            try {
                                final float vol = getVolumen();
                                //El media player entrante siempre comienza de 0 al volumen actual
                                for (float i = 0.0f; i <= vol; i += 0.020) {
                                    try {
                                        if (!isInterrupted()) {
                                            sleep((int) (550 - (200 * vol)));
                                            synchronized (objectMediaplayerVolume) {
                                                if (mediaPlayer != null) {
                                                    mediaPlayer.setVolume(i, i);
                                                    //control audio , desde que nivel se deve empezar a bajar el volumen
                                                    setControlVolumeAudio(i);
                                                }
                                            }

                                        } else {
                                            break;
                                        }
                                    } catch (InterruptedException e) {
                                        break;
                                    }

                                }//for( float  i =0.0f ; i<=vol ;i +=0.020  )
                            }catch (Exception excp){
                                stringException = " 1013 "+excp;
                                handlerException.post(runnableException);
                            }
                        }//run
                    };

                    threadDesvanecersig . start();

                }// if(mp.getDuration () > 7000)
            }
        }catch (Exception e ){
            stringException = " 976 "+e;
            handlerException.post(runnableException);
             }

    }


    public void saveConfigControls(boolean randomMode,int modePlaying ){
        SharedPreferences.Editor preferences = getSharedPreferences(CONFG_CONTROLS,MODE_PRIVATE).edit();
        preferences.putBoolean(MODE_RANDOM,randomMode);
        preferences.putInt(MODE_PLAYING,modePlaying);
        preferences.apply();
    }

     public void getConfigControls(){
        SharedPreferences sharedPreferences = getSharedPreferences(CONFG_CONTROLS,MODE_PRIVATE);
        REPETIR =sharedPreferences.getInt(MODE_PLAYING,3);
        if ( sharedPreferences.getBoolean(MODE_RANDOM,false)) random =true;
        if(actionPlaying != null) {
            actionPlaying.updateControlRepeat();
            actionPlaying.updateControlRandom();
        }
    }

    public int getRepeat(){
        return REPETIR;
    }

   private void  showNotification(int playPauseBtn){

        if(nBuilder != null){
            nBuilder.clearActions();
        }

       nBuilder =new NotificationCompat.Builder(context, CHANNEL_ID_2);

       nBuilder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                       .setMediaSession(mediaSessionCompat.getSessionToken()))
               .setPriority(NotificationCompat.PRIORITY_HIGH)
               .setOnlyAlertOnce(true)
               .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        if(showProgressNotification != null){
            if(showProgressNotification.isAlive()){
                showProgressNotification.interrupt();
            }
        }
            intent.putExtra("position", position).putExtra("elemtSelected", elementSelected);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent contentIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            // atras en boton de notificacion

            PendingIntent prevPending = PendingIntent.getBroadcast(context, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // pausar en boton de notificacion
           PendingIntent pausePending = PendingIntent.getBroadcast(context, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // siguiente en boton de notificacion
            PendingIntent nextPending = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            byte[] picture;
            picture = ImageSong.getBitmapPicture(get(position).getPicturePath());
            Bitmap thumb;
            if (picture != null) {
                thumb = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            } else {
                thumb = BitmapFactory.decodeResource(getResources(), R.drawable.iconp);
            }
            //nBuilder.clearActions();
                      notification = nBuilder.setSmallIcon(playPauseBtn)
                    .setLargeIcon(thumb)
                    .setShowWhen(false)
                    .setColor(Color.parseColor("#E91E63"))
                    .setContentIntent(contentIntent)
                    .setContentTitle(get(position).getTitle())
                    .setContentText(get(position).getArtist())
                    .addAction(R.drawable.ic_previous, "previous", prevPending)
                    .addAction(playPauseBtn, "pause", pausePending)
                    .addAction(R.drawable.ic_next, "previous", nextPending)
                    .setProgress(mediaPlayer.getDuration(),mediaPlayer.getCurrentPosition(),false)
                    .build();
            notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(2,notification);
            //startForeground(2, notification);

            showProgressNotification = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (mediaPlayer.isPlaying()) {
                            Thread.sleep(1000);
                            notification = nBuilder.setProgress(mediaPlayer.getDuration(),mediaPlayer.getCurrentPosition(),false)
                                    .build();
                            notificationManager.notify(2,notification);
                        }
                    }catch (Exception e){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "1154 "+e+"",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            showProgressNotification.start();


    }


    private void songs_Random(){
        int pos =position;
        while  ( pos == position ) {
            pos = cancionAleat.nextInt(sizeMusics());
        }
        position = pos ;
    }

   public void btn_play_pauseClicked(){
        if(mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                pauseMusic();
            } else {
                playMusic();
            }
        }else{
            btn_nextClicked();
        }
    }


    public void btn_prevClickedNotificacion() {
        try {
            if ((position - 1) < 0) {
                position = sizeMusics() - 1;
            } else {
                position = position - 1;
            }
            btn_nextClicked();
        }catch (Exception e){
            Toast.makeText(this, e+" music service 1008",Toast.LENGTH_SHORT).show();
        }
    }



    public void btn_nextClickedNotificacion() {
        try {
            position = (position + 1) % sizeMusics();
            btn_nextClicked();
        }catch(Exception e){
            Toast.makeText(context, e+" 1103 music service",Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_nextClicked(){
        focusGain();
        if ( TIPO_REPRODUCTOR =='a') {

            if (threadProcess != null) {
                if (threadProcess.isAlive()) threadProcess.interrupt();
            }
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

            position = position % sizeMusics();//si elimina un audio la posision sera la anterior
                createMediaPlayer(position);
                mediaPlayer.start();
                inicializarBarraDeProgreso();
            threadProcess.start();
        }else {

            try {
                        clickCompletado++;

                                if (timerCount != null) {
                                    if (timerCount.isAlive()) timerCount.interrupt();
                                }

                                if (threadDesvanecersig != null) {
                                    if (threadDesvanecersig.isAlive())
                                        threadDesvanecersig.interrupt();// interrumpe el hilo de desvanecimiento al principio
                                }

                                if (mediaPlayer != null) {
                                    if (timeCurrent > 5000) {//si ha pasado tiempo suficiente para poder desvaner una cancion
                                        desvanescerMediaPlayerAnterior();
                                    } else {
                                        mediaPlayer.pause();
                                        mediaPlayer.stop();
                                        mediaPlayer.release();
                                        mediaPlayer = null;
                                    }
                                }

                                if (musicFilesService.size() > 0) {
                                    position = position % sizeMusics();
                                    handlerNextMediaPlayer.post(runnableNextMediaPlayer);

                                } else {
                                    if (musicFilesGeneral != null && musicFilesGeneral.size() > 0) {
                                        position = 0;
                                        nowPlayerSongs.addAll(musicFilesGeneral);
                                        musicFilesService.addAll(nowPlayerSongs);
                                        if (listPlayerAdapter != null)
                                            listPlayerAdapter.updateList(nowPlayerSongs);
                                        handlerNextMediaPlayer.post(runnableNextMediaPlayer);
                                    } else {
                                        //Toast.makeText(this, "No hay ningun archivo ", Toast.LENGTH_SHORT).show();
                                    }
                                }
            } catch (Exception e) {
                stringException = " 1147 sig "+e;
                handlerException.post(runnableException);
            }
        }
    }


    public void pauseMusic() {
        try {

            if (threadProcess != null) {
                if (threadProcess.isAlive()) threadProcess.interrupt();
            }
                if (mpDesvanecer != null) {
                    if (mpDesvanecer.isPlaying()) mpDesvanecer.pause();
                }
                mediaPlayer.pause();
                showNotification(R.drawable.ic_play);
            abandomAudio();
        }catch (Exception exception){
            Toast.makeText(context,exception+"1051" ,Toast.LENGTH_SHORT).show();
        }
    }

    public void playMusic() {
        try {
            focusGain();
                if ( mpDesvanecer != null){
                    if( mpDesvanecer.isPlaying()) {
                        mpDesvanecer.pause();
                    }
                }
                mediaPlayer.start();
                inicializarBarraDeProgreso();
               threadProcess.start();
        } catch (Exception exception) {
            Toast.makeText(this, exception + " playMusic", Toast.LENGTH_SHORT).show();
        }
    }



    public void deleteMusic( Context context)  {

        alerta = new AlertDialog.Builder( context );
        alerta. setMessage( " Se eliminará de forma permanente ")
                . setCancelable(false)
                . setPositiveButton(" si ", new DialogInterface. OnClickListener(){
                    @Override
                    public void onClick (DialogInterface dialog, int which ){

                        try {
                            deleteFile(position);
                            btn_nextClicked();
                            Toast.makeText (getApplicationContext (), " eliminado con exito" , Toast.LENGTH_LONG) .show ();
                        }catch (Exception e ){
                            Toast.makeText (getApplicationContext (), " Error al eliminar " , Toast.LENGTH_LONG) .show ();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface. OnClickListener(){
                    @Override
                    public void onClick (DialogInterface dialog, int which ){
                        dialog. cancel();
                    }
                });

        titulo = alerta. create();
        titulo. setTitle(""+musicFilesService.get(position).getTitle().replace (".mp3","").replace(".wav",""));
        titulo. show();

    }

    private void deleteFile ( int position  ){
        try {
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Long.parseLong(musicFilesService.get(position).getId()));
            File file = new File(musicFilesService.get(position).getPatch());
            boolean deleted = file.delete();
            if (deleted) {
                this.getContentResolver().delete(contentUri, null, null);
                MusicFiles fil = musicFilesService.get(position);
                if(musicFilesfavoritos != null) musicFilesfavoritos.remove(fil);
                if(musicFilesrecientes != null) musicFilesrecientes.remove(fil);
                if(musicFilesGeneral != null) musicFilesGeneral.remove(fil);
                if(listPlayerAdapter != null ) listPlayerAdapter.updateItemRemoved(fil);
                nowPlayerSongs.remove(fil);
                musicFilesService = nowPlayerSongs;
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e+" MS1145 ",Toast.LENGTH_SHORT).show();
        }

    }
    public  void itemRemove(int posit){
        try {
            MusicFiles fil = musicFilesService.get(posit);
            Toast.makeText(this, posit+" "+position, Toast.LENGTH_SHORT).show();
            if (fil == musicFilesService.get(position)) {
                if (listPlayerAdapter != null) listPlayerAdapter.updateItemRemoved(fil);
                nowPlayerSongs.remove(fil);

                    musicFilesService = nowPlayerSongs;
                    if (musicFilesService.size() > 0) {
                        btn_nextClicked();
                    } else {
                        if (musicFilesGeneral != null && musicFilesGeneral.size() > 0) {
                            position = 0;
                            nowPlayerSongs.addAll(musicFilesGeneral);
                            musicFilesService = nowPlayerSongs;
                            if (listPlayerAdapter != null) listPlayerAdapter.updateList(nowPlayerSongs);
                            btn_nextClicked();
                        } else {
                            Toast.makeText(this, "No hay ningun archivo ", Toast.LENGTH_SHORT).show();
                        }
                    }

            } else {
                if (listPlayerAdapter != null) listPlayerAdapter.updateItemRemoved(fil);
                nowPlayerSongs.remove(fil);
                musicFilesService = nowPlayerSongs;
                if (musicFilesService.size() > 0) {
                    if (posit < position){
                        position = position - 1;
                    }else {
                        position = position % sizeMusics();
                    }
                    handler.post(runnable);
                } else {
                    if (musicFilesGeneral != null && musicFilesGeneral.size() > 0) {
                        position = 0;
                        nowPlayerSongs.addAll(musicFilesGeneral);
                        musicFilesService=nowPlayerSongs;
                        if (listPlayerAdapter != null) listPlayerAdapter.updateList(nowPlayerSongs);
                        btn_nextClicked();
                    } else {
                        Toast.makeText(this, "No hay ningun archivo ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch(Exception exeption){
            Toast.makeText(this, exeption+" music services 1174",Toast.LENGTH_SHORT).show();
        }
    }
    public void actualizarVistaPequenaDeReproduccion(){
        try {
            if (musicFilesService != null && position >= 0) {
                if (playerSmall != null) {
                    playerSmall.updateImage(ImageSong.getBitmapPicture(musicFilesService.get(position).getPicturePath()));
                    playerSmall.numSongs("" + (position + 1));
                    if (mediaPlayer != null) {
                        playerSmall.statePlaying(mediaPlayer.isPlaying());
                        playerSmall.progressMax(mediaPlayer.getDuration());
                        playerSmall.progress(mediaPlayer.getCurrentPosition());
                    } else {
                        playerSmall.statePlaying(false);
                        playerSmall.progressMax(100);
                    }
                    playerSmall.title(musicFilesService.get(position).getTitle());
                    playerSmall.artist(musicFilesService.get(position).getArtist());
                    playerSmall.updatePosition(position);
                    playerSmall.currentMusicFile(nowPlayerSongs.get(position));

                }
            }
        }catch (Exception exception){
            Toast.makeText(context,exception +" 1146 ",Toast.LENGTH_SHORT).show();
        }
    }

}
