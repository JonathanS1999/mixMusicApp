package com.example.musicx.custom

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.LinearLayout
import android.widget.Toast
import com.example.musicx.MusicService
import com.example.musicx.R
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences
import com.example.musicx.image.ImageSong

class AdaptaBleFramePlayerCard( val context: Context , val settings: SettingsPlayingPreferences?=null
                                , val frameCircle: LinearLayout, val musicService: MusicService ,
                               val themePreview:Int) {
    init {
        var themeSettings=0
        if(settings!= null){
            themeSettings=settings.getMyThemePlayer()
        }else{
            themeSettings=themePreview
        }

        if (themeSettings== 10) {
            try {
                val picture = ImageSong.getBitmapPicture(musicService[musicService.position].picturePath)
                if (picture != null) frameCircle.addView(
                    CustomView(
                        context, BitmapFactory.decodeByteArray(
                            picture,
                            0,
                            picture.size
                        ), "", frameCircle
                    )
                ) else frameCircle.addView(
                    CustomView(
                        context,
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.iconp),
                        "",
                        frameCircle
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(context, "$e 434 player  ", Toast.LENGTH_LONG).show()
            }
        } else if (themeSettings== 11) {
            try {
                val picture = ImageSong.getBitmapPicture(musicService[musicService.position].picturePath)
                if (picture != null) frameCircle.addView(
                    CustomViewPlayer(
                        context, BitmapFactory.decodeByteArray(
                            picture,
                            0,
                            picture.size
                        ), "", frameCircle
                    )
                ) else frameCircle.addView(
                    CustomViewPlayer(
                        context,
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.iconp),
                        "",
                        frameCircle
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(context, "$e 434 player  ", Toast.LENGTH_LONG).show()
            }
        }
    }

}