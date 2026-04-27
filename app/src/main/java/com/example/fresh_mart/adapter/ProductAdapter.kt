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
import com.example.fresh_mart.dataclass.WishlistItem
import com.example.fresh_mart.Database.FreshMart
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*

class ProductAdapter(
    private val list: List<Product>,
    private val context: Context
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val wishlistSet = mutableSetOf<String>()
    private val dao = FreshMart.getDatabase(context).wishlistDao()

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

        holder.name.text = product.name
        holder.desc.text = product.description
        holder.price.text = "₹${product.price}"

        val resId = context.resources.getIdentifier(
            product.image, "drawable", context.packageName
        )
        holder.img.setImageResource(
            if (resId != 0) resId else R.drawable.ic_launcher_background
        )

        try {
            holder.card.setCardBackgroundColor(Color.parseColor(product.color))
        } catch (e: Exception) {}

        try {
            holder.add.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(product.btn_bg))
        } catch (e: Exception) {}

        // ❤️ SET STATE
        if (wishlistSet.contains(product.id)) {
            holder.fav.setImageResource(R.drawable.favorite_filled)
        } else {
            holder.fav.setImageResource(R.drawable.favorite_icon)
        }

        // ❤️ CLICK
        holder.fav.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                if (wishlistSet.contains(product.id)) {

                    dao.deleteById(product.id)
                    wishlistSet.remove(product.id)

                    withContext(Dispatchers.Main) {
                        holder.fav.setImageResource(R.drawable.favorite_icon)
                        Toast.makeText(context, "Removed from wishlist", Toast.LENGTH_SHORT).show()
                    }

                } else {

                    dao.insert(
                        WishlistItem(
                            id = product.id,
                            name = product.name,
                            description = product.description,
                            price = product.price,
                            image = product.image,
                            color = product.color,
                            btn_bg = product.btn_bg,
                            categoryId = product.categoryId
                        )
                    )

                    wishlistSet.add(product.id)

                    withContext(Dispatchers.Main) {
                        holder.fav.setImageResource(R.drawable.favorite_filled)
                        Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        holder.add.setOnClickListener {
            Toast.makeText(context, "${product.name} added", Toast.LENGTH_SHORT).show()
        }
    }

    // ✅ LOAD WISHLIST
    fun loadWishlist() {
        CoroutineScope(Dispatchers.IO).launch {
            val items = dao.getAllWishlist()
            wishlistSet.clear()
            wishlistSet.addAll(items.map { it.id })

            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }
    }
}