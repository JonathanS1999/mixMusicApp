package com.example.musicx.FragmentModeConductor

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.musicx.R
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences
import com.example.musicx.Theme_Font

class FragmentModeConductor : Fragment(),View.OnClickListener {

    private lateinit var controlRepeat : ImageView
    private lateinit var shufle : ImageView
    private var onConductorListener: OnConductorListener?=null
    private lateinit var seekBar: SeekBar
    private lateinit var songTitle:TextView
    private lateinit var songArtist:TextView
    private lateinit var numberLis: TextView
    private lateinit var totalDuration:TextView
    private lateinit var currentPosition: TextView

    //controls
    private lateinit var playPause: ImageView
    private lateinit var next : ImageView
    private lateinit var prev : ImageView

    private lateinit var card: ImageView
    private var bgGradient: ImageView?=null
    private lateinit var favorite : ImageView
    private lateinit var listPlayer : ImageView

    private lateinit var settingsPlayingPreferences:SettingsPlayingPreferences;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        settingsPlayingPreferences= SettingsPlayingPreferences(requireContext())
        val theme = settingsPlayingPreferences.getMyThemePlayerMC()
        if(theme <5 ){
            return inflater.inflate(R.layout.fragment_mode_conductor, container, false)
        }else{
            return inflater.inflate(R.layout.fragment_mode_conductor_2, container, false)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card = view.findViewById(R.id.card)
        favorite = view. findViewById(R.id.favorite)
        controlRepeat = view.findViewById(R.id.id_repeat1)
        shufle = view.findViewById(R.id.id_shuffle1)
        listPlayer = view .findViewById(R.id.list)
        next= view .findViewById(R.id.id_next)
        prev= view.findViewById(R.id.id_prev)
        playPause = view.findViewById(R.id.play_pause)
        controlRepeat.setOnClickListener(this)
        favorite.setOnClickListener(this)
        shufle.setOnClickListener(this)
        next.setOnClickListener(this)
        prev.setOnClickListener(this)
        playPause.setOnClickListener(this)
        listPlayer.setOnClickListener(this)
        // info music
        songTitle = view.findViewById(R.id.song_name)
        songArtist = view.findViewById(R.id.song_artist)
        numberLis = view.findViewById(R.id.num_canciones)

        // seekBar
        seekBar = view.findViewById(R.id.seekBar)
        //durations
        totalDuration = view.findViewById(R.id.durationTotal)
        currentPosition = view.findViewById(R.id.durationPlayer)

        val s = SettingsPlayingPreferences(requireContext())
        if (s.getMyThemeControls() == 2) {
            seekBar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.stylebar)
        } else if (s.getMyThemeControls() == 3) {
            seekBar.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.stylebar2)
        } else if (s.getMyThemeControls() == 4) {
            seekBar.progressDrawable = ColorDrawable(Color.YELLOW)
        }
        Theme_Font.setFont(songTitle, requireContext(),s)
        Theme_Font.setFont(songArtist, requireContext(),s)
        Theme_Font.setFont(numberLis, requireContext(),s)
        Theme_Font.setFont(totalDuration, requireContext(),s)
        Theme_Font.setFont(currentPosition, requireContext(),s)

        val theme = settingsPlayingPreferences.getMyThemePlayerMC()
        if(theme >=  5){
            card.visibility=View.GONE
            card=view.findViewById(R.id.player_card)
            bgGradient=view.findViewById(R.id.player_gradient)
        }
    }


    override fun onClick(v: View?) {
        try {
            if (v != null) {
                onConductorListener?.clickControls(v)
            }
        }catch (e:Exception){
            Toast.makeText(context, "$e frarment mode conductor 55",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if( context is OnConductorListener) {
            onConductorListener = context
        }
    }

    override fun onResume() {
        super.onResume()
        if(onConductorListener != null ) {
            try {
                onConductorListener!!.inicializerInfo(
                    seekBar,
                    card,
                    songTitle,
                    songArtist,
                    numberLis,
                    playPause,
                    currentPosition,
                    totalDuration,
                    controlRepeat,
                    shufle,
                    favorite,
                    bgGradient)
            } catch (e: Exception) {
                Toast.makeText(context, "$e frarment mode conductor 55", Toast.LENGTH_SHORT).show()
            }
            onConductorListener!!.upDateInfo()
        }
    }

}
interface OnConductorListener{
    fun inicializerInfo(seekbar: SeekBar,
                        card: ImageView?,
                        title: TextView,
                        artist: TextView,
                        numberLis: TextView,
                        playPause: ImageView,
                        currentPosition: TextView,
                        totalDuration: TextView,
                        repeat:ImageView,
                        shufle: ImageView,
                        favorite: ImageView?,
                        bgGradient: ImageView?)
    fun upDateInfo()
    fun clickControls( v : View )
}