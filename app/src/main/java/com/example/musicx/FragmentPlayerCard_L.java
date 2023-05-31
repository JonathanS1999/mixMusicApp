package com.example.musicx;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.custom.AddFrameCircle;
import com.example.musicx.custom.CircleTheme;

public class FragmentPlayerCard_L extends Fragment implements View.OnClickListener {

    private ImageView portada, menuMore, favorite;

    private FragmentPlayerCardInterface playerCardInterface;
    private ImageView playerGradient;

    // variables para mostrar detalles de la cancion
    private TextView song_name, artist_name, duration_played, duration_total, numCanciones;

    //variables de controles
    private ImageView nextBtn, prevBtn, shuffleBtn, repeatBtn;
    private ImageView playPauseBtn;
    private SeekBar seekBar;
    private int theme =0;
    private LinearLayout frameCircle;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentPlayerCardInterface) {
            this.playerCardInterface = (FragmentPlayerCardInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment__player_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            theme = bundle.getInt("idStyleCard", 1);

        portada = view.findViewById(R.id.player_card);
        menuMore = view.findViewById(R.id.menuMore_player);
        favorite = view.findViewById(R.id.favorite_player);
        song_name = view.findViewById(R.id.song_name);
        artist_name = view.findViewById(R.id.song_artist);
        duration_played = view.findViewById(R.id.durationPlayer);
        duration_total = view.findViewById(R.id.durationTotal);
        nextBtn = view.findViewById(R.id.id_next);
        nextBtn.setOnClickListener(this);
        prevBtn = view.findViewById(R.id.id_prev);
        prevBtn.setOnClickListener(this);
        shuffleBtn = view.findViewById(R.id.id_shuffle1);
        shuffleBtn.setOnClickListener(this);
        repeatBtn = view.findViewById(R.id.id_repeat1);
        repeatBtn.setOnClickListener(this);
        playPauseBtn = view.findViewById(R.id.play_pause);
        playPauseBtn.setOnClickListener(this);
        seekBar = view.findViewById(R.id.seekBar);
        numCanciones = view.findViewById(R.id.num_canciones);
        playerGradient = view.findViewById(R.id.player_gradient);
        frameCircle= view.findViewById(R.id.layoutFrame);

        if(playerCardInterface !=  null)
        addListeners();

        SettingsPlayingPreferences s = new SettingsPlayingPreferences(requireContext());
        if(s.getMyThemeControls()==2){
            seekBar.setProgressDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.stylebar));
        }else if(s.getMyThemeControls()==3){
            seekBar.setProgressDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.stylebar2));
        }else if(s.getMyThemeControls()==4){
            seekBar.setProgressDrawable(new ColorDrawable(Color.YELLOW));
        }

        Theme_Font.setFont(song_name, requireContext(),s);
        Theme_Font.setFont(artist_name, requireContext(),s);
        Theme_Font.setFont(duration_played, requireContext(),s);
        Theme_Font.setFont(duration_total, requireContext(),s);
        Theme_Font.setFont(numCanciones, requireContext(),s);
    }

    private void addListeners() {
        menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerCardInterface.showMyMenu();
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerCardInterface.setFavoriteMusic();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (requireContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (playerCardInterface != null) {
                    playerCardInterface.updatePager();
                    playerCardInterface.inicializer(song_name, artist_name, duration_played,
                            duration_total, numCanciones, nextBtn,
                            prevBtn, shuffleBtn, repeatBtn, playPauseBtn,
                            seekBar, portada, favorite , playerGradient );
                    playerCardInterface.setFrameCircle(frameCircle);
                }

            }


        }catch (Exception e){
            Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        playerCardInterface.onClickPlayerCard(v);
    }
}
