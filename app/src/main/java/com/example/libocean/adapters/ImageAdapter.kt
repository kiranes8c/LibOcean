package com.example.libocean.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libocean.R
import com.example.libocean.models.ImageModel
import java.util.zip.Inflater

public class  ImageAdapter(
    private var list: ArrayList<ImageModel>
): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        var view: View =LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.cont).load(list[position].image).into(holder.img)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var img: ImageView = view.findViewById(R.id.item_book_img)
        var cont: Context? = view.context
    }
}
