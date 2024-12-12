package com.android.room.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaftarBelanjaDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(daftar: DaftarBelanja)

    @Query("UPDATE DaftarBelanja SET tanggal=:isi_tanggal, item=:isi_item, jumlah=:isi_jumlah, status=:isi_status WHERE id=:pilih_id")
    fun update(isi_tanggal: String, isi_item: String, isi_jumlah: Int, isi_status: Int, pilih_id: Int)

    @Query("SELECT * FROM DaftarBelanja WHERE id=:isi_id")
    suspend fun getItem(isi_id: Int): DaftarBelanja

    @Delete
    fun delete(daftar: DaftarBelanja)

    @Query("SELECT * FROM DaftarBelanja ORDER BY id ASC")
    fun selectAll(): List<DaftarBelanja>
}