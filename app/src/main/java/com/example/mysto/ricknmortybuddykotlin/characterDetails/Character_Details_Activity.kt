package com.example.mysto.ricknmortybuddykotlin.characterDetails

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.Episode
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.RawEpisodesServerResponse
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.Location
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.RawLocationsServerResponse
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.adapters.RecyclerViewEpisodesAdapter
import com.example.mysto.ricknmortybuddykotlin.locationDetails.Location_Details_Activity
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

import java.util.ArrayList


class Character_Details_Activity : AppCompatActivity() {

    @BindView(R.id.personnage_details__name)
    internal var personnage_details_name: TextView? = null
    @BindView(R.id.personnage_details_status)
    internal var personnage_details_status: TextView? = null
    @BindView(R.id.personnage_details_species)
    internal var personnage_details_species: TextView? = null
    @BindView(R.id.personnage_details_gender)
    internal var personnage_details_gender: TextView? = null
    @BindView(R.id.personnage_details_origin)
    internal var personnage_details_origin: TextView? = null
    @BindView(R.id.personnage_details_last_location)
    internal var personnage_details_last_location: TextView? = null
    @BindView(R.id.personnage_details_img)
    internal var personnage_details_img: ImageView? = null
    @BindView(R.id.origin_img)
    internal var origin_img: ImageView? = null
    @BindView(R.id.last_location_img)
    internal var last_location_img: ImageView? = null
    @BindView(R.id.relayOrigin)
    internal var personnage_details_relay_origin: RelativeLayout? = null
    @BindView(R.id.relayLastLocation)
    internal var personnage_details_relay_last_location: RelativeLayout? = null
    @BindView(R.id.episodes_recyclerview_relay)
    internal var episodes_recyclerview_relay: RelativeLayout? = null
    @BindView(R.id.relayStatus)
    internal var personnage_details_relay_status: LinearLayout? = null
    @BindView(R.id.episodes_recyclerview)
    internal var recyclerView: RecyclerView? = null
    @BindView(R.id.cardview_episodes_of_character)
    internal var cardView: CardView? = null
    @BindView(R.id.toolbar)
    internal var toolbar: Toolbar? = null

    internal var adapter: RecyclerViewEpisodesAdapter? = null

    internal var gson: Gson
    internal var sharedPreferences: SharedPreferences? = null

    internal var personnage_details: Character? = null

    internal var listURLEpisodes: MutableList<String>? = arrayListOf()
    internal var listEpisodes: MutableList<Episode>? = arrayListOf()
    internal var listEpisodesDetails: MutableList<Episode>? = arrayListOf()

    internal var lastLocationURL: String? = ""
    internal var lastLocationData: Location? = null
    internal var idLastLocation: Int? = null
    internal var originURL: String? = ""
    internal var listLocations: MutableList<Location> = arrayListOf()
    internal var originData: Location? = null
    internal var idOrigin: Int? = null

    internal var extras: Bundle? = null

    init {
        gson = Gson()
    }

    fun setValuesToViews() {
        personnage_details_name!!.text = personnage_details!!.name

        val status = personnage_details!!.status

        when {
            status?.toLowerCase() == "dead" -> {
                personnage_details_status!!.text = resources.getString(R.string.status_dead)
                personnage_details_relay_status!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDanger))
            }
            status?.toLowerCase() == "alive" -> personnage_details_status!!.text = resources.getString(R.string.status_alive)
            else -> {
                personnage_details_status!!.text = resources.getString(R.string.status_unknown)
                personnage_details_relay_status!!.setBackgroundColor(ContextCompat.getColor(this, R.color.followersBg))
            }
        }

        personnage_details_species!!.text = personnage_details!!.species
        personnage_details_gender!!.text = personnage_details!!.gender

        val gender = personnage_details!!.gender

        when {
            gender?.toLowerCase() == "male" -> personnage_details_gender!!.text = resources.getString(R.string.gender_male)
            gender?.toLowerCase() == "female" -> personnage_details_gender!!.text = resources.getString(R.string.gender_female)
            else -> personnage_details_gender!!.text = resources.getString(R.string.gender_unknown)
        }

        personnage_details_last_location!!.text = personnage_details!!.location!!.name
        personnage_details_origin!!.text = personnage_details!!.origin!!.name
    }

    fun initActionBar() {

        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        actionbar!!.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent)

    }

    fun loadImage(imgUrl: String?, imgView: ImageView?) {

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
                personnage_details_last_location!!.text = lastLocationData?.name

                this.loadImage(lastLocationData!!.image, last_location_img)

                personnage_details_relay_last_location!!.setOnClickListener {
                    val intent = Intent(applicationContext, Location_Details_Activity::class.java)
                    intent.putExtra("location_details", lastLocationData)
                    startActivity(intent)
                }

            }

            if (location.id!! == idOrigin) {
                originData = location
                personnage_details_origin!!.text = originData!!.name

                this.loadImage(originData!!.image, origin_img)

                personnage_details_relay_origin!!.setOnClickListener {
                    val intent = Intent(applicationContext, Location_Details_Activity::class.java)
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
                    adapter?.refreshData(listEpisodesDetails!!)
                }

            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        extras = intent.extras

        ButterKnife.bind(this)
        this.initActionBar()

        listEpisodesDetails = ArrayList()
        adapter = RecyclerViewEpisodesAdapter(listEpisodesDetails!!, this)
        recyclerView!!.layoutManager = GridLayoutManager(this, 5)
        recyclerView!!.adapter = adapter as RecyclerView.Adapter<*>

        if (extras != null) {
            personnage_details = extras!!.getSerializable("personnage_details") as Character?

            this.setValuesToViews()

            listURLEpisodes = personnage_details!!.episode as MutableList<String>?
            lastLocationURL = personnage_details!!.location!!.url
            originURL = personnage_details!!.origin!!.url

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

                val params = episodes_recyclerview_relay!!.layoutParams as FrameLayout.LayoutParams

                if (listURLEpisodes!!.size <= 10) {
                    params.height = FrameLayout.LayoutParams.WRAP_CONTENT
                    params.width = FrameLayout.LayoutParams.MATCH_PARENT
                }

                episodes_recyclerview_relay!!.layoutParams = params

                this.searchForEpisodes()

            }

            this.loadImage(personnage_details!!.image, personnage_details_img)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}