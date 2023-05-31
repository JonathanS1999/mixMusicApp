package com.example.musicx;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;

public class Theme_Font {

    public static void setFont(TextView textView , Context context , SettingsPlayingPreferences s ){
        Typeface typeface;
        switch (s.getMyThemeFont()){
            case 1 : break;
            case 2: typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Digital7-1e1Z.ttf");
                     textView.setTypeface(typeface , Typeface.BOLD);
                    break;

        }


    }

}
