package com.example.musicx;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicx.image.ImageSong;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.musicx.MainActivity.musicFilesfavoritos;


public class AlbumDetailsAdapter extends RecyclerView.Adapter <AlbumDetailsAdapter.MyHolder>{

    private final Context mContext ;
    public static ArrayList<MusicFiles> albumFilesTarget ;
    private Menu menuu;

    public AlbumDetailsAdapter(Context mContext, ArrayList<MusicFiles> albumFiles ) {
        this.mContext = mContext;
        albumFilesTarget = albumFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int positions ) {
        try {
        holder.album_name.setText(albumFilesTarget.get(holder.getAdapterPosition()).getTitle());
        holder.album_name.setTextColor(Color.WHITE);
        holder.artist.setText(albumFilesTarget.get(holder.getAdapterPosition()).getArtist());
        String pos=String.format(Locale.US,"%d",(holder.getAdapterPosition()+1));
        holder.numSongs.setText(pos);
        holder.artist.setTextColor(Color.MAGENTA);
        holder.numSongs.setTextColor(Color.WHITE);
        holder.durationSongs.setText(   milliSecondMinutHors(  Integer.parseInt(albumFilesTarget.get(holder.getAdapterPosition()).getDuration())));
        holder.durationSongs.setTextColor(Color.WHITE);
        try {
            byte[] image = ImageSong.getBitmapPicture(albumFilesTarget.get(holder.getAdapterPosition()).getPicturePath());
            if (image != null) {
                Glide.with(mContext).asBitmap()
                        .load(image)
                        .into(holder.album_image);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.iconp)
                        .into(holder.album_image);
            }
        }catch (Exception e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mContext , PlayerActivity.class);
                intent.putExtra("position", holder.getAdapterPosition()).putExtra("elemtSelected",3);
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent( mContext , ElementSelect.class);
                intent.putExtra("position", holder.getAdapterPosition()).putExtra("elemtSelected",3);
                mContext.startActivity(intent);
                return true;
            }
        });
        holder. menuMore. setTag(  holder);
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupmenu = new PopupMenu(mContext, v ) ;
                popupmenu. getMenuInflater(). inflate(R.menu.popup, popupmenu. getMenu());
                popupmenu. setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick( MenuItem item ){
                        switch ( item. getItemId ()){
                            case R.id.item1: deleteFile(holder.getAdapterPosition() ,v);
                                break ;
                            case R.id.item2:
                                break ;
                            case R.id.item3:
                                break ;
                            case R.id.item4:
                                break ;
                            case R.id.item5:  addFavoritos(holder.getAdapterPosition() ,v);
                                break;
                            case R.id.item6:
                                break;
                            case R.id.item7:
                                AlbumDetailsAdapter.MyHolder holder =(AlbumDetailsAdapter.MyHolder) v.getTag();
                                Intent intent = new Intent(mContext , WebViewYoutube.class);
                                intent.putExtra("url",holder.album_name.getText() );
                                mContext.startActivity(intent);
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
    }

    @Override
    public int getItemCount() {
        if( albumFilesTarget == null ) return 0;
        return albumFilesTarget.size();
    }

    public static class MyHolder  extends RecyclerView.ViewHolder{
        ImageView album_image , menuMore ;
        TextView album_name, artist , numSongs , durationSongs ;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image= itemView.findViewById(R.id.music_img);
            album_name= itemView.findViewById(R.id.music_file_name);
            artist = itemView.findViewById(R.id.texartista);
            numSongs = itemView.findViewById(R.id.numcancion);
            durationSongs = itemView.findViewById(R.id.duracion);
            menuMore = itemView.findViewById(R.id.menuMore);

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





    private void deleteFile ( int position , View v ){
        String temp = albumFilesTarget.get(position).getTitle();

        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(albumFilesTarget.get(position).getId()));
        File file = new File(albumFilesTarget.get(position).getPatch());
        boolean deleted = file.delete();
        if ( deleted) {
            mContext.getContentResolver().delete(contentUri,null, null);
            albumFilesTarget.remove(position );
            notifyItemRemoved(position);
            notifyItemChanged(position, albumFilesTarget.size());
            Snackbar.make(v, "Se eliminó : " + temp, Snackbar.LENGTH_LONG).show();
        }else{
            Snackbar.make(v, "No se eliminó : " + temp, Snackbar.LENGTH_LONG).show();
        }

    }




    private void addFavoritos ( int position , View v ){
        String temp = albumFilesTarget.get(position).getTitle();
        musicFilesfavoritos.add(albumFilesTarget.get(position));
        Snackbar.make(v, "Se añadio a favoritos : "+temp, Snackbar.LENGTH_LONG).show();
    }




}

