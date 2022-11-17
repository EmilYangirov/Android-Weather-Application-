package com.emilyangirov.weatherproject.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emilyangirov.weatherproject.Scripts.FragmentData

class ViewPagerAdapter
    (fragmentActivity: FragmentActivity, private val fragments: List<FragmentData>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }

}