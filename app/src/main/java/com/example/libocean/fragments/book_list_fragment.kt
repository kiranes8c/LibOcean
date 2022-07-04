package com.example.libocean.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libocean.BookListData
import com.example.libocean.R
import com.example.libocean.`interface`.AuthenticationInterface
import com.example.libocean.`interface`.ProgressBar
import com.example.libocean.adapters.ItemBookAdapter
import com.example.libocean.implement.AuthConfig
import com.example.libocean.models.BookInfo
import com.example.libocean.services.LoadProgressBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_book_list.*


class BookListFragment : Fragment() {
    private lateinit var authInter: AuthenticationInterface
    private var bookList: ArrayList<BookInfo> = BookListData().getBookList()
    private lateinit var showPopup: ProgressBar
//    private val lastTouchDownXY = FloatArray(2)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authInter = AuthConfig(context as Activity)
        authInter.initializes()
        showPopup = LoadProgressBar(activity as Activity)
        view.setOnClickListener {
            authInter.hideKeyboard(activity)
            view.clearFocus()
        }
//        view.setOnTouchListener(touchListener)
        checkNetwork()
//        loadBookList()

    }


//    @SuppressLint("ClickableViewAccessibility")
//    private var touchListener = OnTouchListener { _, event ->
//        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
//            lastTouchDownXY[0] = event.x;
//            lastTouchDownXY[1] = event.y;
//        }
//
//        val x = lastTouchDownXY[0]
//        val y = lastTouchDownXY[1]
//        Log.d("tuoch:", "onLongClick: x = $x, y = $y")
//        // let the touch event pass on to whoever needs it
//        false
//    }

    private fun checkNetwork() {

    }

    private fun loadBookList() {
//        var str: String = "vag,uri,key"
//        var arr: List<String> = str.split(",")
//        for (ar in arr) {
//            Toast.makeText(context, ar, Toast.LENGTH_SHORT).show()
//        }
        showPopup.openProgressBar()
        var getDB = authInter.dbRealTime.getReference("user/${authInter.authent.currentUser?.uid}")
        getDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list: MutableList<String?> = ArrayList()
                for (ds in dataSnapshot.children) {
                    val userId = ds.key
                    var name = ds.child("name").value
                    var price = ds.child("price").value
                    var imgUrl = ds.child("imgUrl").value
                    bookList.add(
                        BookInfo(
                            name = name.toString(),
                            price = Integer.parseInt(price.toString()),
                            imgUrl = imgUrl.toString()
                        )
                    )
                }
                showPopup.closeProgressBar()
                bookListShow()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bookListShow() {
        book_list_recycler.layoutManager =  GridLayoutManager(context, 2)
        book_list_recycler.adapter = ItemBookAdapter(bookList)
        book_list_recycler.setHasFixedSize(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        button.setOnClickListener {
//            // Write a message to the database
//            val database = FirebaseDatabase.getInstance()
//            val myRef = database.getReference("message")
////
////            myRef.setValue("Hello, World!")
//
//            // Read from the database
//
//            // Read from the database
//            myRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    val value =
//                        dataSnapshot.getValue(String::class.java)!!
//                    Log.d("tag", "Value is: $value")
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Failed to read value
//                    Log.w("tag", "Failed to read value.", error.toException())
//                }
//            })
//        }
    }
}