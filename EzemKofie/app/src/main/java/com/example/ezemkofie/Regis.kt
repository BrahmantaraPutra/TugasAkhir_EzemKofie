package com.example.ezemkofie

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ezemkofie.api.ApiClient
import com.example.ezemkofie.api.RegisterRequest
import kotlinx.coroutines.launch
import android.content.Intent
import android.content.Context

class Regis : AppCompatActivity() {

    private lateinit var username : EditText
    private lateinit var fullname : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var confirm : EditText
    private lateinit var linklogin : TextView
    private lateinit var btnregis : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register_page)

        username = findViewById(R.id.txt_username)
        fullname = findViewById(R.id.txt_fullname)
        email = findViewById(R.id.txt_email)
        password = findViewById(R.id.txt_password)
        confirm = findViewById(R.id.txt_confirm)
        btnregis = findViewById(R.id.btn_regis)
        linklogin = findViewById(R.id.txt_linklogin)

        linklogin.setOnClickListener {
            finish()
        }

        btnregis.setOnClickListener {
            logic()
        }
    }

    private fun logic(){
        val UserName = username.text.toString().trim()
        val FullName = fullname.text.toString().trim()
        val Email = email.text.toString().trim()
        val Password = password.text.toString().trim()
        val Confirm = confirm.text.toString().trim()

        if(UserName.isEmpty() || FullName.isEmpty() || Email.isEmpty() || Password.isEmpty() || Confirm.isEmpty()){
            Toast.makeText(this, "Semua field wajib di isi", Toast.LENGTH_SHORT).show()
            return
        }

        if(Password != Confirm){
            Toast.makeText(this, "Password dan Konfirmasi harus samaa", Toast.LENGTH_SHORT).show()
            return
        }

        if(Password.length < 4){
            Toast.makeText(this, "Password minimal 4 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try{
                val request = RegisterRequest(UserName, FullName, Email, Password)
                val response = ApiClient.instance.register(request)

                if(response.isSuccessful){
                    val token = response.body() ?: ""
                    val shared = getSharedPreferences("AUTH_DATA", android.content.Context.MODE_PRIVATE)
                    shared.edit().putString("token", token).apply()

                    Toast.makeText(this@Regis, "Selamat datang di EzemKofie", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Regis, MainActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(this@Regis, "Registrasi gagal! Username sudah terdaftar", Toast.LENGTH_SHORT).show()
                }
        }catch (e: Exception){
                Toast.makeText(this@Regis, "Registrasi Gagal", Toast.LENGTH_SHORT).show()
        }
        }
    }
}