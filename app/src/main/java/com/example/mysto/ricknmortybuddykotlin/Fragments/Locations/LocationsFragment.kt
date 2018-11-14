package com.example.mysto.ricknmortybuddykotlin.Fragments.Locations

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.adapter.RecyclerViewAdapter
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.Location
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.RawLocationsServerResponse
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.interfaces.Refreshable
import com.example.mysto.ricknmortybuddykotlin.network.JsonBin.GetDataService
import com.example.mysto.ricknmortybuddykotlin.network.JsonBin.RetrofitClientInstance
import com.google.gson.Gson
import kotlinx.android.synthetic.main.locations_fragment.*
import kotlinx.android.synthetic.main.locations_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class LocationsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, Refreshable {

    private var root: View? = null
    internal var rawLocationsResponse: RawLocationsServerResponse? = null
    internal var listLocations: MutableList<Location>? = null
    internal var gson: Gson = Gson()
    internal var adapter: RecyclerViewAdapter? = null
    internal var sharedPreferences: SharedPreferences? = null
    private var service: GetDataService = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.locations_fragment, container, false)

        listLocations = ArrayList()
        adapter = RecyclerViewAdapter(this, listLocations)
        root!!.locationsFragmentRecyclerView.layoutManager = LinearLayoutManager(root!!.context)
        root!!.locationsFragmentRecyclerView.adapter = adapter

        sharedPreferences = root!!.context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)

        // Refresh Layout
        root!!.locationsFragmentSwipeRefreshLayout.setOnRefreshListener(this)
        root!!.locationsFragmentSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        root!!.locationsFragmentSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark)

        root!!.locationsFragmentSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(userInput: String): Boolean {
                if (!root!!.locationsFragmentSearchView.isIconified) root!!.locationsFragmentSearchView.isIconified = true
                return false
            }
            override fun onQueryTextChange(userInput: String): Boolean {
                val filterLocationsList = setFilter(listLocations!!, userInput)
                adapter!!.setFilter(filterLocationsList)
                root!!.locationsFragmentNumberOfItems.text = adapter!!.itemCount.toString()
                return true
            }
        })

        root!!.locationsFragmentSearchViewMenu.setOnClickListener {
            Toast.makeText(this.context, "Menu is not implemented yet", Toast.LENGTH_LONG).show()
        }

        val json = sharedPreferences!!.getString("Locations_List", null)

        if (json != null) {

            rawLocationsResponse = gson.fromJson(json, RawLocationsServerResponse::class.java)
            listLocations = rawLocationsResponse!!.results as MutableList<Location>?

            listLocations!!.sortWith(Comparator { loc2, loc1 -> loc2.name!!.compareTo(loc1!!.name!!) })

            adapter!!.setFilter(listLocations!!)
            root!!.locationsFragmentNumberOfItems.text = adapter!!.itemCount.toString()

        } else {
            root!!.locationsFragmentSwipeRefreshLayout.post { loadRecyclerViewData() }
        }

        return root
    }

    override fun loadRecyclerViewData() {

        root!!.locationsFragmentSwipeRefreshLayout.isRefreshing = true

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
                root!!.locationsFragmentNumberOfItems.text = adapter!!.itemCount.toString()
                sharedPreferences!!.edit()
                    .putString("Locations_List", gson.toJson(rawLocationsResponse))
                    .apply()

                Toast.makeText(root!!.context, "Vos données sont désormais sauvegardées", Toast.LENGTH_SHORT).show()

                root!!.locationsFragmentSwipeRefreshLayout.isRefreshing = false
            }
            override fun onFailure(call: Call<RawLocationsServerResponse>, t: Throwable) {
                Toast.makeText(
                    root!!.context,
                    "Impossible de joindre le serveur, réessayer ultérieurement",
                    Toast.LENGTH_SHORT
                ).show()
                root!!.locationsFragmentSwipeRefreshLayout.isRefreshing = false
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