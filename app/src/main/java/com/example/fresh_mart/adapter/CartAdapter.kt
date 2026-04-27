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
import com.example.fresh_mart.dataclass.CartItem
import com.example.fresh_mart.Database.FreshMart
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*

class CartAdapter(
    private var list: MutableList<CartItem>,
    private val context: Context
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.rootCard)
        val name: TextView = view.findViewById(R.id.name)
        val price: TextView = view.findViewById(R.id.price)
        val qty: TextView = view.findViewById(R.id.txtQty)
        val plus: Button = view.findViewById(R.id.btnPlus)
        val minus: Button = view.findViewById(R.id.btnMinus)
        val delete: ImageView = view.findViewById(R.id.btnDelete)
        val img: ImageView = view.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val dao = FreshMart.getDatabase(context).cartDao()


        holder.name.text = item.name
        holder.price.text = "₹${item.price}"
        holder.qty.text = item.quantity.toString()


        val resId = context.resources.getIdentifier(
            item.image, "drawable", context.packageName
        )
        holder.img.setImageResource(
            if (resId != 0) resId else R.drawable.ic_launcher_background
        )


        try {
            holder.card.setCardBackgroundColor(
                Color.parseColor(item.color.ifEmpty { "#FFFFFF" })
            )
        } catch (e: Exception) {}

        // 🎨 BUTTON COLOR (from Firebase)
        try {
            val btnColor = Color.parseColor(item.btn_bg.ifEmpty { "#4CAF50" })

            holder.plus.backgroundTintList = ColorStateList.valueOf(btnColor)
            holder.minus.backgroundTintList = ColorStateList.valueOf(btnColor)

        } catch (e: Exception) {}

        // ➕ INCREASE QUANTITY
        holder.plus.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                item.quantity++
                dao.update(item)

                withContext(Dispatchers.Main) {
                    notifyItemChanged(position)
                }
            }
        }

        // ➖ DECREASE QUANTITY
        holder.minus.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                if (item.quantity > 1) {
                    item.quantity--
                    dao.update(item)

                    withContext(Dispatchers.Main) {
                        notifyItemChanged(position)
                    }

                } else {
                    dao.delete(item)

                    withContext(Dispatchers.Main) {
                        list.removeAt(position)
                        notifyItemRemoved(position)
                        Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 🗑️ DELETE ITEM
        holder.delete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dao.delete(item)

                withContext(Dispatchers.Main) {
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Deleted from cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}