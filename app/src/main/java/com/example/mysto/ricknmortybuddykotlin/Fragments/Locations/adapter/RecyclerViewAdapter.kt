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
import butterknife.BindView
import butterknife.ButterKnife
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.models.Location
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.locationDetails.Location_Details_Activity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


class RecyclerViewAdapter(private val mContext: Fragment, private var listLocations: MutableList<Location>?) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mInflater = LayoutInflater.from(mContext.activity)
        return MyViewHolder(mInflater.inflate(R.layout.locations_fragment_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.location_fragment_item_name.text = listLocations!![position].name
        holder.location_fragment_item_type.text = listLocations!![position].type
        holder.location_fragment_item_dimension.text = listLocations!![position].dimension

        val imageView = holder.location_fragment_item__img

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
            val intent = Intent(mContext.activity, Location_Details_Activity::class.java)
            intent.putExtra("location_details", listLocations!![position])

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext.activity!!,
                    holder.location_fragment_item__img, "imageLocation")
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

        @BindView(R.id.location_fragment_item__img)
        lateinit var location_fragment_item__img: ImageView
        @BindView(R.id.location_fragment_item_name)
        lateinit var location_fragment_item_name: TextView
        @BindView(R.id.location_fragment_item_type)
        lateinit var location_fragment_item_type: TextView
        @BindView(R.id.location_fragment_item_dimension)
        lateinit var location_fragment_item_dimension: TextView
        @BindView(R.id.cardview_fragment_item_id)
        lateinit var cardView: CardView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}