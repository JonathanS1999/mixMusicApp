package com.example.musicx

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.musicx.FragmentModeConductor.FragmentModeConductor
import com.example.musicx.FragmentModeConductor.OnConductorListener
import com.example.musicx.MainActivity.musicFilesfavoritos
import com.example.musicx.MusicService.MyBinder
import com.example.musicx.NowPlayingFragment.playerSmall
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences
import com.example.musicx.background.MusicBackground
import com.example.musicx.controlInicService.InicServiceMusic
import com.example.musicx.converTimes.ConvertTime
import com.example.musicx.image.ImageSong
import com.example.musicx.playControls.PlayMusicControls
import org.w3c.dom.Text
import java.util.*


class ActivityModeConductor : AppCompatActivity(), OnConductorListener, ServiceConnection ,
        ActionPlaying ,Fragment_Player_list.FragmentListPlayerInterface,
    ListPlayerAdapter.ListPlayerAdaperInterface, InicServiceMusic.OnIniciMusicServiceListener{
    private lateinit var imageBackground : ImageView
    private lateinit var fragmentConductor :FragmentModeConductor
    private lateinit var fragmentPlayerList: Fragment_Player_list
    private  var listPlayerAdapter : ListPlayerAdapter?=null
    lateinit var toolbar: Toolbar
    // info music
    private var songTitle:TextView? = null
    private var songArtist:TextView? = null
    private var numberLis: TextView? =  null
    private var duratioPlayer:TextView? = null
    private var durationTotal:TextView? = null

    private var card :ImageView? = null
    private var favorite :ImageView? = null
    // controls
    private var playPauseBtn: ImageView? = null
    private var repeatBtn : ImageView? = null
    private var shuffleBtn : ImageView?= null
    private var bgGradient : ImageView?= null
    private var recyclerView :RecyclerView?=null;
    //list music
    private var musicFiles: ArrayList<MusicFiles>? =null
    //musicService
    private  var musicService :MusicService ? = null
    //savedInstanceState
    var booleanSavedInstaceState = false
    //seekBar
    private var seekBar : SeekBar?= null
    private var barPress =false
    private var elementSelected =0


    //controls fraglent
    private var fragmentPlay =false
    private var fragmentList =false

    //play Controls
    private lateinit var playControls : PlayMusicControls

    private lateinit var inicService :InicServiceMusic

    private lateinit var convertTime :ConvertTime
    private lateinit var settingsPlayingPreferences:SettingsPlayingPreferences;

    // config theme player variables
    private var adaptableBackground: AdaptableBackground? = null
    private var setDefaultBackground:Boolean=false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("fragPlay", fragmentPlay)
        outState.putBoolean("fragList", fragmentList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_conductor)
        imageBackground = findViewById(R.id.background)
        card= findViewById(R.id.player_card)
        bgGradient=findViewById(R.id.player_gradient)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setTitle("Modo conductor")
        settingsPlayingPreferences= SettingsPlayingPreferences(this)

        toolbar.setNavigationOnClickListener {
            if(fragmentList){
                fragmentPlay = true
                fragmentList = false
                supportFragmentManager.beginTransaction().replace(R.id.frame, fragmentConductor ,"fragA").commit()
            }else {
                finish()
            }
        }
        try {
            if (playerSmall != null) playerSmall = null;
            booleanSavedInstaceState = savedInstanceState != null;
            if (savedInstanceState != null) {
                fragmentList = savedInstanceState.getBoolean("fragList")
                fragmentPlay = savedInstanceState.getBoolean("fragPlay")
            }
            playControls = PlayMusicControls(this)
            convertTime = ConvertTime()

            if (savedInstanceState == null) {
                fragmentConductor = FragmentModeConductor()
                fragmentPlayerList = Fragment_Player_list()
                fragmentPlay = true
                fragmentList = false

                supportFragmentManager.beginTransaction().add(R.id.frame, fragmentConductor, "fragA").commit()


            } else {
                if (fragmentPlay) {
                    fragmentConductor = supportFragmentManager.findFragmentByTag("fragA") as FragmentModeConductor;
                    fragmentPlayerList = Fragment_Player_list()
                } else {
                    fragmentPlayerList = supportFragmentManager.findFragmentByTag("fragB") as Fragment_Player_list;
                    fragmentConductor = FragmentModeConductor()
                }
            }
            updateSongs()
        }catch (exc : Exception ){
            Toast.makeText(this, "  $exc ",Toast.LENGTH_LONG ).show()
        }
    }

    fun updateSongs() {
        elementSelected = intent.getIntExtra("elemtSelected", -1)
        musicFiles = NowPlayingFragment.nowPlayerSongs
    }

    override fun onResume() {
        super.onResume()
        // me vinculo con el servicio
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
            setBackground()
    }

   private  fun setBackground(){
        //MusicBackground(imageBackground, this)
    }

    override fun inicializerInfo(seekbar: SeekBar, card: ImageView?,
                                 title: TextView, artist: TextView,
                                 numberLis: TextView, playPause: ImageView,
                                 currentPosition: TextView, totalDuration: TextView,
                                 repeat: ImageView, shufle: ImageView, favorite: ImageView?,
                                 bgGradient: ImageView?) {
        songTitle = title
        songArtist = artist
        this.numberLis = numberLis
        playPauseBtn = playPause
        duratioPlayer = currentPosition
        durationTotal = totalDuration
        this.seekBar=seekbar
        repeatBtn = repeat
        shuffleBtn = shufle
        this.card = card
        this.bgGradient=bgGradient
        this.favorite = favorite
        playControls.inicializerControlRepeats(repeatBtn!!, shuffleBtn!!)
        inicializerSeekBar()
        if(musicService != null){
            seekbar.max= musicService!!.duration
            updateInfoGeneral()
            updateControlRandom()
            updateControlRepeat()
        }
        Toast.makeText(this, " inicializer info 150 ", Toast.LENGTH_SHORT).show()
    }


    fun inicializerSeekBar() {
        seekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                duratioPlayer?.setText(convertTime.getMilliSecondMinutHors(musicService!!.currentPosition.toLong()))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                barPress = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                musicService!!.seekTo(seekBar.progress)
                barPress = false
            }
        })
    }

    override fun setMaxSeekBar(maxSeekBar: Int) {
        if (seekBar != null){
            seekBar!!.max = maxSeekBar
        }
    }

    override fun setSeekBarProgres(progres: Int) {
        if(seekBar != null) {
            if (!barPress) {
                seekBar!!.progress = progres // actualiza la barra de progreso
            }
        }
    }

    override fun upDateInfo() {
            updateInfoGeneral()
    }

     override fun clickControls(v: View) {
         val id = v.id
         if(id== R.id.list) {
             setListPlayer()
         }else if(id ==R.id.favorite) {
             card?.let { playControls.setImageCard(it) }
             favorite?.let { playControls.setImageFavorte(it) }
             playControls.setFavoriteMusic()
         }else {
             playControls.onClick(id)
         }
    }

    private fun setListPlayer( ){
        fragmentPlay=false
        fragmentList= true
        supportFragmentManager.beginTransaction().replace(R.id.frame, fragmentPlayerList ,"fragB").commit()
    }


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
         try{
             val myBinder = service as MyBinder
             musicService = myBinder.getService()
             musicService!!.setCallBack(this)
             musicService!!.getSettingsPreferences()
             playControls.setMusicService(musicService!!)
             inicService = InicServiceMusic(this, intent, musicService!!, elementSelected,
                     booleanSavedInstaceState, musicFiles!!, this, 1, convertTime)
             seekBar?.let { inicService.setSeekBar(it) }
             inicService.iniciarVistaReproduccion()

            if(repeatBtn != null){
                playControls.inicializerControlRepeats(repeatBtn!!, shuffleBtn!!)
            }

             if (recyclerView != null && listPlayerAdapter != null) {
                 musicService!!.setListPlayerAdapter(listPlayerAdapter)
                 recyclerView!!.scrollToPosition(musicService!!.position)
                 recyclerView!!.layoutManager?.scrollToPosition(musicService!!.position)
                 recyclerView = null
                 listPlayerAdapter = null
             }
         }catch (e: Exception){
            Toast.makeText(this, e.toString() + "    ${musicService} activity conductor 244 " + e.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun updateViewPlayPause(resource: Int) {
        if(fragmentPlay) {
            playPauseBtn!!.setImageResource(resource)
        }
    }


    override fun updateInfoGeneral() {
       try {
           if(fragmentPlay){
               if (songTitle != null) {

                   var string = String.format(Locale.US, "%s", musicService!!.get(musicService!!.getPosition()).getTitle())

                   songTitle!!.ellipsize=TextUtils.TruncateAt.MARQUEE
                   songArtist!!.ellipsize=TextUtils.TruncateAt.MARQUEE
                   songTitle!!.setSelected(true)
                   songArtist!!.setSelected(true)
                   if(!songTitle!!.text.equals(string)){
                       songTitle!!.setText(string)
                   }
                   val string2=musicService!!.get(musicService!!.getPosition()).getArtist()
                   if(!songArtist!!.text.equals(string2)){
                       songArtist!!.setText(string2)
                   }



                   durationTotal?.setText(convertTime.getMilliSecondMinutHors(musicService!![musicService!!.position].duration.toLong()))
                   string = String.format(Locale.US, "%d / %d ", musicService!!.getPosition() + 1, musicService!!.sizeMusics())
                   numberLis!!.setText(string)
                   updateCard()
                   if (musicService!!.isPlaying()) {
                       playPauseBtn!!.setImageResource(R.drawable.ic_pause)
                   } else {
                       seekBar!!.setProgress(musicService!!.getCurrentPosition())
                       playPauseBtn!!.setImageResource(R.drawable.ic_play)
                   }

               }
           }
        } catch (exception: Exception) {
        Toast.makeText(this, exception.toString() + " activitymode Conductor 278 " + exception.message, Toast.LENGTH_SHORT).show()
    }
}


    private fun updateCard() {
        try {
            if(fragmentPlay) {
                if (card != null) {

                        val theme = settingsPlayingPreferences.getMyThemePlayerMC()
                        if(theme == 5 || theme==6 ){
                            card= findViewById(R.id.player_card)
                            bgGradient=findViewById(R.id.player_gradient)
                        }else{
                            (findViewById(R.id.player_card) as ImageView).setImageDrawable(null)
                            (findViewById(R.id.player_gradient) as ImageView).setImageDrawable(null)
                        }

                        if (theme == 1 || theme == 5 ) {
                            if(theme==1 )ImageSong.setImage(card,this,musicService!![musicService!!.position].picturePath,2,15F,false)
                            ImageSong.setImage(card,this,musicService!![musicService!!.position].picturePath,2,3F,false)
                        }else if (theme == 2 || theme==6 ){
                            ImageSong.setImage(card,this,musicService!![musicService!!.position].picturePath,2,15F,true)
                        }

                }
                if (favorite != null) {
                    if (musicFilesfavoritos != null && musicFilesfavoritos.contains(musicService!![musicService!!.position])) {
                        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                        favorite!!.setImageDrawable(drawable)
                    } else {
                        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_not_favorite)
                        favorite!!.setImageDrawable(drawable)
                    }
                }
            }



            if (settingsPlayingPreferences.getMyThemePlayerMC() == 5 || settingsPlayingPreferences.getMyThemePlayerMC() == 6) {
                if (adaptableBackground == null) adaptableBackground = AdaptableBackground(
                    this,
                    imageBackground,
                    bgGradient!!,
                    songTitle!!,
                    songArtist!!,
                    durationTotal!!,
                    duratioPlayer!!,
                    numberLis!!,
                    settingsPlayingPreferences.getMyThemePlayerMC()
                )
                adaptableBackground!!.setPosition(musicService!![musicService!!.position])
                adaptableBackground!!.inicFondo()
            } else {
                if(!setDefaultBackground){
                    MusicBackground.setBackground(imageBackground,this)
                    setDefaultBackground=true
                }
            }





        } catch (exception: java.lang.Exception) {
            Toast.makeText(this, exception.toString() + " 312", Toast.LENGTH_SHORT).show()
        }
    }
    override fun updateControlRepeat() {
        playControls.updateControlRepeat()
    }

    override fun updateControlRandom() {
        playControls.updateControlRandom()
    }


    override fun onServiceDisconnected(name: ComponentName?) {
   // musicService = null
    Toast.makeText(this, "service disconnect", Toast.LENGTH_LONG).show()
}

    override fun getPositionForList(): Int {
        if (musicService == null) return 0
        return if (musicService!!.sizeMusics() <= 0) 0 else musicService!!.position
    }



    override fun setPlayerListAdapter(adapter: ListPlayerAdapter, recyclerView: RecyclerView?) {
        try {
            fragmentPlay=false
            fragmentList= true
            if (musicService != null) musicService!!.setListPlayerAdapter(adapter) else listPlayerAdapter = adapter
            this.recyclerView = recyclerView
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "$e 337", Toast.LENGTH_SHORT).show()
        }
    }



    override fun setPositionPlayer(position: Int) {
        musicService!!.position = position
        musicService!!.btn_nextClicked()
    }

    override fun getPositionPlayer(): Int {
        if (musicService == null) return 0
        return if (musicService!!.sizeMusics() <= 0) 0 else musicService!!.position
    }

    override fun itemRemove(position: Int) {
        if (musicService != null) musicService!!.itemRemove(position)
    }

}