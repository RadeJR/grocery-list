package com.example.grocerylist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var groceryAdapter: GroceryAdapter
    private lateinit var tvTotalPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        groceryAdapter = GroceryAdapter(mutableListOf(), ::updateTotalPrice)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)

        val rvGroceryItems = findViewById<RecyclerView>(R.id.rvGroceryItems)
        rvGroceryItems.adapter = groceryAdapter
        rvGroceryItems.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            val etGroceryName = findViewById<EditText>(R.id.etGroceryName)
            val etPrice = findViewById<EditText>(R.id.etPrice)
            val etQuantity = findViewById<EditText>(R.id.etQuantity)

            if (etGroceryName.text.toString().isEmpty()) {
                etGroceryName.error = "Name cannot be empty"
            } else if (etPrice.text.toString().isEmpty()) {
                etPrice.error = "Price cannot be empty"
            } else if (etQuantity.text.toString().isEmpty()) {
                etQuantity.error = "Quantity cannot be empty"
            } else {
                val name = etGroceryName.text.toString()
                val price = etPrice.text.toString().toFloat()
                val quantity = etQuantity.text.toString().toInt()

                val grocery = Grocery(name, price, quantity)
                groceryAdapter.addGrocery(grocery)
                tvTotalPrice.text = String.format(groceryAdapter.priceSum().toString() + "rsd")
                clearFields()
            }
        }

        findViewById<Button>(R.id.btnRemoveAll).setOnClickListener {
            groceryAdapter.removeAll()
            tvTotalPrice.text = "0rsd"
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            clearFields()
        }
    }

    fun updateTotalPrice(priceChange: Float) {
        var price = tvTotalPrice.text.toString().dropLast(3).toFloat()
        price -= priceChange // Subtract the removed item's price
        tvTotalPrice.text = String.format(price.toString() + "rsd") // Update TextView
    }

    fun clearFields() {
        findViewById<EditText>(R.id.etGroceryName).setText("")
        findViewById<EditText>(R.id.etPrice).setText("")
        findViewById<EditText>(R.id.etQuantity).setText("")
    }
}