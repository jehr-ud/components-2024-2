package com.ud.memorygame.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.ud.memorygame.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        if (isUserDataSaved()) {
            goToLevelActivity()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // User created successfully
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext,
                            "Cuenta creada con éxito: ${user?.email}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthUserCollisionException) {
                            // Email already registered, try to log in
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { loginTask ->
                                    if (loginTask.isSuccessful) {
                                        // sing in successful
                                        val user = auth.currentUser
                                        Toast.makeText(
                                            baseContext,
                                            "Inicio de sesión exitoso: ${user?.email}",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        user?.let {
                                            saveUserData(user.uid, user.email)
                                        }

                                        goToLevelActivity()
                                    } else {
                                        // Error in sing in
                                        Toast.makeText(
                                            baseContext,
                                            "Error al iniciar sesión: ${loginTask.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            // another error
                            Toast.makeText(
                                baseContext,
                                "Error: ${exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }

    }

    private fun isUserDataSaved(): Boolean {
        val userId = sharedPreferences.getString("userId", "")
        return userId != ""
    }

    private fun saveUserData(userId: String, email: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        email?.let { editor.putString("email", it) }
        editor.apply()
    }

    private fun goToLevelActivity(){
        startActivity(Intent(this, LevelActivity::class.java))
        finish()
    }
}