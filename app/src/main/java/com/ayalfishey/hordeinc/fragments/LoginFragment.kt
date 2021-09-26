package com.ayalfishey.hordeinc.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.activitys.MainActivity
import com.ayalfishey.hordeinc.databinding.FragmentLoginBinding
import com.ayalfishey.hordeinc.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        auth = Firebase.auth

        binding.registerText.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.forgotText.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
        }
        binding.loginButton.setOnClickListener(){
            loginUser()
        }
    }


    private fun loginUser() {
        val email = binding.editTextTextEmailAddress.text.toString().trim { it <= ' ' }
        val password = binding.editTextTextPassword.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            binding.editTextTextEmailAddress.setError("Email is required")
            binding.editTextTextEmailAddress.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextTextEmailAddress.setError("Please provide a valid email")
            binding.editTextTextEmailAddress.requestFocus()
            return

        }
        if (password.isEmpty()) {
            binding.editTextTextPassword.setError("Password is required")
            binding.editTextTextPassword.requestFocus()
            return
        }
        if (password.length < 6) {
            binding.editTextTextPassword.setError("Password must be at least 6 characters long")
            binding.editTextTextPassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() {
            if (it.isSuccessful) {
                Toast.makeText(
                    context,
                    "User has been logged in successfully",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("user_id", auth.currentUser!!.uid)
                intent.putExtra("email_id", email)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(
                    context,
                    "Failed to Login. Try again, " + it.exception!!.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}