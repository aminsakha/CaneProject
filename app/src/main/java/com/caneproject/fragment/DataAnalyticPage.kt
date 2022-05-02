package com.caneproject.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.caneproject.adaptors.KotlinAdaptorForAnalytic
import com.caneproject.classes.db
import com.caneproject.classes.selectedItemInRecView
import com.caneproject.databinding.FragmentDataAnaliticsPageBinding
import kotlinx.coroutines.launch

class DataAnalyticPage : Fragment() {
    private var _binding: FragmentDataAnaliticsPageBinding? = null
    private val binding get() = _binding!!
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
        binding.DateBox.text = selectedItemInRecView
        lifecycleScope.launch {
            initRecyclerView()
        }
    }

    private suspend fun initRecyclerView() {
        val adapter =
            KotlinAdaptorForAnalytic(db.dataDao().getRecordInThisDate(selectedItemInRecView), myContext)
        binding.dataRecView.adapter = adapter
        binding.dataRecView.layoutManager = LinearLayoutManager(myContext)
        val dividerItemDecoration =
            DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL)
        binding.dataRecView.addItemDecoration(dividerItemDecoration)
    }
}
