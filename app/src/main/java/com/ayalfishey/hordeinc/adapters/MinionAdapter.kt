package com.ayalfishey.hordeinc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.databinding.MinionCellBinding

class MinionAdapter : RecyclerView.Adapter<MinionAdapter.MinionHolder>() {

    private val details = arrayOf("test detail 1", "test detail 2")
    private val images = intArrayOf(R.drawable.reaper, R.drawable.reaper)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinionHolder {
        val view = MinionCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MinionHolder(view)
    }

    override fun onBindViewHolder(holder: MinionHolder, position: Int) {
        holder.binding.minionDetails.text = details[position]
        holder.binding.minionImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return details.size
    }

    inner class MinionHolder (val binding : MinionCellBinding) : RecyclerView.ViewHolder(binding.root){

    }
}