package com.example.musicx.background;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.example.musicx.R;

import java.io.InputStream;

public class MusicBackground {
    private static String BACKGROND_THEME ="BACKGROUND_THEME";
    private static String THEME_CUSTOM_BACKGROUND = "THEME_CUSTOM_BACKGROUND";
    private static String BACKGROND ="BACKGROUND" ;

    public static void setBackground(ImageView imageBackground , Context context){
            SharedPreferences preferences =context.getSharedPreferences(BACKGROND_THEME, Context.MODE_PRIVATE);
            if ( preferences.getString(THEME_CUSTOM_BACKGROUND,null) != null){
                 String path = preferences.getString(THEME_CUSTOM_BACKGROUND, null);
                try{
                    Uri uri = Uri. parse(path);
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    Drawable drawable = Drawable.createFromStream(inputStream,uri.toString());
                     imageBackground.setImageDrawable(drawable);
                } catch (Exception e) {
                    int background = preferences.getInt(BACKGROND, -1);
                    setBackgrondColor(imageBackground,background,context);
                }

            }else {
                int background = preferences.getInt(BACKGROND, -1);
                setBackgrondColor(imageBackground, background,context);
            }

        }

        private static void setBackgrondColor( ImageView imageBacmground , int backgrondColor , Context mContext ){
            switch (backgrondColor) {
                case 1 : imageBacmground.setImageDrawable(ContextCompat.getDrawable(
                        mContext, R.drawable.activity_background2));
                    break;
                case 2 : imageBacmground.setImageDrawable(ContextCompat.getDrawable(
                        mContext, R.drawable.activity_background3));
                    break;
                case 3 : imageBacmground.setImageDrawable(ContextCompat.getDrawable(
                        mContext, R.drawable.activity_background4));
                    break;
                case 4 : imageBacmground.setImageDrawable(ContextCompat.getDrawable(
                        mContext, R.drawable.activity_background5));
                    break;
                case 5 : imageBacmground.setImageDrawable(ContextCompat.getDrawable(
                        mContext, R.drawable. activity_background6));
                    break;
                case 6 : imageBacmground.setImageDrawable(ContextCompat.getDrawable(
                        mContext, R.drawable.gradiente));
                    break;
                default: imageBacmground.setImageDrawable(ContextCompat.getDrawable(
                        mContext, R.drawable.activity_main_background));

            }
        }
}
