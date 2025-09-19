package com.jagadish.jdecom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var tvTotal: TextView
    private lateinit var btnBuyNow: Button
    private var cartList = mutableListOf<Product>()
    private val UPI_PAYMENT_REQUEST = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_dark)

        // Toolbar back button
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarCart)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        tvTotal = findViewById(R.id.tvTotal)
        btnBuyNow = findViewById(R.id.btnBuyNow)

        // Get cart items from intent
        cartList = intent.getParcelableArrayListExtra("cart") ?: mutableListOf()

        // Setup RecyclerView
        val rvCart = findViewById<RecyclerView>(R.id.rvCart)
        rvCart.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cartList) { product ->
            cartList.remove(product)
            updateTotal()
        }
        rvCart.adapter = cartAdapter

        updateTotal()

        // Buy Now button triggers UPI payment
        btnBuyNow.setOnClickListener {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                payUsingUPI(cartList.sumOf { it.price })
            }
        }
    }

    private fun updateTotal() {
        val total = cartList.sumOf { it.price }
        tvTotal.text = "Total: ₹%.2f".format(total)
        cartAdapter.notifyDataSetChanged()
    }

    private fun payUsingUPI(amount: Double) {
        val uri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", "8106238184@paytm") // UPI ID
            .appendQueryParameter("pn", "Jagadish Store")   // Payee Name
            .appendQueryParameter("tr", "ORDER${System.currentTimeMillis()}")
            .appendQueryParameter("tn", "Payment for ${cartList.size} items")
            .appendQueryParameter("am", amount.toString())
            .appendQueryParameter("cu", "INR")
            .build()

        val upiIntent = Intent(Intent.ACTION_VIEW)
        upiIntent.data = uri

        // Use chooser to show all UPI apps
        try {
            startActivityForResult(Intent.createChooser(upiIntent, "Pay with"), UPI_PAYMENT_REQUEST)
        } catch (e: Exception) {
            Toast.makeText(this, "No UPI app found. Please install a UPI app.", Toast.LENGTH_LONG).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPI_PAYMENT_REQUEST) {
            val response = data?.getStringExtra("response")
            if (response != null && response.lowercase().contains("success")) {
                Toast.makeText(this, "Payment Successful 🙏", Toast.LENGTH_LONG).show()
                cartList.clear()
                cartAdapter.notifyDataSetChanged()
                updateTotal()
            } else {
                Toast.makeText(this, "Payment Failed or Cancelled", Toast.LENGTH_LONG).show()
            }
        }
    }
}
