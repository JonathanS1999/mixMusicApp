package com.example.musicx;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicx.image.ImageSong;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import static com.example.musicx.MainActivity.musicFilesfavoritos;
import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;
import static com.example.musicx.MainActivity.musicFilesGeneral;

public class ArtistAdapter  extends RecyclerView.Adapter<ArtistAdapter.MyArtistAdapter>{

    private final Context mContext ;
    private ArrayList<MusicFiles> musicFilesArrayList ;
    private Menu menuu;
    private AlertDialog titulo;
    private final OnItemRemove itemRemove;

    public ArtistAdapter (Context mContext, ArrayList<MusicFiles> artistFiles ) {
        this.mContext = mContext;
        itemRemove = (OnItemRemove) mContext;
      getArtist(artistFiles);
    }

    private void getArtist(ArrayList<MusicFiles> artistFiles ){
        try{
        if(artistFiles != null) {
            musicFilesArrayList = new ArrayList<>();
            ArrayList<String> b = new ArrayList<>();
            for (int i = 0; i < artistFiles.size(); i++) {
                if (!b.contains(artistFiles.get(i).getArtist())) {
                    artistFiles.get(i).setCountArtist(0);
                    musicFilesArrayList.add(artistFiles.get(i));
                    b.add(artistFiles.get(i).getArtist());
                } else {
                    for (int j = 0; j < musicFilesArrayList.size(); j++) {
                        if (musicFilesArrayList.get(j).getArtist().equals(artistFiles.get(i).getArtist())) {
                            musicFilesArrayList.get(j).setCountArtist((musicFilesArrayList.get(j).getCountArtist() + 1));
                            break;
                        }
                    }
                }

            }
            b.clear();
        }
        }catch (Exception e){
            Toast.makeText(mContext, e +" 194 album adapter ",Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public MyArtistAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.artist_item , parent ,false);
        return new MyArtistAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.MyArtistAdapter holder, int positions) {
        try{
        holder.album_name.setText(musicFilesArrayList.get(holder.getAdapterPosition()).getArtist());
        holder.countSongnsAlbum.setText(String.valueOf((musicFilesArrayList.get(holder.getAdapterPosition()).getCountArtist()+1)) );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumDetails.class);
                intent.putExtra("albumName", musicFilesArrayList.get(holder.getAdapterPosition()).getArtist());
                intent.putExtra("type",1);
                mContext.startActivity(intent);
            }
        });
        try {
            byte[] image = ImageSong.getBitmapPicture(musicFilesArrayList.get(holder.getAdapterPosition()).getPicturePath());
            if (image == null) {
                holder.album_image.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.play2));
            } else {
                Bitmap port = BitmapFactory.decodeByteArray(
                      image, 0, image.length);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), port);
                roundedBitmapDrawable.setCircular(true);
                holder.album_image.setImageDrawable(roundedBitmapDrawable);
            }
            holder.menuMore.setTag(holder);
            holder.menuMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupmenu = new PopupMenu(mContext, v ) ;
                    popupmenu. getMenuInflater(). inflate(R.menu.menu_artist_albums, popupmenu. getMenu());
                    popupmenu. setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener(){
                        @Override
                        public boolean onMenuItemClick( MenuItem item ){
                            if ( item. getItemId () ==R.id.item1) {
                                androidx.appcompat.app.AlertDialog.Builder alert= new androidx.appcompat.app.AlertDialog.Builder(mContext);
                                alert. setMessage("¿ Eliminar "+musicFilesArrayList.get(holder.getAdapterPosition()).getArtist()+" ?")
                                        . setCancelable(false)
                                        . setPositiveButton(" si ", new DialogInterface. OnClickListener(){
                                            @Override
                                            public void onClick (DialogInterface dialog, int which ){
                                                deleteFile(holder.getAdapterPosition(), v);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface. OnClickListener(){
                                            @Override
                                            public void onClick (DialogInterface dialog, int which ){
                                                dialog. cancel();
                                            }
                                        });
                                titulo = alert. create();
                                titulo. setTitle("Se eliminará del dispositivo");
                                titulo. show();

                            }else if ( item. getItemId () ==R.id.item2) {
                            }else if ( item. getItemId () ==R.id.item3) {
                            }else if ( item. getItemId () ==R.id.item4) {
                                addToQueue(holder.getAdapterPosition(), v);
                            }else if ( item. getItemId () ==R.id.item5) {
                                addFavoritos(holder.getAdapterPosition() ,v);
                            }else if ( item. getItemId () ==R.id.item6) {
                                ArtistAdapter.MyArtistAdapter holder =(ArtistAdapter.MyArtistAdapter) v.getTag();
                                Intent intent = new Intent(mContext , WebViewYoutube.class);
                                intent.putExtra("url",holder.album_name.getText() );
                                mContext.startActivity(intent);
                            }else if ( item. getItemId () ==R.id.item7) {
                            }
                            return true ;
                        }
                    });


                    popupmenu.show();
                    menuu = popupmenu.getMenu();
                    for(int i = 0; i < menuu.size(); i++) {
                        MenuItem itemMenu = menuu.getItem(i);
                        SpannableString spanString = new SpannableString(menuu.getItem(i).getTitle().toString());
                        spanString.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0  ,   spanString.length(), 0); //fix the color to white
                        itemMenu.setTitle(spanString);
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(mContext, e +" 194 album adapter ",Toast.LENGTH_LONG).show();
        }
        }catch (Exception e){
            Toast.makeText(mContext, e +" 194 album adapter ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        if ( musicFilesArrayList == null) return 0;
        return musicFilesArrayList.size();
    }

    private void addToQueue(int position ,View v ){
        String temp = musicFilesArrayList.get(position).getArtist();
        int em=0;
        for (MusicFiles musicFiles: musicFilesGeneral) {
            if(temp.equals(musicFiles.getArtist())){
                if(!nowPlayerSongs.contains(musicFiles)){
                    nowPlayerSongs.add(musicFiles);
                    em++;
                }else{
                    nowPlayerSongs.remove(musicFiles);
                    nowPlayerSongs.add(musicFiles);
                    em++;
                }
            }
        }
        Snackbar.make(v, em+" elementos añadidos a cola", Snackbar.LENGTH_LONG).show();
    }

    private void deleteFile ( int position , View v ) {
        try{
            if( musicFilesArrayList != null) {
                String temp = musicFilesArrayList.get(position).getArtist();
                int em=0;
                int i=0;
                ArrayList<Integer> delet= new ArrayList<>();
                for ( MusicFiles musicFiles :musicFilesGeneral) {
                    if(temp.equals(musicFiles.getArtist())){
                        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                Long.parseLong(musicFiles.getId()));
                        File file = new File(musicFiles.getPatch());
                        boolean deleted = file.delete();
                        if (deleted) {
                            mContext.getContentResolver().delete(contentUri, null, null);
                            delet.add(i);
                            em++;
                        } else {
                            Snackbar.make(v, "No se eliminó : " + musicFilesGeneral.get(i).getTitle(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                    i++;
                }
                for(int del : delet){
                    itemRemove.itemRemove(musicFilesGeneral.get(del));
                }
                delet.clear();
                if(em>0){
                    Snackbar.make(v, "Se eliminó " + em+" archivos ", Snackbar.LENGTH_LONG).show();
                }


            }
        }catch (Exception e){
            Toast.makeText(mContext,e+" Artist Adapter ",Toast.LENGTH_SHORT).show();
        }
    }




    private void addFavoritos ( int position , View v ){
        if(musicFilesArrayList != null) {
            String temp = musicFilesArrayList.get(position).getArtist();
            int em=0 ,iContents=0;
            for (MusicFiles musicFiles: musicFilesGeneral) {
                if(temp.equals(musicFiles.getArtist())){
                    if(!musicFilesfavoritos.contains(musicFiles)){
                        musicFilesfavoritos.add(musicFiles);
                        em++;
                    }else{
                        iContents++;
                    }
                }
            }
            if( iContents >0){
                Snackbar.make(v, iContents+" elementos ya estaban entre tus favoritos", Snackbar.LENGTH_LONG).show();
            }
            Snackbar.make(v, em+" elementos añadidos a favoritos", Snackbar.LENGTH_LONG).show();
        }
    }


    public void updateItemRemove(MusicFiles fil){
        int i=0;
        for(MusicFiles  musicFiles : musicFilesArrayList) {
            if (musicFiles == fil) {
                musicFilesArrayList.remove(fil);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i,musicFilesArrayList.size());
                break;
            }
            i++;
        }
    }

    public static class MyArtistAdapter extends RecyclerView.ViewHolder {
        ImageView album_image ;
        TextView album_name;
        TextView countSongnsAlbum ;
        ImageView menuMore;
        public MyArtistAdapter(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.album_image);
            album_name = itemView.findViewById(R.id.album_name);
            countSongnsAlbum = itemView.findViewById(R.id.numAlbumCard);
            menuMore = itemView.findViewById(R.id.menu_options_albums);
        }
    }

    void updateList ( ArrayList<MusicFiles> musicFilesArray){

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(musicFilesArrayList != null){
                    musicFilesArrayList.clear();
                    getArtist(musicFilesArray);
                    ((MainActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }


}
