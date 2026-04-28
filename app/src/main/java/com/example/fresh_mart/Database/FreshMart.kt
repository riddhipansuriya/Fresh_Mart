package com.example.fresh_mart.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fresh_mart.dataclass.WishlistItem
import com.example.fresh_mart.dataclass.CartItem

@Database(
    entities = [WishlistItem::class, CartItem::class],
    version = 2,
    exportSchema = false
)
abstract class FreshMart : RoomDatabase() {

    abstract fun wishlistDao(): WishlistDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: FreshMart? = null

        fun getDatabase(context: Context): FreshMart {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FreshMart::class.java,
                    "freshmart_db"
                )
                    .fallbackToDestructiveMigration()  // ✅ VERY IMPORTANT
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}