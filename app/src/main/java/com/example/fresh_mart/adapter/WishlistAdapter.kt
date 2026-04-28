package com.example.fresh_mart.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.R
import com.example.fresh_mart.dataclass.*
import com.example.fresh_mart.Database.FreshMart
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*

class WishlistAdapter(
    private var list: MutableList<WishlistItem>,
    private val context: Context
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    private val db = FreshMart.getDatabase(context)
    private val wishDao = db.wishlistDao()
    private val cartDao = db.cartDao()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.img)
        val name: TextView = view.findViewById(R.id.name)
        val price: TextView = view.findViewById(R.id.price)
        val fav: ImageView = view.findViewById(R.id.btnFav)
        val cart: MaterialButton = view.findViewById(R.id.btnCart)
        val card: MaterialCardView = view.findViewById(R.id.rootCard)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = item.name
        holder.price.text = "₹${item.price}"

        val resId = context.resources.getIdentifier(
            item.image, "drawable", context.packageName
        )
        holder.img.setImageResource(resId)

        try {
            holder.card.setCardBackgroundColor(Color.parseColor(item.color))
            holder.cart.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(item.btn_bg))
        } catch (e: Exception) {}

        // ❤️ remove
        holder.fav.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            CoroutineScope(Dispatchers.IO).launch {
                wishDao.delete(item)

                withContext(Dispatchers.Main) {
                    list.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }
        }

        // 🛒 add to cart
        holder.cart.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            CoroutineScope(Dispatchers.IO).launch {

                val existing = cartDao.getItem(item.id)

                if (existing != null) {
                    existing.quantity++
                    cartDao.update(existing)
                } else {
                    cartDao.insert(
                        CartItem(
                            item.id, item.name,
                            item.price, item.image,
                            1, item.color, item.btn_bg
                        )
                    )
                }

                wishDao.delete(item)

                withContext(Dispatchers.Main) {
                    val index = holder.adapterPosition
                    if (index != RecyclerView.NO_POSITION) {
                        list.removeAt(index)
                        notifyItemRemoved(index)
                    }
                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wishlist, parent, false)
        return ViewHolder(view)
    }
}