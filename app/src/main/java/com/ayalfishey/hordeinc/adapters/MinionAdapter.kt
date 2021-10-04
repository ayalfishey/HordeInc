package com.ayalfishey.hordeinc.adapters

import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.classes.Minion
import com.ayalfishey.hordeinc.databinding.MinionCellBinding
import com.ayalfishey.hordeinc.databinding.MinionCellLayoutBinding
import com.ayalfishey.hordeinc.views.MinionCell

class MinionAdapter (private val minions: ArrayList<Minion>) : RecyclerView.Adapter<MinionAdapter.MinionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinionHolder {
        val view = MinionCellLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MinionHolder(view)
    }

    override fun onBindViewHolder(holder: MinionHolder, position: Int) {
        holder.minionCell.setupMinion(minions[position])
        }


    override fun getItemCount(): Int {
        return minions.size
    }

    inner class MinionHolder (val binding : MinionCellLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val minionCell : MinionCell = binding.root

    }
}