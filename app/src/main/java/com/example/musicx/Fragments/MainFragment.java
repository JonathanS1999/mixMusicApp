package com.example.musicx.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicx.AlbumFragment;
import com.example.musicx.ArtistFragment;
import com.example.musicx.FoldersFragment;
import com.example.musicx.List_Fragment;
import com.example.musicx.R;
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.SongsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.musicx.MainActivity.ARTIST_NAME;
import static com.example.musicx.MainActivity.ARTIST_TO_FRAG;
import static com.example.musicx.MainActivity.MUSIC_FILE;
import static com.example.musicx.MainActivity.MUSIC_FILE_LAST_PLAYED;
import static com.example.musicx.MainActivity.PATH_TO_FRAG;
import static com.example.musicx.MainActivity.POSITION_NAME;
import static com.example.musicx.MainActivity.POSITION_TO_FRAG;
import static com.example.musicx.MainActivity.SHOW_MINI_PLAYER;
import static com.example.musicx.MainActivity.SONG_NAME;
import static com.example.musicx.MainActivity.SONG_NAME_TO_FRAG;


public class MainFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewpager2);
        tabLayout = view.findViewById(R.id.tab_layout2);
        initViewPager();
    }


    private void initViewPager() {
        SettingsPlayingPreferences preferences = new SettingsPlayingPreferences(requireContext());
        int theme = preferences.getMyThemeList();

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
            viewPagerAdapter.addFragments(new SongsFragment(), "canciones");
            viewPagerAdapter.addFragments(new List_Fragment(), "lista de reproducción");
            viewPagerAdapter.addFragments(new ArtistFragment(),"artistas");
            AlbumFragment af = new AlbumFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("idStyle",theme);
            viewPagerAdapter.addFragments(af, "albums");
            viewPagerAdapter.addFragments( new FoldersFragment(),"carpetas");

            viewPager.setAdapter(viewPagerAdapter);

            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> tab.setText(viewPagerAdapter.getTitles().get(position))).attach();

    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            SharedPreferences preferences = requireActivity().getSharedPreferences(MUSIC_FILE_LAST_PLAYED, MODE_PRIVATE);
            String path = preferences.getString(MUSIC_FILE, null);
            String artist = preferences.getString(ARTIST_NAME, null);
            String song_name = preferences.getString(SONG_NAME, null);
            String position_name = preferences.getString(POSITION_NAME, null);
            if (path != null) {
                SHOW_MINI_PLAYER = true;
                PATH_TO_FRAG = path;
                ARTIST_TO_FRAG = artist;
                SONG_NAME_TO_FRAG = song_name;
                POSITION_TO_FRAG = position_name;
            } else {
                SHOW_MINI_PLAYER = false;
                PATH_TO_FRAG = null;
                ARTIST_TO_FRAG = null;
                SONG_NAME_TO_FRAG = null;
            }
        }catch(Exception e){
            Toast.makeText(getContext(),e+" main fragment 105",Toast.LENGTH_SHORT).show();
        }
    }


}
