package com.example.musicx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.musicx.MainActivity.ARTIST_TO_FRAG;
import static com.example.musicx.MainActivity.PATH_TO_FRAG;
import static com.example.musicx.MainActivity.POSITION_TO_FRAG;
import static com.example.musicx.MainActivity.SHOW_MINI_PLAYER;
import static com.example.musicx.MainActivity.SONG_NAME_TO_FRAG;
import static com.example.musicx.MainActivity.musicFilesGeneral;

import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;


public class NowPlayingFragment extends Fragment  implements OnUpdatePlayerSmall{
    //static variables
    public static ArrayList<MusicFiles> nowPlayerSongs;
    public static OnUpdatePlayerSmall playerSmall;

    private ImageView albumArtNowFragment ;
    private TextView artistNowFragmet , songNameNowFragment , numSongNowFragment ;
    private ProgressBar progressBarVistapequena;
    private OnNowPlayerListener nowPlayerListener ;
    private ImageView playPauseBtn;

    public NowPlayingFragment() {
    }



    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if( context instanceof OnNowPlayerListener) nowPlayerListener = (OnNowPlayerListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_now_playing, container, false);
        LinearLayout linearLayoutContainer = vista.findViewById(R.id.linear_layout_container);
        artistNowFragmet = vista.findViewById(R.id.texartista_main);
        songNameNowFragment = vista.findViewById(R.id.music_file_name_main);
        numSongNowFragment = vista.findViewById(R.id.numcancion_main);
        albumArtNowFragment = vista.findViewById(R.id.music_img_main);
        progressBarVistapequena = vista.findViewById(R.id.progresBar_main);
        ImageView nextBtn = vista.findViewById(R.id.next_main);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( nowPlayerSongs != null ) {
                    Intent serviceIntent = new Intent(getContext(), MusicService.class);
                    serviceIntent.putExtra("ActionName", "next");
                    String string = POSITION_TO_FRAG;
                    int position = Integer.parseInt(string);
                    serviceIntent.putExtra("Position",position);
                    requireActivity().startService(serviceIntent);
                }
            }
        });
        ImageView prevBtn = vista.findViewById(R.id.prev_main);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( nowPlayerSongs != null ) {
                    Intent serviceIntent = new Intent(getContext(), MusicService.class);
                    serviceIntent.putExtra("ActionName","previous");
                    String string = POSITION_TO_FRAG;
                    int position =Integer.parseInt(string);
                    serviceIntent.putExtra("Position",position);
                    requireActivity().startService(serviceIntent);
                }
            }
        });
        playPauseBtn = vista.findViewById(R.id.play_pause_main);
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( nowPlayerSongs != null ) {
                    Intent serviceIntent = new Intent(getContext(), MusicService.class);
                    serviceIntent.putExtra("ActionName","playPause");
                    String string = POSITION_TO_FRAG;
                    int position  = Integer.parseInt(string);
                    serviceIntent.putExtra("Position",position);
                    requireActivity().startService(serviceIntent);
                }
            }
        });

        linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( nowPlayerSongs != null) {
                    Intent intent = new Intent(getContext(), PlayerActivity.class);
                    intent.putExtra("position", Integer.parseInt(POSITION_TO_FRAG))
                            .putExtra("elemtSelected", 4);
                    requireActivity().startActivity(intent);
                }else{
                    Toast.makeText(getContext(), nowPlayerSongs +" 117 ", Toast.LENGTH_LONG).show();
                }
            }
        });

        return vista;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SettingsPlayingPreferences s = new SettingsPlayingPreferences(requireContext());
        Theme_Font.setFont(artistNowFragmet,requireContext(),s);
        Theme_Font.setFont(songNameNowFragment,requireContext(),s);
        Theme_Font.setFont(numSongNowFragment,requireContext(),s);
    }

    @Override
    public void onResume() {
        super.onResume();
        playerSmall = this;
        try {
                nowPlayerListener.updateViews(
                        albumArtNowFragment,
                        songNameNowFragment,
                        artistNowFragmet,
                        numSongNowFragment,
                        progressBarVistapequena ,
                        nowPlayerSongs);

            if ( musicFilesGeneral != null) {//si hay audios en este dispositivo
                if (SHOW_MINI_PLAYER) {//si se puede mostrar informacion de audio
                    if (PATH_TO_FRAG != null) {// si la ultima cansio escuchada
                            byte[] art = getAlbumArt(PATH_TO_FRAG);
                            if (art != null) {
                                Bitmap port = BitmapFactory.decodeByteArray(art,
                                        0, art.length);
                                albumArtNowFragment.setImageBitmap(port);
                            } else {
                                albumArtNowFragment.setImageResource(R.drawable.play2);
                            }
                            songNameNowFragment.setText(SONG_NAME_TO_FRAG);
                            artistNowFragmet.setText(ARTIST_TO_FRAG);
                            if (POSITION_TO_FRAG == null) {
                                numSongNowFragment.setText("0");
                            } else {
                                String string = String.format(Locale.US, "%d", (Integer.parseInt(POSITION_TO_FRAG) + 1));
                                numSongNowFragment.setText(string);
                            }
                    }
                }

                if(nowPlayerSongs !=null) {
                    Intent intent = new Intent(getContext(), MusicService.class);
                    intent.putExtra("ActionName", "MINIPLAYER");
                    requireActivity().startService(intent);
                }

            }
        }catch(Exception exception ){
            Toast.makeText(getContext(),exception+"   nplayer "+POSITION_TO_FRAG+" "+ARTIST_TO_FRAG+" "+SONG_NAME_TO_FRAG+" "+SHOW_MINI_PLAYER+" "+PATH_TO_FRAG,Toast.LENGTH_SHORT).show();
        }
    }

    private byte [] getAlbumArt(String uri){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        return retriever.getEmbeddedPicture();
    }


    @Override
    public void updateImage(byte[] image) {
            if (image != null) {
                Bitmap port = BitmapFactory.decodeByteArray(image,
                        0,image.length);
                albumArtNowFragment.setImageBitmap(port);
            } else {
                albumArtNowFragment.setImageResource(R.drawable.play2);
            }
    }

    @Override
    public void progressMax(int progress) {
        progressBarVistapequena.setMax(progress);
    }

    @Override
    public void progress(int progress) {
        progressBarVistapequena.setProgress(progress);
    }

    @Override
    public void statePlaying(boolean playing) {
        if(playing){
            playPauseBtn.setImageDrawable(
            ContextCompat.getDrawable(requireContext(),R.drawable.ic_pause));
        }else{
            playPauseBtn.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_play));
        }
        nowPlayerListener.setStatePlaying(playing);
    }

    @Override
    public void title(String title) {
        songNameNowFragment.setText(title);
    }

    @Override
    public void numSongs(String num) {
        numSongNowFragment.setText(num);
    }

    @Override
    public void artist(String artist) {
        artistNowFragmet.setText(artist);
    }

    @Override
    public void updatePosition(int position) {
        if(position >=0 ) POSITION_TO_FRAG= String.valueOf(position);
        nowPlayerListener.setCurrentPosition(position);
    }

    @Override
    public void currentMusicFile(MusicFiles fil) {
     nowPlayerListener.setMusicFileActual(fil);
    }

    public interface OnNowPlayerListener{
         void updateViews(ImageView card , TextView title , TextView artist , TextView num, ProgressBar progressBar, ArrayList<MusicFiles> nowPlayerSongs);
         void setMusicFileActual(MusicFiles fil);
         void setCurrentPosition(int currentPosition);
         void setStatePlaying(boolean statePlaying);
    }
}