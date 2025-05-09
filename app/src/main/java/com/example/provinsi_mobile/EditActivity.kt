package com.example.provinsi_mobile

import android.content.Context
import android.content.SharedPreferences
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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class EditActivity : AppCompatActivity() {
    lateinit var session: SharedPreferences
    private fun display() {
        val tipe = session.getString("tipe_user", "")
        val nama = session.getString("nama", "")
        val alamat = session.getString("alamat", "")
        val telepon = session.getString("telepon", "")
        val username = session.getString("username", "")
        val password = session.getString("password", "")

        findViewById<TextView>(R.id.tipe).text = tipe
        findViewById<EditText>(R.id.nama).setText(nama)
        findViewById<EditText>(R.id.alamat).setText(alamat)
        findViewById<EditText>(R.id.telepon).setText(telepon)
        findViewById<EditText>(R.id.username).setText(username)
        findViewById<EditText>(R.id.password).setText(password)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val kembali : AppCompatButton = findViewById(R.id.kembaliAkun)
        kembali.setOnClickListener {
            finish()
        }
//        session = getSharedPreferences("session", Context.MODE_PRIVATE)
//        val data = session.getString("akun", null)
//        val list = mutableListOf<AkunModel>()
//        if (!data.isNullOrEmpty()) {
//            val jsonArray = JSONArray(data)
//            for (i in 0 until jsonArray.length()){
//                val obj = jsonArray.getJSONObject(i)
//                val item = AkunModel(
//                    id = obj.getString("id_user"),
//                    nama = obj.getString("nama"),
//                    alamat = obj.getString("alamat"),
//                    telepon = obj.getString("telepon"),
//                )
//                list.add(item)
//            }
//        }
        val etTipe: TextView = findViewById(R.id.tipe)
        val etNama: EditText = findViewById(R.id.nama)
        val etAlamat: EditText = findViewById(R.id.alamat)
        val etTelepon: EditText = findViewById(R.id.telepon)
        val etUsername : EditText = findViewById(R.id.username)
        val etPassword : EditText = findViewById(R.id.password)


        session = getSharedPreferences("session", Context.MODE_PRIVATE)
        val id = session.getInt("id_user", -1)
        val tipe = session.getString("tipe_user", "")
        val nama = session.getString("nama", "")
        val alamat = session.getString("alamat", "")
        val telepon = session.getString("telepon", "")
        val username = session.getString("username", "")
        val password = session.getString("password", "")

        etTipe.setText(tipe)
        etNama.setText(nama)
        etAlamat.setText(alamat)
        etTelepon.setText(telepon)
        etUsername.setText(username)
        etPassword.setText(password)


        val requestQueue = Volley.newRequestQueue(applicationContext)

        val simpan : AppCompatButton = findViewById(R.id.simpanAkun)
        simpan.setOnClickListener{
            if (id.toString().isEmpty()){
                Toast.makeText(applicationContext,"ID Not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val params = mapOf(
//                "tipe_user" to "member",
                "nama" to etNama.text.toString(),
                "alamat" to etAlamat.text.toString(),
                "telepon" to etTelepon.text.toString(),
                "username" to etUsername.text.toString(),
                "password" to etPassword.text.toString(),
                //            "username" to session.getString("username", "")!!,
//            "password" to session.getString("password", "")!!,
            )
            val url = "http://192.168.110.39:45455/api/update/$id"
            val jsonObject = JSONObject(params)
            val request = object : JsonObjectRequest(
                Request.Method.PUT , url,
                jsonObject,
                Response.Listener { response ->
                    val message = response.optString("message", "Update berhasil!")
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    val editor = session.edit()
                    editor.putString("nama", etNama.text.toString())
                    editor.putString("alamat", etAlamat.text.toString())
                    editor.putString("telepon", etTelepon.text.toString())
                    editor.putString("username", etUsername.text.toString())
                    editor.putString("password", etPassword.text.toString())
                    editor.apply()

                    // Tampilkan data terbaru
                    display()
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Log.d("Error update user", "$error")
                    Toast.makeText(applicationContext, "Update gagal!", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    return mapOf("Content-type" to "application/json")
                }
            }
            requestQueue.add(request)
            requestQueue.cache.clear()
        }
    }

}