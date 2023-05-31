package com.example.musicx.adapterScreemSize

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdapterScreemSize{

    private var recyclerView: RecyclerView
    private var context: Context
    private var type: Int = 0
    private var listaMedia = false

    constructor(recyclerView: RecyclerView,context: Context, type: Int) {
        this.recyclerView= recyclerView
        this.context=context
        this.type=type
        updateSizeScreem(1)
    }

    constructor(recyclerView: RecyclerView,context: Context, type: Int, medio :Int , listaMedia:Boolean){
        this.recyclerView= recyclerView
        this.context=context
        this.type=type
        this.listaMedia= listaMedia
        updateSizeScreem(medio)
    }


    private fun updateSizeScreem(  medio:Int  ) {

        val ancho: Float
        val displayMetrics: DisplayMetrics = context.resources.getDisplayMetrics()
        var width =200
        if(medio ==2 && context.resources.configuration.orientation ==Configuration.ORIENTATION_LANDSCAPE){
            ancho = (displayMetrics.widthPixels.toFloat()/2) / displayMetrics.density
            width=170
        }else{
            ancho = displayMetrics.widthPixels.toFloat() / displayMetrics.density
        }

        val colums = Math.round(ancho / width)
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || listaMedia) {
       setColums(colums,type,2)
        } else {
            setColums(colums,type,4)
        }
    }

    private fun setColums(colums: Int, type: Int, maxColums: Int) {
        if (type == 0) {
            if (colums > maxColums) {
                recyclerView.layoutManager = GridLayoutManager(context, maxColums)
            } else {
                recyclerView.layoutManager = GridLayoutManager(context, colums)
            }
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, colums)
        }
    }
}