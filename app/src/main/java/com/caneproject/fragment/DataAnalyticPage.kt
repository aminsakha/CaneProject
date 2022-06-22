package com.caneproject.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
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
import com.caneproject.utils.db
import com.caneproject.utils.selectedItemInRecView
import com.caneproject.utils.shareImages
import kotlinx.coroutines.launch
import java.io.File


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
            shareImages(uriList, myContext)
        }
    }

    private suspend fun initRecyclerView() {
        val adapter =
            KotlinAdaptorForAnalytic(
                db.dataDao().getRecordInThisDate(selectedItemInRecView),
                myContext
            )
        tmpList = db.dataDao().getRecordInThisDate(selectedItemInRecView)
        binding.dataRecView.adapter = adapter
        binding.dataRecView.layoutManager = LinearLayoutManager(myContext)
        val dividerItemDecoration =
            DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL)
        binding.dataRecView.addItemDecoration(dividerItemDecoration)
    }
}
