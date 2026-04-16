package com.example.ezemkofie

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ezemkofie.api.ApiClient
import com.example.ezemkofie.api.CheckoutReq
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class CoffeeDetailActivity : AppCompatActivity() {

    private lateinit var txtName: TextView
    private lateinit var txtDesc: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtQty: TextView
    private lateinit var imgCoffee: ImageView

    private var qty = 1
    private var basePrice = 0.0
    private var coffeeId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coffee_view)

        coffeeId = intent.getIntExtra("COFFEE_ID", -1)

        initView()
        loadCoffeeDetail()

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }

        findViewById<ImageButton>(R.id.btn_plus).setOnClickListener {
            qty++
            updateUI()
        }
        findViewById<ImageButton>(R.id.btn_minus).setOnClickListener {
            if (qty > 1) {
                qty--
                updateUI()
            }
        }

        findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            showConfirmDialog()
        }
    }

    private fun initView() {
        txtName = findViewById(R.id.txt_name)
        txtDesc = findViewById(R.id.txt_desc)
        txtPrice = findViewById(R.id.txt_price)
        txtQty = findViewById(R.id.txt_qty)
        imgCoffee = findViewById(R.id.img_coffee)
    }

    private fun loadCoffeeDetail() {
        val shared = getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE)
        val token = "Bearer ${shared.getString("token", "")}"

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getCoffeeById(token, coffeeId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        txtName.text = it.name
                        txtDesc.text = it.description
                        basePrice = it.price

                        val imageUrl = "http://192.168.0.108:5000/images/${it.imagePath}"
                        Picasso.get().load(imageUrl).into(imgCoffee)

                        updateUI()
                    }
                } else {
                    Toast.makeText(this@CoffeeDetailActivity, "Gagal memuat data (Code: ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CoffeeDetailActivity, "Error Koneksi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI() {
        txtQty.text = qty.toString()
        val total = basePrice * qty
        txtPrice.text = "$ ${String.format("%.2f", total)}"
    }

    private fun showConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Checkout")
            .setMessage("Apakah Anda yakin ingin memesan kopi ini?")
            .setPositiveButton("Yes") { _, _ -> processCheckout() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun processCheckout() {
        val shared = getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE)
        val token = "Bearer ${shared.getString("token", "")}"

        val item = CheckoutReq(
            coffeeId = coffeeId,
            size = "M",
            qty = qty
        )

        val requestList = listOf(item)

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.transaksi(token, requestList)
                if (response.isSuccessful) {
                    Toast.makeText(this@CoffeeDetailActivity, "Checkout Berhasil!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorMsg = response.errorBody()?.string()
                    Toast.makeText(this@CoffeeDetailActivity, "Gagal: $errorMsg", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CoffeeDetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}