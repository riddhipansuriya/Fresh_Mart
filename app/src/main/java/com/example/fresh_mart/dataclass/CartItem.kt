package com.example.fresh_mart.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey val id: String,
    val name: String,
    val price: Int,
    val image: String,
    var quantity: Int = 1,

    // ✅ REQUIRED (for colors)
    val color: String = "",
    val btn_bg: String = ""
)