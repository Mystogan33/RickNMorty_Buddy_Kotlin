package com.example.mysto.ricknmortybuddykotlin.locationDetails

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.Location
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.adapters.RecyclerViewEpisodesCharactersAdapter
import com.example.mysto.ricknmortybuddykotlin.network.RickNMortyAPI.GetDataService
import com.example.mysto.ricknmortybuddykotlin.network.RickNMortyAPI.RetrofitClientInstance
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.location_details_activity.*
import retrofit2.Call
import retrofit2.Response

class LocationDetailsActivity : AppCompatActivity() {

    lateinit var locationDetails: Location
    private var service: GetDataService? = RetrofitClientInstance.retrofitInstance?.create(GetDataService::class.java)

    lateinit var adapter: RecyclerViewEpisodesCharactersAdapter
    var listURLCharacters: List<String> = ArrayList()
    var listCharacters: MutableList<Character> = ArrayList()

    private var extras: Bundle? = null
    private var app: AppCompatActivity = this

    private fun initActionBar() {
        setSupportActionBar(location_details_toolbar)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
        actionbar?.setDisplayShowTitleEnabled(false)
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent)
    }

    private fun setValuesToViews() {
        location_details_name.text = locationDetails.name
        location_details_dimension.text = locationDetails.dimension
        location_details_type.text = locationDetails.type
    }

    private fun loadCharacters() {
        for (characterUrl in listURLCharacters) {
            val id = characterUrl.split("/character/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val call = service?.getPersonnageById(Integer.valueOf(id))

            call?.enqueue(object : retrofit2.Callback<Character> {
                override fun onResponse(call: Call<Character>, response: Response<Character>) {
                    response.body()?.let { listCharacters.add(it) }
                    adapter.refreshData(listCharacters!!.toList())
                }
                override fun onFailure(call: Call<Character>, t: Throwable) {}
            })
        }
    }

    private fun loadImage(imgUrl: String?, imgView: ImageView?) {
        Picasso.with(app)
            .load(imgUrl)
            .fit()
            .centerCrop()
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.no_image)
            .into(imgView, object : Callback {
                override fun onSuccess() { supportStartPostponedEnterTransition() }
                override fun onError() {
                    Picasso.with(app)
                        .load(imgUrl)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.no_image)
                        .into(imgView, object : Callback {
                            override fun onSuccess() { supportStartPostponedEnterTransition() }
                            override fun onError() { supportStartPostponedEnterTransition() }
                        })
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_details_activity)
        extras = intent.extras
        initActionBar()

        adapter = RecyclerViewEpisodesCharactersAdapter(listCharacters, this)
        location_details_recyclerview.layoutManager = GridLayoutManager(this, 5)
        location_details_recyclerview.adapter = adapter

        locationDetails = extras!!.getSerializable("location_details") as Location
        setValuesToViews()
        listURLCharacters = locationDetails.residents!!
        loadCharacters()
        supportPostponeEnterTransition()
        loadImage(locationDetails.image, location_details_img_fullsize)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}