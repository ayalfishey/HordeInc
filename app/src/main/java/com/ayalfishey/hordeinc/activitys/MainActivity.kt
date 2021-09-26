package com.ayalfishey.hordeinc.activitys

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var menuClicked : BooleanArray = booleanArrayOf(false,false,false,false,false)
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController!!.hide(android.view.WindowInsets.Type.statusBars())
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        bindClickListeners()

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

    }

    private fun bindClickListeners(){
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
    }

    private fun closeOpenFragments() {
        //TODO: fix fragments not popping (back press shows the last fragment)
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