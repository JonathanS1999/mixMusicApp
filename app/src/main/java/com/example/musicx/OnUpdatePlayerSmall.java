package com.example.musicx;

public interface OnUpdatePlayerSmall  {

   void updateImage(byte[] image);
   void progressMax(int progress);
   void progress(int progress);
   void statePlaying( boolean playing );
   void title(String title );
   void numSongs ( String num);
   void artist (String artist);
   void updatePosition(int position );
   void currentMusicFile(MusicFiles fil);

}
