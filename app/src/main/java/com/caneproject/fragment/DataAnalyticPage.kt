package com.caneproject.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.caneproject.R
import com.caneproject.classes.Data
import com.caneproject.databinding.FragmentDataAnaliticsPageBinding
import com.caneproject.databinding.FragmentGettingDataPageBinding

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
}