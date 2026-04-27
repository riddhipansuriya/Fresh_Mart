package com.example.fresh_mart.Database

import androidx.room.*
import com.example.fresh_mart.dataclass.WishlistItem

@Dao
interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WishlistItem)

    @Delete
    suspend fun delete(item: WishlistItem)

    @Query("SELECT * FROM wishlist")
    suspend fun getAllWishlist(): List<WishlistItem>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE id = :id)")
    suspend fun isInWishlist(id: String): Boolean

    @Query("DELETE FROM wishlist WHERE id = :id")
    fun deleteById(id: String)
}