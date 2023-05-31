package com.example.musicx;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicx.converTimes.ConvertTime;
import com.example.musicx.custom.CustomView;
import com.example.musicx.custom.InicializerColors;
import com.example.musicx.custom.ThemeJS;
import com.example.musicx.image.ImageSong;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static com.example.musicx.MainActivity.musicFilesGeneral;
import static com.example.musicx.MainActivity.musicFilesfavoritos;
import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyAlbumsAdapter>{

    private final Context mContext ;
    private ArrayList<MusicFiles> musicFilesArrayList ;
    private Menu menuu;
    private AlertDialog titulo;
    private  OnItemRemove itemRemove;
    private final ArrayList<String> colorsA= new ArrayList<>();
    private final ArrayList<String> colorsB= new ArrayList<>();
    private int selectorGradient=0;
    private int idStyle;
    private  ConvertTime timec;

    public AlbumsAdapter (Context mContext, ArrayList<MusicFiles> albumFiles , int idStyle) {
        this.mContext = mContext;
        this.idStyle = idStyle;
        if(idStyle == 4 ) timec = new ConvertTime();
        if(mContext instanceof OnItemRemove) itemRemove= (OnItemRemove) mContext;
      getAlbums(albumFiles);
      new InicializerColors( colorsA, colorsB);
    }

    public void getAlbums(ArrayList<MusicFiles> albumFiles) {
        if(albumFiles != null) {
            musicFilesArrayList = new ArrayList<>();
            ArrayList<String> b = new ArrayList<>();
            for (int i = 0; i < albumFiles.size(); i++) {
                if (!b.contains(albumFiles.get(i).getAlbum())) {
                    albumFiles.get(i).setCountAlbums(0);
                    musicFilesArrayList.add(albumFiles.get(i));
                    b.add(albumFiles.get(i).getAlbum());
                } else {
                    for (int j = 0; j < musicFilesArrayList.size(); j++) {
                        if (musicFilesArrayList.get(j).getAlbum().equals(albumFiles.get(i).getAlbum())) {
                            musicFilesArrayList.get(j).setCountAlbums((musicFilesArrayList.get(j).getCountAlbums() + 1));
                            break;
                        }
                    }
                }

            }
            b.clear();
        }
    }

    @NonNull
    @Override
    public AlbumsAdapter.MyAlbumsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(idStyle ==3) {
             view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        }else if(idStyle ==4) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_theme_js, parent, false);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.album_item2, parent, false);
        }
        return new MyAlbumsAdapter(view,idStyle);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsAdapter.MyAlbumsAdapter holder, int positions) {
        try{
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumDetails.class);
                intent.putExtra("albumName", musicFilesArrayList.get(holder.getAdapterPosition()).getAlbum());
                mContext.startActivity(intent);
            }
        });


            byte[] image = ImageSong.getBitmapPicture(musicFilesArrayList.get(holder.getAdapterPosition()).getPicturePath());

            if(idStyle ==3) {
                ImageSong.setImage(holder.album_image,mContext,musicFilesArrayList.get(holder.getAdapterPosition()).getPicturePath());
                //if (image != null) {

                    if (selectorGradient > 6) selectorGradient = 0;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int[] color1 = {Color.parseColor(colorsA.get(selectorGradient)), Color.parseColor(colorsB.get(selectorGradient))};
                            Drawable gd = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color1);
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.containerTitle.setBackground(gd);
                                }
                            });
                            selectorGradient++;
                        }
                    }).start();

                /*} else {
                    Glide.with(mContext)
                            .load(R.drawable.iconp)
                            .into(holder.album_image);
                }
                */
        holder.album_name.setText(musicFilesArrayList.get(holder.getAdapterPosition()).getAlbum());
        holder.countSongnsAlbum.setText(String.valueOf((musicFilesArrayList.get(holder.getAdapterPosition()).getCountAlbums()+1)) );
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
                            alert. setMessage("¿ Eliminar "+musicFilesArrayList.get(holder.getAdapterPosition()).getAlbum()+" ?")
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
                            titulo. setTitle("Se eliminará del dispositivo");
                            titulo. show();
                        }else if ( item. getItemId () ==R.id.item2) {
                        }else if ( item. getItemId () ==R.id.item3) {
                        }else if ( item. getItemId () ==R.id.item4) {
                            addToQueue(holder.getAdapterPosition(),v);
                        }else if ( item. getItemId () ==R.id.item5) {
                            addFavoritos(holder.getAdapterPosition() ,v);
                        }else if ( item. getItemId () ==R.id.item6) {
                            AlbumsAdapter.MyAlbumsAdapter holder =(AlbumsAdapter.MyAlbumsAdapter) v.getTag();
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

            }else if (idStyle == 4){
                try {
                    if (holder.container.getChildCount() > 0) holder.container.removeAllViews();
                    String string = String.format(Locale.US, "%d", (holder.getAdapterPosition() + 1));
                    if (image != null){
                        holder.container.addView(
                                new ThemeJS(mContext,
                                        BitmapFactory.decodeByteArray(image, 0, image.length),
                                        "000000","000000",
                                        musicFilesArrayList.get(holder.getAdapterPosition()).getAlbum(),
                                        timec.getMilliSecondMinutHors(Integer.parseInt(musicFilesArrayList.get(holder.getAdapterPosition()).getDuration())),
                                                string,
                                        musicFilesArrayList.get(holder.getAdapterPosition()).getArtist()
                                        ));
                    }else{
                        holder.container.addView(
                                new ThemeJS(mContext,BitmapFactory.decodeResource(mContext.getResources(), R.drawable.iconp),
                                        "000000","000000",
                                        musicFilesArrayList.get(holder.getAdapterPosition()).getAlbum(),
                                        timec.getMilliSecondMinutHors(Integer.parseInt(musicFilesArrayList.get(holder.getAdapterPosition()).getDuration())),
                                        string,
                                        musicFilesArrayList.get(holder.getAdapterPosition()).getArtist()
                                ));
                    }
                }catch (Exception e){
                    Toast.makeText(mContext, e +" 194 album adapter ",Toast.LENGTH_LONG).show();
                }
            }else {
                try {
                    if (holder.container.getChildCount() > 0) holder.container.removeAllViews();
                    if (image != null)
                        holder.container.addView(new CustomView(mContext, BitmapFactory.decodeByteArray(image, 0, image.length), "", holder.container));
                    else
                        holder.container.addView(new CustomView(mContext, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.iconp), "", holder.container));
                }catch (Exception e){
                    Toast.makeText(mContext, e +" 194 album adapter ",Toast.LENGTH_LONG).show();
                }
        }
        }catch (Exception e){
            Toast.makeText(mContext, e +" 199 album adapter  theme "+idStyle,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        if ( musicFilesArrayList == null) return 0;
        return musicFilesArrayList.size();
    }


    private void addToQueue(int position ,View v ){
        String temp = musicFilesArrayList.get(position).getAlbum();
        int em=0;
        for (MusicFiles musicFiles: musicFilesGeneral) {
            if(temp.equals(musicFiles.getAlbum())){
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
                String temp = musicFilesArrayList.get(position).getAlbum();
                int em=0;
                int i=0;
                ArrayList<Integer> delet = new ArrayList<>();
                for ( MusicFiles musicFiles: musicFilesGeneral) {
                    if(temp.equals(musicFiles.getAlbum())){
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
            Toast.makeText(mContext,e+" Albums Adapter ",Toast.LENGTH_SHORT).show();
        }
    }

    public void updateItemRemove( MusicFiles fil){
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


    private void addFavoritos ( int position , View v ){
        if(musicFilesArrayList != null) {
            String temp = musicFilesArrayList.get(position).getArtist();
            int em=0 , iContents=0;


            for ( int i=0; i < musicFilesGeneral.size(); i++) {
                if(temp.equals(musicFilesGeneral.get(i).getArtist())){
                    if(!musicFilesfavoritos.contains(musicFilesGeneral.get(i))){
                        musicFilesfavoritos.add(musicFilesGeneral.get(i));
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

    public static class MyAlbumsAdapter extends RecyclerView.ViewHolder {
        ImageView album_image ;
        TextView album_name;
        TextView countSongnsAlbum ;
        ImageView menuMore;
        RelativeLayout containerTitle;
        RelativeLayout containerView1;
        LinearLayout container;

        public MyAlbumsAdapter(@NonNull View itemView, int style ) {
            super(itemView);
            if(style ==3 ) {
                album_image = itemView.findViewById(R.id.album_image);
                album_name = itemView.findViewById(R.id.album_name);
                countSongnsAlbum = itemView.findViewById(R.id.numAlbumCard);
                menuMore = itemView.findViewById(R.id.menu_options_albums);
                containerTitle = itemView.findViewById(R.id.containerTitle);
                containerView1 = itemView.findViewById(R.id.container1);
            }else{
                container = itemView.findViewById(R.id.container);
            }
        }
    }

    void updateList ( ArrayList<MusicFiles> musicFilesArray){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (musicFilesArrayList != null) {
                     musicFilesArrayList.clear();
                    getAlbums(musicFilesArray);
                    ((MainActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });

                }
            }}).start();
    }

}
