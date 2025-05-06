package com.example.provinsi_mobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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

class AkunActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_akun)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val logout: AppCompatButton = findViewById(R.id.logout)
        logout.setOnClickListener {
            startActivity(Intent(this@AkunActivity, MainActivity::class.java))
            finish()
        }

        val sharedPreferences = this@AkunActivity.getSharedPreferences("session", Context.MODE_PRIVATE)
        val idUser = sharedPreferences.getInt("id_user", -1)
        
        if (idUser == -1){
            Toast.makeText(applicationContext, "Akun tidak ditemukan, coba hubungi pihak admin untuk informasi lebih lanjut", Toast.LENGTH_SHORT).show()
            return
        }

        val nama: TextView = findViewById(R.id.nama)
        val alamat: TextView = findViewById(R.id.alamat)
        val telepon: TextView = findViewById(R.id.telepon)
        val kembali : AppCompatButton = findViewById(R.id.kembaliAkun)

        kembali.setOnClickListener {
            finish()
        }
        RetrofitClient.instance.akun(idUser).enqueue(object : Callback<UsersResponse> {
            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                if (response.isSuccessful){
                    val user = response.body()
                    nama.text = user?.nama.toString()
                    alamat.text = user?.alamat.toString()
                    telepon.text = user?.telepon.toString()
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                Log.d("Error akun", "$t")
                Toast.makeText(applicationContext, "Akun tidak ditemukan, koneksi error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}