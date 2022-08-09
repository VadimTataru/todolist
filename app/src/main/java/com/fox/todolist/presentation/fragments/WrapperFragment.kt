package com.fox.todolist.presentation.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fox.todolist.R
import com.fox.todolist.databinding.FragmentWrapperBinding

class WrapperFragment : Fragment() {
    private lateinit var binding: FragmentWrapperBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWrapperBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            findNavController().navigate(R.id.action_wrapperFragment_to_mainFragment)
        }, 3000)
    }
}