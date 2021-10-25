package com.ayalfishey.hordeinc.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.activitys.LoginActivity
import com.ayalfishey.hordeinc.data.GameData
import com.ayalfishey.hordeinc.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding : FragmentMainBinding
    private var db : FirebaseFirestore = Firebase.firestore


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
//        updateUI()
        binding.floatingActionButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        updateUI()
    }

    private fun updateUI() {
            val coinObserver = Observer<Long>{ newCoinValue ->
                binding.powerCounter.text = "Coins: $newCoinValue \n" +
                        "Power: ${GameData.power.value}"
            }
            val powerObserver = Observer<Long> {newPowerValue ->
                binding.powerCounter.text = "Coins: ${GameData.coins.value} \n" +
                        "Power: $newPowerValue"
            }
            GameData.coins.observe(viewLifecycleOwner,coinObserver)
            GameData.power.observe(viewLifecycleOwner,powerObserver)
    }

    override fun onResume() {
        super.onResume()
        GameData.updatePower()
//        updateUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.collection("users").document(GameData.user?.email!!).update(mapOf("power" to GameData.updatePower()))
        db.collection("users").document(GameData.user.email!!).update(mapOf("coins" to GameData.coins.value))
    }

}