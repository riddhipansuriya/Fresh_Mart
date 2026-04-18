package com.example.fresh_mart.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.R
import com.example.fresh_mart.dataclass.Product
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class ProductAdapter(
    private val list: List<Product>,
    private val context: Context
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgProduct)
        val name: TextView = view.findViewById(R.id.txtName)
        val desc: TextView = view.findViewById(R.id.txtDesc)
        val price: TextView = view.findViewById(R.id.txtPrice)
        val card: MaterialCardView = view.findViewById(R.id.cardProduct)
        val fav: ImageView = view.findViewById(R.id.btnFav)
        val add: MaterialButton = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = list[position]

        // TEXT DATA
        holder.name.text = product.name
        holder.desc.text = product.description
        holder.price.text = "₹${product.price}"

        // IMAGE FROM DRAWABLE
        val resId = context.resources.getIdentifier(
            product.image, "drawable", context.packageName
        )
        holder.img.setImageResource(
            if (resId != 0) resId else R.drawable.ic_launcher_background
        )

        // CARD COLOR
        try {
            holder.card.setCardBackgroundColor(Color.parseColor(product.color))
        } catch (e: Exception) {}




        holder.add.setOnClickListener {
            Toast.makeText(context, "${product.name} added", Toast.LENGTH_SHORT).show()
        }
    }
}