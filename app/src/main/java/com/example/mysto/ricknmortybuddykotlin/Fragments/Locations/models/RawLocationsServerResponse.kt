package com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RawLocationsServerResponse : Serializable {

    @SerializedName("info")
    @Expose
    var info: Info? = null
    @SerializedName("results")
    @Expose
    var results: List<Location>? = null

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
    constructor(info: Info, results: List<Location>) : super() {
        this.info = info
        this.results = results
    }

}