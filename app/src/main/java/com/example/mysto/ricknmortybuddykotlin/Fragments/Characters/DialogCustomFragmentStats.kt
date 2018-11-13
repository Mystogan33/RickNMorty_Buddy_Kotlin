package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mysto.ricknmortybuddykotlin.R


class DialogCustomFragmentStats : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.characters_fragment_dialog_stats_fragment, container, false)
        return root
    }

    companion object {
        fun createInstance(listCharacters : String): DialogCustomFragmentStats {
            val fragment = DialogCustomFragmentStats()
            return fragment
        }
    }
}