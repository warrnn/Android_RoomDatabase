package com.android.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DaftarBelanja::class, HistoryBelanja::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun funDaftarBelanjaDAO(): DaftarBelanjaDAO
    abstract fun funHistoryBelanjaDAO(): HistoryBelanjaDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "Belanja_DB"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}
