package com.example.provinsi_mobile

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuAdapter
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    private val id = mutableListOf<String>()
    private val nama = mutableListOf<String>()
    private val harga = mutableListOf<String>()
    private val jumlah = mutableListOf<String>()
    private var totalbeli = 0
    lateinit var productAdapter: ProdukAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val cart: ImageView = findViewById(R.id.cart)

        cart.setOnClickListener{
            startActivity(Intent(this@HomeActivity, CartActivity::class.java))
        }
//        val spinner: Spinner = findViewById(R.id.category)
//        val categories = listOf("All", "Elektronik", "Pakaian", "Minuman", "Lainnya")
//        val spinnerAdapter =
//            android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = spinnerAdapter
//
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                val selectedCategory = categories[position]
//                Toast.makeText(this@HomeActivity, "Kategori: $selectedCategory", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }
//        val total: TextView = findViewById(R.id.total)
//        val bayar: AppCompatButton = findViewById(R.id.btnBayar)

        val listView: ListView = findViewById(R.id.listview)
        val requestQueue = Volley.newRequestQueue(this@HomeActivity)
        val models = mutableListOf<ProdukModel>()
        val search: SearchView = findViewById(R.id.search)
        val currentDateTime =
            java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                .format(java.util.Date())
        val akun : ImageView = findViewById(R.id.akun)
        akun.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AkunActivity::class.java))
        }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                productAdapter.filter.filter(newText)
                return false
            }
        })

        val stringRequest = object : StringRequest(
            Request.Method.GET, Connection().url + "tbl_barang",
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val imageUrl = Connection().imgurl + obj.getString("id_barang")
                        models.add(
                            ProdukModel(
                                obj.getString("id_barang"),
                                obj.getString("nama_barang"),
                                obj.getString("harga_satuan"),
                                imageUrl
                            )
                        )
                    }
                    productAdapter = ProdukAdapter(
                        applicationContext,
                        R.layout.menu_item,
                        models,
                        id,
                        nama,
                        harga,
                        jumlah,
                    )
                    listView.adapter = productAdapter
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        ) {}
        requestQueue.add(stringRequest)
//
//        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?) = false
//            override fun onQueryTextChange(newText: String?): Boolean {
//                productAdapter.filter.filter(newText)
//                return false
//            }
//        })
//
//            if (id.isEmpty()) {
//                Toast.makeText(
//                    applicationContext,
//                    "Please select an item first!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            val sharedPreferences =
//                applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
//            val idUser = sharedPreferences.getInt("id_user", -1)
//
//            totalbeli = 0
//            for (i in id.indices) {
//                totalbeli += harga[i].toInt() * jumlah[i].toInt()
//            }
//
//            val noTransaksiRequest = JsonObjectRequest(
//                Request.Method.GET, Connection().url + "tbl_transaksi/last_transaction",
//                null,
//                { response ->
//                    try {
//                        val noTransaksi = response.getString("no_transaksi")
//
//                        val transaksiObject = JSONObject().apply {
//                            put("no_transaksi", noTransaksi)
//                            put("tgl_transaksi", currentDateTime)
//                            put("nama_kasir", "Mobile")
//                            put("total_bayar", totalbeli)
//                            put("id_user", idUser)
//                        }
//
//                        val transaksiRequest = JsonObjectRequest(
//                            Request.Method.POST, Connection().url + "tbl_transaksi",
//                            transaksiObject,
//                            { response ->
//                                try {
//                                    val idTransaksi = response.getInt("id_transaksi")
//                                    var responsesReceived = 0
//                                    val totalItems = id.size
//
//                                    for (i in id.indices) {
//                                        val detailObject = JSONObject().apply {
//                                            put("id_transaksi", idTransaksi)
//                                            put("nama_barang", nama[i])
//                                            put("harga", harga[i])
//                                            put("qty", jumlah[i].toInt())
//                                        }
//
//                                        val detailRequest = JsonObjectRequest(
//                                            Request.Method.POST, Connection().url + "tbl_detail",
//                                            detailObject,
//                                            {
//                                                responsesReceived++
//                                                if (responsesReceived == totalItems) {
//                                                    val editor = sharedPreferences.edit()
//                                                    editor.putInt("id_transaksi", idTransaksi)
//                                                    editor.apply()
//
//                                                    Toast.makeText(
//                                                        applicationContext,
//                                                        "Transaction successfully!",
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//                                                    startActivity(
//                                                        Intent(
//                                                            applicationContext,
//                                                            MainActivity::class.java
//                                                        )
//                                                    )
//                                                    finish()
//                                                }
//                                            },
//                                            { error ->
//                                                Toast.makeText(
//                                                    applicationContext,
//                                                    "Failed to add detail!",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                                error.printStackTrace()
//                                            }
//                                        )
//                                        requestQueue.add(detailRequest)
//                                    }
//                                } catch (e: Exception) {
//                                    e.printStackTrace()
//                                }
//                            },
//                            { error ->
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Failed to add transaction!",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                error.printStackTrace()
//                            }
//                        )
//                        requestQueue.add(transaksiRequest)
//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Toast.makeText(
//                            applicationContext,
//                            "Failed to retrieve transaction number.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                },
//                { error ->
//                    Toast.makeText(
//                        applicationContext,
//                        "Error retrieving transaction number.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    error.printStackTrace()
//                }
//            )
//            requestQueue.add(noTransaksiRequest)
//        }
    }
    }