package com.android.room.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryBelanjaDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: HistoryBelanja)

    @Query("SELECT * FROM HistoryBelanja ORDER BY id ASC")
    fun selectALl(): List<HistoryBelanja>

    @Delete
    fun delete(history: HistoryBelanja)
}