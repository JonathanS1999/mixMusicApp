package com.example.musicx.custom

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.musicx.R
import com.example.musicx.ThemesActivity

class AdapterItemsCustom(private val context: Context) : RecyclerView.Adapter<AdapterItemsCustom.CustomAdapter>() {

    private val ids = arrayOf(
            R.drawable.ic__image_theme,
            R.drawable.ic_reproductor,
            R.drawable.ic_view_mosaico,
            R.drawable.ic_fonts,
            R.drawable.control_settings)
    private val titles = arrayOf("fondo","portadas","listas","fuentes","controles")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter {
         val view = LayoutInflater.from(context).inflate(R.layout.item_custom,parent,false)
         return CustomAdapter(view)
     }

     override fun onBindViewHolder(holder: CustomAdapter, position: Int) {
         holder.iconImage.setImageDrawable(ContextCompat.getDrawable(context,ids[position]))
         holder.title.setText(titles[position])
         settingItem(position,holder)
     }

     override fun getItemCount(): Int {
         return ids.size
     }

    private fun settingItem(position : Int , holder : CustomAdapter){
        if(position == 0){
            holder.itemView.setOnClickListener( View.OnClickListener {
                val intent = Intent(context,ThemesActivity::class.java)
                context.startActivity(intent)
            })
        }else if (position == 1){
            holder.itemView.setOnClickListener( View.OnClickListener {
                val intent = Intent(context,Activity_Custom_Play::class.java)
                intent.putExtra("config",1)
                context.startActivity(intent)
            })
        }else if(position == 2){
            holder.itemView.setOnClickListener( View.OnClickListener {
                val intent = Intent(context,ActivityCustom::class.java)
                intent.putExtra("config",1)
                context.startActivity(intent)
            })
        }else if(position == 3){
            holder.itemView.setOnClickListener( View.OnClickListener {
                val intent = Intent(context,ActivityFonts::class.java)
                context.startActivity(intent)
            })
        }else if(position == 4){
            holder.itemView.setOnClickListener( View.OnClickListener {
                val intent = Intent(context,Activity_Custom_Play::class.java)
                intent.putExtra("config",2)
                context.startActivity(intent)
            })
        }
    }

     class CustomAdapter( itemView: View) : RecyclerView.ViewHolder(itemView) {

         var title:TextView
         var iconImage : ImageView

         init {
             title= itemView.findViewById(R.id.textView)
             iconImage = itemView.findViewById(R.id.image)
         }

     }
 }