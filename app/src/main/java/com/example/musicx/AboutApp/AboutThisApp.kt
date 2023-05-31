package com.example.musicx.AboutApp

import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.musicx.R
import com.example.musicx.background.MusicBackground

class AboutThisApp :  AppCompatActivity (){
    private lateinit var toolBar :Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_about_app)
        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolBar.setNavigationOnClickListener {
            finish()
         }
        val imageView = findViewById<ImageView>(R.id.background)
        MusicBackground.setBackground(imageView,this)

    }

}