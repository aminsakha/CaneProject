package com.caneproject.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.caneproject.R
import com.caneproject.adaptors.HardWareModeAdaptor
import com.caneproject.classes.Data
import com.caneproject.databinding.FragmentDataAnaliticsPageBinding
import java.lang.IndexOutOfBoundsException
import java.text.DateFormat
import java.util.*

var dataList = mutableListOf<Data>()

class DataAnalyticPage : Fragment() {

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
        initRecyclerView()
    }

    private fun initRecyclerView() {
        try {
            for (i in dataList.indices) {
                dataList[i].uri = uriList[i]
            }
        } catch (e: IndexOutOfBoundsException) {
        }

        val adapter = HardWareModeAdaptor(dataList)
        binding.dataRecView.adapter = adapter
        binding.dataRecView.layoutManager = LinearLayoutManager(myContext)
        val dividerItemDecoration = DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL)
        binding.dataRecView.addItemDecoration(dividerItemDecoration)
    }
}