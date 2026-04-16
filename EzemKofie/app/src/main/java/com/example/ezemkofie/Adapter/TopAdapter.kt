package com.example.ezemkofie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ezemkofie.R
import com.example.ezemkofie.api.TopPick
import com.squareup.picasso.Picasso

class TopAdapter(private val list: List<TopPick>) : RecyclerView.Adapter<TopAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.top_image)
        val name: TextView = view.findViewById(R.id.top_name)
        val category: TextView = view.findViewById(R.id.top_category)
        val price: TextView = view.findViewById(R.id.top_price)
        val rating: TextView = view.findViewById(R.id.top_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.coffee_topicks, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.category.text = item.category
        holder.price.text = "$ ${item.price}"
        holder.rating.text = item.rating.toString()

        val imageUrl = "http://192.168.0.108:5000/images/${item.imagePath}"

        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.american_classic)
            .error(R.drawable.american_classic)
            .into(holder.img)
    }

    override fun getItemCount(): Int = list.size
}