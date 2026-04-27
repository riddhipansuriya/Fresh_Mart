package com.example.fresh_mart.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fresh_mart.dataclass.CartItem

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItem)

    @Update
    suspend fun update(item: CartItem)

    @Delete
    suspend fun delete(item: CartItem)

    // ✅ FOR CART SCREEN
    @Query("SELECT * FROM cart")
    fun getAll(): LiveData<List<CartItem>>

    // ✅ FOR ADD TO CART CHECK
    @Query("SELECT * FROM cart WHERE id = :id LIMIT 1")
    suspend fun getItem(id: String): CartItem?
}