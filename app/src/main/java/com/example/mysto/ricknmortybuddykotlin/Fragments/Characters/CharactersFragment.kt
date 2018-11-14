package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.adapter.RecyclerViewAdapter
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.RawCharactersServerResponse
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.interfaces.Refreshable
import com.example.mysto.ricknmortybuddykotlin.network.RickNMortyAPI.GetDataService
import com.example.mysto.ricknmortybuddykotlin.network.RickNMortyAPI.RetrofitClientInstance
import com.google.gson.Gson
import kotlinx.android.synthetic.main.characters_fragment.*
import kotlinx.android.synthetic.main.characters_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CharactersFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, Refreshable {

    private var root: View? = null
    internal var rawCharactersServerResponse: RawCharactersServerResponse? = null
    internal var charactersList: MutableList<Character>? = null
    internal var gson: Gson = Gson()
    internal var service: GetDataService = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
    internal var sharedPreferences: SharedPreferences? = null
    internal var adapter: RecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the view
        root = inflater.inflate(R.layout.characters_fragment, container, false)

        charactersList = ArrayList()
        adapter = RecyclerViewAdapter(this,charactersList)
        root!!.charactersFragmentRecyclerView.layoutManager = GridLayoutManager(this.context , 2)
        root!!.charactersFragmentRecyclerView.adapter = adapter

        sharedPreferences = root!!.context.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)


        // Refresh Layout
        root!!.charactersFragmentSwipe_container.setOnRefreshListener(this)

        // SearchView
        root!!.charactersFragmentSearchViewQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                if (!charactersFragmentSearchViewQuery.isIconified) { charactersFragmentSearchViewQuery.isIconified = true }
                return false
            }
            override fun onQueryTextChange(userInput: String): Boolean {
                val filterCharacterList = filter(rawCharactersServerResponse?.results, userInput)
                adapter!!.setFilter(filterCharacterList)
                root!!.charactersFragmentNumberOfItems.text = adapter!!.itemCount.toString()
                return true
            }
        })

        // Sequences of colors from the loading circle
        root!!.charactersFragmentSwipe_container.setColorSchemeResources(
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        // Background color for the loading
        root!!.charactersFragmentSwipe_container.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark)

        // Get local Data for Character if exist
        val json = sharedPreferences!!.getString("Characters_List", null)

        if (json != null) {
            rawCharactersServerResponse = gson.fromJson<RawCharactersServerResponse>(json, RawCharactersServerResponse::class.java)
            charactersList = rawCharactersServerResponse!!.results as MutableList<Character>?

            charactersList!!.sortWith(Comparator { character2, character1 -> character2!!.name!!.compareTo(character1!!.name!!) })

            adapter!!.setFilter(charactersList!!)
            root!!.charactersFragmentNumberOfItems.text = adapter!!.itemCount.toString()


        } else {
            root!!.charactersFragmentSwipe_container.post {
                root!!.charactersFragmentSwipe_container.isRefreshing = true
                loadRecyclerViewData()
            }
        }

        root!!.charactersFragmentSearchViewMenu.setOnClickListener {

            val ft = fragmentManager!!.beginTransaction()
            val prev = fragmentManager!!.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            val dialogFragment = CharactersDialogFragment()
            dialogFragment.show(ft, "dialog")
        }

        return root
    }

    override fun loadRecyclerViewData() {

        root!!.charactersFragmentSwipe_container.isRefreshing = true

        val call = service.getAllPersonnagesFromPage(1)

        call.enqueue(object : Callback<RawCharactersServerResponse> {
            override fun onResponse(call: Call<RawCharactersServerResponse>, response: Response<RawCharactersServerResponse>) {

                rawCharactersServerResponse = response.body()
                charactersList = rawCharactersServerResponse!!.results as MutableList<Character>?

                val numberOfPages: Int = rawCharactersServerResponse!!.info!!.pages!!

                charactersList!!.sortWith(Comparator { character2, character1 -> character2!!.name!!.compareTo(character1!!.name!!) })

                adapter!!.setFilter(charactersList!!)
                root!!.charactersFragmentNumberOfItems.text = adapter!!.itemCount.toString()

                Toast.makeText(root!!.context, "Vos données sont désormais sauvegardées", Toast.LENGTH_SHORT).show()

                if (numberOfPages > 1) {

                    for (i in 2..numberOfPages) {

                        val additionalCall = service.getAllPersonnagesFromPage(i)

                        additionalCall.enqueue(object : Callback<RawCharactersServerResponse> {
                            override fun onResponse(
                                call: Call<RawCharactersServerResponse>,
                                response: Response<RawCharactersServerResponse>
                            ) {
                                charactersList!!.addAll(response.body()!!.results as List<Character>)
                                charactersList!!.sortWith(Comparator { character2, character1 -> character2!!.name!!.compareTo(character1!!.name!!) })

                                adapter!!.setFilter(charactersList!!)
                                root!!.charactersFragmentNumberOfItems.text = adapter!!.itemCount.toString()
                                rawCharactersServerResponse!!.results = charactersList

                                sharedPreferences!!.edit()
                                    .putString("Characters_List", gson.toJson(rawCharactersServerResponse))
                                    .apply()
                            }

                            override fun onFailure(call: Call<RawCharactersServerResponse>, t: Throwable) {
                                Toast.makeText(
                                    root!!.context,
                                    "Un soucis s'est produit lors de la récupération du reste des personnages",
                                    Toast.LENGTH_LONG
                                ).show()
                                root!!.charactersFragmentSwipe_container.isRefreshing = false
                            }
                        })

                    }

                } else {

                    charactersList!!.sortWith(Comparator { character2, character1 -> character2!!.name!!.compareTo(character1!!.name!!) })

                    sharedPreferences!!.edit()
                        .putString("Characters_List", gson.toJson(rawCharactersServerResponse))
                        .apply()
                }

                root!!.charactersFragmentSwipe_container.isRefreshing = false
            }

            override fun onFailure(call: Call<RawCharactersServerResponse>, t: Throwable) {
                Toast.makeText(
                    root!!.context,
                    "Impossible de joindre le serveur, réessayer ultérieurement",
                    Toast.LENGTH_SHORT
                ).show()
                root!!.charactersFragmentSwipe_container.isRefreshing = false
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
            val lastLocation = model.location!!.name!!.toLowerCase()

            if (name.contains(filter) || status.contains(filter) || gender == filter || origin.contains(filter) || lastLocation.contains(filter)) {
                filteredList.add(model)
            }
        }

        return filteredList
    }

    override fun onRefresh() {
        loadRecyclerViewData()
    }

}