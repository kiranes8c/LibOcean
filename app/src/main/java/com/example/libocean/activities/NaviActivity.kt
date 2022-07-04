package com.example.libocean.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import com.example.libocean.R
import kotlinx.android.synthetic.main.activity_navi.*
import kotlinx.android.synthetic.main.fragment_admin.*
import kotlinx.android.synthetic.main.fragment_admin.listView


class NaviActivity : AppCompatActivity() {
    private var mylist: ArrayList<String> = ArrayList()
    lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navi)

        mylist.add("C");
        mylist.add("C++");
        mylist.add("C#");
        mylist.add("Java");
        mylist.add("Advanced java");
        mylist.add("Interview prep with c++");
        mylist.add("Interview prep with java");
        mylist.add("data structures with c");
        mylist.add("data structures with java");

        adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            mylist
        )
        listView.adapter = adapter

        navi_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (mylist.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(this@NaviActivity, "Not Found", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                Toast.makeText(this@NaviActivity, adapter.filter.filter(newText).toString(), Toast.LENGTH_SHORT).show()
                return false
            }
        })
    }
}