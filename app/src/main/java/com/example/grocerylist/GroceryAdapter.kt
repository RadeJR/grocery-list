package com.example.grocerylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroceryAdapter(
    private val groceries: MutableList<Grocery>,
    private val onPriceChange: (Float) -> Unit
) : RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>() {

    private var expandedItem: Int = -1

    class GroceryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGroceryName: TextView = itemView.findViewById<TextView>(R.id.tvGroceryName) // Find and store the TextView reference
        val tvPrice: TextView = itemView.findViewById<TextView>(R.id.tvPrice)
        val tvQuantity: TextView = itemView.findViewById<TextView>(R.id.tvQuantity)
        val btnRemove: Button = itemView.findViewById<Button>(R.id.btnRemove)
    }

    fun addGrocery(grocery: Grocery) {
        groceries.add(grocery)
        notifyItemInserted(groceries.size -1)
    }

    fun priceSum(): Float {
        var sum: Float = 0f
        groceries.forEach { grocery: Grocery -> sum += grocery.price * grocery.quantity }
        return sum
    }

    fun removeAll() {
        groceries.clear()
        expandedItem = -1
        notifyDataSetChanged()
    }

    private fun removeItem(position: Int) {
        val removedGrocery = groceries.removeAt(position) // Remove item from the list
        notifyItemRemoved(position)  // Notify the adapter that an item was removed
        onPriceChange(removedGrocery.price*removedGrocery.quantity)
        expandedItem = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        return GroceryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_grocery,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val currentGrocery = groceries[position]
        holder.tvGroceryName.text = currentGrocery.name
        holder.tvPrice.text = String.format(currentGrocery.price.toString()+"rsd")
        holder.tvQuantity.text = String.format(currentGrocery.quantity.toString())

        holder.btnRemove.setOnClickListener {
            removeItem(holder.adapterPosition)
        }

        holder.btnRemove.visibility = if (expandedItem == position) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            val currentPosition = holder.adapterPosition
            expandedItem = if (expandedItem == currentPosition) {
                -1
            } else {
                currentPosition
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return groceries.size
    }
}