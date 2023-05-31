package com.example.musicx;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import static com.example.musicx.MainActivity.musicFilesGeneral;

public class FoldersFragment extends Fragment {

    private SwipeRefreshLayout swipe;
    private FoldersAdapter foldersAdapter;
    private RecyclerView recyclerView;
    private OnFoldersListener foldersListener;
    private OnShowButtonList showBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFoldersListener) foldersListener = (OnFoldersListener) context;
        if ( context instanceof  OnShowButtonList){
            showBtn =(OnShowButtonList) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folders, container, false);
         recyclerView = view.findViewById(R.id.recyclerFolders);
        swipe=view.findViewById(R.id.swipeFolders);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicElements();
        refreshElements();
    }

    private void inicElements(){
        try{
             foldersAdapter = new FoldersAdapter(getContext(),musicFilesGeneral);
            recyclerView.setAdapter(foldersAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,
                    false));
            foldersListener.onFoldersAdaper(foldersAdapter);
    }catch (Exception e){
        Toast.makeText(getContext(),e+" 61 folder fragment", Toast.LENGTH_LONG).show();
    }
    }
    @Override
    public void onResume() {
        super.onResume();
        showBtn.showButton(false);
    }

    private void refreshElements(){
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                foldersAdapter.updateList(musicFilesGeneral); //revisar linea ( no actualiza en tiempo real )
                swipe.setRefreshing(false);
            }
        });
    }
interface OnFoldersListener{
        void onFoldersAdaper(FoldersAdapter adapter);
}
}