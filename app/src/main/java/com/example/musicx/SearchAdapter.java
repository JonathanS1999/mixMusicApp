package com.example.musicx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicx.image.ImageSong;
import com.example.musicx.stylesFonts.SearchSpannables;

import java.util.ArrayList;
import java.util.Locale;

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.MyVieHolderSV> {

    private final Context mContex ;
    public static   ArrayList<MusicFiles> mFilesSearchView ;
    private final Activity activity;
    private ArrayList<SearchSpannables> spannables;

   public SearchAdapter ( Activity activity,Context mContext , ArrayList<MusicFiles> mFiles ){
        mFilesSearchView=new ArrayList<>();
        mFilesSearchView.addAll(mFiles );
        this.mContex= mContext;
        this.activity= activity;
    }

    @NonNull
    @Override
    public SearchAdapter.MyVieHolderSV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContex).inflate(R.layout.itemlistplayer, parent , false);
        return new MyVieHolderSV(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyVieHolderSV holder, int positions) {
       try {
           if (spannables != null) {
               if (spannables.get(holder.getAdapterPosition()).getTitleSpannable() != null)
                   holder.file_name.setText(spannables.get(holder.getAdapterPosition()).getTitleSpannable());
               else holder.file_name.setText(mFilesSearchView.get(holder.getAdapterPosition()).getTitle());

               if (spannables.get(holder.getAdapterPosition()).getArtistSpannable() != null)
                   holder.artist.setText(spannables.get(holder.getAdapterPosition()).getArtistSpannable());
               else holder.artist.setText(mFilesSearchView.get(holder.getAdapterPosition()).getArtist());

           } else {
               holder.file_name.setText(mFilesSearchView.get(holder.getAdapterPosition()).getTitle());
               holder.artist.setText(mFilesSearchView.get(holder.getAdapterPosition()).getArtist());
           }
           String string = String.format(Locale.US, "%d", (holder.getAdapterPosition() + 1));
           holder.numSongs.setText(string);
           holder.artist.setTextColor(Color.MAGENTA);
           holder.file_name.setTextColor(Color.WHITE);
           holder.numSongs.setTextColor(Color.WHITE);
           holder.durationSongs.setText(milliSecondMinutHors(Integer.parseInt(mFilesSearchView.get(holder.getAdapterPosition()).getDuration())));
           holder.durationSongs.setTextColor(Color.WHITE);
           try {
               byte[] image = ImageSong.getBitmapPicture(mFilesSearchView.get(holder.getAdapterPosition()).getPicturePath());
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

           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(activity, PlayerActivity.class);
                   intent.putExtra("position", holder.getAdapterPosition() ).putExtra("elemtSelected", 5);
                   activity.startActivity(intent);
                   activity.finish();
               }
           });
       }catch (Exception e){
           Toast.makeText(mContex,e+" ", Toast.LENGTH_LONG).show();
       }
    }


    public void setSpannable(ArrayList<SearchSpannables> spannables){
        this.spannables= spannables;
    }

    @Override
    public int getItemCount() {
        if(mFilesSearchView == null)return 0;
        return mFilesSearchView.size();
    }


    public static class MyVieHolderSV extends RecyclerView.ViewHolder{

        TextView file_name , artist , numSongs , durationSongs ;
        ImageView album_art  ;
        public MyVieHolderSV ( View itemView ){
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name_player);
            artist = itemView.findViewById(R.id.texartista_player);
            numSongs = itemView.findViewById(R.id.numcancion_player);
            durationSongs = itemView.findViewById(R.id.duracion_player);
            album_art = itemView.findViewById(R.id.music_img_player);
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







    void updateList ( ArrayList<MusicFiles> musicFilesArrayList){
        try {
            if(mFilesSearchView != null ){
                mFilesSearchView.clear();
            }
            mFilesSearchView = new ArrayList<>();
            mFilesSearchView.addAll(musicFilesArrayList);
            notifyDataSetChanged();
        }catch ( Exception e){
            Toast.makeText(mContex , e+" 227",Toast.LENGTH_LONG).show();
        }
    }


}
