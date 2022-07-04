package com.example.libocean.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.libocean.R
import com.example.libocean.adapters.PageTabAdapter
import com.example.libocean.fragments.BookListFragment
import com.example.libocean.fragments.MainFragment
import com.example.libocean.fragments.TestFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null){
            replaceFragment(MainFragment())
        }
//        selectedTab()
//        main_profile.setOnClickListener {
//            startActivity(Intent(this, TestActivity::class.java))
//        }
//        icon_view.setOnClickListener {
//            drawer_layout.openDrawer(GravityCompat.END)
//        }
//
//        var toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()
//        nav_view.setNavigationItemSelectedListener(this)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }

    private fun replaceFragment(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.main_frame, newFragment)
            transaction.commit()
        }
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.header_img -> {
//                Toast.makeText(this,"run", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, LoginActivity::class.java))
//            }
//            R.id.logout -> {
//                replaceFragment(TestFragment())
////                startActivity(Intent(this, LoginActivity::class.java))
//            }
//            R.id.user_library -> {
//                replaceEmptyPage(TestFragment())
//            }
//            R.id.btn -> {
//                startActivity(Intent(this, LoginActivity::class.java))
//            }
//        }
//        drawer_layout.closeDrawer(GravityCompat.END)
//        return true
//    }
//
//    private fun selectedTab() {
//        var pageAdapter: PageTabAdapter = PageTabAdapter(supportFragmentManager, main_tab_layout.tabCount)
//
//        main_view.adapter = pageAdapter
//        main_tab_layout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                tab?.position?.let { main_view?.setCurrentItem(it) }
////                main_tab_layout.setupWithViewPager(main_view) : make app conflict and stop
//            }
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                return
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                return
//            }
//        })
//        main_view.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tab_layout))
//    }

//    private fun replaceEmptyPage(newFragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        if (transaction != null){
//            Log.d("this is tag:", "run")
//            transaction.replace(R.id.navi_frame, newFragment)
//            transaction.commit()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }


//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
}