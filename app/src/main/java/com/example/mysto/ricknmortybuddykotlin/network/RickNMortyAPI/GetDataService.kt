package com.example.mysto.ricknmortybuddykotlin.network.RickNMortyAPI

import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.RawCharactersServerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GetDataService {

    @GET("character")
    fun getAllPersonnagesFromPage(@Query("page") pageId: Int): Call<RawCharactersServerResponse>

    @GET("character/{id}")
    fun getPersonnageById(@Path("id") characterId: Int): Call<Character>

}