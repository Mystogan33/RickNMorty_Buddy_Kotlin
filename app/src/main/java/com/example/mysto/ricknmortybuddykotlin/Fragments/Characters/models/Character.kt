package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Character : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("species")
    @Expose
    var species: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("gender")
    @Expose
    var gender: String? = null
    @SerializedName("origin")
    @Expose
    var origin: Origin? = null
    @SerializedName("location")
    @Expose
    var location: Location? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("episode")
    @Expose
    var episode: List<String>? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("created")
    @Expose
    var created: String? = null

    @SerializedName("description")
    @Expose
    private val description: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param id
     * @param episode
     * @param species
     * @param created
     * @param location
     * @param status
     * @param name
     * @param origin
     * @param image
     * @param gender
     * @param type
     * @param url
     * @param
     */
    constructor(
        id: Int?,
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String,
        origin: Origin,
        location: Location,
        image: String,
        episode: List<String>,
        url: String,
        created: String
    ) : super() {
        this.id = id
        this.name = name
        this.status = status
        this.species = species
        this.type = type
        this.gender = gender
        this.origin = origin
        this.location = location
        this.image = image
        this.episode = episode
        this.url = url
        this.created = created
    }

}