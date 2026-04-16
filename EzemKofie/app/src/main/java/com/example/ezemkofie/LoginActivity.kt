package com.example.ezemkofie

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Response
import retrofit2.Retrofit
import android.content.Intent
import android.content.Context
import android.widget.Toast
import com.example.ezemkofie.api.ApiClient
import com.example.ezemkofie.api.LoginRequest
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class LoginActivity : AppCompatActivity() {

    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var linkregis : TextView
    private lateinit var btnlogin : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        username = findViewById(R.id.txt_username)
        password = findViewById(R.id.txt_password)
        linkregis = findViewById(R.id.txt_linkregis)
        btnlogin = findViewById(R.id.btn_login)

        linkregis.setOnClickListener {
            startActivity(Intent(this, Regis::class.java))
        }

        btnlogin.setOnClickListener {
            loginuer()
        }
    }

    private fun loginuer() {
        val user = username.text.toString().trim()
        val password = password.text.toString().trim()

        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Semua field wajib di isi", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {

            try {
                val request = LoginRequest(user, password)
                val response = ApiClient.instance.login(request)

                if(response.isSuccessful){
                    val token = response.body() ?: ""
                    val shared = getSharedPreferences("AUTH_DATA", android.content.Context.MODE_PRIVATE)
                    shared.edit().putString("token", token).apply()

                    Toast.makeText(this@LoginActivity, "Selamat datang di EzemKofie", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception){
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        }

    }
}