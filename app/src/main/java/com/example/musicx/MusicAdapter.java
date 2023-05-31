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

import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.converTimes.ConvertTime;
import com.example.musicx.image.ImageSong;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyVieHolder> {

    private final Context mContex ;
    private ArrayList<MusicFiles> mFiles ;
    private ArrayList<MusicFiles> mFilesFavoritos;
    private Menu menuu;
    private AlertDialog titulo;
    private  OnItemRemove itemRemove;
    private final ConvertTime timec;
    private SettingsPlayingPreferences s;

    public MusicAdapter ( Context mContext , ArrayList<MusicFiles> files , ArrayList<MusicFiles> mFilesFavoritos){
        s = new SettingsPlayingPreferences(mContext);
        mFiles = files ;
        this.mContex= mContext;
        this.mFilesFavoritos = mFilesFavoritos;
        if(mContext instanceof  OnItemRemove) {
            itemRemove = (OnItemRemove) mContext;
        }
        timec = new ConvertTime();
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContex).inflate(R.layout.music_items, parent , false);
        return new MyVieHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, int positions) {
        try {
            holder.file_name.setText(mFiles.get(holder.getAdapterPosition()).getTitle());
            holder.artist.setText(mFiles.get(holder.getAdapterPosition()).getArtist());
            String string = String.format(Locale.US, "%d", (holder.getAdapterPosition() + 1));
            holder.numSongs.setText(string);
            holder.artist.setTextColor(Color.MAGENTA);
            holder.file_name.setTextColor(Color.WHITE);
            holder.numSongs.setTextColor(Color.WHITE);
            holder.durationSongs.setText(timec.getMilliSecondMinutHors(Integer.parseInt(mFiles.get(holder.getAdapterPosition()).getDuration())));
            holder.durationSongs.setTextColor(Color.WHITE);


            Theme_Font.setFont(holder.numSongs, mContex,s);
            Theme_Font.setFont(holder.file_name,mContex,s);
            Theme_Font.setFont(holder.artist,mContex,s);
            Theme_Font.setFont(holder.durationSongs,mContex,s);

            ImageSong.setImage(holder.album_art, mContex,mFiles.get(holder.getAdapterPosition()).getPicturePath(),2,12,false);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContex, PlayerActivity.class);
                    intent.putExtra("position", holder.getAdapterPosition()).putExtra("elemtSelected", 0);
                    mContex.startActivity(intent);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(mContex, ElementSelect.class);
                    intent.putExtra("position", holder.getAdapterPosition()).putExtra("elemtSelected", 0);
                    mContex.startActivity(intent);
                    return true;
                }
            });

            holder.menuMore.setTag(holder);
            holder.menuMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupmenu = new PopupMenu(mContex, v);
                    popupmenu.getMenuInflater().inflate(R.menu.popup, popupmenu.getMenu());
                    popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.item1) {

                                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(mContex);
                                alert.setMessage("¿ Eliminar " + mFiles.get(holder.getAdapterPosition()).getTitle() + " ?")
                                        .setCancelable(false)
                                        .setPositiveButton("si", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                deleteFile(holder.getAdapterPosition(), v);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                titulo = alert.create();
                                titulo.setTitle(" Se eliminará del dispositivo");
                                titulo.show();

                            } else if (item.getItemId() == R.id.item2) {
                            } else if (item.getItemId() == R.id.item3) {
                            } else if (item.getItemId() == R.id.item4) {
                                addToQueue(holder.getAdapterPosition(), v);
                            } else if (item.getItemId() == R.id.item5) {
                                addFavoritos(holder.getAdapterPosition(), v);
                            } else if (item.getItemId() == R.id.item6) {
                            } else if (item.getItemId() == R.id.item7) {
                                MyVieHolder holder = (MyVieHolder) v.getTag();
                                Intent intent = new Intent(mContex, WebViewYoutube.class);
                                intent.putExtra("url", holder.file_name.getText());
                                mContex.startActivity(intent);
                            }
                            return true;
                        }
                    });

                    popupmenu.show();
                    menuu = popupmenu.getMenu();
                    for (int i = 0; i < menuu.size(); i++) {
                        MenuItem itemMenu = menuu.getItem(i);
                        SpannableString spanString = new SpannableString(menuu.getItem(i).getTitle().toString());
                        spanString.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, spanString.length(), 0); //fix the color to white
                        itemMenu.setTitle(spanString);
                    }
                }
            });

        }catch (Exception e){
            Toast.makeText(mContex,e+" 168 ma", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        if(mFiles == null)return 0;
        return mFiles.size();
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



    private void deleteFile ( int position , View v ) {
        try{
        String temp = mFiles.get(position).getTitle();
        MusicFiles musicDeleted = mFiles.get(position);
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mFiles.get(position).getId()));
        File file = new File(mFiles.get(position).getPatch());
        boolean deleted = file.delete();
        if (deleted) {
            mContex.getContentResolver().delete(contentUri, null, null);
            if(itemRemove != null){
                itemRemove.itemRemove(musicDeleted);
            }
            Snackbar.make(v, "Se eliminó : " + temp, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(v, "No se eliminó : " + temp, Snackbar.LENGTH_LONG).show();
        }
    }catch (Exception e){
            Toast.makeText(mContex,e+" MA 215 "+mFiles.get(position).getId(),Toast.LENGTH_SHORT).show();
        }
    }

    public void updateItemRemoved(MusicFiles fil){
        int i=0;
        for(MusicFiles  musicFiles : mFiles) {
            if (musicFiles == fil) {
                mFiles.remove(fil);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i,mFiles.size());
                break;
            }
            i++;
        }
    }


private void addToQueue(int position , View v ){

        if ( nowPlayerSongs != null){
            if(!nowPlayerSongs.contains(mFiles.get(position))){
                nowPlayerSongs.add(mFiles.get(position));
                Snackbar.make(v, "se añadió " + mFiles.get(position).getTitle()+" a la cola", Snackbar.LENGTH_LONG).show();
            }else{
                nowPlayerSongs.remove(mFiles.get(position));
                nowPlayerSongs.add(mFiles.get(position));
                Snackbar.make(v, "se añadió " + mFiles.get(position).getTitle()+" a la cola", Snackbar.LENGTH_LONG).show();
            }
        }else{
            nowPlayerSongs= new ArrayList<>();
            nowPlayerSongs.add(mFiles.get(position));
            Snackbar.make(v, "se añadió " + mFiles.get(position).getTitle()+" a la cola", Snackbar.LENGTH_LONG).show();
        }
}

    private void addFavoritos ( int position , View v ){
        String temp = mFiles.get(position).getTitle();
        if(mFilesFavoritos != null) {
            if(!mFilesFavoritos.contains(mFiles.get(position))) {
                mFilesFavoritos.add(mFiles.get(position));
                Snackbar.make(v, "Se añadio a favoritos : "+temp, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(v, "Ya está favoritos : "+temp, Snackbar.LENGTH_SHORT).show();
            }
        }else{
            mFilesFavoritos= new ArrayList<>();
            mFilesFavoritos.add(mFiles.get(position));
            Snackbar.make(v, "Se añadio a favoritos : "+temp, Snackbar.LENGTH_SHORT).show();
        }

    }


    void updateList ( ArrayList<MusicFiles> musicFilesArray){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    if( musicFilesArray != null) {
                        if (mFiles != null) {
                            mFiles.clear();
                        }
                        mFiles = new ArrayList<>();
                        mFiles.addAll(musicFilesArray);

                        ((MainActivity) mContex).runOnUiThread(new Runnable() {
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

