package com.example.musicx;

import android.content.ContentUris;
import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicx.converTimes.ConvertTime;
import com.example.musicx.image.ImageSong;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import static com.example.musicx.MainActivity.musicFilesfavoritos;

public class RecienteAdapter  extends RecyclerView.Adapter<RecienteAdapter.MyVieHolder>  {


    private Menu menuu;
    private final Context mContex ;
    private ArrayList<MusicFiles> mFilesR ;
    private final ConvertTime timec;

   public RecienteAdapter ( Context mContext , ArrayList<MusicFiles> mFiles ,ArrayList<MusicFiles> mFilesFavoritos ){
        mFilesR = mFiles ;
        this.mContex= mContext;
       timec = new ConvertTime();
    }
    @NonNull
    @Override
    public RecienteAdapter.MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContex).inflate(R.layout.music_items, parent , false);
        return new MyVieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecienteAdapter.MyVieHolder holder, int positions) {
        try {
        holder.file_name.setText(mFilesR.get(holder.getAdapterPosition()).getTitle());
        holder.artist.setText(mFilesR.get(holder.getAdapterPosition()).getArtist());
        holder.numSongs.setText(""+(holder.getAdapterPosition()+1));
        holder.artist.setTextColor(Color.MAGENTA);
        holder.file_name.setTextColor(Color.WHITE);
        holder.numSongs.setTextColor(Color.WHITE);
        holder.durationSongs.setText(timec.getMilliSecondMinutHors(  Integer.parseInt(mFilesR.get(holder.getAdapterPosition()).getDuration())));
        holder.durationSongs.setTextColor(Color.WHITE);

            byte[] image = ImageSong.getBitmapPicture(mFilesR.get(holder.getAdapterPosition()).getPicturePath());
            if (image != null) {
                Glide.with(mContex).asBitmap()
                        .load(image)
                        .into(holder.album_art);
            } else {
                Glide.with(mContex)
                        .load(R.drawable.iconp)
                        .into(holder.album_art);
            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mContex , PlayerActivity.class);
                intent.putExtra("position", holder.getAdapterPosition()).putExtra("elemtSelected",1 );
                mContex.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent( mContex , ElementSelect.class);
                intent.putExtra("position", holder.getAdapterPosition()).putExtra("elemtSelected",1);

                mContex.startActivity(intent);
                return true;
            }
        });


        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupmenu = new PopupMenu(mContex, v ) ;
                popupmenu. getMenuInflater(). inflate(R.menu.popup, popupmenu. getMenu());
                popupmenu. setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick( MenuItem item ){
                        if ( item. getItemId () == R.id.item1 ) {
                            deleteFile(holder.getAdapterPosition(), v);
                        }else if( item.getItemId() == R.id.item2 ) {
                            Toast.makeText(mContex, "  item2 ", Toast.LENGTH_LONG).show();
                        }else if( item.getItemId()==R.id.item3) {
                            Toast.makeText(mContex, "  item3 ", Toast.LENGTH_LONG).show();
                        }else if(item.getItemId()==R.id.item4) {
                            Toast.makeText(mContex, "  item4 ", Toast.LENGTH_LONG).show();
                        }else if(item.getItemId()== R.id.item5) {
                            Toast.makeText(mContex, "  item5 ", Toast.LENGTH_LONG).show();
                        }else if(item.getItemId()==R.id.item6){
                            addFavoritos(holder.getAdapterPosition() ,v);
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
        if (mFilesR == null)return 0;
        return mFilesR.size();
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



    public ArrayList<MusicFiles> getmusicFilesRecientes(){
        return mFilesR;
    }


    private void deleteFile ( int position , View v ){
        String temp = mFilesR.get(position).getTitle();

        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mFilesR.get(position).getId()));
        File file = new File(mFilesR.get(position).getPatch());
        boolean deleted = file.delete();
        if ( deleted) {
            mContex.getContentResolver().delete(contentUri,null, null);
            mFilesR.remove(position );
            notifyItemRemoved(position);
            notifyItemChanged(position, mFilesR.size());
            Snackbar.make(v, "Se eliminó : " + temp, Snackbar.LENGTH_LONG).show();
        }else{
            Snackbar.make(v, "No se eliminó : " + temp, Snackbar.LENGTH_LONG).show();
        }

    }




    private void addFavoritos ( int position , View v ){
        String temp = mFilesR.get(position).getTitle();
        musicFilesfavoritos.add(mFilesR.get(position));
        Snackbar.make(v, "Se añadio a favoritos : "+temp, Snackbar.LENGTH_LONG).show();
    }






    void updateList ( ArrayList<MusicFiles> musicFilesArrayList){
        try {
            mFilesR = new ArrayList<MusicFiles>();
            mFilesR.addAll(musicFilesArrayList);
            notifyDataSetChanged();
        }catch ( Exception e){
            Toast.makeText(mContex , e+" 227",Toast.LENGTH_LONG).show();
        }
    }





}
