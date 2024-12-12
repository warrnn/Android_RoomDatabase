package com.android.room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.room.database.AppDatabase
import com.android.room.database.HistoryBelanja
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class HistoryDaftar : AppCompatActivity() {
    private lateinit var DB: AppDatabase
    private lateinit var adapterHistory: AdapterHistory

    private var arHistory: MutableList<HistoryBelanja> = mutableListOf()

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history_daftar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DB = AppDatabase.getDatabase(this)
        adapterHistory = AdapterHistory(arHistory)

        var _rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        _rvHistory.layoutManager = LinearLayoutManager(this)
        _rvHistory.adapter = adapterHistory

        adapterHistory.setOnItemClickCallBack(
            object : AdapterHistory.OnItemClickCallBack {
                override fun delHistory(dtHistory: HistoryBelanja) {
                    CoroutineScope(Dispatchers.IO).async {
                        DB.funHistoryBelanjaDAO().delete(dtHistory)
                        val history = DB.funHistoryBelanjaDAO().selectALl()
                        withContext(Dispatchers.Main) {
                            adapterHistory.isiData(history)
                        }
                    }
                }
            }
        )

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navDaftar -> {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    true
                }

                R.id.navHistory -> {
                    startActivity(
                        Intent(this, HistoryDaftar::class.java)
                    )
                    true
                }

                else -> false
            }
        }

        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val historyBelanja = DB.funHistoryBelanjaDAO().selectALl()
            Log.d("Data ROOM", historyBelanja.toString())
            adapterHistory.isiData(historyBelanja)
        }
    }
}