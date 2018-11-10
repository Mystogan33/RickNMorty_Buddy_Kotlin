package com.example.mysto.ricknmortybuddykotlin.adapters

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.Episode
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.episodeDetails.EpisodeDetailsActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.recycler_view_item_episode.view.*


class RecyclerViewEpisodesAdapter(
    internal var listEpisodes: List<Episode>,
    internal var mContext: AppCompatActivity
) : RecyclerView.Adapter<RecyclerViewEpisodesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val mInflater = LayoutInflater.from(mContext)
        return MyViewHolder(mInflater.inflate(R.layout.recycler_view_item_episode, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val imageView = holder.imgEpisode

        Picasso.with(mContext.applicationContext)
            .load(listEpisodes[position].image)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.no_data)
            .into(imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(mContext.applicationContext)
                        .load(listEpisodes[position].image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.no_image)
                        .into(imageView, object : Callback {
                            override fun onSuccess() {}
                            override fun onError() {}
                        })
                }
            })

        imageView.setOnClickListener {
            val intent = Intent(mContext, EpisodeDetailsActivity::class.java)
            intent.putExtra("episode_details", listEpisodes[position])

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val optionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, imageView, "imageEpisode")
                mContext.startActivity(intent, optionsCompat.toBundle())

            } else {
                mContext.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return listEpisodes.size
    }

    fun refreshData(list: List<Episode>) {
        listEpisodes = list
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgEpisode: CircleImageView = itemView.rv_details_episode_img
    }


}