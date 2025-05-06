package com.example.provinsi_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val login : AppCompatButton = findViewById(R.id.btnLogin)
        val daftar: TextView = findViewById(R.id.daftar)
        daftar.setOnClickListener {
            startActivity(Intent(applicationContext, DaftarActivity::class.java))
        }
        login.setOnClickListener {
            val call = RetrofitClient.instance.login(
                email.text.toString(),
                password.text.toString()
            )
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful){
                        Log.d("Login respon", "$response")
                        val user = response.body()?.users
                        val sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("id_user", user?.id_user ?: -1)
                        editor.apply()
                        Toast.makeText(this@MainActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@MainActivity,"Email atau password anda salah", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"Login gagal", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}