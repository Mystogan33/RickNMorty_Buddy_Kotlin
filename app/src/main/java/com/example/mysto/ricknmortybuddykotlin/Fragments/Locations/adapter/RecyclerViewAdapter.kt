package com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.Location
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.locationDetails.LocationDetailsActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.locations_fragment_item.view.*


class RecyclerViewAdapter(private val mContext: Fragment, private var listLocations: MutableList<Location>?) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mInflater = LayoutInflater.from(mContext.activity)
        return MyViewHolder(mInflater.inflate(R.layout.locations_fragment_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.name.text = listLocations!![position].name
        holder.type.text = listLocations!![position].type
        holder.dimension.text = listLocations!![position].dimension

        val imageView = holder.img

        Picasso.with(mContext.activity)
            .load(listLocations!![position].image)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.no_image)
            .into(imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(mContext.activity)
                        .load(listLocations!![position].image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.no_image)
                        .into(imageView, object : Callback {
                            override fun onSuccess() {}
                            override fun onError() {}
                        })
                }
            })

        holder.cardView.setOnClickListener {
            val intent = Intent(mContext.activity, LocationDetailsActivity::class.java)
            intent.putExtra("location_details", listLocations!![position])

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext.activity!!,
                    holder.img, "imageLocation")
                mContext.startActivity(intent, optionsCompat.toBundle())
            } else {
                mContext.startActivity(intent)
            }
        }
    }

    fun setFilter(list: List<Location>) {
        listLocations = ArrayList()
        listLocations!!.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listLocations!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.location_fragment_item__img
        var name: TextView = itemView.location_fragment_item_name
        var type: TextView = itemView.location_fragment_item_type
        var dimension: TextView = itemView.location_fragment_item_dimension
        var cardView: CardView = itemView.location_fragment_item_cardview
    }
}