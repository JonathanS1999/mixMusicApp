package com.example.musicx.updateSongs;

import android.content.Context;

import com.example.musicx.SearchSongs.SearchSongs;

public class UpdateSongs {

    public static void updateDataArraysSongs(Context context){

        new SearchSongs(context).getAllAudio((SearchSongs.OnFinishSearchAudio) context);

    }
}
