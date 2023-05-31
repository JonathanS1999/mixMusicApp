package com.example.musicx;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.musicx.SearchSongs.SearchSongs;
import com.example.musicx.adapterScreemSize.AdapterScreemSize;
import com.example.musicx.updateSongs.UpdateSongs;

import java.util.ArrayList;

public class SongsFragment extends Fragment implements OnUpdateSongs{

    private MusicAdapter musicAdapter;
    private OnSongsListener songsListener;
    private SwipeRefreshLayout swipe ;
    private Handler handle;
    private SearchSongs.OnFinishSearchAudio finishSearchAudio;
    private SearchSongs mySongs;
    private static  final Object objectMusic= new Object();
    private Context context ;
    private ArrayList<MusicFiles> musicFilesGeneral;
    private OnShowButtonList showBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if ( context instanceof  OnSongsListener){
            songsListener =(OnSongsListener) context;
        }
        if ( context instanceof  OnShowButtonList){
            showBtn =(OnShowButtonList) context;
        }
        if ( context instanceof SearchSongs.OnFinishSearchAudio){
            finishSearchAudio =(SearchSongs.OnFinishSearchAudio) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handle =new Handler(Looper.getMainLooper());
        RecyclerView recyclerView = view.findViewById(R.id.listamusicRV);
        musicAdapter = new MusicAdapter(getContext(), null, MainActivity.musicFilesfavoritos);
        recyclerView.setAdapter(musicAdapter);
        context = getContext();
        new AdapterScreemSize(recyclerView, requireContext(),0);
        swipe = view.findViewById(R.id.swipeSong);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(songsListener != null) {
                            if (songsListener.permissions()) { //  si los permisos estan habilitados
                                synchronized (objectMusic){
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            UpdateSongs.updateDataArraysSongs(context);
                                            swipe.setRefreshing(false);

                                        }
                                    });

                                }
                            }
                        }
                    }
                }).start();
            }
        });
        if(songsListener != null) {
            songsListener.songsAdapter(musicAdapter);
            mySongs = new SearchSongs(getContext());
            refreshElements();
        }
    }



    public void refreshElements() {
        if(showBtn != null){
            showBtn.showButton(false);
        }
        if(songsListener != null){
        songsListener.setOnUpdate(this);
        if (songsListener.permissions()){
           readSong();
        }else{
            songsListener.removePreferences();
        }
        }else{
            readSong();
        }

        }

    /**
     * Metodo que lee todos los archivos de audio en el dispositivo
     */
    private void readSong() {
        if (songsListener != null){
            musicFilesGeneral = songsListener.getMusicFilesGen();//obtener arrayList de Main Activity

        if (musicFilesGeneral == null ) { // si music files es null lee todos los archivos de audio
            if ( songsListener.permissions()) {
                // hilo que se encarga de leer todos los archivos de audio existentes en segundo plano
                new Thread(new Runnable() {

                    //Metodo run llamado al iniciar el hilo
                    @Override
                    public void run() {

                        synchronized (objectMusic) { // solo un hilo puede escribir en este atributo a la vez
                            try {
                                mySongs.getAllAudio((SearchSongs.OnFinishSearchAudio) context);
                            } catch (Exception e) {

                            }
                        }
                    }
                }).start();
            }
        } else if ( musicAdapter.getItemCount() <= 0 ) { // solamente actualiza la lista de canciones
            if (songsListener.permissions()) {
                musicAdapter.updateList(musicFilesGeneral);
            }
        } else {
            Toast.makeText(context, "nada se cumple songs fragment 123 ", Toast.LENGTH_LONG).show();

        }
    }else{
            if(MainActivity.musicFilesGeneral != null && musicAdapter.getItemCount() <= 0){
                musicAdapter.updateList(MainActivity.musicFilesGeneral);
            }

        }
    }

    private void upDateNowPlaying() {
    songsListener.updateSongs();
    }

    @Override
    public void update() {
        readSong();
    }


    public interface OnSongsListener{
    void updateSongs();
    void setOnUpdate(OnUpdateSongs update);
    boolean permissions();
    void removePreferences();
    void songsAdapter(MusicAdapter adapter);
    ArrayList<MusicFiles> getMusicFilesGen();

    }
}