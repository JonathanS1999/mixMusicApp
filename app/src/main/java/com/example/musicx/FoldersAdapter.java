package com.example.musicx;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;
import static com.example.musicx.MainActivity.musicFilesGeneral;
import static com.example.musicx.MainActivity.musicFilesfavoritos;

public class FoldersAdapter extends RecyclerView.Adapter< FoldersAdapter.ViewHolder_F > {

    private ArrayList<MusicFiles> foldersMusics;
    private final Context mContext;
    private Menu menuu;
    private AlertDialog titulo;
    private final OnItemRemove itemRemove;
    public static final Object object = new Object();

   public FoldersAdapter( Context context,ArrayList<MusicFiles> folderMusic){
        mContext = context;
       itemRemove=(OnItemRemove)context;
       getFolders(folderMusic);
    }

    private void getFolders(ArrayList<MusicFiles> folderMusic){
        if( folderMusic != null) {
            this.foldersMusics = new ArrayList<>();
            ArrayList<String> b = new ArrayList<>();
            for (int i = 0; i < folderMusic.size(); i++) {
                File nameFolder = new File(folderMusic.get(i).getPatch());
                if (!b.contains(Objects.requireNonNull(nameFolder.getParentFile()).getName())) {
                    folderMusic.get(i).setCantMusisInFolder(0);
                    foldersMusics.add(folderMusic.get(i));
                    b.add(nameFolder.getParentFile().getName());
                } else {
                    for (int j = 0; j < foldersMusics.size(); j++) {
                        File n = new File(foldersMusics.get(j).getPatch());
                        if (Objects.requireNonNull(n.getParentFile()).getName().equals(nameFolder.getParentFile().getName())) {
                            foldersMusics.get(j).setCantMusisInFolder((foldersMusics.get(j).getCantMusisInFolder() + 1));
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
    public FoldersAdapter.ViewHolder_F onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_folder, parent , false);
        return new ViewHolder_F(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FoldersAdapter.ViewHolder_F holder, int positions) {
       try {
           File nameFolder = new File(foldersMusics.get(holder.getAdapterPosition()).getPatch());
           try {
               holder.titleFolder.setText(Objects.requireNonNull(nameFolder.getParentFile()).getName());
               String cant = String.format(Locale.US, "%d canciones", (foldersMusics.get(holder.getAdapterPosition()).getCantMusisInFolder() + 1));
               holder.numMusics.setText(cant);
           } catch (Exception e) {
               //  Toast.makeText(mContext,nameFolder.getParentFile().getName() +"\n"+e+"__"+position,Toast.LENGTH_SHORT).show();
           }
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   File file = new File(foldersMusics.get(holder.getAdapterPosition()).getPatch());
                   Intent intent = new Intent(mContext, FoldersActivity.class);
                   intent.putExtra("nameFolder", Objects.requireNonNull(nameFolder.getParentFile()).getName()).
                           putExtra("path", file.getParent());
                   mContext.startActivity(intent);
               }
           });
           holder.menuFolders.setTag(holder);
           holder.menuFolders.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   PopupMenu popupmenu = new PopupMenu(mContext, v);
                   popupmenu.getMenuInflater().inflate(R.menu.menu_folders, popupmenu.getMenu());
                   popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                       @Override
                       public boolean onMenuItemClick(MenuItem item) {
                           if (item.getItemId() == R.id.item1) {
                               File nameFold = new File(foldersMusics.get(holder.getAdapterPosition()).getPatch());
                               androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                               alert.setMessage("¿ Eliminar " + nameFold.getParent() + " ?")
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
                               titulo.setTitle("Se eliminará del dispositivo");
                               titulo.show();
                           } else if (item.getItemId() == R.id.item2) {
                           } else if (item.getItemId() == R.id.item3) {
                           } else if (item.getItemId() == R.id.item4) {
                               addToQueue(holder.getAdapterPosition(), v);
                           } else if (item.getItemId() == R.id.item5) {
                               addFavoritos(holder.getAdapterPosition(), v);
                           } else if (item.getItemId() == R.id.item6) {
                           } else if (item.getItemId() == R.id.item7) {
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
           Toast.makeText(mContext,e+" 159 foldersAdapter",Toast.LENGTH_LONG).show();
       }
    }


    private void addToQueue(int position ,View v ){
        File nameFolder = new File(foldersMusics.get(position).getPatch());
        int em=0;
        for (MusicFiles musicFiles : musicFilesGeneral) {
            File n = new File(musicFiles.getPatch());
            if (Objects.requireNonNull(n.getParentFile()).getName().equals(Objects.requireNonNull(nameFolder.getParentFile()).getName())) {
                if (!nowPlayerSongs.contains(musicFiles)) {
                    nowPlayerSongs.add(musicFiles);
                    em++;
                } else {
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
            if( foldersMusics != null) {
                File nameFolder = new File(foldersMusics.get(position).getPatch());
                    int em=0;
                    int i =0;
                ArrayList<Integer> delet= new ArrayList<>();
                for (MusicFiles musicFiles : musicFilesGeneral) {
        File n = new File(musicFiles.getPatch());
        if (Objects.requireNonNull(n.getParentFile()).getName().equals(Objects.requireNonNull(nameFolder.getParentFile()).getName())) {
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Long.parseLong(musicFiles.getId()));
            boolean deleted = n.delete();
            if (deleted){
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


    public void updateItemRemove(MusicFiles fil){
        int i=0;
        File n = new File(fil.getPatch());
        for(MusicFiles  musicFiles : foldersMusics) {
            File del = new File(musicFiles.getPatch());
            if(Objects.requireNonNull(n.getParentFile()).getName().equals(Objects.requireNonNull(del.getParentFile()).getName())){
                if(foldersMusics.get(i).getCantMusisInFolder() > 0) {
                    foldersMusics.get(i).setCantMusisInFolder((foldersMusics.get(i).getCantMusisInFolder() - 1));
                    break;
                }else {
                    foldersMusics.remove(fil);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i,foldersMusics.size());
                    break;
                }
            }
            i++;
        }
    }


    private void addFavoritos ( int position , View v ){
        if(foldersMusics != null) {
            String temp = foldersMusics.get(position).getPatch();
            int i, iContents=0;
            for ( i=0; i < musicFilesGeneral.size(); i++) {
                if(temp.equals(musicFilesGeneral.get(i).getPatch())){
                    if(!musicFilesfavoritos.contains(musicFilesGeneral.get(i))){
                        musicFilesfavoritos.add(musicFilesGeneral.get(i));
                    }else{
                        iContents ++;
                    }
                }
            }
            if( iContents >0){
                Snackbar.make(v, iContents+" elementos ya estaban entre tus favoritos", Snackbar.LENGTH_LONG).show();
            }
            Snackbar.make(v, i+" elementos añadidos a favoritos", Snackbar.LENGTH_LONG).show();
        }
    }



    @Override
    public int getItemCount() {
        if(foldersMusics == null) return 0;
        return foldersMusics.size();
    }

    void updateList ( ArrayList<MusicFiles> musicFilesArray){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    if (musicFilesArray != null) {
                        if (foldersMusics != null) foldersMusics.clear();
                        getFolders(musicFilesArray);
                        ((MainActivity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        }).start();


    }

    public static class ViewHolder_F extends RecyclerView.ViewHolder{

         TextView numMusics;
         TextView titleFolder;
         ImageView menuFolders;
        public ViewHolder_F ( View itemView ){
            super(itemView);
            numMusics =  itemView.findViewById(R.id.numMusicsInFold);
            titleFolder = itemView.findViewById(R.id.nameFolders);
            menuFolders=itemView.findViewById(R.id.menuFolders);
        }
    }


}
