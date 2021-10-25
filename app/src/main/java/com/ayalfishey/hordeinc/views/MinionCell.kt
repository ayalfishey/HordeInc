package com.ayalfishey.hordeinc.views

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.HandlerCompat.postDelayed
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.classes.Minion
import com.ayalfishey.hordeinc.data.GameData
import com.ayalfishey.hordeinc.databinding.MinionCellBinding
import com.ayalfishey.hordeinc.fragments.MinionsFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread
import kotlin.math.pow

class MinionCell : ConstraintLayout{


    var db : FirebaseFirestore = Firebase.firestore

    var binding : MinionCellBinding = MinionCellBinding.inflate(LayoutInflater.from(context), this,true)

    private var calculatedTotal : Long = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context,attrs: AttributeSet?,defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context,attrs: AttributeSet?,defStyleAttr: Int,defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun View.findViewTreeLifecycleOwner(): LifecycleOwner? = ViewTreeLifecycleOwner.get(this)

    private fun setListeners(minion: Minion) {
        val coinObserver = Observer<Long>{ newCoinValue ->
            binding.buy1Button.isEnabled = newCoinValue >= calculatedTotal
            Log.d("observer", "setListeners: $calculatedTotal")
        }
        let { GameData.coins.observeForever(coinObserver) }

        binding.buy1Button.isEnabled = GameData.coins.value!! >= calculatedTotal
        binding.buy1Button.setOnClickListener {
                db.collection("users").document(GameData.user?.email!!).update(mapOf(
                    "minions.${Minion.getMinionName(minion.minionNo)}.amount" to ++minion.amount
                ))
            GameData.coins.postValue(GameData.coins.value!! - calculatedTotal)
                updateMinionCost(minion)
                updateDetails(minion)
        }

        binding.combineButton.setOnClickListener {
            if (minion.amount>=minion.combineCost){
                //add to higher minion and reduce amount of current
                db.collection("users").document(GameData.user?.email!!).update(mapOf(
                    "minions.${Minion.getMinionName(minion.minionNo)}.amount" to minion.amount - minion.combineCost,
                    "minions.${Minion.getMinionName(minion.minionNo)}.lost_to_combine" to minion.lostToCombine + minion.combineCost,
                    "minions.${Minion.getMinionName(minion.minionNo+1)}.combined" to ++GameData.minions[minion.minionNo + 1].combined,
                    "minions.${Minion.getMinionName(minion.minionNo+1)}.amount" to ++GameData.minions[minion.minionNo + 1].amount,
                ))
                    .addOnFailureListener { e ->
                    Log.w(
                        "DataBase",
                        "Error setting minions",
                        e
                    )
                }
                minion.lostToCombine += minion.combineCost
                minion.amount -= minion.combineCost
                updateDetails(minion)
                MinionsFragment.notifyAdapter(minion.minionNo+1)
                }
            }
        }

    fun setupMinion(minion: Minion){
        setMinionInfo(minion)
        setListeners(minion)
    }


        private fun setMinionInfo(minion: Minion) {
            val unitAnim: AnimationDrawable
            binding.minionImage.apply {
                setBackgroundResource(minion.sprite)
                unitAnim = background as AnimationDrawable
            }
            unitAnim.start()
            updateMinionCost(minion)
            updateDetails(minion)
            binding.minionDetails.setBackgroundResource(R.drawable.pngegg_copy)

        }

        private fun updateDetails(minion: Minion) {
            val minionName = when (minion.minionNo) {
                0 -> "Peasant"
                1 -> "Archer"
                2 -> "Rogue"
                else -> ""
            }
            binding.minionDetails.text = "\b${minionName}" +
                    "\nAmount: ${minion.amount}    Power: ${
                        if (minion.power == 0.1) {
                            minion.power
                        } else {
                            minion.power.toLong()
                        }
                    }   Combine Cost: ${minion.combineCost}"
        }

        private fun updateMinionCost(minion: Minion) {
            //TODO: add minion upgrade discount (BaseCost * MinionMulti.pow(amount-upgrade))
            calculatedTotal = (minion.cost * (GameData.MINION_MULTIPLIER).pow((minion.amount + minion.lostToCombine - minion.combined).toDouble())).toLong()
            binding.costText.text = ("Cost: $calculatedTotal")
            Log.d("MinionData", "Cost: $calculatedTotal")
        }
}
/* First Attempt at getting and setting
                db.collection("users").document(GameData.user?.email!!).get()
                    .addOnSuccessListener {
                        when {
                            it.exists() -> {
                                var minions = it["minions"] as HashMap<String, HashMap<String,Long>>
                                minions[getMinionName]!![] = minion.amount + 1
                                userDoc = hashMapOf(
                                    "exploration" to it["exploration"],
                                    "minions" to minions
                                )
                                db.collection("users").document(GameData.user?.email!!).set(userDoc)
                                    .addOnSuccessListener {
                                        Log.d(
                                            "DataBase",
                                            "Minions set successfully"
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(
                                            "DataBase",
                                            "Error setting minions",
                                            e
                                        )
                                    }
                            }
                            else -> Log.d("DataBase", "fetched empty doc, check if path is correct")
                        }
                    }
                    .addOnFailureListener {e -> Log.d("DataBase" , "failed to fetch data",e) }
                minion.amount++
                setMinionCost(minion)
            }

 */