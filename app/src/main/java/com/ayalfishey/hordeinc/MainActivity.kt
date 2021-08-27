package com.ayalfishey.hordeinc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ayalfishey.hordeinc.databinding.ActivityMainBinding
import com.ayalfishey.hordeinc.fragments.MainFragment
import com.ayalfishey.hordeinc.fragments.MinionsFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mainFragment = MainFragment()
        val minionsFragment = MinionsFragment()


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, mainFragment)
            commit()
        }
        binding.minionToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                replaceFragment(minionsFragment)

            } else {
                replaceFragment(mainFragment)
            }
        }


//        binding.minionToggle.setOnClickListener {
//            if (binding.minionToggle.isActivated) {
//
//            } else {
//
//            }
//        }
//        binding.explorationToggle.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fragmentContainerView, mainFragment)
//                commit()
//            }
//        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_up,
            R.anim.no_anim,
            R.anim.slide_down,
            R.anim.no_anim,

        )
        transaction.replace(R.id.fragmentContainerView, fragment).commit()
    }
}