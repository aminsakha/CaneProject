package com.caneproject.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.caneproject.R
import com.caneproject.classes.db
import com.caneproject.databinding.FragmentInitPageBinding
import com.caneproject.db.DataDb
import com.caneproject.utils.changeFragment
import com.caneproject.utils.toastShower


class InitPage : Fragment() {
    private var _binding: FragmentInitPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var myContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitPageBinding.inflate(layoutInflater, container, false)
        if (container != null)
            myContext = container.context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Room.databaseBuilder(
            myContext.applicationContext,
            DataDb::class.java,
            "data_table"
        ).build()

        binding.ConnectionButton.setOnClickListener {
            gotoGettingDataPage()
        }
        binding.fileManagerBTN.setOnClickListener {
            changeFragment(binding.ConnectionButton, R.id.action_initPage_to_dataManaging)
        }
    }


    private fun gotoGettingDataPage() {
        changeFragment(binding.ConnectionButton, R.id.action_initPage_to_gettingDataPage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}