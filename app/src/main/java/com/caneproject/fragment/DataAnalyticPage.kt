package com.caneproject.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.caneproject.adaptors.HardWareModeAdaptor
import com.caneproject.classes.DataClass
import com.caneproject.databinding.FragmentDataAnaliticsPageBinding
import com.caneproject.db.Data
import com.caneproject.db.DataDb
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

var dataList = mutableListOf<Data>()

class DataAnalyticPage : Fragment() {
    lateinit var db: DataDb
    var _binding: FragmentDataAnaliticsPageBinding? = null
    val binding get() = _binding!!
    private lateinit var myContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataAnaliticsPageBinding.inflate(layoutInflater, container, false)
        if (container != null)
            myContext = container.context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.DateBox.text = dateAndTime
        db = Room.databaseBuilder(
            myContext.applicationContext,
            DataDb::class.java,
            "data_table"
        ).build()
        lifecycleScope.launch {
            db.dataDao().deleteEntire()
            insertListToDB()
            setUriToDB()
            initRecyclerView()
        }
    }

    private suspend fun initRecyclerView() {
        val adapter = HardWareModeAdaptor(db.dataDao().readAllData(), myContext)
        binding.dataRecView.adapter = adapter
        binding.dataRecView.layoutManager = LinearLayoutManager(myContext)
        val dividerItemDecoration =
            DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL)
        binding.dataRecView.addItemDecoration(dividerItemDecoration)
    }

    private suspend fun insertListToDB() {
        for (data in dataList) {
            db.dataDao().addData(data)
        }
    }

    private suspend fun setUriToDB() {
        try {
            val list = db.dataDao().readAllData()
            for (i in list.indices) {
                db.dataDao().updateUser(uriList[i].toString(), list[i].id)
            }
        } catch (e: IndexOutOfBoundsException) {
        }
    }
}