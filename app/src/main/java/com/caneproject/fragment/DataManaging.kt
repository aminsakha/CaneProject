package com.caneproject.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.caneproject.R
import com.caneproject.adaptors.DataManagingAdaptor
import com.caneproject.databinding.FragmentDataManagingBinding
import com.caneproject.utils.*
import kotlinx.coroutines.launch

private const val READ_CODE = 41

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
        binding.addFileBTN.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
            startActivityForResult(intent, READ_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == READ_CODE && data != null) {
            val strings = readFile(data.data!!, myContext)
            uriOfTextFile = data.data!!
            dataListFromFile = jsonFileToObjectList(strings.joinToString(""))
            changeFragment(binding.addFileBTN, R.id.action_dataManaging_to_dataAnaliticsPage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}