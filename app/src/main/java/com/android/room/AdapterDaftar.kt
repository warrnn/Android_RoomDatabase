package com.android.room

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.room.database.DaftarBelanja

class AdapterDaftar(private val daftarBelanja: MutableList<DaftarBelanja>) :
    RecyclerView.Adapter<AdapterDaftar.ListViewHolder>() {

    private lateinit var onItemClickCall: OnItemClickCallBack

    interface OnItemClickCallBack {
        fun delData(dtBelanja: DaftarBelanja)
        fun doneData(dtBelanja: DaftarBelanja)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCall = onItemClickCallBack
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvTanggal = itemView.findViewById<TextView>(R.id.tvTanggal)
        var _tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        var _tvJumlah = itemView.findViewById<TextView>(R.id.tvJumlah)

        var _ibDone = itemView.findViewById<ImageButton>(R.id.ibDone)
        var _ibEdit = itemView.findViewById<ImageButton>(R.id.ibEdit)
        var _ibDelete = itemView.findViewById<ImageButton>(R.id.ibDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDaftar.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_list, parent, false
        )
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterDaftar.ListViewHolder, position: Int) {
        var daftar = daftarBelanja[position]

        holder._tvTanggal.setText(daftar.tanggal)
        holder._tvItem.setText(daftar.item)
        holder._tvJumlah.setText(daftar.jumlah.toString())

        holder._ibDone.setOnClickListener {
            onItemClickCall.doneData(daftar)
        }

        holder._ibEdit.setOnClickListener {
            val intent = Intent(it.context, TambahDaftar::class.java)
            intent.putExtra("id", daftar.id)
            intent.putExtra("addEdit", 1)
            it.context.startActivity(intent)
        }

        holder._ibDelete.setOnClickListener {
            onItemClickCall.delData(daftar)
        }
    }

    override fun getItemCount(): Int {
        return daftarBelanja.size
    }

    fun isiData(daftar: List<DaftarBelanja>) {
        daftarBelanja.clear()
        daftarBelanja.addAll(daftar)
        notifyDataSetChanged()
    }
}