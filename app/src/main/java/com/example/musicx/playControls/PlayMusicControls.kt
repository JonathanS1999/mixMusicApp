package com.example.musicx.playControls

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.musicx.MainActivity
import com.example.musicx.MusicService
import com.example.musicx.R
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences
import com.example.musicx.image.ImageSong
import com.google.android.material.snackbar.Snackbar
import java.util.*

class PlayMusicControls(val context: Context) {
    private var repeatBtn:ImageView?= null
    private var shuffleBtn:ImageView?=null
    private var favorite: ImageView? = null
    private var imageViewCardFragment:ImageView? = null
    private var musicService: MusicService?=null
    private var settingsPlayingPreferences= SettingsPlayingPreferences(context)
    private var configTheme=0

    fun setMusicService(musicService: MusicService){
        this.musicService=musicService
    }

    fun inicializerControlRepeats(repeatBtn: ImageView, shufleBtn: ImageView ){
        this.repeatBtn=repeatBtn
        this.shuffleBtn = shufleBtn
    }

    fun setImageCard(imageCard :ImageView){
        imageViewCardFragment = imageCard
    }

    fun setImageFavorte(favorite: ImageView){
        this.favorite =favorite;
    }

    fun onClick(id: Int){
        if (id == R.id.id_repeat1) {
            viewControlRepeat()
        } else if (id == R.id.id_shuffle1) {
            musicService!!.setRandom(!musicService!!.getRandom())
            if (musicService!!.getRandom()) {
                Toast.makeText(context, "Cancion Alt activado", Toast.LENGTH_SHORT).show()
                if (musicService!!.getRepeat() != 1) { //si repetir != 1 establece 1 para que no se pause si se da la ultima
                    musicService!!.setRepeat(1)
                    if (musicService!!.getTypePlay() == 'a') {
                        musicService!!.setLooping(false)
                    }
                    Toast.makeText(context, "Rep Actual y lineal desctivado  ", Toast.LENGTH_SHORT).show()
                    repeatBtn?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_repeat))
                    repeatBtn?.setColorFilter(Color.WHITE)
                    musicService!!.setLooping(false)
                }
            } else {
                repeatBtn?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_repeat))
                repeatBtn?.setColorFilter(Color.WHITE)
                musicService!!.setLooping(false)
                Toast.makeText(context, "cancion aleatoria desactivado ", Toast.LENGTH_SHORT).show()
            }
            updateControlRandom()
        } else if (id == R.id.play_pause) {
            if (musicService!!.getClickCompletado() == 0) {
                musicService!!.btn_play_pauseClicked()
            }
        } else if (id == R.id.id_next) {
            if (musicService!!.getClickCompletado() == 0) {
                musicService!!.btn_nextClickedNotificacion()
            }
        } else if (id == R.id.id_prev) {
            if (musicService!!.getClickCompletado() == 0) {
                musicService!!.btn_prevClickedNotificacion()
            }
        }
        musicService!!.saveConfigControls(musicService!!.getRandom(), musicService!!.getRepeat())
    }

    private fun viewControlRepeat() {
        when (musicService!!.getRepeat()) {
            0 -> {
                musicService!!.setRepeat(1)
                musicService!!.setLooping(false)
                Toast.makeText(context, "Repetir todo una y otra vez" + musicService!!.getRepeat(), Toast.LENGTH_SHORT).show()
            }
            1 -> {
                musicService!!.setRepeat(2)
                Toast.makeText(context, " repetir musica actual una y otra vez " + musicService!!.getRepeat(), Toast.LENGTH_SHORT).show()
                if (musicService!!.getTypePlay() == 'a') musicService!!.setLooping(true)
                musicService!!.setLooping(true)
                if (musicService!!.getRandom()) musicService!!.setRandom(false)
            }
            2 -> {
                musicService!!.setRepeat(3)
                Toast.makeText(context, " repetir linalmente y pausar al final  " + musicService!!.getRepeat(), Toast.LENGTH_SHORT).show()
                if (musicService!!.getTypePlay() == 'a') musicService!!.setLooping(false)
                musicService!!.setLooping(false)
            }
            3 -> {
                musicService!!.setRepeat(0)
                Toast.makeText(context, " pausar despues de esta cancion " + musicService!!.getRepeat(), Toast.LENGTH_SHORT).show()
                if (musicService!!.getTypePlay() == 'a') musicService!!.setLooping(false)
                musicService!!.setLooping(false)
            }
        }
        if (musicService!!.getRandom()) {
            musicService!!.setRandom(false)
        }
        updateControlRepeat()
        updateControlRandom()
        musicService!!.saveConfigControls(musicService!!.getRandom(), musicService!!.getRepeat())
    }


    fun updateControlRandom() {
        if (musicService!!.random) {
            shuffleBtn?.setColorFilter(Color.GREEN)
        } else {
            shuffleBtn?.setColorFilter(Color.WHITE)
        }
    }

    fun updateControlRepeat() {
        when (musicService!!.repeat) {
            0 -> {
                repeatBtn?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_repeat))
                repeatBtn?.setColorFilter(Color.WHITE)
            }
            1 -> {
                repeatBtn?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_repeat))
                if (musicService!!.random) {
                    repeatBtn?.setColorFilter(Color.WHITE)
                } else {
                    repeatBtn?.setColorFilter(Color.GREEN)
                }
            }
            2 -> {
                repeatBtn?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_repeat_one))
                repeatBtn?.setColorFilter(Color.WHITE)
            }
            3 -> {
                repeatBtn?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_repeat))
                repeatBtn?.setColorFilter(Color.WHITE)
            }
        }
    }


    fun setFavoriteMusic() {

       if(imageViewCardFragment != null){
                val index = musicService!!.getPosition()
                val temp = musicService!!.get(index).title
                if (MainActivity.musicFilesfavoritos != null) {
                    if (!MainActivity.musicFilesfavoritos.contains(musicService!!.get(index))) {
                        MainActivity.musicFilesfavoritos.add(musicService!!.get(index))
                        favorite?.let { Snackbar.make(it, " Se añadio $temp a fovoritos", Snackbar.LENGTH_LONG).show() }
                    } else {
                        MainActivity.musicFilesfavoritos.remove(musicService!!.get(index))
                        if (MainActivity.musicFilesfavoritos.size == 0) MainActivity.musicFilesfavoritos = null
                        favorite?.let { Snackbar.make(it, "se eliminó $temp de tus favoritos", Snackbar.LENGTH_LONG).show() }
                    }
                } else {
                    MainActivity.musicFilesfavoritos = ArrayList()
                    MainActivity.musicFilesfavoritos.add(musicService!!.get(index))
                    favorite?.let { Snackbar.make(it, "Se añadio $temp a favoritos ", Snackbar.LENGTH_LONG).show() }
                }
                if (MainActivity.musicFilesfavoritos != null && MainActivity.musicFilesfavoritos.contains(musicService!!.get(musicService!!.getPosition()))) {
                    val drawable = ContextCompat.getDrawable(context, R.drawable.ic_favorite)
                    favorite?.setImageDrawable(drawable)
                } else {
                    val drawable = ContextCompat.getDrawable(context, R.drawable.ic_not_favorite)
                    favorite?.setImageDrawable(drawable)
                }
        }

    }

    fun isFavorite() {
        if(favorite != null){
            if (MainActivity.musicFilesfavoritos != null && MainActivity.musicFilesfavoritos.contains(musicService!!.get(musicService!!.getPosition()))) {
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_favorite)
                favorite?.setImageDrawable(drawable)
            } else {
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_not_favorite)
                favorite?.setImageDrawable(drawable)
            }
        }

    }

    fun getSettingsTheme(config : Int ):Int{

            if(config == 3){
                configTheme=3
                return 3
            }else{
                configTheme=0
                return settingsPlayingPreferences.getMyThemePlayer()
            }
    }

     fun actualizarPortada( viewPrevOrThemePlayer:Char ) {
         try {

             if (imageViewCardFragment != null) {

                     if(viewPrevOrThemePlayer=='A'){
                         establecerImagenPortada(1)
                         //ImageSong.setImage(imageViewCardFragment,context,musicService!!.get(musicService!!.getPosition()).picturePath,2,0f, true)
                         //getImageTheme(null , 1)---------------------------------------------------------------------------------------
                     }else{
                        // ImageSong.setImage(imageViewCardFragment,context,musicService!!.get(musicService!!.getPosition()).picturePath,2,0f, false)
                         //getImageThemeViewPrev(null,1)-----------------------------------------------------------------------------------
                         establecerImagenPortada(configTheme)
                     }

                 if (MainActivity.musicFilesfavoritos != null && MainActivity.musicFilesfavoritos.contains(
                         musicService!!.get(musicService!!.getPosition())
                     )
                 ) {
                     val drawable = ContextCompat.getDrawable(context, R.drawable.ic_favorite)
                     favorite?.setImageDrawable(drawable)
                 } else {
                     val drawable = ContextCompat.getDrawable(context, R.drawable.ic_not_favorite)
                     favorite?.setImageDrawable(drawable)
                 }
             }
         }catch (exception : Exception){
             Toast.makeText(context , " $exception play controls portada ", Toast.LENGTH_SHORT).show()
         }
    }


    /*

    fun getImageTheme( array: ByteArray? , opc: Int){
        var image:Bitmap?= null
        if( opc == 1 ){
            image= BitmapFactory.decodeByteArray(
                musicService!!.get(musicService!!.getPosition()).picture,
                0, musicService!!.get(musicService!!.getPosition()).picture.size
            )
        }else{
            if(array != null){
                image = BitmapFactory.decodeByteArray(
                    array,
                    0, array.size
                )
            }

        }

        if (image != null) {
            settingPort(getSettingsTheme(configTheme) , image  )

        } else {
            imageViewCardFragment!!.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.play2
                )
            )
        }
    }*/

    fun configThemePrev(theme:Int ){
        configTheme=theme
    }

/*
    fun getImageThemeViewPrev( array: ByteArray? , opc: Int){
        var image:Bitmap?= null
        if( opc == 1 ){
            image= BitmapFactory.decodeByteArray(
                musicService!!.get(musicService!!.getPosition()).picture,
                0, musicService!!.get(musicService!!.getPosition()).picture.size
            )
        }else{
            if(array != null){
                image = BitmapFactory.decodeByteArray(
                    array,
                    0, array.size
                )
            }

        }

        if (image != null) {
            settingPort(configTheme , image  )
        } else {
           setImageDefault()
        }
    }


    fun setImageDefault(){
        imageViewCardFragment!!.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.play2
            )
        )
    }

    fun settingort( theme : Int , port:Bitmap){
        val roundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(context.getResources(), port)
        if (theme == 1 || theme ==3 ) {
            roundedBitmapDrawable.isCircular = true
            imageViewCardFragment!!.setImageDrawable(roundedBitmapDrawable)
        } else  if (theme == 2 || theme ==4 ){
            roundedBitmapDrawable.setCornerRadius(12f)
            imageViewCardFragment!!.setImageDrawable(roundedBitmapDrawable)
        }else  if (theme == 5 || theme ==6 || theme ==7 ||  theme ==8){
            imageViewCardFragment!!.setImageDrawable(roundedBitmapDrawable)
        }
    }*/


    fun establecerImagenPortada( theme : Int){
        if (theme == 1 || theme ==3 ) {
            ImageSong.setImage(imageViewCardFragment,context,musicService!!.get(musicService!!.getPosition()).picturePath,2,0f, true)
        } else  if (theme == 2 || theme ==4 ){
            ImageSong.setImage(imageViewCardFragment,context,musicService!!.get(musicService!!.getPosition()).picturePath,2,12f, false)
        }else  if (theme == 5 || theme ==6 || theme ==7 ||  theme ==8){
            ImageSong.setImage(imageViewCardFragment,context,musicService!!.get(musicService!!.getPosition()).picturePath,2,0f, true)
        }
    }
}