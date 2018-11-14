package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.mysto.ricknmortybuddykotlin.R


public class CharactersDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.characters_fragment_dialog, container, false)
    }
    
}