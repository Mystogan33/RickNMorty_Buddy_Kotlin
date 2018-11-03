package com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RawEpisodesServerResponse : Serializable {

    @SerializedName("results")
    @Expose
    var results: List<Episode>? = null
    @SerializedName("info")
    @Expose
    var info: Info? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param results
     * @param info
     */
    constructor(results: List<Episode>, info: Info) : super() {
        this.results = results
        this.info = info
    }

}