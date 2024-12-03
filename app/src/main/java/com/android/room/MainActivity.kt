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
import com.android.room.database.DaftarBelanjaDatabase
import com.android.room.database.DatftarBelanja
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var DB : DaftarBelanjaDatabase
    private lateinit var _fabAdd : FloatingActionButton
    private lateinit var adapterDaftar: AdapterDaftar

    private var arDaftar : MutableList<DatftarBelanja> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DB = DaftarBelanjaDatabase.getDatabase(this)
        adapterDaftar = AdapterDaftar(arDaftar)
        _fabAdd = findViewById(R.id.fabAdd)

        var _rvDaftar = findViewById<RecyclerView>(R.id.rvDaftar)
        _rvDaftar.layoutManager = LinearLayoutManager(this)
        _rvDaftar.adapter = adapterDaftar

        _fabAdd.setOnClickListener {
            startActivity(
                Intent(this, TambahDaftar::class.java)
            )
        }

        adapterDaftar.setOnItemClickCallBack(
            object : AdapterDaftar.OnItemClickCallBack {
                override fun delData(dtBelanja: DatftarBelanja) {
                    CoroutineScope(Dispatchers.IO).async {
                        DB.funDaftarBelanjaDAO().delete(dtBelanja)
                        val daftar = DB.funDaftarBelanjaDAO().selectAll()
                        withContext(Dispatchers.Main) {
                            adapterDaftar.isiData(daftar)
                        }
                    }
                }
            }
        )

        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val daftarBelanja = DB.funDaftarBelanjaDAO().selectAll()
            Log.d("Data ROOM", daftarBelanja.toString())
            adapterDaftar.isiData(daftarBelanja)
        }
    }
}