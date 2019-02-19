package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.adapter

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysto.ricknmortybuddykotlin.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.models.Character
import com.example.mysto.ricknmortybuddykotlin.characterDetails.CharacterDetailsActivity
import kotlinx.android.synthetic.main.characters_fragment_item.view.*
import android.widget.LinearLayout
import timber.log.Timber


class RecyclerViewAdapter(private val mContext: Fragment, private var listCharacters: MutableList<Character>?) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mInflater = LayoutInflater.from(mContext.context)
        return MyViewHolder(mInflater.inflate(R.layout.characters_fragment_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.characterFragmentItemName.text = listCharacters!![position].name

        val status = listCharacters!![position].status
        when {
            status?.toLowerCase() == "dead" -> {
                holder.characterFragmentItemStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext.activity!!,
                        R.color.colorDanger
                    )
                )
                holder.characterFragmentItemStatus.text = mContext.resources.getString(R.string.status_dead)
            }
            status?.toLowerCase() == "alive" -> {
                holder.characterFragmentItemStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext.context!!,
                        R.color.colorValidate
                    )
                )
                holder.characterFragmentItemStatus.text = mContext.resources.getString(R.string.status_alive)
            }
            else -> {
                holder.characterFragmentItemStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext.context!!,
                        R.color.followersBg
                    )
                )
                holder.characterFragmentItemStatus.text = mContext.resources.getString(R.string.status_unknown)
            }
        }

        holder.characterFragmentItemSpecies.text = listCharacters!![position].species

        val gender = listCharacters!![position].gender

        when {
            gender?.toLowerCase() == "female" -> holder.characterFragmentItemGender.text = mContext.resources.getString(R.string.gender_female)
            gender?.toLowerCase() == "male" -> holder.characterFragmentItemGender.text = mContext.resources.getString(R.string.gender_male)
            else -> holder.characterFragmentItemGender.text = gender
        }

        holder.characterFragmentItemOrigin.text = listCharacters!![position].origin!!.name
        holder.characterFragmentItemLastLocation.text = listCharacters!![position].location!!.name

        val imageView = holder.characterFragmentItemImg

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

        holder.characterFragmentItemCardView.setOnClickListener {
            val intent = Intent(mContext.activity, CharacterDetailsActivity::class.java)
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
        var characterFragmentItemImg: ImageView = itemView.personnage_fragment_item__img
        var characterFragmentItemName: TextView = itemView.personnage_fragment_item_name
        var characterFragmentItemStatus: TextView = itemView.personnage_fragment_item_status
        var characterFragmentItemSpecies: TextView = itemView.personnage_fragment_item_species
        var characterFragmentItemGender: TextView = itemView.personnage_fragment_item_gender
        var characterFragmentItemOrigin: TextView = itemView.personnage_fragment_item_origin
        var characterFragmentItemLastLocation: TextView = itemView.personnage_fragment_item_last_location
        var characterFragmentItemCardView : CardView = itemView.cardview_fragment_item_id
    }
}