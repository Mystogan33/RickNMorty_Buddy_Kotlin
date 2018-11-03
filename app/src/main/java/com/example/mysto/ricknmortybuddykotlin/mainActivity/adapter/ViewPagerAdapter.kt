package com.example.mysto.ricknmortybuddykotlin.mainActivity.adapter

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.Characters_Fragment
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.Episodes_Fragment
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.Locations_Fragment


class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentList = ArrayList<Any>()
    private val fragmentListTitles = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position] as Fragment
    }

    override fun getCount(): Int {
        return fragmentListTitles.size
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentListTitles[position]
    }

    fun addFragment(fragment: Fragment, Title: String) {
        fragmentList.add(fragment)
        fragmentListTitles.add(Title)
    }

    fun refreshAllFragments() {
        for (fg in fragmentList) {
            when (fg) {
                is Episodes_Fragment -> fg.loadRecyclerViewData()
                is Locations_Fragment -> fg.loadRecyclerViewData()
                is Characters_Fragment -> fg.loadRecyclerViewData()
            }
        }
    }

}