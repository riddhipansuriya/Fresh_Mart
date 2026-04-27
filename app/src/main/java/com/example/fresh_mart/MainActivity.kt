package com.example.fresh_mart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.dataclass.Category
import com.example.fresh_mart.dataclass.Product
import com.example.fresh_mart.adapter.CategoryAdapter
import com.example.fresh_mart.adapter.ProductAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private val categoryList = ArrayList<Category>()

    private lateinit var productRecycler: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList = ArrayList<Product>()

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // CATEGORY
        recyclerView = findViewById(R.id.recyclerCategories)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.isNestedScrollingEnabled = false
        adapter = CategoryAdapter(categoryList, this)
        recyclerView.adapter = adapter

        // PRODUCT
        productRecycler = findViewById(R.id.recyclerProducts)
        productRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        productRecycler.isNestedScrollingEnabled = false
        productAdapter = ProductAdapter(productList, this)
        productRecycler.adapter = productAdapter

        fetchCategories()
        fetchProducts()

        // BOTTOM NAV
        bottomNav = findViewById(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    showHome()
                    true
                }

                R.id.nav_cart -> {
                    loadFragment(CartFragment())
                    true
                }

                R.id.nav_favorite -> {
                    loadFragment(WishlistFragment())
                    true
                }


                else -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }

    private fun showHome() {
        findViewById<View>(R.id.recyclerCategories).visibility = View.VISIBLE
        findViewById<View>(R.id.recyclerProducts).visibility = View.VISIBLE

        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }

        // 🔥 REFRESH WISHLIST STATE
        productAdapter.loadWishlist()
    }

    private fun loadFragment(fragment: Fragment) {
        findViewById<View>(R.id.recyclerCategories).visibility = View.GONE
        findViewById<View>(R.id.recyclerProducts).visibility = View.GONE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun fetchCategories() {
        val db = FirebaseFirestore.getInstance()

        db.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                categoryList.clear()

                for (doc in result) {
                    val category = doc.toObject(Category::class.java)
                    categoryList.add(category)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchProducts() {
        val db = FirebaseFirestore.getInstance()

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                productList.clear()

                for (doc in result) {
                    val product = doc.toObject(Product::class.java)

                    // ✅ IMPORTANT (FIXES EVERYTHING)
                    product.id = doc.id

                    productList.add(product)
                }

                productAdapter.notifyDataSetChanged()

                // ✅ LOAD WISHLIST AFTER DATA
                productAdapter.loadWishlist()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
    }
}