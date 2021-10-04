package com.ayalfishey.hordeinc.data

import android.util.Log
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.classes.Minion
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class GameData {

    companion object{

        val user = Firebase.auth.currentUser
        val PEASANT_BASE_COST : Long = 15
        val ARCHER_BASE_COST : Long = 100
        val ROGUE_BASE_COST : Long = 1100
        val MINION_MULTIPLIER = 1.15
        var minions : ArrayList<Minion> = arrayListOf(
            Minion(GameData.PEASANT_BASE_COST, R.drawable.pesent_anim, 9, 0, 0.1),
            Minion(GameData.ARCHER_BASE_COST,  R.drawable.archer_anim, 18, 1,1.0),
            Minion(GameData.ROGUE_BASE_COST, R.drawable.rogue_anim, 36, 2, 20.0),
        )


        fun updatePower() : Long{
            var armyPower : Long = 0
            for (minion in minions){
                armyPower+=(minion.amount.toDouble() * minion.power).toLong()
            }
            return armyPower
        }
        fun loadInfo(){
            val db = Firebase.firestore
                user?.email?.let { email ->
                db.collection("users").document(email).get().addOnSuccessListener { document ->
                    when { document.exists() -> {
                       val dbMinions = document["minions"] as HashMap<String,HashMap<String,Long>>
                            minions[0].amount = dbMinions["peasant"]!!["amount"]!!
                            minions[0].combined = dbMinions["peasant"]!!["combined"]!!
                            minions[1].amount = dbMinions["archer"]!!["amount"]!!
                            minions[1].combined = dbMinions["archer"]!!["combined"]!!
                            minions[2].amount = dbMinions["rogue"]!!["amount"]!!
                            minions[2].combined = dbMinions["rogue"]!!["combined"]!!
                    } else -> {
                        Log.d("Database", "No Document")
                }
                    }
                }.addOnFailureListener{
                    Log.d("Database", "get failed with", it)
                }
            }

        }
    }


}