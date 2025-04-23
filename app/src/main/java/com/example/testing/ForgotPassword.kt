package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPassword: AppCompatActivity() {

    private lateinit var submitButton: Button
    private lateinit var cancelButton: TextView
    private lateinit var emailEditText: EditText
    private lateinit var newPassword: EditText
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)

        cancelButton = findViewById<TextView>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            // Handle cancel action here
            // For example, navigate back to the sign in page
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }


        submitButton = findViewById(R.id.submitButton)
        emailEditText = findViewById(R.id.editTextTextEmailAddress0)
        newPassword = findViewById(R.id.editTextTextPassword0)
        db = DBHelper(this)

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    this,
                    "Please enter your email",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (db.checkEmailExists(email)) {
                db.updatePassword(email, newPassword.text.toString())
                Toast.makeText(
                    this,
                    "Password reset successful",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
            } else {
                // Email does not exist
                Toast.makeText(
                    this,
                    "Email not found. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
