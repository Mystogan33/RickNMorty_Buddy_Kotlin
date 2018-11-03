package com.example.mysto.ricknmortybuddykotlin.episodeDetails

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dailymotion.android.player.sdk.PlayerWebView
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
import retrofit2.Call
import retrofit2.Response

class Episode_Details_Activity : AppCompatActivity() {

    @BindView(R.id.episode_details_img_fullsize)
    internal var episode_details_img_fullsize: ImageView? = null
    @BindView(R.id.episode_details_img)
    internal var episode_details_img: ImageView? = null

    @BindView(R.id.webView)
    internal var webView: PlayerWebView? = null

    @BindView(R.id.episode_details_season)
    internal var episode_details_season: TextView? = null
    @BindView(R.id.episode_details_name)
    internal var episode_details_name: TextView? = null
    @BindView(R.id.episode_details_air_date)
    internal var episode_details_air_date: TextView? = null
    @BindView(R.id.episode_details_description)
    internal var episode_details_description: TextView? = null

    @BindView(R.id.episode_details_toolbar)
    internal var toolbar: Toolbar? = null

    @BindView(R.id.episode_details_recyclerview)
    internal var recyclerView: RecyclerView? = null

    internal var episode_details: Episode? = null

    internal var adapter: RecyclerViewEpisodesCharactersAdapter? = null
    internal var listURLCharacters: List<String> = ArrayList()
    internal var listCharacters: MutableList<Character> = ArrayList()

    internal var extras: Bundle? = null

    internal var service: GetDataService? = null
    internal var gson: Gson? = null

    internal var app: AppCompatActivity? = null

    init {
        gson = Gson()
        service = RetrofitClientInstance.retrofitInstance?.create(GetDataService::class.java)
    }

    fun initActionBar() {

        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        actionbar!!.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent)

    }

    private fun setValuesToViews() {
        episode_details_season!!.text = episode_details?.episode
        episode_details_name!!.text = episode_details?.name
        episode_details_air_date!!.text = episode_details?.airDate
        episode_details_description!!.text = episode_details?.description
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
                override fun onSuccess() {
                    supportStartPostponedEnterTransition()
                }
                override fun onError() {
                    Picasso.with(app)
                        .load(imgUrl)
                        .fit()
                        .noFade()
                        .centerCrop()
                        .error(R.drawable.no_data)
                        .into(imgView, object : Callback {
                            override fun onSuccess() {
                                supportStartPostponedEnterTransition()
                            }
                            override fun onError() {}
                        })
                }
            })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode_details)

        extras = intent.extras

        ButterKnife.bind(this)
        this.initActionBar()

        listCharacters = ArrayList()
        adapter = RecyclerViewEpisodesCharactersAdapter(listCharacters, this)
        recyclerView!!.layoutManager = GridLayoutManager(this, 5)
        recyclerView!!.adapter = adapter as RecyclerView.Adapter<*>

        webView!!.load("x62qgh0")

        if (extras != null) {

            episode_details = extras!!.getSerializable("episode_details") as Episode

            this.setValuesToViews()

            listURLCharacters = episode_details!!.characters!!

            app = this

            this.loadCharacters()

            supportPostponeEnterTransition()

            this.loadImage(episode_details!!.image!!, episode_details_img_fullsize)
            this.loadImage(episode_details!!.image!!, episode_details_img)

        }

    }

    override fun onPause() {
        super.onPause()
        webView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView!!.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}