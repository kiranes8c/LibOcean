package com.example.libocean.activities

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.example.libocean.R
import com.example.libocean.fragments.BookListFragment
import com.example.libocean.fragments.TestFragment
import kotlinx.android.synthetic.main.activity_test.*
import kotlin.math.roundToInt

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_test)

        if (savedInstanceState == null) {
            login_background.visibility = View.VISIBLE

            var viewTreeObserver: ViewTreeObserver = login_background.viewTreeObserver

            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
                    override fun onGlobalLayout() {
                        circularRevealActivity();
                        login_background.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }

                })
            }
        }

        button2.setOnClickListener {
            replaceFragment(TestFragment())
        }
    }

    private fun replaceFragment(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, newFragment)
        transaction.addToBackStack(null).commit()
    }

    private fun circularRevealActivity() {
        var bundle: Bundle? = intent.extras
        var cx: Float = bundle?.get("x") as Float
        var cy: Float = bundle?.get("y") as Float

//        var cx: Int = login_background.right/2
//        var cy: Int = login_background.bottom/2

        var finalRadius: Float = Math.max(login_background.width, login_background.height).toFloat()
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            login_background,
            cx.roundToInt(),
            cy.roundToInt(), 0f,
            finalRadius
        )

        circularReveal.duration = 2000
        login_background.visibility = View.VISIBLE
        circularReveal.start()
    }

    private fun getDips(dps: Int): Int {
        var resources: Resources = resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(),resources.displayMetrics).toInt()
    }
}