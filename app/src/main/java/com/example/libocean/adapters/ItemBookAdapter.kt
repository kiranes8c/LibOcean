package com.example.libocean.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libocean.R
import com.example.libocean.models.BookInfo

class ItemBookAdapter(
    var bookList: ArrayList<BookInfo>
): RecyclerView.Adapter<ItemBookAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBookAdapter.ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_book,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.context).load(bookList[position].imgUrl).placeholder(R.drawable.bg_book).error(R.drawable.bg_book).into(holder.img)
        holder.name.text = bookList[position].name
        holder.price.text = bookList[position].price.toString()
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var context: Context = view.context
        var img: ImageView = view.findViewById(R.id.item_book_img)
        var name: TextView = view.findViewById(R.id.item_book_name)
        var price: TextView = view.findViewById(R.id.item_book_price)
    }
}