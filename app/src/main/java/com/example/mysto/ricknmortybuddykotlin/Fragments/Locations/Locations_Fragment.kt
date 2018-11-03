package com.example.mysto.ricknmortybuddykotlin.Fragments.Locations

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.adapter.RecyclerViewAdapter
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.Location
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.RawLocationsServerResponse
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.R.id.locationsRecyclerView
import com.example.mysto.ricknmortybuddykotlin.interfaces.Refreshable
import com.example.mysto.ricknmortybuddykotlin.network.JsonBin.GetDataService
import com.example.mysto.ricknmortybuddykotlin.network.JsonBin.RetrofitClientInstance
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Locations_Fragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, Refreshable {

    internal var view: View? = null
    @BindView(locationsRecyclerView)
    lateinit var rv_locations: RecyclerView
    @BindView(R.id.swipe_container)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.searchViewQuery)
    lateinit var searchViewLocations: SearchView

    internal var rawLocationsResponse: RawLocationsServerResponse? = null
    internal var listLocations: MutableList<Location>? = null
    internal var gson: Gson = Gson()
    internal var adapter: RecyclerViewAdapter? = null
    internal var sharedPreferences: SharedPreferences? = null
    private var service: GetDataService = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        view = inflater.inflate(R.layout.locations_fragment, container, false)
        ButterKnife.bind(this, view!!)

        listLocations = ArrayList()
        adapter = RecyclerViewAdapter(this, listLocations)
        rv_locations.layoutManager = LinearLayoutManager(view!!.context)
        rv_locations.adapter = adapter

        sharedPreferences = view!!.context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)

        // Refresh Layout
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark)

        searchViewLocations.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(userInput: String): Boolean {
                if (!searchViewLocations.isIconified) searchViewLocations.isIconified = true
                return false
            }

            override fun onQueryTextChange(userInput: String): Boolean {
                val filterLocationsList = setFilter(listLocations!!, userInput)
                adapter!!.setFilter(filterLocationsList)
                return true
            }
        })

        val json = sharedPreferences!!.getString("Locations_List", null)

        if (json != null) {

            rawLocationsResponse = gson.fromJson(json, RawLocationsServerResponse::class.java)
            listLocations = rawLocationsResponse!!.results as MutableList<Location>?

            listLocations!!.sortWith(Comparator { loc2, loc1 -> loc2.name!!.compareTo(loc1!!.name!!) })

            adapter!!.setFilter(listLocations!!)

        } else {
            mSwipeRefreshLayout.post { loadRecyclerViewData() }
        }

        return view
    }

    override fun loadRecyclerViewData() {

        mSwipeRefreshLayout.isRefreshing = true

        val call = service.allLocations
        call.enqueue(object : Callback<RawLocationsServerResponse> {
            override fun onResponse(call: Call<RawLocationsServerResponse>, response: Response<RawLocationsServerResponse>) {
                rawLocationsResponse = response.body()
                listLocations = ArrayList()

                for (loc in rawLocationsResponse!!.results!!) {
                    val image = loc.image
                    if (image == null || image == "unknown") loc.image = ("https://i.redd.it/lwwt86ci5anz.jpg")
                    listLocations!!.add(loc)
                }

                listLocations!!.sortWith(Comparator { loc2, loc1 -> loc2.name!!.compareTo(loc1!!.name!!) })

                adapter!!.setFilter(listLocations!!)
                sharedPreferences!!.edit()
                    .putString("Locations_List", gson.toJson(rawLocationsResponse))
                    .apply()

                Toast.makeText(view!!.context, "Vos données sont désormais sauvegardées", Toast.LENGTH_SHORT).show()

                mSwipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<RawLocationsServerResponse>, t: Throwable) {
                Toast.makeText(
                    view!!.context,
                    "Impossible de joindre le serveur, réessayer ultérieurement",
                    Toast.LENGTH_SHORT
                ).show()
                mSwipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun setFilter(pl: List<Location>, query: String): List<Location> {
        val filter = query.toLowerCase()
        val filteredList = arrayListOf<Location>()

        for (model in pl) {
            val name = model.name!!.toLowerCase()
            val type = model.type!!.toLowerCase()
            val dimension = model.dimension!!.toLowerCase()
            if (name.contains(filter) || type.contains(filter) || dimension.contains(filter)) filteredList.add(model)
        }

        return filteredList
    }

    override fun onRefresh() {
        loadRecyclerViewData()
    }
}