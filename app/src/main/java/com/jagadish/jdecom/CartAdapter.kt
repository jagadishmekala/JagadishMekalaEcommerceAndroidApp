package com.jagadish.jdecom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private val cartList: List<Product>,
    private val onRemove: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCartProduct: ImageView = view.findViewById(R.id.imgCartProduct)
        val tvCartName: TextView = view.findViewById(R.id.tvCartName)
        val tvCartPrice: TextView = view.findViewById(R.id.tvCartPrice)
        val btnRemove: Button = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartList[position]
        holder.tvCartName.text = product.name
        holder.tvCartPrice.text = "₹${product.price}"
        Glide.with(holder.itemView.context).load(product.image).into(holder.imgCartProduct)

        holder.btnRemove.setOnClickListener {
            onRemove(product)
        }
    }

    override fun getItemCount(): Int = cartList.size
}
