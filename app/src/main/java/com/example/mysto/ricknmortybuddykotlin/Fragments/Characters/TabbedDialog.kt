package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.adapter.dialogViewPagerAdapter
import com.example.mysto.ricknmortybuddykotlin.R
import kotlinx.android.synthetic.main.characters_fragment_dialog.view.*


class TabbedDialog : DialogFragment() {

    var listCharacters : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listCharacters = arguments!!.getString("listCharacters")!!

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.characters_fragment_dialog, container)
        val adapter = dialogViewPagerAdapter(childFragmentManager)
        adapter.addFragment("Infos", DialogCustomFragmentStats.createInstance("Infos"))
        adapter.addFragment("Filtres", DialogCustomFragmentFilters.createInstance("Filters"))
        rootView.dialogCharViewPager.adapter = adapter
        rootView.dialogCharTabLayout.setupWithViewPager(rootView.dialogCharViewPager)

        rootView.dialogCharDismissBtn.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun createInstance(): TabbedDialog {
            val fragment = TabbedDialog()
            return fragment
        }
    }
}