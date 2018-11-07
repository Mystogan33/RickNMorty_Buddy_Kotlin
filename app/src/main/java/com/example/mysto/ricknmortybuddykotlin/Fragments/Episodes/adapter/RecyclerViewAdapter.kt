package com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.adapter

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
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.Episode
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.episodeDetails.EpisodeDetailsActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.episode_details_activity.view.*
import kotlinx.android.synthetic.main.episodes_fragment_item.view.*

class RecyclerViewAdapter(private val mContext: Fragment, private var listEpisodes: MutableList<Episode>?) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mInflater = LayoutInflater.from(mContext.activity)
        return MyViewHolder(mInflater.inflate(R.layout.episodes_fragment_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.episodeFragmentItemName.text = listEpisodes!![position].name
        holder.episodeFragmentItemSeason.text = listEpisodes!![position].episode

        val imageView = holder.episodeFragmentItemImg

        Picasso.with(mContext.activity)
            .load(listEpisodes!![position].image)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .fit()
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.no_image)
            .into(imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(mContext.activity)
                        .load(listEpisodes!![position].image)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.no_image)
                        .into(imageView, object : Callback {
                            override fun onSuccess() {}
                            override fun onError() {}
                        })
                }
            })

        holder.episodeFragmentItemCardView.setOnClickListener {
            val intent = Intent(mContext.activity, EpisodeDetailsActivity::class.java)
            intent.putExtra("episode_details", listEpisodes!![position])

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext.activity!!,
                    (holder.episodeFragmentItemImg as View?)!!, "imageEpisode")
                mContext.startActivity(intent, optionsCompat.toBundle())

            } else {
                mContext.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return listEpisodes!!.size
    }

    fun setFilter(list: List<Episode>) {
        listEpisodes = ArrayList()
        listEpisodes!!.addAll(list)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var episodeFragmentItemName: TextView = itemView.episode_fragment_item_name
        var episodeFragmentItemSeason: TextView = itemView.episode_fragment_item_season
        var episodeFragmentItemImg: ImageView = itemView.episode_fragment_item_img
        var episodeFragmentItemCardView: CardView = itemView.episode_fragment_item_cardview
    }
}