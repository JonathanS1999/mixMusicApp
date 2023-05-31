package com.example.musicx;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicx.converTimes.ConvertTime;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import static com.example.musicx.MainActivity.musicFilesGeneral;
import static com.example.musicx.MainActivity.musicFilesrecientes;
import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.MyVieHolder> {


    private Menu menuu;
    private final Context mContex ;
    private ArrayList<MusicFiles> mFilesF ;
    private AlertDialog titulo;
    private ConvertTime timec ;

    public FavoritoAdapter( Context mContext , ArrayList<MusicFiles> mFilesF){
        this.mFilesF = mFilesF ;
        this.mContex= mContext;
        this.timec= new ConvertTime();
    }
    @NonNull
    @Override
    public FavoritoAdapter.MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContex).inflate(R.layout.music_items, parent , false);
        return new FavoritoAdapter.MyVieHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritoAdapter.MyVieHolder holder, int positions) {
        try{
        holder.file_name.setText(mFilesF.get(holder.getAdapterPosition()).getTitle());
        holder.artist.setText(mFilesF.get(holder.getAdapterPosition()).getArtist());
        holder.numSongs.setText(""+(holder.getAdapterPosition()+1));
        holder.artist.setTextColor(Color.MAGENTA);
        holder.file_name.setTextColor(Color.WHITE);
        holder.numSongs.setTextColor(Color.WHITE);
        holder.durationSongs.setText(  timec.getMilliSecondMinutHors( Integer.parseInt(mFilesF.get(holder.getAdapterPosition()).getDuration())));
        holder.durationSongs.setTextColor(Color.WHITE);
        try {
            byte[] image = getAlbumArt(mFilesF.get(holder.getAdapterPosition()).getPatch());
            if (image != null) {
                Glide.with(mContex).asBitmap()
                        .load(image)
                        .into(holder.album_art);
            } else {
                Glide.with(mContex)
                        .load(R.drawable.iconp)
                        .into(holder.album_art);
            }
        }catch (Exception e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mContex , PlayerActivity.class);
                intent.putExtra("position", holder.getAdapterPosition()).putExtra("elemtSelected",2);

                mContex.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent( mContex , ElementSelect.class);
                intent.putExtra("position", holder.getAdapterPosition()).putExtra("elemtSelected",2);

                mContex.startActivity(intent);
                return true;
            }
        });

        holder.menuMore.setTag(holder);
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupmenu = new PopupMenu(mContex, v ) ;
                popupmenu. getMenuInflater(). inflate(R.menu.menu_favorites, popupmenu. getMenu());
                popupmenu. setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick( MenuItem item ){
                        if ( item. getItemId () ==R.id.item1) {
                            androidx.appcompat.app.AlertDialog.Builder alert= new androidx.appcompat.app.AlertDialog.Builder(mContex);
                            alert. setMessage("¿ Eliminar "+mFilesF.get(holder.getAdapterPosition()).getTitle()+" ?")
                                    . setCancelable(false)
                                    . setPositiveButton("si", new DialogInterface. OnClickListener(){
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
                            titulo. setTitle( "Se eliminará del dispositivo");
                            titulo. show();

                        }else if ( item. getItemId () ==R.id.item2) {
                        }else if ( item. getItemId () ==R.id.item3) {
                        }else if ( item. getItemId () ==R.id.item4) {
                            addToQueue(holder.getAdapterPosition(),v);
                        }else if ( item. getItemId () ==R.id.item5) {
                        }else if ( item. getItemId () ==R.id.item6) {
                            MusicAdapter.MyVieHolder holder =(MusicAdapter.MyVieHolder) v.getTag();
                            Intent intent = new Intent(mContex , WebViewYoutube.class);
                            intent.putExtra("url",holder.file_name.getText() );
                            mContex.startActivity(intent);
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
            Toast.makeText(mContex, e +" 194 album adapter ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return mFilesF.size();
    }


    public static class MyVieHolder extends RecyclerView.ViewHolder{

        TextView file_name , artist , numSongs , durationSongs ;
        ImageView album_art, menuMore  ;
        public MyVieHolder ( View itemView ){
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            artist = itemView.findViewById(R.id.texartista);
            numSongs = itemView.findViewById(R.id.numcancion);
            durationSongs = itemView.findViewById(R.id.duracion);
            album_art = itemView.findViewById(R.id.music_img);
            menuMore = itemView.findViewById(R.id.menuMore);

        }
    }


    private byte [] getAlbumArt(String uri){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }


    private void deleteFile ( int position , View v ) {
        try{
            String temp = mFilesF.get(position).getTitle();
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Long.parseLong(mFilesF.get(position).getId()));
            MusicFiles musicDeleted = mFilesF.get(position);
            File file = new File(mFilesF.get(position).getPatch());
            boolean deleted = file.delete();
            if (deleted) {
                mContex.getContentResolver().delete(contentUri, null, null);
                mFilesF.remove(musicDeleted);
                nowPlayerSongs.remove(musicDeleted);
                musicFilesrecientes.remove(musicDeleted);
                musicFilesGeneral.remove(musicDeleted);
                notifyItemRemoved(position);
                notifyItemChanged(position, mFilesF.size());
                Snackbar.make(v, "Se eliminó : " + temp, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(v, "No se eliminó : " + temp, Snackbar.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(mContex,e+" MA 215 "+mFilesF.get(position).getId(),Toast.LENGTH_SHORT).show();
        }
    }


    private void addToQueue(int position , View v ){
        if ( nowPlayerSongs != null){
            if(!nowPlayerSongs.contains(mFilesF.get(position))){
                nowPlayerSongs.add(mFilesF.get(position));
                Snackbar.make(v, "se añadió " + mFilesF.get(position).getTitle()+" a la cola", Snackbar.LENGTH_LONG).show();
            }else{
                nowPlayerSongs.remove(mFilesF.get(position));
                nowPlayerSongs.add(mFilesF.get(position));
                Snackbar.make(v, "se añadió " + mFilesF.get(position).getTitle()+" a la cola", Snackbar.LENGTH_LONG).show();
            }
        }else{
            nowPlayerSongs= new ArrayList<>();
            nowPlayerSongs.add(mFilesF.get(position));
            Snackbar.make(v, "se añadió " + mFilesF.get(position).getTitle()+" a la cola", Snackbar.LENGTH_LONG).show();
        }
    }



    void updateList ( ArrayList<MusicFiles> musicFilesArrayList){
        try {
            mFilesF = new ArrayList<MusicFiles>();
            mFilesF.addAll(musicFilesArrayList);
            notifyDataSetChanged();
        }catch ( Exception e){
            Toast.makeText(mContex , e+" 227",Toast.LENGTH_LONG).show();
        }
    }




}
