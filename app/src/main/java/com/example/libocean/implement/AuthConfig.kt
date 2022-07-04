package com.example.libocean.implement

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.libocean.R
import com.example.libocean.`interface`.AuthenticationInterface
import com.example.libocean.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AuthConfig(var context: Activity) : AuthenticationInterface {

    override lateinit var mStorageRef: StorageReference
    override lateinit var authent: FirebaseAuth
    override lateinit var dbStore: FirebaseFirestore
    override lateinit var dbRealTime: FirebaseDatabase

    override fun initializes() {
        mStorageRef = FirebaseStorage.getInstance().reference
        authent = FirebaseAuth.getInstance()
        dbStore = FirebaseFirestore.getInstance()
        dbRealTime = FirebaseDatabase.getInstance()
    }

    override fun hideKeyboard(activityF: FragmentActivity?) {
        val view = activityF?.currentFocus
        if (view != null) {
            val inputH: InputMethodManager = activityF?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputH.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun loginUser(username: EditText, password: EditText) {
        var user: String = username.text.toString()
        var pass: String = password.text.toString()
        if (!validate(username, password))
            return
        else {
            configUser(user, pass)
        }
    }

    override fun validate(username: EditText, password: EditText): Boolean {
        var user: String = username.text.toString()
        var pass: String = password.text.toString()
        var handler = Handler()

        if (user.isEmpty()) {
            username.error = "Must enter Email!!!"
            handler.postDelayed({ username.error = null }, 5000)
            username.requestFocus()
            return false
        }else if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            username.error = "Email is invalid!!!"
            handler.postDelayed({ username.error = null }, 5000)
            username.requestFocus()
            return false
        }else if (pass.isEmpty()) {
            password.error = "Must enter Password!!!"
            handler.postDelayed({ password.error = null }, 5000)
            password.requestFocus()
            return false
        }else if (pass.length < 6) {
            password.error = "Minimum length must be 6!!"
            handler.postDelayed({ username.error = null }, 5000)
            password.requestFocus()
            return false
        }
        return true
    }

    override fun tesClick() {
        Toast.makeText(context, "run", Toast.LENGTH_SHORT).show()
    }

    override fun configUser(username: String, password: String) {
        authent.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    startActivity(context, Intent(context, MainActivity::class.java), null)
                    Toast.makeText(context, "Welcome to LibOcean", Toast.LENGTH_SHORT).show()
                    context.finish()
                } else {
                    Toast.makeText(context, "Email or password invalid!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun getUserById(name: TextView, img: ImageView) {
        dbStore.collection("UserId").document("${authent.currentUser?.uid}").get().addOnSuccessListener {
            name.text = it.data?.get("name").toString()
            img.setImageResource(R.drawable.ic_baseline_local_library_24)
        }.addOnFailureListener {
            Log.d("on fail:", "error")
        }
    }
}

