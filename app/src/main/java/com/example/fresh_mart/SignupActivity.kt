package com.example.fresh_mart

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        // Firebase init
        auth = FirebaseAuth.getInstance()

        // UI IDs
        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val confirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val tvSignin = findViewById<TextView>(R.id.tvSignup)

        // SIGNUP BUTTON
        btnSignup.setOnClickListener {

            val n = name.text.toString().trim()
            val e = email.text.toString().trim()
            val p = password.text.toString().trim()
            val cp = confirmPassword.text.toString().trim()

            if (n.isEmpty() || e.isEmpty() || p.isEmpty() || cp.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (p != cp) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (p.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase Signup
            auth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()

                        // Go to Login
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Go to Login
        tvSignin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}