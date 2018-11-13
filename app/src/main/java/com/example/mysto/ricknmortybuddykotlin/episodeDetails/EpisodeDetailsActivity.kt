package com.example.mysto.ricknmortybuddykotlin.episodeDetails

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.Episode
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.adapters.RecyclerViewEpisodesCharactersAdapter
import com.example.mysto.ricknmortybuddykotlin.network.RickNMortyAPI.GetDataService
import com.example.mysto.ricknmortybuddykotlin.network.RickNMortyAPI.RetrofitClientInstance
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.episode_details_activity.*
import retrofit2.Call
import retrofit2.Response

class EpisodeDetailsActivity : AppCompatActivity() {

    private var episodeDetails: Episode? = null

    private var adapter: RecyclerViewEpisodesCharactersAdapter? = null
    private var listURLCharacters: List<String> = ArrayList()
    private var listCharacters: MutableList<Character> = ArrayList()

    private var extras: Bundle? = null

    private var service: GetDataService? = null
    private var gson: Gson? = null

    private var app: AppCompatActivity? = null

    init {
        gson = Gson()
        service = RetrofitClientInstance.retrofitInstance?.create(GetDataService::class.java)
    }

    private fun initActionBar() {
        setSupportActionBar(episode_details_toolbar)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
        actionbar?.setDisplayShowTitleEnabled(false)
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent)
    }

    private fun setValuesToViews() {
        episode_details_season.text = episodeDetails?.episode
        episode_details_name.text = episodeDetails?.name
        episode_details_air_date.text = episodeDetails?.airDate
        episode_details_description.text = episodeDetails?.description
    }

    private fun loadCharacters() {
        for (characterUrl in listURLCharacters) {

            val id = characterUrl.split("/character/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val call = service?.getPersonnageById(Integer.valueOf(id))

            call?.enqueue(object : retrofit2.Callback<Character> {
                override fun onResponse(call: Call<Character>, response: Response<Character>) {
                    response.body()?.let { listCharacters.add(it) }
                    adapter?.refreshData(listCharacters)
                }
                override fun onFailure(call: Call<Character>, t: Throwable) {}
            })

        }
    }

    private fun loadImage(imgUrl: String, imgView: ImageView?) {
        Picasso.with(app)
            .load(imgUrl)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .fit()
            .noFade()
            .centerCrop()
            .error(R.drawable.no_data)
            .into(imgView!!, object : Callback {
                override fun onSuccess() { supportStartPostponedEnterTransition() }
                override fun onError() {
                    Picasso.with(app)
                        .load(imgUrl)
                        .fit()
                        .noFade()
                        .centerCrop()
                        .error(R.drawable.no_data)
                        .into(imgView, object : Callback {
                            override fun onSuccess() { supportStartPostponedEnterTransition() }
                            override fun onError() {}
                        })
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.episode_details_activity)

        extras = intent.extras

        this.initActionBar()

        listCharacters = ArrayList()
        adapter = RecyclerViewEpisodesCharactersAdapter(listCharacters, this)
        episode_details_recyclerview.layoutManager = GridLayoutManager(this, 5)
        episode_details_recyclerview.adapter = adapter

        webView.load("x62qgh0")
        webView.pause()

        if (extras != null) {

            episodeDetails = extras!!.getSerializable("episode_details") as Episode

            this.setValuesToViews()

            listURLCharacters = episodeDetails?.characters!!

            app = this

            this.loadCharacters()
            supportPostponeEnterTransition()

            this.loadImage(episodeDetails?.image!!, episode_details_img_fullsize)
            this.loadImage(episodeDetails?.image!!, episode_details_img)
        }

    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}