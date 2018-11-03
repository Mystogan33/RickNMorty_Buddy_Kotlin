package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import com.google.gson.Gson
import android.widget.ImageButton
import butterknife.BindView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import android.content.SharedPreferences
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.adapter.RecyclerViewAdapter
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.RawCharactersServerResponse
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.interfaces.Refreshable
import com.example.mysto.ricknmortybuddykotlin.network.RickNMortyAPI.GetDataService
import com.example.mysto.ricknmortybuddykotlin.network.RickNMortyAPI.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Characters_Fragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, Refreshable {

    internal var view: View? = null
    internal var rawPersonnagesResponse: RawCharactersServerResponse? = null
    internal var listPersonnages: MutableList<Character>? = null
    internal var gson: Gson = Gson()
    internal var service: GetDataService = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
    internal var sharedPreferences: SharedPreferences? = null
    internal var adapter: RecyclerViewAdapter? = null

    @BindView(R.id.personnagesRecyclerView)
    lateinit var rv_personnages: RecyclerView
    @BindView(R.id.swipe_container)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.searchViewQuery)
    lateinit var searchViewCharacter: SearchView
    @BindView(R.id.imageViewSearchMenu)
    lateinit var imageButton: ImageButton

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the view
        view = inflater.inflate(R.layout.characters_fragment, container, false)
        ButterKnife.bind(this, view!!)

        listPersonnages = ArrayList()
        adapter = RecyclerViewAdapter(this,listPersonnages)
        rv_personnages.layoutManager = GridLayoutManager(view!!.context, 2)
        rv_personnages.adapter = adapter

        sharedPreferences = view!!.context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)

        // Refresh Layout
        mSwipeRefreshLayout.setOnRefreshListener(this)

        // SearchView
        searchViewCharacter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                if (!searchViewCharacter.isIconified) {
                    searchViewCharacter.isIconified = true
                }
                return false
            }


            override fun onQueryTextChange(userInput: String): Boolean {
                val filterCharacterList = filter(rawPersonnagesResponse?.results, userInput)
                adapter!!.setFilter(filterCharacterList)
                return true
            }
        })

        // Sequences of colors from the loading circle
        mSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        // Background color for the loading
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark)

        // Get local Data for Character if exist
        val json = sharedPreferences!!.getString("Characters_List", null)

        if (json != null) {
            rawPersonnagesResponse = gson.fromJson<RawCharactersServerResponse>(json, RawCharactersServerResponse::class.java)
            listPersonnages = rawPersonnagesResponse!!.results as MutableList<Character>?

            listPersonnages!!.sortWith(Comparator { character2, character1 -> character2!!.name!!.compareTo(character1!!.name!!) })

            adapter!!.setFilter(listPersonnages!!)

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

        val call = service.getAllPersonnagesFromPage(1)

        call.enqueue(object : Callback<RawCharactersServerResponse> {
            override fun onResponse(call: Call<RawCharactersServerResponse>, response: Response<RawCharactersServerResponse>) {

                rawPersonnagesResponse = response.body()
                listPersonnages = rawPersonnagesResponse!!.results as MutableList<Character>?

                val numberOfPages = rawPersonnagesResponse!!.info!!.pages

                listPersonnages!!.sortWith(Comparator { character2, character1 -> character2!!.name!!.compareTo(character1!!.name!!) })


                adapter!!.setFilter(listPersonnages!!)

                Toast.makeText(view!!.context, "Vos données sont désormais sauvegardées", Toast.LENGTH_SHORT).show()

                if (numberOfPages!! > 1) {

                    for (i in 2..numberOfPages) {

                        val additionalCall = service.getAllPersonnagesFromPage(i)

                        additionalCall.enqueue(object : Callback<RawCharactersServerResponse> {
                            override fun onResponse(
                                call: Call<RawCharactersServerResponse>,
                                response: Response<RawCharactersServerResponse>
                            ) {

                                listPersonnages!!.addAll(response.body()!!.results as List<Character>)

                                listPersonnages!!.sortWith(Comparator { character2, character1 -> character2!!.name!!.compareTo(character1!!.name!!) })

                                adapter!!.setFilter(listPersonnages!!)
                                rawPersonnagesResponse!!.results = listPersonnages

                                sharedPreferences!!.edit()
                                    .putString("Characters_List", gson.toJson(rawPersonnagesResponse))
                                    .apply()

                            }

                            override fun onFailure(call: Call<RawCharactersServerResponse>, t: Throwable) {
                                Toast.makeText(
                                    view!!.context,
                                    "Un soucis s'est produit lors de la récupération du reste des personnages",
                                    Toast.LENGTH_LONG
                                ).show()
                                mSwipeRefreshLayout.isRefreshing = false
                            }
                        })

                    }

                } else {

                    listPersonnages!!.sortWith(Comparator { character2, character1 -> character2!!.name!!.compareTo(character1!!.name!!) })

                    sharedPreferences!!.edit()
                        .putString("Characters_List", gson.toJson(rawPersonnagesResponse))
                        .apply()
                }

                mSwipeRefreshLayout.isRefreshing = false

            }

            override fun onFailure(call: Call<RawCharactersServerResponse>, t: Throwable) {
                Toast.makeText(
                    view!!.context,
                    "Impossible de joindre le serveur, réessayer ultérieurement",
                    Toast.LENGTH_SHORT
                ).show()
                mSwipeRefreshLayout.isRefreshing = false
            }
        })

    }

    private fun filter(pl: List<Character>?, query: String): List<Character> {

        val filter = query.toLowerCase()

        val filteredList = arrayListOf<Character>()

        for (model in pl!!) {
            val name = model.name!!.toLowerCase()
            val status = model.status!!.toLowerCase()
            val gender = model.gender!!.toLowerCase()
            val origin = model.origin!!.name!!.toLowerCase()
            val last_location = model.location!!.name!!.toLowerCase()

            if (name.contains(filter) || status.contains(filter) || gender == filter || origin.contains(filter) || last_location.contains(filter)) {
                filteredList.add(model)
            }
        }

        return filteredList
    }

    override fun onRefresh() {
        loadRecyclerViewData()
    }

}