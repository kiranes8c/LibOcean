package com.example.libocean.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuItemCompat
import androidx.core.view.contains
import androidx.fragment.app.Fragment
import com.example.libocean.R
import kotlinx.android.synthetic.main.fragment_admin.*


class AdminFragment() : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        listView.adapter = adapter
//        var searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        admin_search.apply {
//            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
//        }
//
//        val queryTextListener: OnQueryTextListener = object : OnQueryTextListener {
//            override fun onQueryTextChange(newText: String): Boolean {
//                Log.d("text change", newText)
//                return true
//            }
//
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return true
//            }
//        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {


    }
}

