package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RawCharactersServerResponse(
    @field:SerializedName("info")
    @field:Expose
    var info: Info?, results: List<Character>
) : Serializable {

    @SerializedName("results")
    @Expose
    var results: List<Character>? = null

    init {
        this.results = results
    }

}