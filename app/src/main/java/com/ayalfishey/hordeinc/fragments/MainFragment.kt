package com.ayalfishey.hordeinc.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.activitys.LoginActivity
import com.ayalfishey.hordeinc.data.GameData
import com.ayalfishey.hordeinc.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding : FragmentMainBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        GameData.minions
        binding.powerCounter.text = "Coins: 0 \n" +
                "Power: ${GameData.updatePower()}"
        binding.floatingActionButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

    }

    override fun onResume() {
        super.onResume()
        GameData.updatePower()
        binding.powerCounter.text = "Coins: 0 \n" +
                "Power: ${GameData.updatePower()}"
    }

}