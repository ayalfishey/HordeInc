package com.ayalfishey.hordeinc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.adapters.MinionAdapter
import com.ayalfishey.hordeinc.classes.Minion
import com.ayalfishey.hordeinc.data.GameData
import com.ayalfishey.hordeinc.data.Minions
import com.ayalfishey.hordeinc.databinding.FragmentMainBinding
import com.ayalfishey.hordeinc.databinding.FragmentMinionsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MinionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MinionsFragment : Fragment(R.layout.fragment_minions) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private lateinit var binding : FragmentMinionsBinding
    private lateinit var minionRecycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMinionsBinding.bind(view)
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = MinionAdapter(GameData.minions)
        adapter = binding.recyclerView.adapter as MinionAdapter

    }


    companion object {
        fun notifyAdapter(pos : Int){
            adapter.notifyItemChanged(pos)
        }
        private lateinit var adapter : MinionAdapter
    }
}