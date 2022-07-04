package com.example.libocean.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.libocean.R
import com.example.libocean.`interface`.AuthenticationInterface
import com.example.libocean.`interface`.ProgressBar
import com.example.libocean.implement.AuthConfig
import com.example.libocean.models.BookInfo
import com.example.libocean.services.LoadProgressBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_add_book.*
import java.io.File

class AddBookFragment : Fragment() {
    private var file:String = ""
    private lateinit var authInter: AuthenticationInterface
    private lateinit var showPopup: ProgressBar
    private var handler = Handler()
    private var price = 0
    private lateinit var r: Runnable
    private lateinit var saveLink: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showPopup = LoadProgressBar(activity as Activity)
        authInter = AuthConfig(context as Activity)
        authInter.initializes()

        saveLink = "user/${authInter.authent.currentUser?.uid}"

        var editPrice: EditText = view.findViewById(R.id.add_item_edit_price)
        editPrice.inputType = InputType.TYPE_CLASS_NUMBER

        view.setOnClickListener {
            authInter.hideKeyboard(activity)
            view.clearFocus()
        }

        add_item_back.setOnClickListener {
            if (fragmentManager?.backStackEntryCount != 0) {
                fragmentManager?.popBackStack()
            }
        }
        add_item_error_button.setOnClickListener {
            add_item_error_layout.visibility = View.GONE
        }
        add_item_choose.setOnClickListener {
            permissionCheck()
        }
        add_item_button.setOnClickListener {
            authInter.hideKeyboard(activity)
            showPopup.openProgressBar()
            var rivRef = authInter.mStorageRef.child("bg_book.jpg")
            rivRef.downloadUrl.addOnSuccessListener {
                uploadBookOtherDetail(it)
            }.addOnFailureListener {
                errorMessage(
                    "Upload Book Detail Fail!!",
                    "Upload Book Detail Fail!!",
                    context
                )
            }
        }
    }

    private fun uploadValidate() {
        //set value for price
        price = if (add_item_edit_price.text.toString() == "")
            0
        else
            Integer.parseInt(add_item_edit_price.text.toString())
        //check name is empty
        if (add_item_edit_name.text.toString() == "") {
            var errorText = "Name Must Fill!"
            add_item_edit_name.error = errorText
            handler.postDelayed({ add_item_edit_name.error = null }, 5000)
            add_item_error_button.visibility = View.VISIBLE
            add_item_error.text = errorText
            add_item_error_layout.visibility = View.VISIBLE
        } else {
            add_item_error_layout.visibility = View.GONE
            add_item_error_button.visibility = View.VISIBLE
            showPopup.openProgressBar()

            var connectRef = authInter.dbRealTime.getReference(".info/connected")

            connectRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java) ?: false
                    Log.d("check", connected.toString())

                    if (connected) {
                        var queryRef = authInter.dbRealTime.getReference(saveLink)
                        var name = add_item_edit_name.text.toString()
                        //check if book Name already exit in DB
                        queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                //if exit book Name in DB -> try new one
                                if (snapshot.hasChild(name)) {
                                    var errorText = "Please Try other Name!"
                                    add_item_edit_name.error = errorText
                                    handler.postDelayed(
                                        { add_item_edit_name.error = null },
                                        5000
                                    )
                                    errorMessage("Please Try other Name!", "", context)
                                } else {
                                    add_item_error_layout.visibility = View.GONE
                                    //thiếu  trường hợp khi user chọn hình rồi nhưng muốn bỏ trống chỗ hình
                                    //check if user are choose img or not -> if not then load default img
                                    if (file == "" || file == "null") {
                                        var rivRef = authInter.mStorageRef.child("bg_book.jpg")
                                        rivRef.downloadUrl.addOnSuccessListener {
                                            uploadBookOtherDetail(it)
                                        }.addOnFailureListener {
                                            errorMessage(
                                                "Upload Book Detail Fail!!",
                                                "Upload Book Detail Fail!!",
                                                context
                                            )
                                        }
                                    } else
                                        uploadBookInfo(file)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                errorMessage("", "Database Disconnected!!!", context)
                            }
                        })
                    } else {
                        add_item_error_button?.visibility = View.GONE
                        errorMessage("Unable to add book - NO DATABASE CONNECTION!!!", "", context)
                        connectRef.removeEventListener(this)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    errorMessage(error.toString(), "", context)
                    return
                }
            })
        }
    }

    private fun errorMessage(message: String, toastMess: String, thisContext: Context?) {
        handler.postDelayed({
            showPopup.closeProgressBar()
            if (message.trim().isNotEmpty()) {
                add_item_error.text = message
                add_item_error_layout.visibility = View.VISIBLE
            }
            if (toastMess.trim().isNotEmpty()) {
                Toast.makeText(thisContext, toastMess, Toast.LENGTH_SHORT).show()
            }
        }, 1500)
    }

    private fun uploadBookOtherDetail(imgFile: Uri) {
        //create new book
        var rtRef =
            authInter.dbRealTime.getReference("user/${authInter.authent.currentUser?.uid}")
        var bookInfo = BookInfo(
            add_item_edit_name.text.toString(),
            price,
            imgFile.toString()
        )
        rtRef.child("${add_item_edit_name.text}").setValue(bookInfo).addOnSuccessListener {
            clearEditChoose()
            errorMessage("", "Data Loaded Successful!", context)
        }.addOnFailureListener {
            errorMessage("Upload data Fail!!!", "", context)
        }
    }

    private fun clearEditChoose() {
        add_item_edit_name.text = null
        add_item_img.setImageResource(R.drawable.ic_baseline_local_library_24)
        add_item_edit_price.text = null
    }

    private fun uploadBookInfo(imgPath: String) {
        var imgFile = Uri.fromFile(File(imgPath)).lastPathSegment
        var riverRef = authInter.mStorageRef.child("image/${authInter.authent.currentUser?.uid}/${imgFile}")
        //add new img in user Storage Firebase
        riverRef.putFile(Uri.parse(file)).addOnSuccessListener {
            riverRef.downloadUrl.addOnSuccessListener {
                uploadBookOtherDetail(it)
            }.addOnFailureListener {
                errorMessage("Upload Book Detail Fail!!", "Upload Book Detail Fail!!", context)
            }
        }.addOnFailureListener {
            errorMessage("Upload Image Fail!!", "Upload Image Fail!!", context)
        }
    }

    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity?.let { it1 -> ActivityCompat.checkSelfPermission(
                    it1,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) } != PackageManager.PERMISSION_GRANTED) {
                //get permission denied
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to access permission
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                //if permission already accept
                pickImageGallery(IMAGE_GALERY_PICK)
            }
        } else{
            //system OS < Marshmallow
            pickImageGallery(IMAGE_GALERY_PICK)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                else
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun pickImageGallery(imageCode: Int) {
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imageCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        file = data?.data.toString()
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_GALERY_PICK) {
            Glide.with(context).load(file).placeholder(R.drawable.bg_book).error(R.drawable.bg_book).into(
                add_item_img
            )
        }
    }

    companion object {
        private const val IMAGE_GALERY_PICK = 1000

        private const val PERMISSION_CODE = 10893
    }
}