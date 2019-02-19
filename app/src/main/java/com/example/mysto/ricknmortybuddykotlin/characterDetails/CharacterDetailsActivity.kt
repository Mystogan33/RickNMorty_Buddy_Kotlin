package com.example.mysto.ricknmortybuddykotlin.characterDetails

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager

import android.widget.FrameLayout
import android.widget.ImageView

import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.Episode
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.RawEpisodesServerResponse
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.Location
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.RawLocationsServerResponse
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.adapters.RecyclerViewEpisodesAdapter
import com.example.mysto.ricknmortybuddykotlin.locationDetails.LocationDetailsActivity
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_details_activity.*
import kotlinx.android.synthetic.main.episode_details_activity.*


class CharacterDetailsActivity : AppCompatActivity() {

    lateinit var adapter: RecyclerViewEpisodesAdapter
    internal var gson: Gson = Gson()
    private var sharedPreferences: SharedPreferences? = null
    private var characterDetails: Character? = null
    private var listURLEpisodes: MutableList<String>? = arrayListOf()
    private var listEpisodes: MutableList<Episode>? = arrayListOf()
    private var listEpisodesDetails: MutableList<Episode>? = arrayListOf()
    private var lastLocationURL: String? = ""
    private var lastLocationData: Location? = null
    private var idLastLocation: Int? = null
    private var originURL: String? = ""
    private var listLocations: MutableList<Location> = arrayListOf()
    private var originData: Location? = null
    private var idOrigin: Int? = null
    private var extras: Bundle? = null

    private fun setValuesToViews() {
        character_name.text = characterDetails!!.name

        val status = characterDetails!!.status

        when {
            status?.toLowerCase() == "dead" -> {
                character_status.text = resources.getString(R.string.status_dead)
                character_relay_status.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDanger))
            }
            status?.toLowerCase() == "alive" -> character_status.text = resources.getString(R.string.status_alive)
            else -> {
                character_status.text = resources.getString(R.string.status_unknown)
                character_relay_status.setBackgroundColor(ContextCompat.getColor(this, R.color.followersBg))
            }
        }

        character_species.text = characterDetails!!.species

        val gender = characterDetails!!.gender

        when {
            gender?.toLowerCase() == "male" -> character_gender.text = resources.getString(R.string.gender_male)
            gender?.toLowerCase() == "female" -> character_gender.text = resources.getString(R.string.gender_female)
            else -> character_gender.text = resources.getString(R.string.gender_unknown)
        }

        character_last_location.text = characterDetails!!.location!!.name
        character_origin.text = characterDetails!!.origin!!.name

        episodes_recyclerview.layoutManager = GridLayoutManager(this, 5)
        episodes_recyclerview.adapter = adapter
    }

    private fun initActionBar() {

        setSupportActionBar(character_toolbar)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
        actionbar?.setDisplayShowTitleEnabled(false)
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent)

    }

    private fun loadImage(imgUrl: String?, imgView: ImageView?) {

        Picasso.with(this)
            .load(imgUrl)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.no_image)
            .into(imgView, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(parent)
                        .load(imgUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.no_image)
                        .into(imgView, object : Callback {
                            override fun onSuccess() {}
                            override fun onError() {}
                        })
                }
            })
    }

    private fun searchForLocations() {

        for (location in listLocations) {

            if (location.id!! == idLastLocation) {
                lastLocationData = location
                character_last_location.text = lastLocationData?.name

                this.loadImage(lastLocationData!!.image, last_location_img)

                character_relayLastLocation.setOnClickListener {
                    val intent = Intent(this, LocationDetailsActivity::class.java)
                    intent.putExtra("location_details", lastLocationData)
                    startActivity(intent)
                }

            }

            if (location.id!! == idOrigin) {
                originData = location
                character_origin.text = originData!!.name

                this.loadImage(originData!!.image, origin_img)

                character_relayOrigin.setOnClickListener {
                    val intent = Intent(applicationContext, LocationDetailsActivity::class.java)
                    intent.putExtra("location_details", originData)
                    startActivity(intent)
                }
            }

        }

    }

    private fun searchForEpisodes() {

        for (episodeUrl in listURLEpisodes!!) {

            val id = episodeUrl.split("/episode/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

            for (episode in listEpisodes!!) {

                if (episode.id!! == Integer.valueOf(id)) {
                    listEpisodesDetails?.add(episode)
                    adapter.refreshData(listEpisodesDetails!!)
                }

            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_details_activity)

        extras = intent.extras
        this.initActionBar()

        listEpisodesDetails = ArrayList()
        adapter = RecyclerViewEpisodesAdapter(listEpisodesDetails!!, this)

            characterDetails = extras!!.getSerializable("personnage_details") as Character?

            this.setValuesToViews()

            listURLEpisodes = characterDetails!!.episode as MutableList<String>?
            lastLocationURL = characterDetails!!.location!!.url
            originURL = characterDetails!!.origin!!.url

            sharedPreferences = applicationContext.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)

            val locationsData = gson.fromJson<RawLocationsServerResponse>(
                sharedPreferences!!.getString("Locations_List", null),
                RawLocationsServerResponse::class.java
            )
            val episodesData = gson.fromJson<RawEpisodesServerResponse>(
                sharedPreferences!!.getString("Episodes_List", null),
                RawEpisodesServerResponse::class.java
            )


            if (episodesData != null) listEpisodes = episodesData.results as MutableList<Episode>?
            if (locationsData != null) listLocations = locationsData.results as MutableList<Location>
            if (lastLocationURL !== "") idLastLocation =
                    Integer.valueOf(lastLocationURL!!.split("/location/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            if (originURL !== "") idOrigin =
                    Integer.valueOf(originURL!!.split("/location/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])

            this.searchForLocations()

            if (listURLEpisodes != null) {

                val params = episodes_recyclerview.layoutParams

                if (listURLEpisodes!!.size <= 10) {
                    params.height = FrameLayout.LayoutParams.WRAP_CONTENT
                    params.width = FrameLayout.LayoutParams.MATCH_PARENT
                }

                episodes_recyclerview.layoutParams = params
                this.searchForEpisodes()

            }

            this.loadImage(characterDetails!!.image, character_img)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}