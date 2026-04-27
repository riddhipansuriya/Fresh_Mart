package com.example.fresh_mart.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.R
import com.example.fresh_mart.dataclass.WishlistItem
import com.example.fresh_mart.dataclass.CartItem
import com.example.fresh_mart.Database.FreshMart
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*

class WishlistAdapter(
    private var list: MutableList<WishlistItem>,
    private val context: Context
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.img)
        val name: TextView = view.findViewById(R.id.name)
        val price: TextView = view.findViewById(R.id.price)
        val fav: ImageView = view.findViewById(R.id.btnFav)
        val cart: MaterialButton = view.findViewById(R.id.btnCart)
        val card: MaterialCardView = view.findViewById(R.id.rootCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wishlist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = item.name
        holder.price.text = "₹${item.price}"

        // IMAGE
        val resId = context.resources.getIdentifier(
            item.image, "drawable", context.packageName
        )
        holder.img.setImageResource(resId)

        // 🎨 CARD COLOR
        try {
            holder.card.setCardBackgroundColor(Color.parseColor(item.color))
        } catch (e: Exception) {}

        // 🎨 BUTTON COLOR
        try {
            holder.cart.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(item.btn_bg))
        } catch (e: Exception) {}

        // DATABASE INSTANCE (CREATE ONCE)
        val db = FreshMart.getDatabase(context)
        val wishDao = db.wishlistDao()
        val cartDao = db.cartDao()

        // ❤️ REMOVE FROM WISHLIST
        holder.fav.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                CoroutineScope(Dispatchers.IO).launch {
                    wishDao.delete(item)

                    withContext(Dispatchers.Main) {
                        list.removeAt(pos)
                        notifyItemRemoved(pos)
                        Toast.makeText(context, "Removed from wishlist", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 🛒 ADD TO CART + REMOVE FROM WISHLIST
        holder.cart.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {

                CoroutineScope(Dispatchers.IO).launch {

                    val existing = cartDao.getItem(item.id)

                    if (existing != null) {
                        existing.quantity += 1
                        cartDao.update(existing)
                    } else {
                        cartDao.insert(
                            CartItem(
                                id = item.id,
                                name = item.name,
                                price = item.price,
                                image = item.image,
                                quantity = 1,
                                color = item.color,
                                btn_bg = item.btn_bg
                            )
                        )
                    }

                    // REMOVE FROM WISHLIST
                    wishDao.delete(item)

                    withContext(Dispatchers.Main) {
                        list.removeAt(pos)
                        notifyItemRemoved(pos)
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}