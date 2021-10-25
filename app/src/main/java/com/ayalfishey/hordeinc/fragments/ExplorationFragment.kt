package com.ayalfishey.hordeinc.fragments

import android.content.res.ColorStateList
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.data.GameData
import com.ayalfishey.hordeinc.databinding.FragmentExplorationBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread
import kotlin.system.exitProcess

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "Exploration"

/**
 * A simple [Fragment] subclass.
 * Use the [ExplorationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExplorationFragment : Fragment(R.layout.fragment_exploration) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private lateinit var binding: FragmentExplorationBinding
    private var db : FirebaseFirestore = Firebase.firestore
    private var autoProgress : Boolean = false
    private lateinit var combatThread : Thread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExplorationBinding.bind(view)
        val coinObserver = Observer<Long>{ newCoinValue ->
            binding.powerCounter.text = "Coins: $newCoinValue \n" +
                    "Power: ${GameData.power.value}"
        }
        val powerObserver = Observer<Long> {newPowerValue ->
            binding.powerCounter.text = "Coins: ${GameData.coins.value} \n" +
                    "Power: $newPowerValue"
        }
        GameData.coins.observe(viewLifecycleOwner,coinObserver)
        setupViews()
        updateUI()
        startCombat()
    }

    private fun setupViews() {
        autoProgress = if (autoProgress){
            binding.autoProgressBtn.setBackgroundResource(R.drawable.play)
            false
        }else{
            binding.autoProgressBtn.setBackgroundResource(R.drawable.pause)
            true
        }
        binding.autoProgressBtn.setOnClickListener {
            autoProgress = if (autoProgress){
                binding.autoProgressBtn.setBackgroundResource(R.drawable.play)
                false
            }else{
                binding.autoProgressBtn.setBackgroundResource(R.drawable.pause)
                true
            }
        }
        binding.nextBtn.setOnClickListener {
            GameData.currentEnemy += 1
            resetHp()
            updateUI()
        }
        binding.previousBtn.setOnClickListener {
            if (GameData.currentEnemy > 0) {
                GameData.currentEnemy -= 1
                resetHp()
                updateUI()
            }
        }
    }

    private fun resetHp() {
        GameData.enemyCurrentHp = GameData.enemys[GameData.currentEnemy].hp
        GameData.heroCurrentHp = GameData.heroMaxHp
    }

    private fun startCombat(){
        var count = 0
            combatThread = thread (name = "combat") {
            while (!Thread.interrupted()) {
                Thread.sleep(1980)
                requireActivity().runOnUiThread {
                    updateUI()
                }
                val enemy = GameData.enemys[GameData.currentEnemy]
                when {
                    GameData.heroCurrentHp <= 0 -> {
                        resetHp()
                        count = 0
                    }
                    GameData.enemyCurrentHp <= 0 -> {
                        if (autoProgress) {
                            GameData.currentEnemy += 1
                        }
                        GameData.coins.postValue(GameData.coins.value?.plus(enemy.coin))
                        //TODO: Check if cps is actually better
                        db.collection("users").document(GameData.user?.email!!).update(
                            mapOf(
                                "coins" to GameData.coins.value,
                                "exploration" to GameData.currentEnemy,
                                "collect timer" to count * 1980
                            )
                        ).addOnFailureListener { e ->
                            Log.e("DataBase", "combat: error updating database",)
                            Toast.makeText(
                                context,
                                "connection error please restart application",
                                Toast.LENGTH_LONG
                            )
                            exitProcess(-1)
                        }
                        count = 0
                        resetHp()
                    }
                    else -> {
                        GameData.heroCurrentHp = GameData.heroCurrentHp - enemy.power
                        GameData.enemyCurrentHp = GameData.enemyCurrentHp - GameData.updatePower()
                        count++
                    }

                }
                requireActivity().runOnUiThread {
                    updateUI()
                }

            }
        }
    }

    private fun updateUI() {
        binding.heroHp.progress = ((GameData.heroCurrentHp).toDouble() / (GameData.heroMaxHp).toDouble() * 100).toInt()
//        Log.d(TAG, "setHpBars: ${((GameData.heroCurrentHp).toDouble() / (GameData.heroMaxHp).toDouble() * 100).toInt()}, heroCurrentHP: ${GameData.heroCurrentHp}, heroMaxHP: ${GameData.heroMaxHp}, Hero Division: ${(GameData.heroCurrentHp).toDouble() / (GameData.heroMaxHp).toDouble()}")
        binding.monsterHp.progress = ((GameData.enemyCurrentHp).toDouble()/ (GameData.enemys[GameData.currentEnemy].hp).toDouble() * 100).toInt()
        setEnemy()
    }

    private fun setEnemy() {
        var unitAnim : AnimationDrawable
        if(GameData.enemys[GameData.currentEnemy].enemyNo > 0) {
            binding.monsterImg.backgroundTintList =
                ColorStateList.valueOf(-5550 * GameData.enemys[GameData.currentEnemy].enemyNo)
        }
        binding.monsterImg.apply{
            setBackgroundResource(R.drawable.skele_anim)
            unitAnim = background as AnimationDrawable
        }
        unitAnim.start()
        binding.heroImg.apply{
            setBackgroundResource(R.drawable.hero_anim)
            unitAnim = background as AnimationDrawable
        }
        unitAnim.start()
        binding.monsterName.text = GameData.enemys[GameData.currentEnemy].name
    }

    override fun onDestroy() {
        super.onDestroy()
        combatThread.interrupt()
    }

}
