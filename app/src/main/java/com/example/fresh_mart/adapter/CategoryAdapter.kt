package com.example.fresh_mart.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.R
import com.example.fresh_mart.dataclass.Category
import com.google.android.material.card.MaterialCardView

class CategoryAdapter(
    private val list: List<Category>,
    private val context: Context
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgCategory)
        val name: TextView = view.findViewById(R.id.txtCategory)
        val layout: MaterialCardView = view.findViewById(R.id.bgBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = list[position]

        holder.name.text = category.name

        val resId = context.resources.getIdentifier(
            category.image,
            "drawable",
            context.packageName
        )

        if (resId != 0) {
            holder.img.setImageResource(resId)
        } else {
            holder.img.setImageResource(R.drawable.ic_launcher_background)
        }

        try {
            val colorInt = Color.parseColor(category.color)
            holder.layout.setCardBackgroundColor(colorInt)
        } catch (e: Exception) {
            holder.layout.setCardBackgroundColor(Color.LTGRAY)
        }
    }
}