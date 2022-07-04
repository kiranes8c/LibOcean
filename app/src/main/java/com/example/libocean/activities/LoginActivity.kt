package com.example.libocean.activities

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libocean.R
import com.example.libocean.`interface`.AuthenticationInterface
import com.example.libocean.implement.AuthConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.context.AttributeContext
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.roundToInt


class LoginActivity : AppCompatActivity() {
    private lateinit var authInter: AuthenticationInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authInter = AuthConfig(this)
        authInter.initializes()

        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move)
        setContentView(R.layout.activity_login)
        //--------------------
        if (savedInstanceState == null) {
            login_background.visibility = View.VISIBLE
            var viewTreeObserver:ViewTreeObserver = login_background.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
                    override fun onGlobalLayout() {
                        circularRevealActivity();
                        login_background.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        }
        //--------------------
        var username: EditText = findViewById(R.id.edit_username)
        var password: EditText = findViewById(R.id.edit_password)
        var btn: Button = findViewById(R.id.button)

        var presenceRef = FirebaseDatabase.getInstance().getReference("disconnectmessage")
        presenceRef.onDisconnect().setValue("Disconnected!")
        presenceRef.onDisconnect().removeValue { error, ref ->
            if (error != null) {
                Log.d("Check: ", "could not establish onDisconnect event:" + error.message);
            }
        }

        btn.setOnClickListener{
            authInter.loginUser(username, password)
        }
    }

    private fun circularRevealActivity() {
        var bundle: Bundle? = intent.extras
        var cx: Float = bundle?.get("x") as Float
        var cy: Float = bundle?.get("y") as Float

        var finalRadius: Float = Math.max(login_background.width, login_background.height).toFloat()
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            login_background,
            cx.roundToInt(),
            cy.roundToInt(), 0f,
            finalRadius
        )
        circularReveal.duration = 1000
        login_background.visibility = View.VISIBLE
        circularReveal.start()
    }

//    private fun getDips(dps: Int): Int {
//        var resources:Resources = resources
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(),resources.displayMetrics).toInt()
//    }
}