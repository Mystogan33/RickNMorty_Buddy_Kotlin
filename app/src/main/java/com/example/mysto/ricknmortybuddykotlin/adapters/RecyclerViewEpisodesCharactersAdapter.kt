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
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.characterDetails.Character_Details_Activity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


class RecyclerViewEpisodesCharactersAdapter(
    internal var listCharacters: MutableList<Character>,
    internal var mContext: AppCompatActivity
) : RecyclerView.Adapter<RecyclerViewEpisodesCharactersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mInflater = LayoutInflater.from(mContext)
        return MyViewHolder(mInflater.inflate(R.layout.activity_episode_details_character, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val imageView = holder.img_character

        Picasso.with(mContext.applicationContext)
            .load(listCharacters[position].image)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.no_data)
            .into(imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(mContext.applicationContext)
                        .load(listCharacters[position].image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.no_image)
                        .into(imageView, object : Callback {
                            override fun onSuccess() {}
                            override fun onError() {}
                        })
                }
            })

        imageView.setOnClickListener {
            val intent = Intent(mContext, Character_Details_Activity::class.java)
            intent.putExtra("personnage_details", listCharacters[position])

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val optionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, imageView, "imageCharacter")
                mContext.startActivity(intent, optionsCompat.toBundle())

            } else {
                mContext.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return listCharacters.size
    }

    fun refreshData(list: List<Character>?) {
        listCharacters = arrayListOf()
        list?.let { listCharacters.addAll(it) }
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.img)
        lateinit var img_character: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }
    }

}