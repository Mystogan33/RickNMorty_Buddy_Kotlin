package com.example.mysto.ricknmortybuddykotlin

import android.app.Application
import com.squareup.picasso.Picasso
import com.jakewharton.picasso.OkHttp3Downloader



class Global : Application() {

    override fun onCreate() {
        super.onCreate()

        val builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(this, Integer.MAX_VALUE.toLong()))
        val built = builder.build()
        Picasso.setSingletonInstance(built)
    }

}