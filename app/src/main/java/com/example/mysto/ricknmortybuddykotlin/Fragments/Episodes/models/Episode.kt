package com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Episode : Serializable {

    @SerializedName("created")
    @Expose
    var created: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("characters")
    @Expose
    var characters: List<String>? = null
    @SerializedName("episode")
    @Expose
    var episode: String? = null
    @SerializedName("air_date")
    @Expose
    var airDate: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param id
     * @param airDate
     * @param episode
     * @param created
     * @param name
     * @param image
     * @param characters
     * @param url
     * @param description
     */
    constructor(
        created: String,
        image: String,
        url: String,
        characters: List<String>,
        episode: String,
        airDate: String,
        name: String,
        id: Int?,
        description: String
    ) : super() {
        this.created = created
        this.image = image
        this.url = url
        this.characters = characters
        this.episode = episode
        this.airDate = airDate
        this.name = name
        this.id = id
        this.description = description
    }

}