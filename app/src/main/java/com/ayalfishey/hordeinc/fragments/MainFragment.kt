package com.ayalfishey.hordeinc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding : FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        binding.powerCounter.text = "0pp"
    }

}