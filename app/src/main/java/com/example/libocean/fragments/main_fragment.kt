package com.example.libocean.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.libocean.R
import com.example.libocean.`interface`.AuthenticationInterface
import com.example.libocean.activities.LoginActivity
import com.example.libocean.activities.NaviActivity
import com.example.libocean.activities.TestActivity
import com.example.libocean.adapters.PageTabAdapter
import com.example.libocean.implement.AuthConfig
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.w3c.dom.Text

class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private val lastTouchDownXY = FloatArray(2)
    private lateinit var authInter: AuthenticationInterface
    private var checkUid = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authInter = AuthConfig(context as Activity)
        authInter.initializes()
        selectedTab()

        if (authInter.authent.currentUser?.uid == null) {
            checkUid = false
        }

        if (!checkUid) {
            nav_view.getHeaderView(0).findViewById<LinearLayout>(R.id.header_layout).visibility = View.GONE
            var cardView: CardView = nav_view.getHeaderView(0).findViewById(R.id.header_login)
            cardView.visibility = View.VISIBLE
            cardView.setOnTouchListener(touchListener)
            cardView.setOnClickListener {
                var i = Intent(context, NaviActivity::class.java)
                i.putExtra("x", lastTouchDownXY[0] + 752)
                i.putExtra("y", lastTouchDownXY[1] + 185)
                drawer_layout.closeDrawer(GravityCompat.END)
                startActivity(i)
            }

            nav_view.menu.findItem(R.id.logout).isVisible = false
            nav_view.menu.findItem(R.id.add_book).isVisible = false
            main_profile.setOnTouchListener(touchListener)
            main_profile.setOnClickListener {
                var i = Intent(context, LoginActivity::class.java)
                i.putExtra("x", lastTouchDownXY[0])
                i.putExtra("y", lastTouchDownXY[1] + 92)
                startActivity(i)
            }
        } else {
            nav_view.getHeaderView(0).findViewById<LinearLayout>(R.id.header_layout).visibility = View.VISIBLE
            nav_view.getHeaderView(0).findViewById<CardView>(R.id.header_login).visibility = View.GONE

            var name: TextView = nav_view.getHeaderView(0).findViewById(R.id.header_name)
            var img: ImageView = nav_view.getHeaderView(0).findViewById(R.id.header_img)
            authInter.getUserById(name, img)
        }

        icon_view.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.END)
        }

        var toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            context as Activity?,
            drawer_layout,
            R.string.open,
            R.string.close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private var touchListener = View.OnTouchListener { _, event ->
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            lastTouchDownXY[0] = event.x
            lastTouchDownXY[1] = event.y
        }
        Log.d("lis: ", "x: ${lastTouchDownXY[0]}, y: ${lastTouchDownXY[1]}")
        // let the touch event pass on to whoever needs it
        false
    }

    private fun selectedTab() {
        //using childFragmentManager instead of fragmentManager to reload fragment when go back form some page
        var pageAdapter: PageTabAdapter =
            activity?.supportFragmentManager?.let { PageTabAdapter(childFragmentManager, main_tab_layout.tabCount) }!!

        main_view.adapter = pageAdapter
        main_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { main_view?.setCurrentItem(it) }
//                main_tab_layout.setupWithViewPager(main_view) : make app conflict and stop
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.position?.let { main_view?.setCurrentItem(it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
        main_view.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tab_layout))
    }

    private fun replaceFragment(newFragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.main_frame, newFragment)
            transaction.addToBackStack(null).commit()
        }
    }

    private fun reloadFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.detach(this)
            transaction.attach(this)
            transaction.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.activity_main_drawer, menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.header_img -> {

            }
            R.id.logout -> {
                authInter.authent.signOut()
                reloadFragment()
            }
            R.id.user_library -> {
                replaceFragment(AdminFragment())
            }
            R.id.add_book -> {
                replaceFragment(AddBookFragment())
            }
        }
        drawer_layout.closeDrawer(GravityCompat.END)
        return true
    }
}


