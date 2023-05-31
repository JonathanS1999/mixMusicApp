package com.example.musicx.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.musicx.FragmentPlayerCardInterface
import com.example.musicx.R
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences
import com.example.musicx.Theme_Font

class FragmentCardImg: Fragment(),View.OnClickListener {
    private var theme=0
    private lateinit var imageCard : ImageView
    private var playerCardInterface: FragmentPlayerCardInterface? = null
    private  var menuMore:ImageView? = null
    private  var favorite:ImageView? = null

    private var playerGradient: ImageView? = null

    // variables para mostrar detalles de la cancion
    private var song_name: TextView? = null  // variables para mostrar detalles de la cancion
    private var artist_name: TextView? = null  // variables para mostrar detalles de la cancion
    private var duration_played: TextView? = null  // variables para mostrar detalles de la cancion
    private var duration_total: TextView? = null  // variables para mostrar detalles de la cancion
    private var numCanciones: TextView? = null

    //variables de controles
    private var nextBtn: ImageView? = null  //variables de controles
    private var prevBtn: ImageView? = null  //variables de controles
    private var shuffleBtn: ImageView? = null  //variables de controles
    private var repeatBtn: ImageView? = null
    private var playPauseBtn: ImageView? = null
    private var seekBar: SeekBar? = null
    private var frameCircle: LinearLayout? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerCardInterface=context as FragmentPlayerCardInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        return inflater.inflate(R.layout.fragment__player_card,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null){
            theme = bundle.getInt("idStyleCard", 1)
        }

        imageCard=view.findViewById(R.id.player_card)
        menuMore = view.findViewById(R.id.menuMore_player)
        favorite = view.findViewById(R.id.favorite_player)
        song_name = view.findViewById(R.id.song_name)
        artist_name = view.findViewById(R.id.song_artist)
        duration_played = view.findViewById(R.id.durationPlayer)
        duration_total = view.findViewById(R.id.durationTotal)
        nextBtn = view.findViewById(R.id.id_next)
        nextBtn!!.setOnClickListener(this)
        prevBtn = view.findViewById(R.id.id_prev)
        prevBtn!!.setOnClickListener(this)
        shuffleBtn = view.findViewById(R.id.id_shuffle1)
        shuffleBtn!!.setOnClickListener(this)
        repeatBtn = view.findViewById(R.id.id_repeat1)
        repeatBtn!!.setOnClickListener(this)
        playPauseBtn = view.findViewById(R.id.play_pause)
        playPauseBtn!!.setOnClickListener(this)
        seekBar = view.findViewById(R.id.seekBar)
        numCanciones = view.findViewById(R.id.num_canciones)
        playerGradient = view.findViewById(R.id.player_gradient)
        frameCircle = view.findViewById(R.id.layoutFrame)


        if (playerCardInterface != null) addListeners()


        val s = SettingsPlayingPreferences(requireContext())
        if (s.getMyThemeControls() == 2) {
            seekBar!!.setProgressDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.stylebar
                )
            )
        } else if (s.getMyThemeControls() == 3) {
            seekBar!!.setProgressDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.stylebar2
                )
            )
        } else if (s.getMyThemeControls() == 4) {
            seekBar!!.setProgressDrawable(ColorDrawable(Color.YELLOW))
        }

        Theme_Font.setFont(song_name, requireContext(), s)
        Theme_Font.setFont(artist_name, requireContext(), s)
        Theme_Font.setFont(duration_played, requireContext(), s)
        Theme_Font.setFont(duration_total, requireContext(), s)
        Theme_Font.setFont(numCanciones, requireContext(), s)
    }

    private fun addListeners(){
        menuMore!!.setOnClickListener { playerCardInterface!!.showMyMenu() }
        favorite!!.setOnClickListener { playerCardInterface!!.setFavoriteMusic() }
    }

    override fun onResume() {
        super.onResume()
        (context as OnThemeCard).setThemeCard(theme)
        playerCardInterface!!.inicializer(
            song_name, artist_name, duration_played,
            duration_total, numCanciones, nextBtn,
            prevBtn, shuffleBtn, repeatBtn, playPauseBtn,
            seekBar, imageCard, favorite, playerGradient
        )
        playerCardInterface!!.setFrameCircle(frameCircle)
    }

    override fun onClick(v: View?) {
        playerCardInterface!!.onClickPlayerCard(v)
    }

    interface OnThemeCard{
        fun setThemeCard(theme :Int )
    }
}