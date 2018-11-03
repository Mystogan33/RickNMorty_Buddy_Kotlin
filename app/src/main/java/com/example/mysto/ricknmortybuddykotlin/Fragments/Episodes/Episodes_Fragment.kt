package com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes

import android.content.Context
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.Episode
import android.widget.Toast
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.RawEpisodesServerResponse
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import butterknife.BindView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.adapter.RecyclerViewAdapter
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.interfaces.Refreshable
import com.example.mysto.ricknmortybuddykotlin.network.JsonBin.GetDataService
import com.example.mysto.ricknmortybuddykotlin.network.JsonBin.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Episodes_Fragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, Refreshable {

    internal var view: View? = null
    internal var listEpisodes: RawEpisodesServerResponse? = null
    internal var gson: Gson = Gson()
    internal var service: GetDataService = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
    internal var adapter: RecyclerViewAdapter? = null

    @BindView(R.id.episodesRecyclerView)
    lateinit var rv_episodes: RecyclerView
    @BindView(R.id.swipe_container)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.searchViewQuery)
    lateinit var searchViewEpisodes: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        view = inflater.inflate(R.layout.episodes_fragment, container, false)
        ButterKnife.bind(this, view!!)

        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark)

        searchViewEpisodes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                if (!searchViewEpisodes.isIconified) searchViewEpisodes.isIconified = true
                return false
            }
            override fun onQueryTextChange(userInput: String): Boolean {
                val filterEpisodesList = setFilter(listEpisodes!!.results!!, userInput)
                adapter!!.setFilter(filterEpisodesList)
                return true
            }
        })

        val sharedPreferences = view!!.context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("Episodes_List", null)

        adapter = RecyclerViewAdapter(this, ArrayList())
        rv_episodes.layoutManager = GridLayoutManager(view!!.context, 3)
        rv_episodes.adapter = adapter!!

        if (json != null) {
            listEpisodes = gson.fromJson<RawEpisodesServerResponse>(json, RawEpisodesServerResponse::class.java)
            adapter!!.setFilter(listEpisodes!!.results!!)
        } else {
            mSwipeRefreshLayout.post {
                mSwipeRefreshLayout.isRefreshing = true
                loadRecyclerViewData()
            }
        }

        return view
    }

    override fun loadRecyclerViewData() {
        mSwipeRefreshLayout.isRefreshing = true
        val call = service.allEpisodes

        call.enqueue(object : Callback<RawEpisodesServerResponse> {
            override fun onResponse(call: Call<RawEpisodesServerResponse>, response: Response<RawEpisodesServerResponse>) {
                listEpisodes = response.body()!!
                val sharedPreferences = view!!.context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString("Episodes_List", gson.toJson(listEpisodes))
                    .apply()
                adapter!!.setFilter(listEpisodes!!.results!!)
                mSwipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<RawEpisodesServerResponse>, t: Throwable) {
                Toast.makeText(
                    view!!.context,
                    "Impossible de joindre le serveur, réessayer ultérieurement",
                    Toast.LENGTH_SHORT
                ).show()
                mSwipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun setFilter(pl: List<Episode>, query: String): List<Episode> {

        val filter = query.toLowerCase()

        val filteredList = arrayListOf<Episode>()

        for (model in pl) {
            val name = model.name!!.toLowerCase()
            val episode = model.episode!!.toLowerCase()
            if (name.contains(filter) || episode.contains(filter)) filteredList.add(model)
        }

        return filteredList
    }

    override fun onRefresh() {
        loadRecyclerViewData()
    }
}