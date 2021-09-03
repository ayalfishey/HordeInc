package com.ayalfishey.hordeinc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ayalfishey.hordeinc.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var menuClicked : BooleanArray = booleanArrayOf(false,false,false,false,false)
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController


        binding.minionButton.setOnClickListener{
            if(!menuClicked[1]){
                closeOpenFragments()
                navController.navigate(R.id.action_mainFragment_to_minionsFragment)
                binding.minionButton.setImageResource(R.drawable.glossy_button_paint2)
            }else{
                navController.navigate(R.id.action_minionsFragment_to_mainFragment)
                binding.minionButton.setImageResource(R.drawable.glossy_button_paint1)

            }
            menuClicked[1] = !menuClicked[1]
        }
        binding.explorationToggle.setOnClickListener{
            if(!menuClicked[2]){
                closeOpenFragments()
                navController.navigate(R.id.action_mainFragment_to_explorationFragment)
//                binding.minionButton.setImageResource(R.drawable.glossy_button_paint2)
            }else{
                navController.navigate(R.id.action_explorationFragment_to_mainFragment)
//                binding.minionButton.setImageResource(R.drawable.glossy_button_paint1)

            }
            menuClicked[2] = !menuClicked[2]
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

    private fun closeOpenFragments() {
        for (i in 0..4){
            if (menuClicked[i]){
                when(i){
                    1-> {
                        navController.navigate(R.id.action_minionsFragment_to_mainFragment)
                        binding.minionButton.setImageResource(R.drawable.glossy_button_paint1)
                    }
                    2-> navController.navigate(R.id.action_explorationFragment_to_mainFragment)
//                    3-> navController.navigate(R.id.action_explorationFragment_to_mainFragment)
//                    4-> navController.navigate(R.id.action_explorationFragment_to_mainFragment)
//                    5-> navController.navigate(R.id.action_explorationFragment_to_mainFragment)
                }
                menuClicked[i] = false
            }

        }
    }
}