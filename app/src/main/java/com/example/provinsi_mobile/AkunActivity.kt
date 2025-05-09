package com.example.provinsi_mobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AkunActivity : AppCompatActivity() {
    lateinit var session: SharedPreferences
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
        session = getSharedPreferences("session", Context.MODE_PRIVATE)
        val idUser = session.getInt("id_user", -1)
        
        if (idUser == -1){
            Toast.makeText(applicationContext, "Akun tidak ditemukan, coba hubungi pihak admin untuk informasi lebih lanjut", Toast.LENGTH_SHORT).show()
            return
        }
        val tipe : TextView = findViewById(R.id.tipe)
        val username : TextView = findViewById(R.id.username)
        val password : TextView = findViewById(R.id.password)
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
                    tipe.text = user?.tipe_user.toString()
                    nama.text = user?.nama.toString()
                    alamat.text = user?.alamat.toString()
                    telepon.text = user?.telepon.toString()
                    username.text = user?.username.toString()
                    password.text = user?.password .toString()
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                Log.d("Error akun", "$t")
                Toast.makeText(applicationContext, "Akun tidak ditemukan, koneksi error", Toast.LENGTH_SHORT).show()
            }
        })
        val edit : LinearLayout = findViewById(R.id.edit)
        edit.setOnClickListener{
            val editor = session.edit()
            editor.putString("tipe_user", tipe.text.toString())
            editor.putString("nama", nama.text.toString())
            editor.putString("alamat", alamat.text.toString())
            editor.putString("telepon", telepon.text.toString())
            editor.putString("username", username.text.toString())
            editor.putString("password", password.text.toString())
            editor.apply()
            startActivity(Intent(applicationContext, EditActivity::class.java))
//            val jsonObject = JSONObject().apply {
//                put("id_user", "id-${System.currentTimeMillis()}")
//                put("nama", nama)
//                put("alamat", alamat)
//                put("telepon", telepon)
//            }
//            val data = session.getString("akun", null)
//            val jsonArray = if (data != null) JSONArray(data) else JSONArray()
//            jsonArray.put(jsonObject)
//
//            editor.putString("akun", jsonArray.toString())
//            editor.apply()
//            startActivity(Intent(applicationContext, EditActivity::class.java))
    }
    }
}