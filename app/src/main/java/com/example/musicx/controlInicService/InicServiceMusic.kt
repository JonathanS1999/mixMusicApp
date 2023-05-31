package com.example.musicx.controlInicService

import android.content.Context
import android.content.Intent
import android.widget.SeekBar
import android.widget.Toast
import com.example.musicx.MusicFiles
import com.example.musicx.MusicService
import com.example.musicx.converTimes.ConvertTime

class InicServiceMusic(val context: Context, val intent: Intent, val musicService: MusicService,
                       private val elementSelected: Int, private val booleanSavedInstaceState: Boolean,
                       private val listSongs: ArrayList<MusicFiles>, private val inicServiceMusic: OnIniciMusicServiceListener,
                       private val mode: Int, val convertTime: ConvertTime
                       ) {

    private var seekBar:SeekBar? = null

    fun setSeekBar(seekBar: SeekBar){
        this.seekBar=seekBar
    }

    fun iniciarVistaReproduccion() {
        val b = intent.extras
        musicService.setCategoryMusic(elementSelected)
        try {
            if (musicService.getMediaPlayer() != null) {
                seekBar?.max = musicService.duration
                if (!booleanSavedInstaceState) { //booleanSaveInstanceState ==false
                    if (musicService.sizeMusics() == listSongs.size) {
                        if (musicService.getPosition() == b!!.getInt("position", -1)) {
                            if (musicService.get(musicService.getPosition()).getTitle().toString() == listSongs.get(musicService.getPosition()).getTitle().toString()) {
                                inicServiceMusic.updateInfoGeneral()
                            } else {
                                prepare()
                            }
                        } else {
                            prepare()
                        }
                    } else {
                        prepare()
                    }
                } else { //if booleanSaved == true
                    inicServiceMusic.updateInfoGeneral()
                }
            } else {
                prepare()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "$e  1732  error", Toast.LENGTH_SHORT).show()
           // prepare()
        }
        musicService.getConfigControls()
    }

    fun prepare() {
        val b = intent.extras
        musicService.setPosition(b!!.getInt("position", -1))
        musicService.updateMusics(listSongs)
        inicServiceMusic.updateInfoGeneral()
        if(mode==1) {
            musicService.btn_nextClicked()
        }
    }


    interface OnIniciMusicServiceListener{
        fun updateInfoGeneral();
    }
}