package com.example.mysto.ricknmortybuddykotlin.network.JsonBin

import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.RawEpisodesServerResponse
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.RawLocationsServerResponse
import retrofit2.Call
import retrofit2.http.GET


interface GetDataService {

    @get:GET("/b/5b0568367a973f4ce5784514/1")
    val allLocations: Call<RawLocationsServerResponse>

    @get:GET("/b/5b1e327ec83f6d4cc734b197/1")
    val allEpisodes: Call<RawEpisodesServerResponse>

}