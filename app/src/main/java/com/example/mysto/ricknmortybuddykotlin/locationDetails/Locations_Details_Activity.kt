package com.example.mysto.ricknmortybuddykotlin.locationDetails

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
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
import retrofit2.Call
import retrofit2.Response

class Location_Details_Activity : AppCompatActivity() {

    internal var location_details: Location? = null

    @BindView(R.id.location_details_img_fullsize)
    internal var location_details_img_fullsize: ImageView? = null
    @BindView(R.id.location_details_name)
    internal var location_details_name: TextView? = null
    @BindView(R.id.location_details_dimension)
    internal var location_details_dimension: TextView? = null
    @BindView(R.id.location_details_type)
    internal var location_details_type: TextView? = null
    @BindView(R.id.location_details_recyclerview)
    internal var recyclerView: RecyclerView? = null
    @BindView(R.id.location_details_toolbar)
    internal var toolbar: Toolbar? = null

    internal var gson: Gson? = null
    internal var service: GetDataService? = null

    internal var adapter: RecyclerViewEpisodesCharactersAdapter? = null
    internal var listURLCharacters: List<String>? = null
    internal var listCharacters: MutableList<Character>? = null

    internal var extras: Bundle? = null
    internal var app: AppCompatActivity? = null

    init {
        gson = Gson()
        service = RetrofitClientInstance.retrofitInstance?.create(GetDataService::class.java)
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        actionbar!!.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent)
    }

    private fun setValuesToViews() {
        location_details_name!!.text = location_details?.name
        location_details_dimension!!.text = location_details?.dimension
        location_details_type!!.text = location_details?.type
    }

    private fun loadCharacters() {
        for (characterUrl in listURLCharacters!!) {
            val id = characterUrl.split("/character/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val call = service?.getPersonnageById(Integer.valueOf(id))

            call?.enqueue(object : retrofit2.Callback<Character> {
                override fun onResponse(call: Call<Character>, response: Response<Character>) {
                    response.body()?.let { listCharacters?.add(it) }
                    adapter?.refreshData(listCharacters)
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
                override fun onSuccess() {
                    supportStartPostponedEnterTransition()
                }
                override fun onError() {
                    Picasso.with(app)
                        .load(imgUrl)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.no_image)
                        .into(imgView, object : Callback {
                            override fun onSuccess() {
                                supportStartPostponedEnterTransition()
                            }
                            override fun onError() {
                                supportStartPostponedEnterTransition()
                            }
                        })
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)
        extras = intent.extras
        ButterKnife.bind(this)
        this.initActionBar()

        listCharacters = ArrayList()
        adapter = RecyclerViewEpisodesCharactersAdapter(listCharacters!!, this)
        recyclerView!!.layoutManager = GridLayoutManager(this, 5)
        recyclerView!!.adapter = adapter as RecyclerView.Adapter<*>

        if (extras != null) {
            location_details = extras!!.getSerializable("location_details") as Location
            this.setValuesToViews()
            listURLCharacters = location_details?.residents
            listCharacters = ArrayList()
            app = this
            this.loadCharacters()
            supportPostponeEnterTransition()
            this.loadImage(location_details?.image, location_details_img_fullsize)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}