package com.ayalfishey.hordeinc.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.classes.Enemy
import com.ayalfishey.hordeinc.classes.Minion
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class GameData : ViewModel() {

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
        var enemys : ArrayList<Enemy> = arrayListOf()
        var currentEnemy : Int = 0
        var maxEnemy : Int = currentEnemy
        var heroMaxHp: Long = 100
        var heroCurrentHp : Long = heroMaxHp
        var enemyCurrentHp : Long = 100
        val coins : MutableLiveData <Long> by lazy { MutableLiveData<Long>()}
//            .also { loadInfo() } }
        val power : MutableLiveData <Long> by lazy { MutableLiveData<Long>()}
//            .also { updatePower() } }
        var collectTimer : Long = 0

        fun updatePower() : Long{
            var armyPower : Long = 0
            for (minion in minions){
                armyPower+=(minion.amount.toDouble() * minion.power).toLong()
            }
            armyPower++
            power.postValue(armyPower)
            return armyPower
        }
        fun loadInfo(){
            val db = Firebase.firestore
                db.collection("users").document(user?.email!!).get().addOnSuccessListener { document ->
                    when { document.exists() -> {
                       val dbMinions = document["minions"] as HashMap<String,HashMap<String,Long>>
                            minions[0].amount = dbMinions["peasant"]!!["amount"]!!
                            minions[0].combined = dbMinions["peasant"]!!["combined"]!!
                            minions[1].amount = dbMinions["archer"]!!["amount"]!!
                            minions[1].combined = dbMinions["archer"]!!["combined"]!!
                            minions[2].amount = dbMinions["rogue"]!!["amount"]!!
                            minions[2].combined = dbMinions["rogue"]!!["combined"]!!
                        currentEnemy = (document["exploration"] as Long).toInt()
                        maxEnemy = currentEnemy
                        enemyCurrentHp = enemys[currentEnemy].hp
                        coins.value = document["coins"] as Long
                        collectTimer = document["collect timer"] as Long
//                        power = document["power"] as Long
                        updatePower()
                    } else -> {
                        Log.d("Database", "No Document")
                }
                    }
                }.addOnFailureListener{
                    Log.d("Database", "get failed with", it)
                }
            for (i in 0..100){
                //TODO: add more unique enemys
                enemys.add(Enemy(R.drawable.skele_anim,i,(1.45*i+1).toLong()+5,(1.45 * i +1).toLong()+10, (1.3 * i+1).toLong(),"Skeleton ${i+1}"))
            }
//            updatePower()
        }
    }


}