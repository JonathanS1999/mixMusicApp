package com.example.musicx.custom

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.musicx.*
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences
import com.example.musicx.background.MusicBackground
import com.example.musicx.controlInicService.InicServiceMusic
import com.example.musicx.converTimes.ConvertTime
import com.example.musicx.image.ImageSong
import com.example.musicx.playControls.PlayMusicControls
import com.example.musicx.ui.main.SectionsPagerAdapter
import java.util.*

class Activity_Custom_Play:AppCompatActivity(), OnThemeSelected ,
    View.OnClickListener , CustomList.OnCustomList ,FragmentPlayerCardInterface ,
    ActionPlaying,ServiceConnection , InicServiceMusic.OnIniciMusicServiceListener , FragmentCardImg.OnThemeCard{

    private lateinit var background:ImageView
    private var themeS=0
    private lateinit var btnSelected : Button
    private var modeTheme : Int =2
    private var musicService: MusicService?= null
    private lateinit var playMusicControls:PlayMusicControls
    private lateinit var myIntent:Intent

    private var styleThemeCard:Int=0


    private var song_name: TextView? = null
    private var artist_name:TextView? = null
    private var duration_played:TextView? = null
    private var duration_total:TextView? = null
    private var numCanciones:TextView? = null

    //variables de controles
    private var nextBtn: ImageView? = null   //variables de controles
    private var prevBtn: ImageView? = null    //variables de controles
    private var shuffleBtn: ImageView? = null   //variables de controles
    private var repeatBtn: ImageView? = null
    private var playPauseBtn: ImageView? = null
    private var seekBar: SeekBar? = null
    private var setBackgroud: ImageView? = null
    private var bgGradient:android.widget.ImageView? = null
    private var imageCard:android.widget.ImageView? = null
    private var imgPort8: ImageView? = null
    private var frameCircle: LinearLayout? = null

    private val elementSelected = 0
    private val listSongs: ArrayList<MusicFiles>? = null
    private val sectionsPagerAdapter: SectionsPagerAdapter? = null
    private val viewPager2: ViewPager2? = null
    private val linearPuntos: LinearLayout? = null
    private var booleanSavedInstaceState = false

    private val toolbar: Toolbar? = null
    private var inicServiceMusic: InicServiceMusic? = null
    private var convertTime: ConvertTime? = null
    private var barPress = false
    private val playerAdapter: ListPlayerAdapter? = null
    private val recyclerView: RecyclerView? = null
    private val seekBarSpeed: SeekBar? = null
    private val context: Context? = null
    private var adaptableBackground: AdaptableBackground? = null
    private val settings: SettingsPlayingPreferences? = null
    private var setDefaultBackground:Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_play)
         val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
         background = findViewById(R.id.background)
         btnSelected = findViewById<Button>(R.id.btnSelected)
         btnSelected.setOnClickListener(this)
         setSupportActionBar(toolbar)
         toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
         toolbar.setNavigationOnClickListener { finish() }


         val config = intent.extras?.getInt("config",1)
         if(config == 1 ){
             Objects.requireNonNull(supportActionBar)!!.title = "Portada"
         }else if ( config == 2 ){
             Objects.requireNonNull(supportActionBar)?.title = "Controles"
         }
        booleanSavedInstaceState = savedInstanceState != null;

         if (savedInstanceState == null) {
             val fg = supportFragmentManager
             val ft = fg.beginTransaction()
             val bundle = Bundle()
             if (config != null) {
                 bundle.putInt("config_controls_or_play",config)
             }
             val customList=CustomList()
             customList.arguments=bundle
             ft.add(R.id.container,customList , "fragA").commit()
         }

    }

    override fun onResume() {
        super.onResume()
        MusicBackground.setBackground(background, this)
    }

    override fun startServiceMusic() {
        convertTime = ConvertTime()
        intent= Intent()
        playMusicControls=PlayMusicControls(this)
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
    }

    override fun onClick(v: View?) {
        if(modeTheme == 3){
            val preferences = SettingsPlayingPreferences(this)
            preferences.setSavedThemePlayerMC(themeS)

        }else {

            val config = intent.extras?.getInt("config", 1)
            if (config == 1) {
                val preferences = SettingsPlayingPreferences(this)
                preferences.setSavedThemePlayer(themeS)
            } else {
                val preferences = SettingsPlayingPreferences(this)
                preferences.setSavedThemeControls(themeS)
            }
        }
        Toast.makeText(this, "tema $themeS aplicado", Toast.LENGTH_LONG).show()
    }

    override fun setMyTheme(theme: Int) {
        themeS = theme + 1
    }

    override fun getThemeConfig(): Int {
        return modeTheme
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mode_conductor_custom, menu)
        return true
    }

   override fun onOptionsItemSelected( item :  MenuItem) :Boolean{
       if (item.itemId == R.id.mode_conducor) {
           if(modeTheme ==2){
               modeTheme=3
           }else{
               modeTheme=2
           }

           val sg = supportFragmentManager
           val ft = sg.beginTransaction()
           val customList=CustomList()
           ft.replace(R.id.container,customList,"fragB" ).commit()
       }
       return true
    }

    override fun inicializer(
        song_name: TextView?,
        artist_name: TextView?,
        duration_played: TextView?,
        duration_total: TextView?,
        numCanciones: TextView?,
        nextBtn: ImageView?,
        prevBtn: ImageView?,
        shuffleBtn: ImageView?,
        repeatBtn: ImageView?,
        playPauseBtn: ImageView?,
        seekBar: SeekBar?,
        imageMusic: ImageView?,
        favorite: ImageView?,
        bgGradient: ImageView?
    ){
        this.song_name = song_name
        this.artist_name = artist_name
        this.duration_played = duration_played
        this.duration_total = duration_total
        this.numCanciones = numCanciones
        this.nextBtn = nextBtn
        this.prevBtn = prevBtn
        this.shuffleBtn = shuffleBtn
        this.repeatBtn = repeatBtn
        this.playPauseBtn = playPauseBtn
        this.seekBar = seekBar
        this.bgGradient = bgGradient
        this.imageCard = imageMusic

        inicializerSeekBar()
        if(musicService == null){
            startServiceMusic()
        }
try{
    convertTime = ConvertTime()
    intent= Intent()
    playMusicControls=PlayMusicControls(this)

    intent.putExtra("position",MainActivity.POSITION_TO_FRAG.toInt())
    inicServiceMusic = InicServiceMusic(
        this, intent, musicService!!, 4,
        booleanSavedInstaceState, NowPlayingFragment.nowPlayerSongs, this, 1, convertTime!!
    )
    inicServiceMusic!!.setSeekBar(seekBar!!)
    inicServiceMusic!!.iniciarVistaReproduccion()
    seekBar.let { inicServiceMusic!!.setSeekBar(it) }
    inicServiceMusic!!.iniciarVistaReproduccion()

    playMusicControls.inicializerControlRepeats(repeatBtn!!, shuffleBtn!!)

}  catch (e:Exception) {
        Toast.makeText(this, "$e    odhgoiexmhrchimr´c{jicr", Toast.LENGTH_SHORT).show()
    }


    }

    override fun setFavoriteMusic() {
        try {
            playMusicControls.setFavoriteMusic()
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "$e 213", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showMyMenu() {
        TODO("Not yet implemented")
    }

    override fun onClickPlayerCard(v: View?) {
        val id: Int = v!!.getId()
        playMusicControls.onClick(id)
    }

    override fun updatePager() {
        // not used
    }

    override fun setFrameCircle(frameCircle: LinearLayout?) {
        this.frameCircle=frameCircle
    }

    override fun updateViewPlayPause(resource: Int) {
        if (playPauseBtn != null) playPauseBtn!!.setImageResource(resource)
    }

    override fun updateInfoGeneral() {

        var string = String.format(
            Locale.US,
            "%s",
            musicService!![musicService!!.position].title.replace(".mp3", "").replace(".wav", "")
        )
        song_name!!.text = string
        artist_name!!.text = musicService!![musicService!!.position].artist
        song_name!!.isSelected = true
        artist_name!!.isSelected = true
        duration_total!!.text = convertTime!!.getMilliSecondMinutHors(
            musicService!![musicService!!.position].duration.toLong())
        string = String.format(Locale.US, "%d / %d ", musicService!!.position + 1, musicService!!.sizeMusics())
        numCanciones!!.text = string

        playMusicControls.setMusicService(musicService!!)
        playMusicControls.setImageCard(imageCard!!)
        playMusicControls.configThemePrev(styleThemeCard)
        playMusicControls.actualizarPortada('B')

        if (musicService!!.isPlaying ){
            playPauseBtn!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_pause
                )
            )
        } else {
            seekBar!!.progress = musicService!!.currentPosition
            playPauseBtn!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_play
                )
            )
        }






        if (settings!!.getMyThemePlayer() >= 3) {
            if (adaptableBackground == null) adaptableBackground = AdaptableBackground(
                this,
                setBackgroud!!,
                bgGradient!!,
                song_name!!,
                artist_name!!,
                duration_played!!,
                duration_total!!,
                numCanciones!!, settings.getMyThemePlayer()
            )
            adaptableBackground!!.setPosition(musicService!![musicService!!.position])
            adaptableBackground!!.inicFondo()
        } else {
            if(!setDefaultBackground){
                MusicBackground.setBackground(setBackgroud, this)
                setDefaultBackground=true
            }
        }

        if (frameCircle != null) {
            if (frameCircle!!.childCount > 0) frameCircle!!.removeAllViews()
            if (styleThemeCard == 10 || styleThemeCard == 11) {
                AdaptaBleFramePlayerCard(this, null, frameCircle!!, musicService!!, styleThemeCard)
            } else if (styleThemeCard == 12) {
                val picture= ImageSong.getBitmapPicture(musicService!![musicService!!.position].picturePath)
                val bitmap = BitmapFactory.decodeByteArray(picture,0 , picture.size)
                frameCircle!!.addView(ViewCardImg(this, bitmap))
            }
        }

    }

    override fun updateControlRepeat() {

    }

    override fun updateControlRandom() {

    }


    private fun inicializerSeekBar() { //verificar llamadas a este metodo
            seekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    duration_played!!.text =
                        convertTime!!.getMilliSecondMinutHors(musicService!!.currentPosition.toLong())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    barPress = true
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    if (musicService!!.clickCompletado == 0) musicService!!.seekTo(seekBar.progress)
                    barPress = false
                }
            })
    }

    override fun setMaxSeekBar(maxSeekBar: Int) {
        try {
            seekBar!!.max = maxSeekBar
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "$e 337", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setSeekBarProgres(progres: Int) {
        try {
            if (!barPress) {
                seekBar!!.progress = progres // actualiza la barra de progreso
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "$e 337", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?){
        val myBinder = service as MusicService.MyBinder
        musicService = myBinder.service
        musicService!!.setCallBack(this)
        musicService!!.getSettingsPreferences()
        playMusicControls.setMusicService(musicService!!)
    }

    override fun onServiceDisconnected(name: ComponentName?) {

    }

    override fun setThemeCard(theme: Int) {
        styleThemeCard=theme
    }

}