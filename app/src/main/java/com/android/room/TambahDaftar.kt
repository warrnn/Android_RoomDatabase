package com.android.room

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.room.database.AppDatabase
import com.android.room.database.DaftarBelanja
import com.android.room.helper.DateHelper.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TambahDaftar : AppCompatActivity() {
    private lateinit var _etItem: EditText
    private lateinit var _etJumlah: EditText
    private lateinit var _btnTambah: Button
    private lateinit var _btnUpdate: Button

    var DB = AppDatabase.getDatabase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_daftar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _etItem = findViewById(R.id.etItem)
        _etJumlah = findViewById(R.id.etJumlah)
        _btnTambah = findViewById(R.id.btnTambah)
        _btnUpdate = findViewById(R.id.btnUpdate)

        var tanggal = getCurrentDate()

        _btnTambah.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.funDaftarBelanjaDAO().insert(
                    DaftarBelanja(
                        tanggal = tanggal,
                        item = _etItem.text.toString(),
                        jumlah = _etJumlah.text.toString().toInt(),
                        status = 0
                    )
                )
            }
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }

        var iID: Int = 0
        var iAddEdit: Int = 0

        iID = intent.getIntExtra("id", 0)
        iAddEdit = intent.getIntExtra("addEdit", 0)

        if (iAddEdit == 0) {
            _btnTambah.visibility = View.VISIBLE
            _btnUpdate.visibility = View.GONE
            _etItem.isEnabled = true
        } else {
            _btnTambah.visibility = View.GONE
            _btnUpdate.visibility = View.VISIBLE
            _etItem.isEnabled = false

            CoroutineScope(Dispatchers.IO).async {
                val item = DB.funDaftarBelanjaDAO().getItem(iID)
                _etItem.setText(item.item)
                _etJumlah.setText(item.jumlah.toString())
            }
        }

        _btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.funDaftarBelanjaDAO().update(
                    isi_tanggal = tanggal,
                    isi_item = _etItem.text.toString(),
                    isi_jumlah = _etJumlah.text.toString().toInt(),
                    isi_status = 0,
                    pilih_id = iID
                )
            }
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }
}