package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.adapter

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.characterDetails.Character_Details_Activity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(private val mContext: Fragment, private var listCharacters: MutableList<Character>?) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mInflater = LayoutInflater.from(mContext.context)
        return MyViewHolder(mInflater.inflate(R.layout.characters_fragment_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.personnage_fragment_item_name.text = listCharacters!![position].name

        val status = listCharacters!![position].status
        when {
            status?.toLowerCase() == "dead" -> {
                holder.personnage_fragment_item_status.setTextColor(
                    ContextCompat.getColor(
                        mContext.context!!,
                        R.color.colorDanger
                    )
                )
                holder.personnage_fragment_item_status.text = mContext.resources.getString(R.string.status_dead)
            }
            status?.toLowerCase() == "alive" -> {
                holder.personnage_fragment_item_status.setTextColor(
                    ContextCompat.getColor(
                        mContext.context!!,
                        R.color.colorValidate
                    )
                )
                holder.personnage_fragment_item_status.text = mContext.resources.getString(R.string.status_alive)
            }
            else -> {
                holder.personnage_fragment_item_status.setTextColor(
                    ContextCompat.getColor(
                        mContext.context!!,
                        R.color.followersBg
                    )
                )
                holder.personnage_fragment_item_status.text = mContext.resources.getString(R.string.status_unknown)
            }
        }

        holder.personnage_fragment_item_species.text = listCharacters!![position].species

        val gender = listCharacters!![position].gender

        when {
            gender?.toLowerCase() == "female" -> holder.personnage_fragment_item_gender.text = mContext.resources.getString(R.string.gender_female)
            gender?.toLowerCase() == "male" -> holder.personnage_fragment_item_gender.text = mContext.resources.getString(R.string.gender_male)
            else -> holder.personnage_fragment_item_gender.text = gender
        }

        holder.personnage_fragment_item_origin.text = listCharacters!![position].origin!!.name
        holder.personnage_fragment_item_last_location.text = listCharacters!![position].location!!.name

        val imageView = holder.personnage_fragment_item__img

        Picasso.with(mContext.activity)
            .load(listCharacters!![position].image)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.no_data)
            .into(imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError() {
                    Picasso.with(mContext.activity)
                        .load(listCharacters!![position].image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.no_image)
                        .into(imageView, object : Callback {
                            override fun onSuccess() {}
                            override fun onError() {}
                        })
                }
            })

        holder.cardView.setOnClickListener {
            val intent = Intent(mContext.activity, Character_Details_Activity::class.java)
            intent.putExtra("personnage_details", listCharacters!![position])

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mContext.startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(mContext.activity).toBundle()
                )
            } else {
                mContext.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return listCharacters!!.size
    }

    fun setFilter(list: List<Character>) {

        listCharacters = ArrayList()
        listCharacters!!.addAll(list)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.personnage_fragment_item__img)
        lateinit var personnage_fragment_item__img: ImageView
        @BindView(R.id.personnage_fragment_item_name)
        lateinit var personnage_fragment_item_name: TextView
        @BindView(R.id.personnage_fragment_item_status)
        lateinit var personnage_fragment_item_status: TextView
        @BindView(R.id.personnage_fragment_item_species)
        lateinit var personnage_fragment_item_species: TextView
        @BindView(R.id.personnage_fragment_item_gender)
        lateinit var personnage_fragment_item_gender: TextView
        @BindView(R.id.personnage_fragment_item_origin)
        lateinit var personnage_fragment_item_origin: TextView
        @BindView(R.id.personnage_fragment_item_last_location)
        lateinit var personnage_fragment_item_last_location: TextView
        @BindView(R.id.cardview_fragment_item_id)
        lateinit var cardView: CardView


        init {
            ButterKnife.bind(this, itemView)
        }
    }
}