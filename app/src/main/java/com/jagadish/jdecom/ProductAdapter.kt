package com.jagadish.jdecom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Keep track of added products
    private val addedProducts = mutableSetOf<Int>()

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        addedProducts.clear() // reset added state when filtering
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val isAdded = addedProducts.contains(product.id)

        holder.bind(product, isAdded)

        holder.addToCartBtn.setOnClickListener {
            if (!addedProducts.contains(product.id)) {
                onAddToCart(product)
                addedProducts.add(product.id)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.ivProduct)
        private val nameTextView: TextView = itemView.findViewById(R.id.tvProductName)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvProductPrice)
        private val categoryTextView: TextView = itemView.findViewById(R.id.tvProductCategory)
        val addToCartBtn: Button = itemView.findViewById(R.id.btnAddToCart)

        fun bind(product: Product, isAdded: Boolean) {
            // Load image with Picasso
            Picasso.get()
                .load(product.image)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .fit()
                .centerCrop()
                .into(imageView)

            nameTextView.text = product.name
            priceTextView.text = "₹${product.price}"
            categoryTextView.text = product.category

            if (isAdded) {
                addToCartBtn.text = "Added"
                addToCartBtn.isEnabled = false
            } else {
                addToCartBtn.text = "Add to Cart"
                addToCartBtn.isEnabled = true
            }
        }
    }
}
