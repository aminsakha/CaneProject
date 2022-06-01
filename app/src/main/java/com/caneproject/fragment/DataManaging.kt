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
import com.caneproject.adaptors.DataManagingAdaptor
import com.caneproject.databinding.FragmentDataManagingBinding
import com.caneproject.utils.*
import kotlinx.coroutines.launch

class DataManaging : Fragment() {
    private var adaptor: DataManagingAdaptor = DataManagingAdaptor(emptyList())
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
        binding.deleteItemBTN.setOnClickListener {
            lifecycleScope.launch {
                for (date in deletedItemsDate) {
                    db.dataDao().deleteData(date)
                }
                simpleSnackBar(binding.dataManagingRecView, "Deleted Successfully")
                initRecyclerView()
                selectMultipleRow = false
            }
        }
        lifecycleScope.launch {
            initRecyclerView()
        }
    }

    private suspend fun initRecyclerView() {
        adaptor = DataManagingAdaptor(getRecordDates())
        binding.dataManagingRecView.adapter = adaptor
        binding.dataManagingRecView.layoutManager = LinearLayoutManager(myContext)
        val dividerItemDecoration =
            DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL)
        binding.dataManagingRecView.addItemDecoration(dividerItemDecoration)

    }

    private suspend fun getRecordDates(): MutableList<String> {
        return db.dataDao().getDates()
    }
}