package com.example.musicx;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.musicx.adapterScreemSize.AdapterScreemSize;
import com.example.musicx.updateSongs.UpdateSongs;

import static com.example.musicx.MainActivity.musicFilesGeneral;

public class ArtistFragment extends Fragment {

private SwipeRefreshLayout swipe;
private  RecyclerView recyclerView  ;
private  ArtistAdapter artistAdapter ;
private OnArtistListener artistListener;
private OnShowButtonList showBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if( context instanceof OnArtistListener) artistListener= (OnArtistListener) context;
        if ( context instanceof  OnShowButtonList){
            showBtn =(OnShowButtonList) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.artist_fragment, container, false);
         recyclerView = view.findViewById(R.id.recycler_artist);
        swipe= view.findViewById(R.id.swipeArtist);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            initialsElements();
            refreshElements();
        }catch (Exception e){
            Toast.makeText(getContext(),e+" 48 artist fragment", Toast.LENGTH_LONG).show();
        }
    }

    private void initialsElements(){
        artistAdapter = new ArtistAdapter(getContext(),musicFilesGeneral);
        recyclerView.setAdapter(artistAdapter);
        new AdapterScreemSize(recyclerView, requireContext(),1);
        artistListener.artistAdapter(artistAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        showBtn.showButton(false);
    }

    private void refreshElements (){
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                UpdateSongs.updateDataArraysSongs(getContext());
                swipe.setRefreshing(false);
            }
        });
    }

    interface OnArtistListener{
        void artistAdapter(ArtistAdapter adapter);
    }

}
