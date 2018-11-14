package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mysto.ricknmortybuddykotlin.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.characters_bottom_sheet.view.*

class CharactersBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.characters_bottom_sheet, container, false)

        view.hello.setOnClickListener {
            Toast.makeText(this.context, "Hello", Toast.LENGTH_LONG).show()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun getInstance() : CharactersBottomSheetDialogFragment {
            return CharactersBottomSheetDialogFragment()
        }
    }

}