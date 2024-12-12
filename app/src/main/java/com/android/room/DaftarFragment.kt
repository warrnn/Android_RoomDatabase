package com.android.room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.room.database.AppDatabase
import com.android.room.database.DaftarBelanja
import com.android.room.database.HistoryBelanja
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var DB: AppDatabase
private lateinit var _fabAdd: FloatingActionButton
private lateinit var adapterDaftar: AdapterDaftar

private var arDaftar: MutableList<DaftarBelanja> = mutableListOf()

/**
 * A simple [Fragment] subclass.
 * Use the [DaftarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DaftarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DB = AppDatabase.getDatabase(requireContext())
        adapterDaftar = AdapterDaftar(arDaftar)
        _fabAdd = view.findViewById(R.id.fabAdd)

        var _rvDaftar = view.findViewById<RecyclerView>(R.id.rvDaftar)
        _rvDaftar.layoutManager = LinearLayoutManager(requireContext())
        _rvDaftar.adapter = adapterDaftar

        _fabAdd.setOnClickListener {
            startActivity(
//                For Activity
//                Intent(this, TambahDaftar::class.java)

//                For Fragment
                Intent(requireActivity(), TambahDaftar::class.java)
            )
        }

        adapterDaftar.setOnItemClickCallBack(
            object : AdapterDaftar.OnItemClickCallBack {
                override fun delData(dtBelanja: DaftarBelanja) {
                    CoroutineScope(Dispatchers.IO).async {
                        DB.funDaftarBelanjaDAO().delete(dtBelanja)
                        val daftar = DB.funDaftarBelanjaDAO().selectAll()
                        withContext(Dispatchers.Main) {
                            adapterDaftar.isiData(daftar)
                        }
                    }
                }

                override fun doneData(dtBelanja: DaftarBelanja) {
                    CoroutineScope(Dispatchers.IO).async {
                        DB.funHistoryBelanjaDAO().insert(
                            HistoryBelanja(
                                tanggal = dtBelanja.tanggal,
                                item = dtBelanja.item,
                                jumlah = dtBelanja.jumlah
                            )
                        )
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
            Log.d("Data ROOM - Daftar", daftarBelanja.toString())
            adapterDaftar.isiData(daftarBelanja)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DaftarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DaftarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}