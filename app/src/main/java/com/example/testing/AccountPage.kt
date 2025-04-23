package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccountPage : AppCompatActivity() {

    private val dbHelper = DBHelper(this)

    private lateinit var editName: EditText
    private lateinit var editPhone: EditText
    private lateinit var editEmail: EditText
    private lateinit var submitEditButton: Button

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomePage::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_favorite -> {
                    startActivity(Intent(this, FavoritePage::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_account -> {
                    startActivity(Intent(this, AccountPage::class.java))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_page)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        editName = findViewById(R.id.editName)
        editPhone = findViewById(R.id.editPhone)
        editEmail = findViewById(R.id.editEmail)
        submitEditButton = findViewById(R.id.submitEditButton)

        // Retrieve the user's information and populate the EditText fields
        val userInfo = dbHelper.getUserInfo() // Implement getUserInfo() in DBHelper
        editName.setText(userInfo.name)
        editPhone.setText(userInfo.phone)
        editEmail.setText(userInfo.email)

        // Set click listener for the submit button
        submitEditButton.setOnClickListener {
            val newName = editName.text.toString()
            val newPhone = editPhone.text.toString()
            val newEmail = editEmail.text.toString()

            // Update user information in the database
            val isUpdated = dbHelper.updateUser(newName, newPhone, newEmail)

            if (isUpdated) {
                Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Unable to Update. Please Try Again!", Toast.LENGTH_SHORT).show()
            }
        }
        val logout = findViewById<TextView>(R.id.logoutButton)
        logout.setOnClickListener{
            startActivity(Intent(this, LoginPage::class.java))
        }
    }
}
