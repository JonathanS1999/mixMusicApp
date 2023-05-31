package com.example.musicx.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicx.AlbumDetailsAdapter
import com.example.musicx.MainActivity.musicFilesGeneral
import com.example.musicx.R
import com.example.musicx.background.MusicBackground

class FragmentListNormal: Fragment() {
   private lateinit var recyclerView:RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View{
        return inflater.inflate(R.layout.fragment_list_normal,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         recyclerView= view.findViewById(R.id.normalList)
        if (musicFilesGeneral.size > 0) {
            val albumDetailsAdapter = AlbumDetailsAdapter(context, musicFilesGeneral)
            recyclerView.setAdapter(albumDetailsAdapter)
            recyclerView.setLayoutManager(LinearLayoutManager(context, RecyclerView.VERTICAL, false))
        }

    }


}