package com.example.libocean.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.libocean.activities.NaviActivity
import com.example.libocean.fragments.AddBookFragment
import com.example.libocean.fragments.BookListFragment
import com.example.libocean.fragments.HomeFragment
import com.example.libocean.fragments.TestFragment

public class PageTabAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {

    private var numTabs: Int = behavior
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> AddBookFragment()
            else -> TestFragment()
        }
    }

    override fun getCount(): Int {
        return numTabs
    }
}