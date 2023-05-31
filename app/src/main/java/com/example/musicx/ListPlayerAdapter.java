package com.example.musicx;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.converTimes.ConvertTime;
import com.example.musicx.custom.CustomView;
import com.example.musicx.custom.InicializerColors;
import com.example.musicx.custom.ThemeJS;
import com.example.musicx.image.ImageSong;

import java.util.ArrayList;
import java.util.Locale;

public class ListPlayerAdapter extends RecyclerView.Adapter<ListPlayerAdapter.MyVieHolder> {

    Context mContex ;
    static ArrayList<MusicFiles> mFiles_list_player ;
    private ListPlayerAdaperInterface playerAdaperInterface;
    private int it=0;
    private int idStyle=0;
    private final ArrayList<String> colorsA= new ArrayList<>();
    private final ArrayList<String> colorsB= new ArrayList<>();
    private int selectorGradient=0;
    private SettingsPlayingPreferences s;
    private ConvertTime timec;

    public ListPlayerAdapter( Context mContext , int idStyle){
        s = new SettingsPlayingPreferences(mContext);
        this.mContex= mContext;
        this.idStyle= idStyle;
        if(idStyle == 3 ){
            new InicializerColors(colorsA, colorsB);
        }else if(idStyle == 4 ) timec = new ConvertTime();
        if ( mContext instanceof ListPlayerAdaperInterface){
            this.playerAdaperInterface = ( ListPlayerAdaperInterface) mContext;
        }
    }

    @NonNull
    @Override
    public ListPlayerAdapter.MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(idStyle ==1 || idStyle == 2 ) {
            view = LayoutInflater.from(mContex).inflate(R.layout.itemlistplayer, parent, false);
        }else if ( idStyle == 3) {
            view = LayoutInflater.from(mContex).inflate(R.layout.album_item, parent, false);
        }else if(idStyle ==4) {
            view = LayoutInflater.from(mContex).inflate(R.layout.layout_theme_js, parent, false);
        } else {
                view = LayoutInflater.from(mContex).inflate(R.layout.album_item2, parent, false);
            }
        return new MyVieHolder(view,idStyle);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPlayerAdapter.MyVieHolder holder, int positions ) {
        try {

        byte[] image = ImageSong.getBitmapPicture(mFiles_list_player.get(holder.getAdapterPosition()).getPicturePath());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerAdaperInterface.setPositionPlayer(  holder.getAdapterPosition());
                //v.setBackground(ContextCompat.getDrawable(mContex,R.drawable.background_item_player_list));
            }
        });

        if(idStyle==1 || idStyle == 2 ){

            try {

                holder.file_name.setText(mFiles_list_player.get(holder.getAdapterPosition()).getTitle());
                holder.artist.setText(mFiles_list_player.get(holder.getAdapterPosition()).getArtist());
                String string = String.format(Locale.US, "%d", (holder.getAdapterPosition() + 1));
                holder.numSongs.setText(string);
                holder.artist.setTextColor(Color.MAGENTA);
                holder.file_name.setTextColor(Color.WHITE);
                holder.numSongs.setTextColor(Color.WHITE);
                holder.durationSongs.setText(milliSecondMinutHors(Integer.parseInt(mFiles_list_player.get(holder.getAdapterPosition()).getDuration())));
                holder.durationSongs.setTextColor(Color.WHITE);


                Theme_Font.setFont(holder.numSongs, mContex,s);
                Theme_Font.setFont(holder.file_name,mContex,s);
                Theme_Font.setFont(holder.artist,mContex,s);
                Theme_Font.setFont(holder.durationSongs,mContex,s);

                try {

                    if (image != null) {
                        Glide.with(mContex).asBitmap()
                                .load(image)
                                .into(holder.album_art);
                    } else {
                        Glide.with(mContex)
                                .load(R.drawable.iconp)
                                .into(holder.album_art);
                    }
                } catch (Exception e) {

                }


                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        return true;
                    }
                });
                holder.removeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playerAdaperInterface.itemRemove(holder.getAdapterPosition());
                    }
                });
            } catch (Exception e) {
                Toast.makeText(mContex, e +" error dentro ",Toast.LENGTH_LONG).show();
            }
    }else if ( idStyle == 3){
            holder.file_name.setText(mFiles_list_player.get(holder.getAdapterPosition()).getTitle());
            String string = String.format(Locale.US, "%d", (holder.getAdapterPosition() + 1));
            holder.numSongs.setText(string);
            holder.file_name.setTextColor(Color.WHITE);
            holder.numSongs.setTextColor(Color.WHITE);

            Theme_Font.setFont(holder.numSongs, mContex,s);
            Theme_Font.setFont(holder.file_name,mContex,s);

                if (image != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    holder.album_art.setImageBitmap(bitmap);
                    if (selectorGradient > 6) selectorGradient = 0;
                    int[] color1 = {Color.parseColor(colorsA.get(selectorGradient)), Color.parseColor(colorsB.get(selectorGradient))};
                    Drawable gd = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color1);
                    holder.containerTitle.setBackground(gd);
                    selectorGradient++;
                } else {
                    Glide.with(mContex)
                            .load(R.drawable.iconp)
                            .into(holder.album_art);
                }

            }else if (idStyle == 4){
            try {
                if (holder.container.getChildCount() > 0) holder.container.removeAllViews();
                String string = String.format(Locale.US, "%d", (holder.getAdapterPosition() + 1));
                if (image != null){
                    holder.container.addView(
                            new ThemeJS(mContex,
                                    BitmapFactory.decodeByteArray(image, 0, image.length),
                                    "000000","000000",
                                    mFiles_list_player.get(holder.getAdapterPosition()).getTitle(),
                                    timec.getMilliSecondMinutHors(Integer.parseInt(mFiles_list_player.get(holder.getAdapterPosition()).getDuration())),
                                    string,
                                    mFiles_list_player.get(holder.getAdapterPosition()).getArtist()
                            ));
                }else{
                    holder.container.addView(
                            new ThemeJS(mContex,BitmapFactory.decodeResource(mContex.getResources(), R.drawable.iconp),
                                    "000000","000000",
                                    mFiles_list_player.get(holder.getAdapterPosition()).getTitle(),
                                    timec.getMilliSecondMinutHors(Integer.parseInt(mFiles_list_player.get(holder.getAdapterPosition()).getDuration())),
                                    string,
                                    mFiles_list_player.get(holder.getAdapterPosition()).getArtist()
                            ));
                }
            }catch (Exception e){
                Toast.makeText(mContex, e +" 194 album adapter ",Toast.LENGTH_LONG).show();
            }
        } else{
            try {
                if (holder.container.getChildCount() > 0) holder.container.removeAllViews();
                if (image != null)
                    holder.container.addView(new CustomView(mContex, BitmapFactory.decodeByteArray(image, 0, image.length), "", holder.container));
                else
                    holder.container.addView(new CustomView(mContex, BitmapFactory.decodeResource(mContex.getResources(), R.drawable.iconp), "", holder.container));
            } catch (Exception e) {
                Toast.makeText(mContex, e + " 194 album adapter ", Toast.LENGTH_LONG).show();
            }
        }
        }catch (Exception e){
                Toast.makeText(mContex, e +" 194 album adapter ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        if (mFiles_list_player== null) return 0;
            return mFiles_list_player.size();
    }


    public static class MyVieHolder extends RecyclerView.ViewHolder{

        TextView file_name , artist , numSongs , durationSongs ;
        ImageView album_art , removeItem ;
        LinearLayout container;

        RelativeLayout containerTitle;
        RelativeLayout containerView1;

        public MyVieHolder ( View itemView , int idStyle){
            super(itemView);
            if(idStyle ==1 || idStyle == 2 ) {
            file_name = itemView.findViewById(R.id.music_file_name_player);
            artist = itemView.findViewById(R.id.texartista_player);
            numSongs = itemView.findViewById(R.id.numcancion_player);
            durationSongs = itemView.findViewById(R.id.duracion_player);
            album_art = itemView.findViewById(R.id.music_img_player);
            removeItem =itemView.findViewById(R.id.deleteQueQue);

            }else if ( idStyle == 3 ){
                album_art = itemView.findViewById(R.id.album_image);
                file_name = itemView.findViewById(R.id.album_name);
                numSongs = itemView.findViewById(R.id.numAlbumCard);
                ( (ImageView) itemView.findViewById(R.id.menu_options_albums) ).setVisibility(View.GONE);
                containerTitle = itemView.findViewById(R.id.containerTitle);
                containerView1 = itemView.findViewById(R.id.container1);
                ViewGroup.LayoutParams params =  itemView.getLayoutParams();
                if(params instanceof ViewGroup.MarginLayoutParams){
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) params;
                    p.setMargins(8,8,8,8);
                }
            }else {
                    container = itemView.findViewById(R.id.container);
                }
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







    void updateList(ArrayList<MusicFiles> musicFilesArrayList){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (musicFilesArrayList != null) {
                        if (mFiles_list_player != null) {
                            mFiles_list_player.clear();
                        }
                        mFiles_list_player = new ArrayList<>();
                        mFiles_list_player.addAll(musicFilesArrayList);
                        ((Activity) mContex).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            }).start();
        }catch ( Exception e){
            Toast.makeText(mContex , e+" 150 list player adapter",Toast.LENGTH_LONG).show();
        }
    }


    public void updateItemRemoved(MusicFiles fil){
        new Thread(new Runnable() {
            @Override
            public void run() {
                 it=0;
                for(MusicFiles  musicFiles : mFiles_list_player ) {
                    if (musicFiles == fil) {
                        mFiles_list_player.remove(fil);
                        ((Activity) mContex).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyItemRemoved(it);
                                notifyItemRangeChanged(it,mFiles_list_player.size());
                            }
                        });
                        break;
                    }
                    it++;
                }
                }
        }).start();
    }

public  interface ListPlayerAdaperInterface{
         void setPositionPlayer( int position );
         int getPositionPlayer();
         void itemRemove(int position);
}

}
