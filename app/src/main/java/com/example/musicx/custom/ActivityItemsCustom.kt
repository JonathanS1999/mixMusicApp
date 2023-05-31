package com.example.musicx.custom

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicx.R
import com.example.musicx.background.MusicBackground
import java.util.*

class ActivityItemsCustom : AppCompatActivity() {
private lateinit var image : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_custom)
        // my toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        image = findViewById(R.id.image)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { finish() }
        Objects.requireNonNull(supportActionBar)!!.title = "Personalizacion"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = AdapterItemsCustom(this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    override fun onResume() {
        super.onResume()
        MusicBackground.setBackground(image, this)
    }

}