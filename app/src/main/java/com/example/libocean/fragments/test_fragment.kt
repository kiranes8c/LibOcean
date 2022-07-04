package com.example.libocean.fragments

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.libocean.ImageData
import com.example.libocean.R
import com.example.libocean.adapters.ImageAdapter
import com.example.libocean.models.ImageModel
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_test.*
import java.io.File
import kotlin.math.roundToInt


class TestFragment : Fragment() {
    private lateinit var mStorageRef: StorageReference
    var list:ArrayList<ImageModel> = ImageData().getImage()
    var run: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView: View = inflater.inflate(R.layout.fragment_test, container, false)
        rootView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                v.removeOnLayoutChangeListener(this)
                var bundle: Bundle = Bundle()
                var cx: Float = bundle.getFloat("x")
                var cy: Float = bundle.getFloat("y")

                var finalRadius: Float = Math.max(test_f_bg.width, test_f_bg.height).toFloat()
                val anim =
                    ViewAnimationUtils.createCircularReveal(v, cx.roundToInt(),
                        cy.roundToInt(), 0f, finalRadius)
                anim.duration = 1500
                anim.start()
            }
        })
        //        return inflater.inflate(R.layout.fragment_test, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mStorageRef = FirebaseStorage.getInstance().reference

        recycler_view.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = ImageAdapter(list)
            setHasFixedSize(true)
        }
        btn99.setOnClickListener {
            pickImageFromGallery(1)
        }
        img.setOnClickListener {
            //get img URL in storage by name
//            var riversRef2: StorageReference = mStorageRef.child("home1.PNG")
//            riversRef2.downloadUrl.addOnSuccessListener {
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
//                Glide.with(context).load(it).placeholder(R.drawable.bg_book).error(R.drawable.bg_book).into(img)
//            }.addOnFailureListener {
//                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity?.let { it1 -> ActivityCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
                    //get permission denied
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to access permission
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    //if permission already accept
                    pickImageFromGallery(0)
                }
            } else{
                //system OS < Marshmallow
                pickImageFromGallery(0)
            }
        }
    }

    private fun pickImageFromGallery(imageCode: Int) {
//        val intent = Intent(Intent.ACTION_PICK)
        val intent = Intent(Intent.ACTION_PICK)
        intent.type ="image/*"
        startActivityForResult(intent, imageCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var file: String = data?.data.toString()
        var imgFile = Uri.fromFile(File(file)).lastPathSegment
//        Toast.makeText(context, imgFile.queryParameterNames.toString(), Toast.LENGTH_SHORT).show()
//        var bmp = Bitmap.createBitmap(img.width, img.height, Bitmap.Config.ARGB_8888)
//        try {
//            val out: FileOutputStream = FileOutputStream(file)
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
//            Log.d("bitmap: ", bmp.toString())
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
//            var riversRef: StorageReference = mStorageRef.child("images/" + UUID.randomUUID().toString()) - save img with random name
            var riversRef: StorageReference = mStorageRef.child("images/${imgFile}")
            riversRef.putFile(Uri.parse(file)).addOnSuccessListener {
                riversRef.downloadUrl.addOnSuccessListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    Glide.with(context).load(it).placeholder(R.drawable.bg_book).error(R.drawable.bg_book).into(img)
                }

            }.addOnFailureListener {
                Toast.makeText(context, "Upload file fail!!", Toast.LENGTH_SHORT).show()
            }
        }else if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            list.add(ImageModel(file))
            if (recycler_view.adapter?.itemCount!! < 6) {
                recycler_view.adapter?.notifyDataSetChanged()
            }
        }
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000

        private val PERMISSION_CODE = 1001
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery(0)
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //    private fun findImageRotate(file: String): Float {
//        var degree: Float = 0F
//        try {
//            val exif = ExifInterface(file)
//            Toast.makeText(context, "tun", Toast.LENGTH_SHORT).show()
//            var orientation: Int = exif.getAttributeInt(
//                ExifInterface.TAG_ORIENTATION,
//                ExifInterface.ORIENTATION_NORMAL
//            )
//            Log.d("orientation :", orientation.toString())
//            degree = when (orientation) {
//                ExifInterface.ORIENTATION_NORMAL ->
//                    0F
//                ExifInterface.ORIENTATION_ROTATE_90 ->
//                    90F
//                ExifInterface.ORIENTATION_ROTATE_180 ->
//                    180F
//                ExifInterface.ORIENTATION_ROTATE_270 ->
//                    270F
//                ExifInterface.ORIENTATION_UNDEFINED ->
//                    0F
//                else ->
//                    90F
//            }
//        } catch (e:Exception) {
//            e.printStackTrace()
//        }
//        return degree
//    }
}