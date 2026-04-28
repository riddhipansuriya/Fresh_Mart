package com.example.fresh_mart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.adapter.CategoryAdapter
import com.example.fresh_mart.adapter.ProductAdapter
import com.example.fresh_mart.dataclass.Category
import com.example.fresh_mart.dataclass.Product
import com.example.fresh_mart.CartFragment
import com.example.fresh_mart.WishlistFragment
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
    private lateinit var homeLayout: View   // ✅ IMPORTANT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ HOME LAYOUT
        homeLayout = findViewById(R.id.homeLayout)

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

    // ✅ FIXED HOME SHOW
    private fun showHome() {
        homeLayout.visibility = View.VISIBLE

        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }

        productAdapter.loadWishlist()
    }

    // ✅ FIXED FRAGMENT LOAD
    private fun loadFragment(fragment: Fragment) {

        homeLayout.visibility = View.GONE   // 🔥 THIS FIXES YOUR ISSUE

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
                    product.id = doc.id   // ✅ IMPORTANT
                    productList.add(product)
                }

                productAdapter.notifyDataSetChanged()
                productAdapter.loadWishlist()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
    }
}