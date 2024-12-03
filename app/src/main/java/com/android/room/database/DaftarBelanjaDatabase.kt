package com.android.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatftarBelanja::class], version = 1)
abstract class DaftarBelanjaDatabase : RoomDatabase() {
    abstract fun funDaftarBelanjaDAO() : DaftarBelanjaDAO

    companion object {
        @Volatile
        private var INSTANCE: DaftarBelanjaDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : DaftarBelanjaDatabase {
            if (INSTANCE == null) {
                synchronized(DaftarBelanjaDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DaftarBelanjaDatabase::class.java, "DaftarBelanja_DB"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as DaftarBelanjaDatabase
        }
    }
}