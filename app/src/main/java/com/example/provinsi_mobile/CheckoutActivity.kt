package com.example.provinsi_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class CheckoutActivity : AppCompatActivity() {
    private lateinit var totalText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val checkout: AppCompatButton = findViewById(R.id.checkout)
        checkout.setOnClickListener {
            startActivity(Intent(this@CheckoutActivity, InvoiceActivity::class.java))
        }
        val kembali: TextView = findViewById(R.id.kembaliC)
        kembali.setOnClickListener {
            finish()
        }
        totalText = findViewById(R.id.total)

        val totalHarga = intent.getIntExtra("total_harga", 0) // Default 0 jika tidak ada nilai
        totalText.text = "Rp. $totalHarga"
    }
}