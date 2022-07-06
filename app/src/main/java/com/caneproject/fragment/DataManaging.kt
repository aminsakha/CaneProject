package com.caneproject.fragment

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
private var isJsonSelected = false

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
            selectJsonFile()
        }

        lifecycleScope.launch {
            initRecyclerView()
        }
    }

    private fun chooseMultipleImages() {
        val filePickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        filePickerIntent.type = "image/*"
        filePickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(filePickerIntent, READ_CODE)
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
            if (isJsonSelected) {
                val clipData: ClipData = data.clipData!!

                for (i in 0 until clipData.itemCount) {
                    for (j in 0 until dataListFromFile.size) {
                        Log.d(
                            "onActivityResult",
                            "droped : ${
                                dataListFromFile[j].uriString.split("/").last().dropLast(4)
                            }"
                        )
                        Log.d(
                            "onActivityResult",
                            "cliped : ${
                                clipData.getItemAt(i).uri
                            }"
                        )
                        if (clipData.getItemAt(i).uri.toString().contains(
                                dataListFromFile[j].uriString.split("/").last().dropLast(4)
                            )
                        ) {
                            dataListFromFile[j].uriString = clipData.getItemAt(i).uri.toString()
                        }
                    }
                }
                isJsonSelected = false
                changeFragment(binding.addFileBTN, R.id.action_dataManaging_to_dataAnaliticsPage)
            } else {
                isJsonSelected = true
                val strings = readFile(data.data!!, myContext)
                uriOfTextFile = data.data!!
                dataListFromFile = jsonFileToObjectList(strings.joinToString(""))
                chooseMultipleImages()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun selectJsonFile() {
        val filePickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        filePickerIntent.type = "text/plain"
        startActivityForResult(filePickerIntent, READ_CODE)
    }
}