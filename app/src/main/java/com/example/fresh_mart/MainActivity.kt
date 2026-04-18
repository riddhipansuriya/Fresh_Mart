package com.example.fresh_mart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fresh_mart.dataclass.Category
import com.example.fresh_mart.dataclass.Product
import com.example.fresh_mart.adapter.CategoryAdapter
import com.example.fresh_mart.adapter.ProductAdapter
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private val categoryList = ArrayList<Category>()


    private lateinit var productRecycler: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerCategories)

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.isNestedScrollingEnabled = false

        adapter = CategoryAdapter(categoryList, this)
        recyclerView.adapter = adapter


        productRecycler = findViewById(R.id.recyclerProducts)

        productRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        productRecycler.isNestedScrollingEnabled = false

        productAdapter = ProductAdapter(productList, this)
        productRecycler.adapter = productAdapter

        fetchCategories()
        fetchProducts()
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

    // ✅ ADD THIS FUNCTION (products)
    private fun fetchProducts() {
        val db = FirebaseFirestore.getInstance()

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                productList.clear()

                for (doc in result) {
                    val product = doc.toObject(Product::class.java)
                    productList.add(product)
                }

                productAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
    }
}