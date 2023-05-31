package com.example.musicx.image;

import static com.example.musicx.NowPlayingFragment.nowPlayerSongs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.musicx.R;

public class ImageSong {

    public static void setImage(ImageView image , Context context , String path) {
        setImage( image ,  context ,  path,  1 ,  0, false);
    }

    public static void setImage(ImageView image , Context context , String path, int tipe, float radius , boolean circular ){
        if(image != null) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    byte[] picture = getPicture(path);

                    if (picture != null) {
                        Bitmap portada;
                        Drawable drawable;
                        switch (tipe) {
                            case 1:
                                portada = BitmapFactory.decodeByteArray(picture,
                                        0, picture.length);
                                setImageBtmap(context, portada, image);
                                break;
                            case 2:
                                setImageRounded(image, context ,picture, circular, radius);
                                break;
                            default:
                                portada = BitmapFactory.decodeByteArray(picture,
                                        0, picture.length);
                                setImageBtmap(context, portada, image);
                                break;
                        }

                    }else{
                        setImageDefault(context,image);
                    }
                }
            }).start();
        }
    }

    private static void setImageRounded(ImageView image , Context context , byte[] picture , boolean circular, float radius){

        Drawable drawable;
        RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(context.getResources(),BitmapFactory.decodeByteArray(picture,
                                0, picture.length));
        if(circular){
            roundedBitmapDrawable.setCircular(true);
        }else {
            roundedBitmapDrawable.setCornerRadius(radius);
        }
        drawable=roundedBitmapDrawable;
        setImageDrawable(context,drawable,image);

    }

    private static byte[] getPicture(String path){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] picture = retriever.getEmbeddedPicture();
        retriever.release();
        return picture;
    }
    public static byte[] getBitmapPicture(String path){
        return  getPicture(path);
    }


    private static void setImageBtmap(Context context, Bitmap bitmap , ImageView image){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageBitmap(bitmap);
            }
        });
    }

    private static void setImageDrawable(Context context, Drawable drawable , ImageView image){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageDrawable(drawable);
            }
        });
    }

    private static void setImageDefault(Context context , ImageView image){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_music_note_24,null));
            }
        });
    }

}
