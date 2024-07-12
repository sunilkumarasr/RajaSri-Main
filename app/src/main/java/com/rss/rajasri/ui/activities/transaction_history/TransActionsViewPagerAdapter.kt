package com.rss.rajasri.ui.activities.transaction_history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TransActionsViewPagerAdapter(var listOfFragments:ArrayList<Fragment>, var fragmentManager: FragmentManager, var lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return  listOfFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return  listOfFragments[position]
    }
}