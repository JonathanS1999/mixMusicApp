package com.example.musicx.SearchSongs;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.musicx.MusicFiles;

import java.io.File;
import java.util.ArrayList;

import static com.example.musicx.MainActivity.MY_SORT_PREF;

public class SearchSongs {
    private final Context context;
    private static int countSonds=0;
    private ContentResolver contentResolver;

    public SearchSongs ( Context context) {
        this.context=context;
    }


    public void getAllAudio(OnFinishSearchAudio finishSearchAudio) {

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = context.getSharedPreferences(MY_SORT_PREF,Context.MODE_PRIVATE);
                contentResolver=context.getContentResolver();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String sortOrder = preferences.getString("sorting", "sortNameAZ" );
                        String order ;
                        switch (sortOrder){
                            case "sortDate":
                                order=MediaStore.Audio.Media.DATE_ADDED+" ASC";
                                break;
                            case "sortDateU":
                                order=MediaStore.Audio.Media.DATE_MODIFIED+" ASC";
                                break;
                            case "sortNameAZ":
                                order=MediaStore.Audio.Media.TITLE+" ASC";
                                break;
                            case "sortNameZA":
                                order=MediaStore.Audio.Media.TITLE + " DESC";
                                break;
                            case"sortSizeAs":
                                order=MediaStore.Audio.Media.SIZE +" ASC";
                                break;
                            case"sortSizeDes":
                                order=MediaStore.Audio.Media.SIZE +" DESC";
                                break;
                            default:
                                order=null;
                        }
                        try {
                            ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
                            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                            countSonds++;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {// si es mayor que android 10
                                String[] informacion = new String[]{
                                        MediaStore.Audio.Media._ID,  //0
                                        MediaStore.Audio.Media.DATA, //1
                                        MediaStore.Audio.Media.TRACK, //2
                                        MediaStore.Audio.Media.YEAR, //3
                                        MediaStore.Audio.Media.DURATION, //4
                                        MediaStore.Audio.Media.ALBUM_ID, //5
                                        MediaStore.Audio.Albums.ALBUM, //6
                                        MediaStore.Audio.Media.TITLE, //7
                                        MediaStore.Audio.Media.ARTIST_ID,//8
                                        MediaStore.Audio.Artists.ARTIST,//9
                                        MediaStore.Audio.Media.DISPLAY_NAME,//10
                                        MediaStore.Audio.Media.RELATIVE_PATH, // 11
                                };

                                Cursor cursor = contentResolver.query(uri, informacion,
                                        null, null, order);


                                        if (cursor != null) {
                                            while (cursor.moveToNext()) {
                                                try {
                                                    int data =cursor.getColumnIndex("_data");
                                                    String strIng = cursor.getString(data);
                                                    File fil= new File(strIng);
                                                    if (fil.exists()) {

                                                        String _id = String.valueOf(cursor.getLong(0));
                                                        String album = cursor.getString(6);
                                                        String title = cursor.getString(7);
                                                        String duration = cursor.getString(4);
                                                        String patch = cursor.getString(1);
                                                        String artist = cursor.getString(9);

                                                        if (duration != null) {
                                                            if (Long.parseLong(duration) > 1000) {
                                                                MusicFiles musicFiles = new MusicFiles(strIng, title, artist, album, duration, _id, patch);
                                                                tempAudioList.add(musicFiles);
                                                            }
                                                        }
                                                    }

                                                } catch (Exception e){
                                                    Toast.makeText(context, e+"  search songs 103 ", Toast.LENGTH_LONG ). show ();
                                                }
                                            }//while
                                            cursor.close();
                                        }
                                        finishSearchAudio.onAudio( tempAudioList);



                            } else {
                                String informacion[] = new String[]{
                                        MediaStore.Audio.Media._ID,  //0
                                        MediaStore.Audio.Media.DATA, //1
                                        MediaStore.Audio.Media.TRACK, //2
                                        MediaStore.Audio.Media.YEAR, //3
                                        MediaStore.Audio.Media.DURATION, //4
                                        MediaStore.Audio.Media.ALBUM_ID, //5
                                        MediaStore.Audio.Media.ALBUM, //6
                                        MediaStore.Audio.Media.TITLE, //7
                                        MediaStore.Audio.Media.ARTIST_ID,//8
                                        MediaStore.Audio.Media.ARTIST,//9
                                        MediaStore.Audio.Media.ALBUM_ARTIST
                                };

                                Cursor cursor = contentResolver.query(uri, informacion,
                                        null, null, order);

                                        if (cursor != null) {
                                            while (cursor.moveToNext()) {
                                                try {
                                                    File fil = new File(cursor.getString(1));

                                                    if (fil.exists()) {
                                                        String id = String.valueOf(cursor.getLong(0));
                                                        String album = cursor.getString(6);
                                                        String title = cursor.getString(7);
                                                        String duration = cursor.getString(4);
                                                        String patch = cursor.getString(1);
                                                        String artist = cursor.getString(9);

                                                        if (duration != null) {
                                                            if (Long.parseLong(duration) > 1000) {
                                                                MusicFiles musicFiles = new MusicFiles(patch, title, artist, album, duration, id,patch);
                                                                tempAudioList.add(musicFiles);
                                                            }
                                                        }

                                                    }

                                                } catch (Exception e) {
                                                    Toast.makeText(context, e+" search songs 126", Toast.LENGTH_LONG ). show ();
                                                }
                                            }//while
                                            cursor.close();
                                        }

                                        finishSearchAudio.onAudio( tempAudioList);

                            }



                        }catch (Exception e ){
                            Toast.makeText(context, e+" 162 search songs ", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });

    }




    public interface OnFinishSearchAudio{
        void onAudio(ArrayList<MusicFiles> tempAudioList);
    }

}


