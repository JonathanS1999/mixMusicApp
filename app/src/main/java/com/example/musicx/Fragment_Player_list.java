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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.adapterScreemSize.AdapterScreemSize;
import com.example.musicx.custom.FrameTheme;


public class Fragment_Player_list extends Fragment {

    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;

    private FragmentListPlayerInterface fragmentListPlayerInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if ( context instanceof FragmentListPlayerInterface ){
            fragmentListPlayerInterface = ( FragmentListPlayerInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment__player_list, container, false);
        recyclerView= vista.findViewById(R.id.player_lista);
        recyclerView.setHasFixedSize(true);
        relativeLayout=vista.findViewById(R.id.layoutTheme);
        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (fragmentListPlayerInterface != null) {
                SettingsPlayingPreferences preferences = new SettingsPlayingPreferences(requireContext());
                int theme = preferences.getMyThemeList();

                if(preferences.getMyThemeList() ==5){
                    relativeLayout.addView(new FrameTheme(requireContext()));
                    FrameLayout.LayoutParams p=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    p.setMargins(60,60,60,60);
                    recyclerView.setLayoutParams(p);
                }
                ListPlayerAdapter listPlayerAdapter = new ListPlayerAdapter(getContext(),theme);
                recyclerView.setAdapter(listPlayerAdapter);
                if(theme ==1 || theme == 2 ) {
                    if (theme == 2 ){
                        new AdapterScreemSize(recyclerView, requireContext(),0 , 2 , true);
                    }else {
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                                false));
                    }
                }else if( theme == 3 ){
                        new AdapterScreemSize(recyclerView, requireContext(),0,2, true);
                }else if(theme == 4 ){
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                }else{
                        new AdapterScreemSize(recyclerView, requireContext(),1,2, false);
                }

                fragmentListPlayerInterface.setPlayerListAdapter(listPlayerAdapter, recyclerView);
                recyclerView.scrollToPosition(fragmentListPlayerInterface.getPositionForList());
            }
        }catch (Exception exception){
            Toast.makeText(getContext(),exception+" player list error 61 "+exception.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }




    interface FragmentListPlayerInterface{
        int getPositionForList();
        void setPlayerListAdapter(ListPlayerAdapter adapter ,RecyclerView recyclerView);
    }

}