package com.example.fresh_mart.dataclass
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val image: String,
    val color: String,
    val btn_bg: String,
    val categoryId: String

)
