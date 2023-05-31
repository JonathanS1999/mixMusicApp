package com.example.musicx.SettingsPlayerPreferences

import android.content.Context
import android.content.SharedPreferences

class FoldersPreferences ( val context : Context ) {

    lateinit var  prefFolders : SharedPreferences.Editor

    fun setSaveHideFolders( arrayList: ArrayList<String>){
        // prefFolders = context.getSharedPreferences("PREFERENCE_FOLDER",Context.MODE_PRIVATE).edit()

    }



}