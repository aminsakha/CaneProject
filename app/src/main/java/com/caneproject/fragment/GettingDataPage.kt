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
                dataListFromModulo.clear()
                uriList.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraInstance?.turnOffCamera()
        _binding = null
    }

    /**
     * this function insert every object got from modulo into DB
     */
    private suspend fun insertListToDB() {
        try {
            for (data in dataListFromModulo) {
                if (data.White.isNotEmpty())
                    dataDb.dataDao().addData(data)
            }
        } catch (e: Exception) {
        }
    }
    /**
     * this function set uri for every object in dataListFromModulo
     */
    private fun setUris() {
        try {
            for (i in dataListFromModulo.indices) {
                dataListFromModulo[i].uriString = uriList[i].toString()
            }
        } catch (e: IndexOutOfBoundsException) {
        }
    }
}