package com.caneproject.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.caneproject.adaptors.KotlinAdaptorForAnalytic
import com.caneproject.databinding.FragmentDataAnaliticsPageBinding
import com.caneproject.db.Data
import com.caneproject.utils.*
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.utils.AppCenterLog
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
                val photoURI =
                    Uri.parse(data.uriString).path?.let { it1 -> File(it1) }?.let { it2 ->
                        FileProvider.getUriForFile(
                            myContext,
                            myContext.applicationContext.packageName.toString() + ".provider",
                            it2
                        )
                    }
                if (photoURI != null) {
                    uriList.add(photoURI)
                }
            }
            writeToFile(dataListIntoJson(tmpList))
            //shareImages(uriList, myContext)
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
        binding.dataRecView.adapter = adapter
        binding.dataRecView.layoutManager = LinearLayoutManager(myContext)
        val dividerItemDecoration =
            DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL)
        binding.dataRecView.addItemDecoration(dividerItemDecoration)
        selectedItemInRecView = ""
    }

    private fun writeToFile(content: String) {
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path
        val writer = FileOutputStream(File(path, "textTest1.txt"))
        writer.write(content.toByteArray())
        writer.close()
        toastShower(myContext, "got it")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
