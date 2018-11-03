package com.example.mysto.ricknmortybuddykotlin.adapters

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.models.Episode
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.episodeDetails.Episode_Details_Activity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


class RecyclerViewEpisodesAdapter(
    internal var listEpisodes: MutableList<Episode>,
    internal var mContext: AppCompatActivity
) : RecyclerView.Adapter<RecyclerViewEpisodesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val mInflater = LayoutInflater.from(mContext)
        return MyViewHolder(mInflater.inflate(R.layout.activity_character_details_episode, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val imageView = holder.img_episode

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
            val intent = Intent(mContext, Episode_Details_Activity::class.java)
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
        listEpisodes = ArrayList()
        listEpisodes.addAll(list)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.img)
        lateinit var img_episode: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }
    }


}