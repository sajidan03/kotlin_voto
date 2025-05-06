package com.example.provinsi_mobile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import java.util.*

class ProdukAdapter(
    private val context: Context,
    private val resource: Int,
    private var menuModelList: List<ProdukModel>,
    private val listid: MutableList<String>,
    private val listnm: MutableList<String>,
    private val listhg: MutableList<String>,
    private val listjml: MutableList<String>
) : ArrayAdapter<ProdukModel>(context, resource, menuModelList), Filterable {
    private var menuList: List<ProdukModel> = menuModelList
    private var ff: Filterfilter? = null

    inner class Filterfilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            filterResults.values = if (!constraint.isNullOrEmpty()) {
                val constraintUpper = constraint.toString().uppercase(Locale.getDefault())
                menuList.filter {
                    it.nama.uppercase(Locale.getDefault()).contains(constraintUpper)
                }
            } else {
                menuModelList
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            menuList = results?.values as List<ProdukModel>
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        if (ff == null) {
            ff = Filterfilter()
        }
        return ff!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val pos1 = position * 2
        val pos2 = pos1 + 1

        val left = menuList.getOrNull(pos1)
        val right = menuList.getOrNull(pos2)

        val itemLeft = view.findViewById<LinearLayout>(R.id.item_left)
        val nameLeft = view.findViewById<TextView>(R.id.name_left)
        val priceLeft = view.findViewById<TextView>(R.id.price_left)
        val imgLeft = view.findViewById<ImageView>(R.id.image_left)

        left?.let {
            nameLeft.text = it.nama
            priceLeft.text = "Rp. ${it.harga}"
            Picasso.get().load(it.gambar).into(imgLeft)

            itemLeft.setOnClickListener {
                val intent = Intent(context, DetailProduk::class.java).apply {
                    putExtra("id_barang", left.gambar)
                    putExtra("image", left.gambar)
                    putExtra("nama_barang", left.nama)
                    putExtra("harga_barang", left.harga)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }

        val itemRight = view.findViewById<LinearLayout>(R.id.item_right)
        val nameRight = view.findViewById<TextView>(R.id.name_right)
        val priceRight = view.findViewById<TextView>(R.id.price_right)
        val imgRight = view.findViewById<ImageView>(R.id.image_right)

        if (right != null) {
            itemRight.visibility = View.VISIBLE
            nameRight.text = right.nama
            priceRight.text = "Rp. ${right.harga}"
            Picasso.get().load(right.gambar).into(imgRight)

            itemRight.setOnClickListener {
                val intent = Intent(context, DetailProduk::class.java).apply {
                    putExtra("id_barang", right.gambar)
                    putExtra("image", right.gambar)
                    putExtra("nama_barang",right.nama)
                    putExtra("harga_barang", right.harga)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        } else {
            itemRight.visibility = View.INVISIBLE
        }

        return view
    }

    private fun updateList(menuModel: ProdukModel, jumlah: String) {
        val idx = listid.indexOf(menuModel.id)
        if (idx != -1) {
            listhg[idx] = menuModel.harga
            listjml[idx] = jumlah
        } else {
            listid.add(menuModel.id)
            listnm.add(menuModel.nama)
            listhg.add(menuModel.harga)
            listjml.add(jumlah)
        }
    }

    override fun getCount(): Int = (menuList.size + 1) / 2
    override fun getItemId(position: Int): Long = position.toLong()
}
