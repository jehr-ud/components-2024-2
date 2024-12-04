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
            goToGame()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Usuario creado exitosamente
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext,
                            "Cuenta creada con éxito: ${user?.email}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthUserCollisionException) {
                            // El correo ya está registrado, intentar iniciar sesión
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { loginTask ->
                                    if (loginTask.isSuccessful) {
                                        // Inicio de sesión exitoso
                                        val user = auth.currentUser
                                        Toast.makeText(
                                            baseContext,
                                            "Inicio de sesión exitoso: ${user?.email}",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        user?.let {
                                            saveUserData(user.uid, user.email)
                                        }

                                        goToGame()
                                    } else {
                                        // Error al iniciar sesión
                                        Toast.makeText(
                                            baseContext,
                                            "Error al iniciar sesión: ${loginTask.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            // Otro error durante la creación de la cuenta
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

    private fun goToGame(){
        startActivity(Intent(this, MatchActivity::class.java))
        finish()
    }
}