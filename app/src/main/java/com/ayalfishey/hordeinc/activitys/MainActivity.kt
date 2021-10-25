package com.ayalfishey.hordeinc.activitys

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.data.GameData
import com.ayalfishey.hordeinc.databinding.ActivityMainBinding
import com.ayalfishey.hordeinc.fragments.MainFragment



class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var menuClicked : BooleanArray = booleanArrayOf(false,false,false,false,false)
    private lateinit var navController : NavController
    private lateinit var cpsHandler: Handler
    private lateinit var cpsRunnable: Runnable


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

        val mainFragment = navHostFragment.childFragmentManager.fragments.get(0) as MainFragment
        bindClickListeners()

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        cpsHandler = Handler(Looper.getMainLooper())
        cpsRunnable = Runnable{
            if (GameData.collectTimer > 0) {
                Log.d(TAG, "onCreate: ${GameData.collectTimer} ")
                GameData.coins.postValue(GameData.coins.value?.plus(GameData.enemys[GameData.maxEnemy - 1].coin))
//                        mainFragment.updateUI()
            }
            cpsHandler.postDelayed(cpsRunnable,GameData.collectTimer)
        }
        cpsHandler.postDelayed(cpsRunnable, GameData.collectTimer)
    }

    override fun onStart() {
        super.onStart()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val mainFragment = navHostFragment.childFragmentManager.fragments.get(0) as MainFragment
//        mainFragment.updateUI()
    }
    private fun bindClickListeners(){
        // Minion Button
        binding.minionButton.setOnClickListener{
            if(!menuClicked[1]){
                closeOpenFragments()
                navController.navigate(R.id.action_mainFragment_to_minionsFragment)
                binding.minionButton.setBackgroundResource(R.drawable.button2)
            }else{
                navController.navigate(R.id.action_minionsFragment_to_mainFragment)
                binding.minionButton.setBackgroundResource(R.drawable.button1)

            }
            menuClicked[1] = !menuClicked[1]
        }

        //Exploration Button
        binding.explorationButton.setOnClickListener{
            if(!menuClicked[2]){
                closeOpenFragments()
                navController.navigate(R.id.action_mainFragment_to_explorationFragment)
                binding.explorationButton.setBackgroundResource(R.drawable.button2)
                cpsHandler.removeCallbacks(cpsRunnable)
            }else{
                navController.navigate(R.id.action_explorationFragment_to_mainFragment)
                binding.explorationButton.setBackgroundResource(R.drawable.button1)
                cpsHandler.postDelayed(cpsRunnable, GameData.collectTimer)

            }
            menuClicked[2] = !menuClicked[2]
        }

        //Upgrades Button
        binding.upgradesButton.setOnClickListener{
            if(!menuClicked[3]){
                closeOpenFragments()
//                navController.navigate()
                binding.upgradesButton.setBackgroundResource(R.drawable.button2)
            }else{
//                navController.navigate()
                binding.upgradesButton.setBackgroundResource(R.drawable.button1)

            }
            menuClicked[3] = !menuClicked[3]
        }

        //Hero Button
        binding.heroButton.setOnClickListener{
            if(!menuClicked[4]){
                closeOpenFragments()
//                navController.navigate()
                binding.heroButton.setBackgroundResource(R.drawable.button2)
            }else{
//                navController.navigate()
                binding.heroButton.setBackgroundResource(R.drawable.button1)

            }
            menuClicked[4] = !menuClicked[4]
        }

        //Settings Button
        binding.settingsButton.setOnClickListener{
            if(!menuClicked[5]){
                closeOpenFragments()
//                navController.navigate()
                binding.settingsButton.setBackgroundResource(R.drawable.button2)
            }else{
//                navController.navigate()
                binding.settingsButton.setBackgroundResource(R.drawable.button1)

            }
            menuClicked[5] = !menuClicked[5]
        }
    }

    private fun closeOpenFragments() {
        //TODO: fix fragments not popping (back press shows the last fragment)
        for (i in 0..4){
            if (menuClicked[i]){
                when(i){
                    1-> {
                        navController.navigate(R.id.action_minionsFragment_to_mainFragment)
                        binding.minionButton.setBackgroundResource(R.drawable.button1)
                    }
                    2-> {
                        navController.navigate(R.id.action_explorationFragment_to_mainFragment)
                        binding.explorationButton.setBackgroundResource(R.drawable.button1)
                        cpsHandler.postDelayed(cpsRunnable, GameData.collectTimer)
                    }
//                    3-> navController.navigate(R.id.action_explorationFragment_to_mainFragment){}
//                    4-> navController.navigate(R.id.action_explorationFragment_to_mainFragment){}
//                    5-> navController.navigate(R.id.action_explorationFragment_to_mainFragment){}
                }
                menuClicked[i] = false
            }

        }
    }
}