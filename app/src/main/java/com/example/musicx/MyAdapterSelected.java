package com.example.musicx;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicx.image.ImageSong;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

class MyAdapterselected extends RecyclerView.Adapter< MyAdapterselected.MyViewHolder > {

    private final Context mContext ;
     ArrayList<Items> data;
     ArrayList < MusicFiles > listSongs;
     ArrayList <MusicFiles> listaElementosSeleccionados = new ArrayList<>();

    public MyAdapterselected  ( ArrayList < MusicFiles > listSongs , Context context ,ArrayList<Items> items){
        this. mContext = context;
        this.listSongs = listSongs;
        this.data = items;
    }




     @NonNull
     @Override
     public MyAdapterselected.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(mContext).inflate(R.layout.songselected, parent , false);
         return new MyViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull MyAdapterselected.MyViewHolder holder, int position) {
         holder.duracion.setText(milliSecondMinutHors(  Integer.parseInt(listSongs.get(position).getDuration()) ));
         String string = String.format(Locale.US,"%d",(position+1));
         holder.numCancion.setText(string);
         holder.artistas.setText(listSongs.get(position).getArtist());
         holder.canciones.setText(listSongs.get(position).getTitle());
         holder.duracion.setTextColor(Color. WHITE );
         holder.numCancion.setTextColor( Color. WHITE );
         holder.artistas.setTextColor(Color. MAGENTA );
         holder.canciones.setTextColor(Color. WHITE );
         holder.checkBox.setChecked(data.get(position).isCheckbox());
         try {
             byte[] image = ImageSong.getBitmapPicture(listSongs.get(position).getPicturePath());
             if (image != null) {
                 Glide.with(mContext).asBitmap()
                         .load(image)
                         .into(holder.portada);
             } else {
                 Glide.with(mContext)
                         .load(R.drawable.iconp)
                         .into(holder.portada);
             }
         }catch (Exception e){
         }
         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 data.get(position).setCheckbox(!data.get(position).isCheckbox());
                 holder.checkBox.setChecked( data.get(position).isCheckbox() );

                     if (data.get(position).isCheckbox()) {
                         listaElementosSeleccionados.add(listSongs.get(position));
                     } else {
                         listaElementosSeleccionados.remove(listSongs.get(position));
                     }

             }
         });

     }

     @Override
    public long getItemId(int i) {
        return i;
    }

     @Override
     public int getItemCount() {
        if(listSongs== null) return 0;
         return listSongs.size();
     }

     public ArrayList<Items> getSelected(){
        return data;
     }

    public void addToQueue(View v ){
       /* int em=0;
        int i =0;
        for (Items items : data) {
            if (items.isCheckbox()){
                if (!nowPlayerSongs.contains(listSongs.get(i))) {
                    nowPlayerSongs.add(listSongs.get(i));
                    em++;
                } else {
                    nowPlayerSongs.remove(listSongs.get(i));
                    nowPlayerSongs.add(listSongs.get(i));
                    em++;
                }
            }
            i++;
        }
        Snackbar.make(v, em+" elementos añadidos a cola", Snackbar.LENGTH_LONG).show();
        */
    }
     public void deleteMusic(){
        /* try {
             int i =0;
             for( Items items : data ){
                 if( items.isCheckbox() ){
                     Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                             Long.parseLong(listSongs.get(i).getId()));
                     File file = new File(listSongs.get(i).getPatch());
                     boolean deleted = file.delete();
                     if (deleted) {
                         mContext.getContentResolver().delete(contentUri, null, null);
                         listSongs.remove(i);
                         if(nowPlayerSongs.contains(musicFilesGeneral.get(i))){
                             nowPlayerSongs.remove(i);
                         }
                         if(musicFiles_favoritos.contains(musicFilesGeneral.get(i))){
                             musicFiles_favoritos.remove(i);
                         }
                         if(musicFiles_recientes.contains(musicFilesGeneral.get(i))){
                             musicFiles_recientes.remove(i);
                         }
                         musicFilesGeneral.remove(i);
                         notifyItemRemoved(i);
                     }
                 }else {
                     i++;
                 }
             }//for
         }catch (Exception e ){
             Toast.makeText(mContext,e+" Adapter selected ",Toast.LENGTH_SHORT).show();
         }
         */
     }

    public ArrayList<MusicFiles> get_E_seleccionados(){
        return listaElementosSeleccionados;
    }

     public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView numCancion;
        TextView duracion ;
        TextView artistas ;
        ImageView portada ;
        TextView canciones ;
        CheckBox checkBox;
        public MyViewHolder ( View itemView ){
            super(itemView);
            checkBox = itemView.findViewById(R.id. chexs );
            canciones = itemView.findViewById(R.id. namecancion );
            duracion = itemView.findViewById(R.id.duracions);
            artistas = itemView.findViewById(R.id.artist);
            numCancion = itemView.findViewById(R.id.numcancions);
            portada = itemView.findViewById(R.id.imageViewsongs);

        }
    }

    private  String milliSecondMinutHors( long millisecond){
        String timerString = "";
        String secondString ="";

        int hour = (int )(millisecond / (1000 * 60 * 60 ));
        int minutes = (int ) (millisecond % (1000 * 60 * 60 )/(1000*60));
        int second = (int ) (millisecond %(1000 * 60 * 60 )%(1000*60) /1000);
        if( hour > 0){
            timerString = hour + ":";
        }

        if (second < 10 ){
            secondString = "0"+second;
        }else{
            secondString = ""+second;
        }

        timerString = timerString + minutes +":"+secondString;
        return timerString;
    }


 }
