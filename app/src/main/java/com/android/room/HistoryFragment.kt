package com.android.room

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.room.database.AppDatabase
import com.android.room.database.HistoryBelanja
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var DB: AppDatabase
private lateinit var adapterHistory: AdapterHistory

private var arHistory: MutableList<HistoryBelanja> = mutableListOf()

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
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
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DB = AppDatabase.getDatabase(requireContext())
        adapterHistory = AdapterHistory(arHistory)

        var _rvHistory = view.findViewById<RecyclerView>(R.id.rvHistory)
        _rvHistory.layoutManager = LinearLayoutManager(requireContext())
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

        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val historyBelanja = DB.funHistoryBelanjaDAO().selectALl()
            Log.d("Data ROOM - History", historyBelanja.toString())
            adapterHistory.isiData(historyBelanja)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}