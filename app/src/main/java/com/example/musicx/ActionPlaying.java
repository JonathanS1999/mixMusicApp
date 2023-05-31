package com.example.musicx;

public interface ActionPlaying {

    //update views
    void updateViewPlayPause(int mode );

    //update views generals
    void updateInfoGeneral();

    //update controls repeat and random
    void updateControlRepeat();
    void updateControlRandom();

    void setMaxSeekBar(int maxSeekBar);
   void setSeekBarProgres(int progres);


}
