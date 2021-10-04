package com.ayalfishey.hordeinc.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.databinding.FragmentForgotBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotFragment : Fragment(R.layout.fragment_forgot) {

    private lateinit var binding: FragmentForgotBinding
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForgotBinding.bind(view)
        auth = Firebase.auth
        binding.forgotButton.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim { it <= ' ' }
            if(isEmailValid(email)){
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Failed to send. Try again, " + it.exception.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        if (email.isEmpty()) {
            binding.editTextTextEmailAddress.error = "Email is required"
            binding.editTextTextEmailAddress.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextTextEmailAddress.error = "Please provide a valid email"
            binding.editTextTextEmailAddress.requestFocus()
            return false

        }
        return true
    }

}