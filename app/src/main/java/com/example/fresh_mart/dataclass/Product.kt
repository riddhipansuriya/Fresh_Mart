package com.example.fresh_mart.dataclass

data class Product(
    var name: String = "",
    var description: String = "",
    var price: Int = 0,
    var image: String = "",
    var color: String = "",
    var btn_bg: String = "",
    var categoryId: String = ""
)