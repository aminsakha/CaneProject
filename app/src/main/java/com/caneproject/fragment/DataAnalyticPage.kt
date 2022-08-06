package com.caneproject.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.caneproject.adaptors.AdaptorForAnalytic
import com.caneproject.databinding.FragmentDataAnaliticsPageBinding
import com.caneproject.db.Data
import com.caneproject.utils.*
import kotlinx.coroutines.launch

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
            val pdf = ExportPdf(myContext, tmpList, selectedItemInRecView)
            pdf.createContent()
            choosePdf()
        }
    }

    private suspend fun initRecyclerView() {
        val adapter =
            AdaptorForAnalytic(
                db.dataDao().getRecordInThisDate(selectedItemInRecView),
                myContext
            )
        tmpList = adapter.dataList as MutableList<Data>
        binding.dataRecView.adapter = adapter
        binding.dataRecView.layoutManager = LinearLayoutManager(myContext)
        val dividerItemDecoration =
            DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL)
        binding.dataRecView.addItemDecoration(dividerItemDecoration)
    }
    private fun choosePdf() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 2 && data != null) {
            val uriList = ArrayList<Uri>()
            uriList.add(data.data!!)
            sharePdf(uriList, myContext)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
