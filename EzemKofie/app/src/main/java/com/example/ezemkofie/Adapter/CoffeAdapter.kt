package com.example.ezemkofie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ezemkofie.R
import com.example.ezemkofie.api.CoffeResponse
import com.squareup.picasso.Picasso
import android.content.Intent
import com.example.ezemkofie.CoffeeDetailActivity

class CoffeAdapter(private val list: List<CoffeResponse>) : RecyclerView.Adapter<CoffeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.img_coffe)
        val name: TextView = view.findViewById(R.id.txt_coffe_name)
        val price: TextView = view.findViewById(R.id.txt_coffe_price)
        val rating: TextView = view.findViewById(R.id.txt_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.coffe_filtercategory, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.price.text = "$ ${String.format("%.2f", item.price)}"
        holder.rating.text = item.rating.toString()

        val imageUrl = "http://192.168.0.108:5000/images/${item.imagePath}"

        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.american_classic)
            .error(R.drawable.american_classic)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CoffeeDetailActivity::class.java).apply {
                putExtra("COFFEE_ID", item.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size
}