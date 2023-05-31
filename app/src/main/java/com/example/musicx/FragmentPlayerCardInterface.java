package com.example.musicx;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public interface FragmentPlayerCardInterface {

    void setFavoriteMusic();
    void showMyMenu();
    void onClickPlayerCard(View v);
    void updatePager();
    void setFrameCircle(LinearLayout frameCircle);
    void inicializer(TextView song_name , TextView artist_name ,
                     TextView duration_played , TextView duration_total ,
                     TextView numCanciones, ImageView nextBtn , ImageView prevBtn ,
                     ImageView shuffleBtn , ImageView repeatBtn , ImageView playPauseBtn ,
                     SeekBar seekBar , ImageView imageMusic, ImageView favorite , ImageView bgGradient );

}
