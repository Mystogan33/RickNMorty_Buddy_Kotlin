package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Location : Serializable {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param name
     * @param url
     */
    constructor(name: String, url: String) : super() {
        this.name = name
        this.url = url
    }

}