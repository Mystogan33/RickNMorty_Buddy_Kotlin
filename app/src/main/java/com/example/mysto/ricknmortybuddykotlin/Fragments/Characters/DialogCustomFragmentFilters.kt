package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mysto.ricknmortybuddykotlin.R

class DialogCustomFragmentFilters : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.characters_fragment_dialog_filters_fragment, container, false)
    }

    companion object {
        fun createInstance(txt: String): DialogCustomFragmentFilters {
            return DialogCustomFragmentFilters()
        }
    }
}