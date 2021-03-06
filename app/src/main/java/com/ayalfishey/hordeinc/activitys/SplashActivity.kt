package com.ayalfishey.hordeinc.activitys



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayalfishey.hordeinc.data.GameData
import com.ayalfishey.hordeinc.databinding.ActivitySplashBinding
import com.ayalfishey.hordeinc.views.hideAndroidUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase

import kotlinx.coroutines.runBlocking


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth
    private var currentUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideAndroidUI()
        auth = Firebase.auth
        currentUser = auth.currentUser
        loadActivity()
    }
    private  fun loadActivity(){
        if(currentUser != null){
            runBlocking { GameData.loadInfo()}
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}