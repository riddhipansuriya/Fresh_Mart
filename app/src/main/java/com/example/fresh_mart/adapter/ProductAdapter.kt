package com.example.fresh_mart.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.R
import com.example.fresh_mart.dataclass.Product
import com.example.fresh_mart.dataclass.WishlistItem
import com.example.fresh_mart.dataclass.CartItem
import com.example.fresh_mart.Database.FreshMart
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*

class ProductAdapter(
    private val list: List<Product>,
    private val context: Context
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val wishlistSet = mutableSetOf<String>()
    private val db = FreshMart.getDatabase(context)
    private val wishDao = db.wishlistDao()
    private val cartDao = db.cartDao()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgProduct)
        val name: TextView = view.findViewById(R.id.txtName)
        val desc: TextView = view.findViewById(R.id.txtDesc)
        val price: TextView = view.findViewById(R.id.txtPrice)
        val card: MaterialCardView = view.findViewById(R.id.cardProduct)
        val fav: ImageView = view.findViewById(R.id.btnFav)
        val add: MaterialButton = view.findViewById(R.id.btnAdd)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = list[position]

        holder.name.text = product.name
        holder.desc.text = product.description
        holder.price.text = "₹${product.price}"

        val resId = context.resources.getIdentifier(
            product.image, "drawable", context.packageName
        )
        holder.img.setImageResource(resId)

        try {
            holder.card.setCardBackgroundColor(Color.parseColor(product.color))
            holder.add.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(product.btn_bg))
        } catch (e: Exception) {}

        holder.fav.setImageResource(
            if (wishlistSet.contains(product.id)) R.drawable.favorite_filled
            else R.drawable.favorite_icon
        )

        // ❤️ wishlist
        holder.fav.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                if (wishlistSet.contains(product.id)) {
                    wishDao.deleteById(product.id)
                    wishlistSet.remove(product.id)
                } else {
                    wishDao.insert(
                        WishlistItem(
                            product.id, product.name, product.description,
                            product.price, product.image,
                            product.color, product.btn_bg, product.categoryId
                        )
                    )
                    wishlistSet.add(product.id)
                }

                withContext(Dispatchers.Main) {
                    notifyItemChanged(position)
                }
            }
        }

        // 🛒 cart
        holder.add.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val existing = cartDao.getItem(product.id)

                if (existing != null) {
                    existing.quantity++
                    cartDao.update(existing)
                } else {
                    cartDao.insert(
                        CartItem(
                            product.id, product.name,
                            product.price, product.image,
                            1, product.color, product.btn_bg
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    fun loadWishlist() {
        CoroutineScope(Dispatchers.IO).launch {
            val items = wishDao.getAllWishlist()
            wishlistSet.clear()
            wishlistSet.addAll(items.map { it.id })

            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }
    }
}