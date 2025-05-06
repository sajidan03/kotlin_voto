package com.example.provinsi_mobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class DetailProduk : AppCompatActivity() {

    lateinit var session: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)
        session = getSharedPreferences("session", Context.MODE_PRIVATE)

        val nama = intent.getStringExtra("nama_barang")
        val harga = intent.getStringExtra("harga_barang")
        val gambar = intent.getStringExtra("image")

        val checkout: Button = findViewById(R.id.checkout)
        val imageView = findViewById<ImageView>(R.id.product_image)
        val nameTextView = findViewById<TextView>(R.id.product_name)
        val priceTextView = findViewById<TextView>(R.id.product_price)

        nameTextView.text = nama
        priceTextView.text = "Rp$harga"

        val back: TextView = findViewById(R.id.kembali)
        back.setOnClickListener {
            startActivity(Intent(this@DetailProduk, HomeActivity::class.java))
            finish()
        }

        Picasso.get().load(gambar).into(imageView)

        checkout.setOnClickListener {
            try {
                val editor = session.edit()
                val product = JSONObject().apply {
                    put("id", "id-${System.currentTimeMillis()}")
                    put("nama", nama)
                    put("harga", harga)
                    put("jumlah", "1")
                    put("gambar", gambar ?: "")
                }

                val existingData = session.getString("cart_list", null)
                val jsonArray = if (existingData != null) JSONArray(existingData) else JSONArray()
                jsonArray.put(product)

                editor.putString("cart_list", jsonArray.toString())
                editor.apply()

                val intent = Intent(this@DetailProduk, CartActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.d("Error detail", "$e")
            }
        }
    }
}