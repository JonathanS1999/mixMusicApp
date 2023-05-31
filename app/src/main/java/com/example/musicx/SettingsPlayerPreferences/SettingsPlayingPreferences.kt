package com.example.musicx.SettingsPlayerPreferences

import android.content.Context
import android.content.SharedPreferences

class SettingsPlayingPreferences (private val context: Context){
    private var speed :Float = 0.0f
    private lateinit var  sharedPreferences : SharedPreferences.Editor

    fun  setSavedSpeed(speed : Float  ){
        sharedPreferences =   context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putFloat("SPEED",speed)
        sharedPreferences.apply()
    }

    fun setSavedTypePlay(typePlay : Char ){
        sharedPreferences =  context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putString("PLAY_TYPE",typePlay.toString())
        sharedPreferences.apply()
    }


    fun setSavedTypeFade(typePlay : Char ){
        sharedPreferences =   context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putString("FADE_TYPE",typePlay.toString())
        sharedPreferences.apply()
    }


    fun  setSavedTimeStart(start : Int  ){
        sharedPreferences =   context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putInt("TIME_START",start)
        sharedPreferences.apply()
    }

    fun setSavedTimeFinish(end : Int  ){
        sharedPreferences =   context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putInt("TIME_FINISH",end)
        sharedPreferences.apply()
    }


    fun setSavedThemeList(theme : Int  ){
        sharedPreferences =   context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putInt("MY_THEME",theme)
        sharedPreferences.apply()
    }

    fun setSavedThemePlayer(theme : Int  ){
        sharedPreferences = context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putInt("MY_THEME_PLAYER",theme)
        sharedPreferences.apply()
    }

    fun setSavedThemePlayerMC(theme : Int  ){
        sharedPreferences = context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putInt("MY_THEME_PLAYER_MC",theme)
        sharedPreferences.apply()
    }



    fun setSavedThemeFont(theme : Int  ){
        sharedPreferences = context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putInt("MY_THEME_FONT",theme)
        sharedPreferences.apply()
    }

    fun setSavedThemeControls(theme : Int  ){
        sharedPreferences = context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE).edit()
        sharedPreferences.putInt("MY_THEME_CONTROLS",theme)
        sharedPreferences.apply()
    }

    fun getSpeed() : Float{
       val preferences_= context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getFloat("SPEED",1.0f)
    }


    fun getTypePlayer() : String? {
        val preferences_= context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getString("PLAY_TYPE","a")
    }

    fun getTypeFade() : String? {
        val preferences_= context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getString("FADE_TYPE","a")
    }

    fun getTimeStart() : Int{
        val preferences_= context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getInt("TIME_START",0)
    }


    fun getTimeFinish() : Int {
        val preferences_= context.getSharedPreferences("PLAYER_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getInt("TIME_FINISH",0)
    }

    fun getMyThemeList() : Int {
        val preferences_= context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getInt("MY_THEME",1)
    }

    fun getMyThemePlayer() : Int {
        val preferences_= context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getInt("MY_THEME_PLAYER",1)
    }

    fun getMyThemePlayerMC() : Int {
        val preferences_= context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getInt("MY_THEME_PLAYER_MC",1)
    }

    fun getMyThemeFont() : Int {
        val preferences_= context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getInt("MY_THEME_FONT",1)
    }

    fun getMyThemeControls() : Int {
        val preferences_= context.getSharedPreferences("THEME_PREFERENCES",Context.MODE_PRIVATE)
        return preferences_.getInt("MY_THEME_CONTROLS",1)
    }
}