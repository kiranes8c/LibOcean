package com.example.libocean.`interface`

import android.os.Handler
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

interface AuthenticationInterface {
    var mStorageRef: StorageReference
    var authent: FirebaseAuth
    var dbStore: FirebaseFirestore
    var dbRealTime: FirebaseDatabase

    fun validate(username: EditText, password: EditText): Boolean
    fun initializes()
    fun hideKeyboard(activityF: FragmentActivity?)
    fun loginUser(username: EditText, password: EditText)
    fun configUser(username: String, password: String)
    fun tesClick()
    fun getUserById(name: TextView, img: ImageView)
//    fun getUserById(img: ImageView, name: TextView)
}