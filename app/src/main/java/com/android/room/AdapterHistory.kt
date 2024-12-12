package com.android.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.room.database.HistoryBelanja

class AdapterHistory(private val historyBelanja: MutableList<HistoryBelanja>) :
    RecyclerView.Adapter<AdapterHistory.ListViewHolder>() {

    private lateinit var onItemClickCall: OnItemClickCallBack

    interface OnItemClickCallBack {
        fun delHistory(dtHistory: HistoryBelanja)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCall = onItemClickCallBack
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvTanggal = itemView.findViewById<TextView>(R.id.tvTanggal)
        var _tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        var _tvJumlah = itemView.findViewById<TextView>(R.id.tvJumlah)

        var _ibDelete = itemView.findViewById<ImageButton>(R.id.ibDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterHistory.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_history, parent, false
        )
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterHistory.ListViewHolder, position: Int) {
        var history = historyBelanja[position]

        holder._tvTanggal.setText(history.tanggal)
        holder._tvItem.setText(history.item)
        holder._tvJumlah.setText(history.jumlah.toString())

        holder._ibDelete.setOnClickListener( {
            onItemClickCall.delHistory(history)
        })
    }

    override fun getItemCount(): Int {
        return historyBelanja.size
    }

    fun isiData(history: List<HistoryBelanja>) {
        historyBelanja.clear()
        historyBelanja.addAll(history)
        notifyDataSetChanged()
    }
}