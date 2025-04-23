package com.example.testing

import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class AdminHome: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_home)

        val addTour = findViewById<TextView>(R.id.addTour)
        addTour.setOnClickListener {
            val intent = Intent(this, AddTour::class.java)
            startActivity(intent)
        }

        val editTour = findViewById<TextView>(R.id.updateTour)
        editTour.setOnClickListener {
            val intent = Intent(this, SelectEditTour::class.java)
            startActivity(intent)
        }

        val deleteTour = findViewById<TextView>(R.id.deleteTour)
        deleteTour.setOnClickListener {
            val intent = Intent(this, DeleteTour::class.java)
            startActivity(intent)
        }

        val logout = findViewById<TextView>(R.id.logoutButton)
        logout.setOnClickListener{
            startActivity(Intent(this, LoginPage::class.java))
        }
    }
}