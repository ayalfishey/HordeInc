package com.ayalfishey.hordeinc.classes

import com.ayalfishey.hordeinc.data.Minions

class Minion (var cost: Long,
              var sprite : Int,
              var combineCost : Int,
              var minionNo : Int,
              var power : Double,
              var combined : Long = 0,
              var amount : Long = 0,
              var lostToCombine : Long = 0,
) {
    companion object {
        fun getMinionName(minionNumber: Int): String {
            return when (minionNumber) {
                0 -> "peasant"
                1 -> "archer"
                2 -> "rogue"
                3 -> "empty_temp"
                else -> ""
            }
        }
    }


}