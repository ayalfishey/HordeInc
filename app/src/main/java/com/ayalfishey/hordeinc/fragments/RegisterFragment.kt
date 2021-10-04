package com.ayalfishey.hordeinc.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.ayalfishey.hordeinc.R
import com.ayalfishey.hordeinc.activitys.MainActivity
import com.ayalfishey.hordeinc.classes.Minion
import com.ayalfishey.hordeinc.data.GameData
import com.ayalfishey.hordeinc.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
//    private lateinit var db : FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        auth = Firebase.auth
//        db = Firebase.firestore
        binding.registerButton.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val email = binding.editTextTextEmailAddress.text.toString().trim { it <= ' ' }
        val password = binding.editTextTextPassword.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            binding.editTextTextEmailAddress.error = "Email is required"
            binding.editTextTextEmailAddress.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextTextEmailAddress.error = "Please provide a valid email"
            binding.editTextTextEmailAddress.requestFocus()
            return

        }
        if (password.isEmpty()) {
            binding.editTextTextPassword.error = "Password is required"
            binding.editTextTextPassword.requestFocus()
            return
        }
        if (password.length < 6) {
            binding.editTextTextPassword.error = "Password must be at least 6 characters long"
            binding.editTextTextPassword.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val firebaseUser: FirebaseUser = it.result!!.user!!
                Toast.makeText(
                    context,
                    "User has been registered successfully",
                    Toast.LENGTH_LONG
                ).show()
                //TODO: Redo minions in Database to hold minion object
                //Writing to db new user
                val user = hashMapOf(
                    "exploration" to 0,
                    "minions" to hashMapOf<String,HashMap<String,Long>>(
                        "peasant" to hashMapOf<String, Long>(
                            "amount" to 0,
                            "combined" to 0,
                            "lost_to_combine" to 0
                        ),
                        "archer" to hashMapOf<String, Long>(
                            "amount" to 0,
                            "combined" to 0,
                            "lost_to_combine" to 0
                        ),
                        "rogue" to hashMapOf<String, Long>(
                            "amount" to 0,
                            "combined" to 0,
                            "lost_to_combine" to 0
                        ),
                    )
                 )
                val db = Firebase.firestore
                db.collection("users").document(email).set(user).addOnSuccessListener {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("user_id", firebaseUser.uid)
                    intent.putExtra("email_id", email)
                    startActivity(intent)
                    activity?.finish()
                }
                    .addOnFailureListener {
                        auth.currentUser?.delete()
                        Log.d("Database", "Failed to add user to collections")
                    }

            } else {
                Toast.makeText(
                    context,
                    "Failed to register. Try again, " + it.exception!!.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
