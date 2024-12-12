package com.android.room.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryBelanja(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0,

    @ColumnInfo(name = "tanggal")
    var tanggal : String? = null,

    @ColumnInfo(name = "item")
    var item : String? = null,

    @ColumnInfo(name = "jumlah")
    var jumlah : Int = 0,
)
