package com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Location : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("dimension")
    @Expose
    var dimension: String? = null
    @SerializedName("residents")
    @Expose
    var residents: List<String>? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("created")
    @Expose
    var created: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param id
     * @param dimension
     * @param created
     * @param name
     * @param image
     * @param type
     * @param url
     * @param residents
     */
    constructor(
        id: Int?,
        name: String,
        type: String,
        dimension: String,
        residents: List<String>,
        url: String,
        image: String,
        created: String
    ) : super() {
        this.id = id
        this.name = name
        this.type = type
        this.dimension = dimension
        this.residents = residents
        this.url = url
        this.image = image
        this.created = created
    }

}