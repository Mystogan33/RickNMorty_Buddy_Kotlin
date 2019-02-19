package com.example.mysto.ricknmortybuddykotlin.Fragments.Characters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CharacterItemDecoration(private val mSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = mSpace
        outRect.right = mSpace
        outRect.bottom = mSpace

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) outRect.top = mSpace
        if(parent.getChildAdapterPosition(view) == 1) outRect.top = mSpace * 2
    }
}