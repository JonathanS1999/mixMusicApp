package com.example.musicx;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.musicx.adapterScreemSize.AdapterScreemSize;
import com.example.musicx.custom.FrameTheme;
import com.example.musicx.updateSongs.UpdateSongs;

import static com.example.musicx.MainActivity.musicFilesGeneral;
public class AlbumFragment extends Fragment  {

private SwipeRefreshLayout swipe;
private AlbumsAdapter albumAdapter;
private  RecyclerView recyclerView ;
private OnAlbumListener albumListener;
private int idStyle=0;
private OnShowButtonList showBtn;
private RelativeLayout relativeLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnAlbumListener) albumListener =( OnAlbumListener) context;
        if ( context instanceof  OnShowButtonList){
            showBtn =(OnShowButtonList) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_album, container, false);
         recyclerView = view.findViewById(R.id.recycler_albums);
          swipe =view.findViewById(R.id.swipealbums);
           return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayout=view.findViewById(R.id.layoutTheme);
        try{

            Bundle bundle = getArguments();
            if(bundle != null) idStyle=bundle.getInt("idStyle",1);

            if(idStyle ==5){
                relativeLayout.addView(new FrameTheme(requireContext()));
                RelativeLayout.LayoutParams p=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                p.setMargins(60,60,60,60);
                swipe.setLayoutParams(p);
            }

        albumAdapter = new AlbumsAdapter(getContext(), musicFilesGeneral, idStyle);
        recyclerView.setAdapter(albumAdapter);
        if(idStyle == 4 ){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        }else{
            new AdapterScreemSize(recyclerView, requireContext(),1);
        }
        refreshElements();
        if(albumListener != null) {
            albumListener.albumAdapter(albumAdapter);
        }
    }catch (Exception e){
        Toast.makeText(getContext(),e+" 51 album fragment", Toast.LENGTH_LONG).show();
    }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(showBtn != null) {
            showBtn.showButton(false);
        }
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

    interface OnAlbumListener{
        void albumAdapter(AlbumsAdapter adapter);
    }
}