package com.example.musicx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.musicx.AdaptableBackground;
import com.example.musicx.R;
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.background.MusicBackground;
import com.example.musicx.custom.AdaptaBleFramePlayerCard;
import com.example.musicx.custom.ViewCardImg;
import com.example.musicx.image.ImageSong;

import java.util.Locale;

public class StyleInterfacePlayer {

    public static void setStyle(SettingsPlayingPreferences settings){

    }

    private static void setText(){

    }

    /*

            try {

        if(settings.getMyThemePlayer() > 4 ){
            imgPort8 = imageCard ;
            imageCard.setImageDrawable(null);
            bgGradient = findViewById(R.id.player_gradient);
            imageCard = findViewById(R.id.playercard);
        }else{
            ( (ImageView)findViewById(R.id.player_gradient) ).setImageDrawable(null);
            ( (ImageView)findViewById(R.id.playercard) ).setImageDrawable(null);
        }

        if(settings.getMyThemePlayer() != 12){
            playMusicControls.setImageCard(imageCard);
        }

        String string = String.format(Locale.US,"%s", musicService.get(musicService.getPosition()).getTitle().replace(".mp3", "").replace(".wav", ""));
        song_name.setText(string);
        artist_name.setText(musicService.get(musicService.getPosition()).getArtist());
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

            Glide.with(context).load(uri).placeholder(drawable).override(20,20).into(imageBackground);

            // adapta el color del fondo con la portada de la imagen
            //adaptableBackground.inicFondo();

            if (musicBackground == null )
                musicBackground = new MusicBackground(setBackgroud,this);
        }else{
            if (musicBackground == null ){
                musicBackground = new MusicBackground(setBackgroud,this);
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

     */
}
