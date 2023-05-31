package com.example.musicx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.musicx.background.MusicBackground


class AdaptableBackground (val context : Context, val backgroundImg:ImageView, val backgroundGradient:ImageView,
                           val song_name:TextView, val artist_name:TextView, val duration_played:TextView,
                           val duration_total:TextView, val numCanciones:TextView , val theme:Int ){
    private lateinit var mFile: MusicFiles

    fun setPosition(mFile:MusicFiles){
        this.mFile=mFile
    }

    fun getAlbumImagen(path: String?): Bitmap? {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(path)
        val data = mmr.embeddedPicture
        return if (data != null) BitmapFactory.decodeByteArray(data, 0, data.size) else null
    }


    fun genColors(bitmap: Bitmap?): Int {
        val newbitmap = Bitmap.createScaledBitmap(bitmap!!, 1, 1, true)
        val color = newbitmap.getPixel(0, 0)
        newbitmap.recycle()
        return color
    }

    fun inicFondo() {
        try {
            val port: Bitmap = getAlbumImagen( mFile.patch)!!

            val colorgrad = GradientDrawable()
            if(theme == 7  || theme == 8 ){
                colorgrad.colors = intArrayOf(0, Color.parseColor("#180129"))
            } else{
                colorgrad.colors = intArrayOf(0, genColors(port))
            }

            //colorgrad.color
            backgroundImg.setImageDrawable(null)
            backgroundImg.setBackgroundColor(genColors(port))
            if(theme == 7 || theme == 8 ){
                backgroundImg.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.background_my_player))

            }
            backgroundGradient.setImageDrawable(colorgrad)
            val col: Int = Color.rgb(
                255 - Color.red(genColors(port)),
                255 - Color.green(genColors(port)),
                255 - Color.blue(genColors(port))
            )
            if(theme == 7 || theme == 8 ){
                song_name.setTextColor(Color.WHITE)
                duration_played.setTextColor(Color.WHITE)
                numCanciones.setTextColor(Color.WHITE)
                duration_total.setTextColor(Color.WHITE)
                artist_name.setTextColor(Color.WHITE)
            }else{
                song_name.setTextColor(col)
                duration_played.setTextColor(col)
                numCanciones.setTextColor(col)
                duration_total.setTextColor(col)
                artist_name.setTextColor(col)
            }

            /*if (theme ==11 ){
                Glide.with(context).load(uri).placeholder(drawable).override(20,20).into(imageBackground);
            }*/

        } catch (e: Exception) {
            setDefaultTheme()
        }
    }

    fun setDefaultTheme(){
        song_name.setTextColor(Color.WHITE)
        duration_played.setTextColor(Color.WHITE)
        numCanciones.setTextColor(Color.WHITE)
        duration_total.setTextColor(Color.WHITE)
        artist_name.setTextColor(Color.WHITE)
        backgroundImg.setImageDrawable(null)
        backgroundGradient.setImageDrawable(null)
        MusicBackground.setBackground(backgroundImg, context)
    }

}