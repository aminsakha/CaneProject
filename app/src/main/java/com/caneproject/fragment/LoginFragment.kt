package com.caneproject.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.caneproject.R
import com.caneproject.databinding.FragmentInitPageBinding
import com.caneproject.databinding.FragmentLoginBinding
import com.caneproject.db.UserDb
import com.caneproject.utils.isUserSet
import com.caneproject.utils.userDb
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var myContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDb = Room.databaseBuilder(
            requireContext(),
            UserDb::class.java,
            "user_table"
        ).build()
        lifecycleScope.launch {
            if (userDb.userDao().getUser().isNotEmpty())
                findNavController().navigate(R.id.action_loginFragment_to_initPage, null)
        }
    }
}