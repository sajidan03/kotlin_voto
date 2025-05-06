//package com.example.provinsi_mobile
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.ListView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.AppCompatButton
//import com.squareup.picasso.Picasso
//
//class CartActivity : AppCompatActivity(), CartAdapter.OnQuantityChangeListener {
//
//    private lateinit var cartList: MutableList<CartModel>
//    private var total = 0
//    private lateinit var totalText: TextView
//    private lateinit var listView: ListView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_cart)
//        val checkout: AppCompatButton = findViewById(R.id.bayar)
//        listView = findViewById(R.id.list_menu)
//        totalText = findViewById(R.id.total)
//
//        //
//        val namaProduk = intent.getStringExtra("nama_produk")
//        val hargaProduk = intent.getStringExtra("harga_produk")
//        val gambarProduk = intent.getStringExtra("image")
//
//
//        cartList = mutableListOf()
//
//        if (!namaProduk.isNullOrEmpty() && !hargaProduk.isNullOrEmpty()) {
//            val item = CartModel(
//                id = "id-${System.currentTimeMillis()}",
//                nama = namaProduk,
//                harga = hargaProduk,
//                jumlah = "1",
//                gambar = gambarProduk ?: ""
//            )
//            cartList.add(item)
//        }
////        if (!namaProduk.isNullOrEmpty() && !hargaProduk.isNullOrEmpty()) {
////            val item = ProdukModel(
////                id = "id-${System.currentTimeMillis()}",
////                nama = namaProduk,
////                harga = hargaProduk,
////                gambar = gambarProduk ?: ""
////            )
////            CartTemp.cartList.add(item)
////        }
//
//
//        val adapter = CartAdapter(this, cartList, this)
//        listView.adapter = adapter
//        checkout.setOnClickListener {
//            val intent = Intent(this@CartActivity, CheckoutActivity::class.java)
//            intent.putExtra("total_harga", total)
//            startActivity(intent)
//        }
//        updateTotalHarga()
//    }
//
//
//    override fun onQuantityChanged() {
//        updateTotalHarga()
//    }
//
//    private fun updateTotalHarga() {
//        total = 0
//
//        for (item in cartList) {
//            val harga = item.harga.replace(Regex("[^\\d]"), "").toIntOrNull() ?: 0
//            val jumlah = item.jumlah.toIntOrNull() ?: 0
//            total += harga * jumlah
//        }
//        totalText.text = "Rp. $total"
//    }
//
//}

package com.example.provinsi_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity(), CartAdapter.OnQuantityChangeListener {

    private lateinit var cartList: MutableList<CartModel>
    private var total = 0
    private lateinit var totalText: TextView
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        enableEdgeToEdge()
        val checkout: AppCompatButton = findViewById(R.id.bayar)
        listView = findViewById(R.id.list_menu)
        totalText = findViewById(R.id.total)
        cartList = loadCartFromSharedPreferences()

        val adapter = CartAdapter(this, cartList, this)
        listView.adapter = adapter
        val back : TextView = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        }
        checkout.setOnClickListener {
            val intent = Intent(this@CartActivity, CheckoutActivity::class.java)
            intent.putExtra("total_harga", total)
            startActivity(intent)
        }

        updateTotalHarga()
    }

    private fun loadCartFromSharedPreferences(): MutableList<CartModel> {
        val list = mutableListOf<CartModel>()
        val session = getSharedPreferences("session", Context.MODE_PRIVATE)
        val cartString = session.getString("cart_list", null)

        if (!cartString.isNullOrEmpty()) {
            val jsonArray = JSONArray(cartString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val item = CartModel(
                    id = obj.getString("id"),
                    nama = obj.getString("nama"),
                    harga = obj.getString("harga"),
                    jumlah = obj.getString("jumlah"),
                    gambar = obj.getString("gambar")
                )
                list.add(item)
            }
        }
        return list
    }

    override fun onQuantityChanged() {
        updateTotalHarga()
    }

    private fun updateTotalHarga() {
        total = 0
        for (item in cartList) {
            val harga = item.harga.replace(Regex("[^\\d]"), "").toIntOrNull() ?: 0
            val jumlah = item.jumlah.toIntOrNull() ?: 0
            total += harga * jumlah
        }
        totalText.text = "Rp. $total"
    }
}
