package com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes

import android.content.Context
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.Episode
import android.widget.Toast
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.RawEpisodesServerResponse
import androidx.recyclerview.widget.GridLayoutManager
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.adapter.RecyclerViewAdapter
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.interfaces.Refreshable
import com.example.mysto.ricknmortybuddykotlin.network.JsonBin.GetDataService
import com.example.mysto.ricknmortybuddykotlin.network.JsonBin.RetrofitClientInstance
import kotlinx.android.synthetic.main.episodes_fragment.*
import kotlinx.android.synthetic.main.episodes_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EpisodesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, Refreshable {

    private var root: View? = null
    internal var listEpisodes: RawEpisodesServerResponse? = null
    internal var gson: Gson = Gson()
    private var service: GetDataService = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
    internal var adapter: RecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.episodes_fragment, container, false)

        root!!.episodesFragmentSwipeRefreshLayout.setOnRefreshListener(this)
        root!!.episodesFragmentSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        root!!.episodesFragmentSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark)

        root!!.episodesFragmentSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                if (!root!!.episodesFragmentSearchView.isIconified) root!!.episodesFragmentSearchView.isIconified = true
                return false
            }
            override fun onQueryTextChange(userInput: String): Boolean {
                val filterEpisodesList = setFilter(listEpisodes!!.results!!, userInput)
                adapter!!.setFilter(filterEpisodesList)
                root!!.episodesFragmentNumberOfItems.text = adapter!!.itemCount.toString()
                return true
            }
        })

        root!!.episodesFragmentSearchViewMenu.setOnClickListener {
            Toast.makeText(this.context, "Menu is not implemented yet", Toast.LENGTH_LONG).show()
        }

        val sharedPreferences = root!!.context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("Episodes_List", null)

        adapter = RecyclerViewAdapter(this, ArrayList())
        root!!.episodesFragmentRecyclerView.layoutManager = GridLayoutManager(root!!.context, 3)
        root!!.episodesFragmentRecyclerView.adapter = adapter!!

        if (json != null) {
            listEpisodes = gson.fromJson<RawEpisodesServerResponse>(json, RawEpisodesServerResponse::class.java)
            adapter!!.setFilter(listEpisodes!!.results!!)
            root!!.episodesFragmentNumberOfItems.text = adapter!!.itemCount.toString()
        } else {
            root!!.episodesFragmentSwipeRefreshLayout.post {
                root!!.episodesFragmentSwipeRefreshLayout.isRefreshing = true
                loadRecyclerViewData()
            }
        }

        return root
    }

    override fun loadRecyclerViewData() {
        root!!.episodesFragmentSwipeRefreshLayout.isRefreshing = true
        val call = service.allEpisodes

        call.enqueue(object : Callback<RawEpisodesServerResponse> {
            override fun onResponse(call: Call<RawEpisodesServerResponse>, response: Response<RawEpisodesServerResponse>) {
                listEpisodes = response.body()!!
                val sharedPreferences = root!!.context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString("Episodes_List", gson.toJson(listEpisodes))
                    .apply()
                adapter!!.setFilter(listEpisodes!!.results!!)
                root!!.episodesFragmentNumberOfItems.text = adapter!!.itemCount.toString()
                root!!.episodesFragmentSwipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<RawEpisodesServerResponse>, t: Throwable) {
                Toast.makeText(
                    root!!.context,
                    "Impossible de joindre le serveur, réessayer ultérieurement",
                    Toast.LENGTH_SHORT
                ).show()
                root!!.episodesFragmentSwipeRefreshLayout.isRefreshing = false
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