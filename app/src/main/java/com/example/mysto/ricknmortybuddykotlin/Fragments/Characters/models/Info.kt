package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Info : Serializable {

    @SerializedName("count")
    @Expose
    var count: Int? = null
    @SerializedName("pages")
    @Expose
    var pages: Int? = null
    @SerializedName("next")
    @Expose
    var next: String? = null
    @SerializedName("prev")
    @Expose
    var prev: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param count
     * @param next
     * @param pages
     * @param prev
     */
    constructor(count: Int?, pages: Int?, next: String, prev: String) : super() {
        this.count = count
        this.pages = pages
        this.next = next
        this.prev = prev
    }

}