package com.caneproject.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.caneproject.classes.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.caneproject.adaptors.DataManagingAdaptor
import com.caneproject.databinding.FragmentDataManagingBinding
import com.caneproject.db.Data
import com.caneproject.db.DataDb
import kotlinx.coroutines.launch

class DataManaging : Fragment() {

    private var _binding: FragmentDataManagingBinding? = null
    private val binding get() = _binding!!
    private lateinit var myContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataManagingBinding.inflate(layoutInflater, container, false)
        if (container != null)
            myContext = container.context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val tmp = getRecordDates()
            Log.d("lifeScope", "tmp : $tmp")
            initRecyclerView(tmp)
        }
    }

    private fun initRecyclerView(tmp: MutableList<String>) {
        val adapter = DataManagingAdaptor(tmp)
        binding.dataManagingRecView.adapter = adapter
        binding.dataManagingRecView.layoutManager = LinearLayoutManager(myContext)
        val dividerItemDecoration =
            DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL)
        binding.dataManagingRecView.addItemDecoration(dividerItemDecoration)
    }

    private suspend fun getRecordDates(): MutableList<String> {
        Log.d("lifeScope", "dates :  : ${db.dataDao().getDates()}")
        return db.dataDao().getDates()
    }
}