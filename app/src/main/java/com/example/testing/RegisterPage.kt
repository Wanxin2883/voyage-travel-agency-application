package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterPage: AppCompatActivity() {

    private lateinit var signUpButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        val loginButton = findViewById<TextView>(R.id.signinbutton)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        signUpButton = findViewById(R.id.button)
        nameEditText = findViewById(R.id.editTextTextPersonName)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextTextPassword)
        db = DBHelper(this)

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(
                    this,
                    "Please enter all fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (db.checkEmailExists(email)) {
                Toast.makeText(
                    this,
                    "Email already exists. Please log in.",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
            } else {
                db.addUser(name, phone, email, password)
                Toast.makeText(
                    this,
                    "Account created successfully. Please log in.",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
            }
        }
    }
}