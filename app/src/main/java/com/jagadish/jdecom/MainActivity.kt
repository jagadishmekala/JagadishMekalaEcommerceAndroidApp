package com.jagadish.jdecom

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private val cartList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_dark)
        setSupportActionBar(findViewById(R.id.toolbar))

        try {
            val products = loadProducts()

            // Categories: All + distinct categories from products
            val categories = listOf("All") + products.map { it.category }.distinct()

            // Setup Category RecyclerView (Horizontal)
            val rvCategories = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvCategories)
            rvCategories.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            categoryAdapter = CategoryAdapter(categories) { category ->
                val filteredProducts = if (category == "All") products
                else products.filter { it.category == category }
                productAdapter.updateData(filteredProducts)
            }
            rvCategories.adapter = categoryAdapter

            // Setup Product RecyclerView (Grid 2 columns)
            val rvProducts = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvProducts)
            rvProducts.layoutManager = GridLayoutManager(this, 2)

            productAdapter = ProductAdapter(products) { product ->
                if (!cartList.contains(product)) {
                    cartList.add(product)
                    Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
                }
            }
            rvProducts.adapter = productAdapter

        } catch (e: Exception) {
            Toast.makeText(this, "Error loading products: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun loadProducts(): List<Product> {
        val inputStream = resources.openRawResource(R.raw.products)
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(reader, type)
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == R.id.action_cart) {
            val intent = Intent(this, CartActivity::class.java)
            intent.putParcelableArrayListExtra("cart", ArrayList(cartList))
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
