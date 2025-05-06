package com.example.provinsi_mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class CartAdapter(
    private val context: Context,
    private val cartList: MutableList<CartModel>,
    private val quantityChangeListener: OnQuantityChangeListener
) : BaseAdapter() {

    interface OnQuantityChangeListener {
        fun onQuantityChanged()
    }

    override fun getCount(): Int = cartList.size

    override fun getItem(position: Int): Any = cartList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = cartList[position]
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false)

        val namaText = view.findViewById<TextView>(R.id.nama_barang)
        val hargaText = view.findViewById<TextView>(R.id.harga_barang)
        val jumlahText = view.findViewById<TextView>(R.id.jml)
        val addButton = view.findViewById<ImageView>(R.id.add)
        val removeButton = view.findViewById<ImageView>(R.id.remove)
        val img = view.findViewById<ImageView>(R.id.image_barang)

        namaText.text = item.nama
        hargaText.text = "Rp. ${item.harga}"
        jumlahText.text = item.jumlah
        Picasso.get().load(item.gambar).into(img)

        addButton.setOnClickListener {
            val qty = item.jumlah.toInt() + 1
            item.jumlah = qty.toString()
            jumlahText.text = item.jumlah
            quantityChangeListener.onQuantityChanged()
            saveCartToSharedPreferences()
        }

        removeButton.setOnClickListener {
            val qty = item.jumlah.toInt() - 1
            if (qty <= 0) {
                cartList.removeAt(position)
                notifyDataSetChanged()
            } else {
                item.jumlah = qty.toString()
                jumlahText.text = item.jumlah
            }
            quantityChangeListener.onQuantityChanged()
            saveCartToSharedPreferences()
        }

        return view
    }

    private fun saveCartToSharedPreferences() {
        val sharedPref = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val jsonArray = JSONArray()
        for (item in cartList) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("nama", item.nama)
                put("harga", item.harga)
                put("jumlah", item.jumlah)
                put("gambar", item.gambar)
            }
            jsonArray.put(obj)
        }
        editor.putString("cart_list", jsonArray.toString())
        editor.apply()
    }
}
