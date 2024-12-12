package com.android.room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.room.database.AppDatabase
import com.android.room.database.DaftarBelanja
import com.android.room.database.HistoryBelanja
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mFragmentManager = supportFragmentManager

        val fDaftar = DaftarFragment()
        val fHistory = HistoryFragment()

        mFragmentManager.findFragmentByTag(fDaftar::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, fDaftar, DaftarFragment::class.java.simpleName)
            .commit()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.navDaftar -> {
                    mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fDaftar, DaftarFragment::class.java.simpleName)
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navHistory -> {
                    mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fHistory, HistoryFragment::class.java.simpleName)
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }
        }
    }
}