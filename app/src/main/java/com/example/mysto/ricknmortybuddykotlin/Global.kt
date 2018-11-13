package com.example.mysto.ricknmortybuddykotlin

import android.app.Application
import com.example.mysto.ricknmortybuddykotlin.db.DataBaseHelper
import com.squareup.picasso.Picasso
import com.jakewharton.picasso.OkHttp3Downloader



class Global : Application() {

    override fun onCreate() {
        super.onCreate()

        val builder = Picasso.Builder(this)
            .downloader(OkHttp3Downloader(this, Integer.MAX_VALUE.toLong()))
            .build()
        Picasso.setSingletonInstance(builder)
    }

}