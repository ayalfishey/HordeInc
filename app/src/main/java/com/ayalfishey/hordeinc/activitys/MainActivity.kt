package com.ayalfishey.hordeinc.activitys

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var menuClicked : BooleanArray = booleanArrayOf(false,false,false,false,false)
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(android.view.WindowInsets.Type.navigationBars())
                it.hide(android.view.WindowInsets.Type.statusBars())
//                window.navigationBarColor = getColor(R.color.design_default_color_background)

            }
        }else{
            //TODO: Fix screen black bar and nav not disappearing after swipe
            @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        )
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
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