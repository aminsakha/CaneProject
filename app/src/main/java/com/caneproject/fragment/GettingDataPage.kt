package com.caneproject.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.caneproject.R
import com.caneproject.databinding.FragmentGettingDataPageBinding
import com.caneproject.utils.*
import kotlinx.coroutines.launch

var cameraInstance: Camera? = null

class GettingDataPage : Fragment() {
    private var _binding: FragmentGettingDataPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var myContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGettingDataPageBinding.inflate(layoutInflater, container, false)
        if (container != null)
            myContext = container.context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateAndTime = currentDateAndTime()

        cameraInstance = Camera(myContext, binding.camera, binding.countBox)
        cameraInstance?.showCamera()

        simpleSnackBar(binding.endBTN, "Connected SuccessFully")
        binding.endBTN.setOnClickListener {
            lifecycleScope.launch {
                setUris()
                insertListToDB()
                toastShower(myContext, "Data Successfully Saved")
                changeFragment(
                    binding.endBTN,
                    R.id.action_gettingDataPage_to_initPage
                )
                dataList.clear()
                uriList.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraInstance?.turnOffCamera()
        _binding = null
    }


    private suspend fun insertListToDB() {
        try {
            for (data in dataList) {
                if (data.White.isNotEmpty())
                    db.dataDao().addData(data)
            }
        } catch (e: Exception) {
        }
    }

    private fun setUris() {
        try {
            for (i in dataList.indices) {
                dataList[i].uriString = uriList[i].toString()
            }
        } catch (e: IndexOutOfBoundsException) {
        }
    }
}