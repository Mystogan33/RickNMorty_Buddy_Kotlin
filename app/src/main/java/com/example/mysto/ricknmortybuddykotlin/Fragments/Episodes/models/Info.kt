package com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Info : Serializable {

    @SerializedName("prev")
    @Expose
    var prev: String? = null
    @SerializedName("next")
    @Expose
    var next: String? = null
    @SerializedName("pages")
    @Expose
    var pages: Int? = null
    @SerializedName("count")
    @Expose
    var count: Int? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param count
     * @param pages
     * @param next
     * @param prev
     */
    constructor(prev: String, next: String, pages: Int?, count: Int?) : super() {
        this.prev = prev
        this.next = next
        this.pages = pages
        this.count = count
    }

}