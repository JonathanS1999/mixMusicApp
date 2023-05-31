package com.example.musicx;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicFiles  {

    private String patch;
    private String title ;
    private String album;
    private String artist ;
    private String duration ;
    private String id;
    private int countAlbums=0;
    private int countArtist=0;
    private int cantMusisInFolder=0;
    private String picturePath;

    public MusicFiles(String patch, String title,String artist , String album, String duration ,String id , String picturePath) {
        this.patch = patch;
        this.title = title;
        this.album = album;
        this.duration = duration;
        this.artist= artist ;
        this.id= id;
        this.picturePath=picturePath;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setId( String id ){
        this.id= id;
    }
    public String getId(){
        return id;
    }

    public int getCountAlbums() {
        return countAlbums;
    }

    public void setCountAlbums(int countAlbums) {
        this.countAlbums = countAlbums;
    }

    public int getCantMusisInFolder() {
        return cantMusisInFolder;
    }

    public void setCantMusisInFolder(int cantMusisInFolder) {
        this.cantMusisInFolder = cantMusisInFolder;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicture(String picturePath) {
        this.picturePath = picturePath;
    }

    public int getCountArtist() {
        return countArtist;
    }

    public void setCountArtist(int countArtist) {
        this.countArtist = countArtist;
    }

}
