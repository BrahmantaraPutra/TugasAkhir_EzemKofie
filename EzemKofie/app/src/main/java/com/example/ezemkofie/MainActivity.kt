package com.example.ezemkofie

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import com.example.ezemkofie.api.ApiClient
import com.example.ezemkofie.api.CoffeResponse
import com.example.ezemkofie.api.coffeCategory
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ezemkofie.adapter.CoffeAdapter
import com.example.ezemkofie.adapter.TopAdapter
import com.example.ezemkofie.api.TopPick

class MainActivity : AppCompatActivity() {

    private lateinit var fullname: TextView
    private lateinit var container: LinearLayout
    private lateinit var rvCoffe: RecyclerView
    private lateinit var adapter: CoffeAdapter

    private lateinit var rvTop: RecyclerView
    private lateinit var topAdaptrer: TopAdapter

    private var CharId: Int = 4
    private var listCoffe = mutableListOf<CoffeResponse>()
    private var listTop = mutableListOf<TopPick>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_page)

        fullname = findViewById(R.id.txt_fullname)
        container = findViewById(R.id.con_cat)
        rvCoffe = findViewById(R.id.rv_coffe)
        rvTop = findViewById(R.id.top_coffe)

        adapter = CoffeAdapter(listCoffe)
        rvCoffe.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCoffe.adapter = adapter

        topAdaptrer = TopAdapter(listTop)
        rvTop.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTop.adapter = topAdaptrer
        rvTop.isNestedScrollingEnabled = false


        val shared = getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE)
        val token = shared.getString("token", null)

        if (token == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        getProfile(token)
        fetchCategory(token)
        fetchTop(token)
    }

    private fun fetchTop(token: String){
        lifecycleScope.launch {
            try{
                val response = ApiClient.instance.getTop("Bearer $token")
                if (response.isSuccessful) {
                    listTop.clear()
                    listTop.addAll(response.body() ?: emptyList())
                    topAdaptrer.notifyDataSetChanged()
                }
            } catch (e: Exception){
                Toast.makeText(this@MainActivity, "Gagal memuat TopPics", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCategory(token: String) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getCategory("Bearer $token")
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    renderCategory(list)
                    loadCoffe("Americano")
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Gagal load kategori", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderCategory(listCategory: List<coffeCategory>) {
        container.removeAllViews()
        for (cat in listCategory) {
            val btn = Button(this).apply {
                text = cat.name
                isAllCaps = false
                textSize = 14f
                setPadding(0, 0, 0, 0)
                includeFontPadding = false

                if (cat.id == CharId) {
                    setBackgroundResource(R.drawable.bgchar_active)
                    setTextColor(Color.WHITE)
                } else {
                    setBackgroundResource(R.drawable.bgchar_inactive)
                    setTextColor(Color.BLACK)
                }

                setOnClickListener {
                    CharId = cat.id
                    renderCategory(listCategory)
                    loadCoffe(cat.name)
                }
            }
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 110)
            params.setMargins(0, 0, 20, 0)
            btn.layoutParams = params
            container.addView(btn)
        }
    }

    private fun loadCoffe(categoryName: String) {
        val shared = getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE)
        val token = shared.getString("token", "") ?: ""

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getCoffe("Bearer $token")
                if (response.isSuccessful) {
                    val allData = response.body() ?: emptyList()
                    listCoffe.clear()
                    listCoffe.addAll(allData.filter { it.category == categoryName })
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Gagal load kopi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getProfile(token: String) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getUser("Bearer $token")
                if (response.isSuccessful) {
                    fullname.text = response.body()?.fullName ?: "User"
                } else {
                    logout()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        val shared = getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE)
        shared.edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}