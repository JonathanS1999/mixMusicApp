package com.example.musicx.custom

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicx.R
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences
import com.example.musicx.background.MusicBackground
import java.util.*

class ActivityFonts: AppCompatActivity() {

    private lateinit var background: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fonts)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        background = findViewById(R.id.background)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { finish() }
        Objects.requireNonNull(supportActionBar)?.title = "Fuentes"
        MusicBackground.setBackground(background, this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = AdapterFonts(this)
        recyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL,
            false
        )
    }






    private class AdapterFonts(val context:Context):RecyclerView.Adapter<AdapterFonts.MyVieHolder>(),OnFontSelected{
        private val array = arrayListOf("fonts/Digital7-1e1Z.ttf")

         class MyVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var textFont:TextView
         var check : CheckBox
         init {
             textFont = itemView.findViewById(R.id.text)
             check = itemView.findViewById(R.id.checkbox)
         }
         }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVieHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_fonts,parent,false)
            return MyVieHolder(view)
        }

        override fun onBindViewHolder(holder: MyVieHolder, position: Int) {
            if (position > 0){
            val typeface = Typeface.createFromAsset(context.assets, array[0])
            holder.textFont.setTypeface(typeface)
             }
            holder.textFont.text="Music fonts "

            holder.itemView.setOnClickListener(View.OnClickListener {
                selectedFont(position+1)
            })
        }

        override fun getItemCount(): Int {
            return 2
        }
        override fun selectedFont(s: Int) {
            val setting= SettingsPlayingPreferences(context)
            setting.setSavedThemeFont(s)
            Toast.makeText(context," fuente $s aplicada",Toast.LENGTH_SHORT).show()
        }
    }

    interface OnFontSelected{
        fun selectedFont(s: Int)
    }

}