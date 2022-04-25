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
import com.caneproject.R
import com.caneproject.databinding.FragmentInitPageBinding
import com.caneproject.utils.changeFragment


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
        Log.d("onDestroyView", "onDestroyView: created")
        val arr = listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
        binding.ConnectionButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                checkPermission(arr)
            else
                startConnection()
        }
    }

    private fun checkPermission(permissions: List<String>) {
        val missingPermissions = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(
                myContext,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(myContext as Activity, permissions.toTypedArray(), 1)
        } else
            startConnection()
    }

    private fun startConnection() {
        changeFragment(binding.ConnectionButton, R.id.action_initPage_to_gettingDataPage)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.none { it != PackageManager.PERMISSION_GRANTED }) {
            startConnection()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}