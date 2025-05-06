package com.example.provinsi_mobile

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
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.Response

class DaftarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val daftar: AppCompatButton = findViewById(R.id.btnDaftar)
        val login : TextView = findViewById(R.id.login)
        login.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        val nama : EditText = findViewById(R.id.namaDa)
        val alamat : EditText = findViewById(R.id.alamatDa)
        val telepon :  EditText = findViewById(R.id.teleponDa)
        val email : EditText = findViewById(R.id.emailDa)
        val password : EditText = findViewById(R.id.passwordDa)
        val konfirmasi : EditText = findViewById(R.id.konfirmasiDa)

        daftar.setOnClickListener {
            if (email.text.length == 0 && password.text.length == 0){
                Toast.makeText(this@DaftarActivity, "Tolong isi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                val requestQueue = Volley.newRequestQueue(applicationContext)
                val params = mutableMapOf(
                    "tipe_user " to "member",
                    "nama" to nama.text.toString(),
                    "alamat" to alamat.text.toString(),
                    "telepon" to telepon.text.toString(),
                    "username" to email.text.toString(),
                    "password" to password.text.toString(),
                )
                val jsonObject = JSONObject(params as Map<*, *>?)
                val jsonObjectRequest = object : JsonObjectRequest (
                    Request.Method.POST, Connection().url + "tbl_users",
                    jsonObject,
                        Response.Listener { response ->
                        try {
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            Toast.makeText(applicationContext, "Daftar berhasil, silahkan login!", Toast.LENGTH_SHORT).show()

                        }catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener {
                        error->
                        Log.d("Error daftar", "$error")
                    }
                ){
                    override fun getHeaders(): Map<String, String> {
                        return mapOf("Content-type" to "application/json")
                    }
                }
                requestQueue.add(jsonObjectRequest)
                requestQueue.cache.clear()
            }
        }
    }
}