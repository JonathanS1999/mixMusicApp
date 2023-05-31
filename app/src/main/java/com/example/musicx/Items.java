package com.example.musicx;

import android.graphics.Bitmap;


public class Items {

    String path ;
    String cancion;
    boolean checkbox;
    Bitmap imagen ;

    public Items() {
    }

    public  Items(String country, String path, Bitmap imagen , boolean status){
        this.cancion = country;
        this.checkbox = status;
        this.path = path;
        this. imagen= imagen;
    }






    //Getter and Setter

    public String getCancion() {
        return cancion;
    }



    public String getDir(){
        return path;
    }



    public void setCancion(String cancion) {
        this.cancion = cancion ;
    }





    public boolean isCheckbox() {
        return checkbox;
    }


    public Bitmap getBitmap(){
        return imagen;
    }




    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }





}
