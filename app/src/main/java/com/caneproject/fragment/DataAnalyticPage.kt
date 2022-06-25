package com.caneproject.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.caneproject.adaptors.KotlinAdaptorForAnalytic
import com.caneproject.databinding.FragmentDataAnaliticsPageBinding
import com.caneproject.db.Data
import com.caneproject.utils.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class DataAnalyticPage : Fragment() {
    private var _binding: FragmentDataAnaliticsPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var myContext: Context
    private var tmpList = mutableListOf<Data>()
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
        binding.sendBTN.setOnClickListener {
            val uriList = ArrayList<Uri>()
            for (data in tmpList) {
                val photoURI = getUriForSharing(data.uriString, myContext)
                uriList.add(photoURI)
            }
            uriOfTextFile = writeToFile(dataListIntoJson(tmpList), tmpList[0].dateAndTime)
            uriList.add(getUriForSharing(uriOfTextFile.toString(), myContext))
            shareImages(uriList, myContext)
        }
    }

    private suspend fun initRecyclerView() {
        val adapter: KotlinAdaptorForAnalytic = if (selectedItemInRecView.isNotEmpty()) {
            KotlinAdaptorForAnalytic(
                db.dataDao().getRecordInThisDate(selectedItemInRecView),
                myContext
            )
        } else {
            KotlinAdaptorForAnalytic(
                dataListFromFile,
                myContext
            )
        }
        tmpList = adapter.dataList as MutableList<Data>
        binding.dataRecView.adapter = adapter
        binding.dataRecView.layoutManager = LinearLayoutManager(myContext)
        val dividerItemDecoration =
            DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL)
        binding.dataRecView.addItemDecoration(dividerItemDecoration)
        selectedItemInRecView = ""
    }

    private fun writeToFile(content: String, fileName: String): Uri? {
        val tmp = fileName.split(",")
        val validFileName =
            "${tmp[0]}${tmp[1]}".replace("\\s".toRegex(), "").replace("/", ":").replace(":", ",")
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path
        FileOutputStream(File(path, "$validFileName.txt")).apply {
            write(content.toByteArray())
            close()
        }
        toastShower(myContext, "got it")
        return Uri.fromFile(File(path, "$validFileName.txt"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
