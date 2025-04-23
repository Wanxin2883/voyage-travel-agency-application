package com.example.testing

import PackageDetailData
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginPage: AppCompatActivity(){

    private lateinit var validate: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        dbHelper = DBHelper(this)

        //Add packages into the list once the app launch at the login page
        val packageDetails = listOf(
            PackageDetailData(
                1,
                "Machu Picchu Adventure",
                dbHelper.drawableToByteArray(this, R.drawable.peru),
                "10.9.2024",
                "20.9.2024",
                "Peru",
                "RM1500 per person",
                "Embark on an unforgettable journey to the ancient Inca city of Machu Picchu, one of the New Seven Wonders of the World. " +
                        "Trek along the Inca Trail, surrounded by breathtaking mountain scenery, and witness the sunrise over the majestic ruins. " +
                        "Experience the rich culture of Peru, from vibrant markets to traditional Andean villages. " +
                        "This package includes guided hikes, accommodation, meals, and transportation."
            ),
            PackageDetailData(
                2,
                "Great Wall Adventure",
                dbHelper.drawableToByteArray(this, R.drawable.china),
                "5.10.2024",
                "15.10.2024",
                "China",
                "RM1800 per person",
                "Embark on an unforgettable adventure to explore the majestic Great Wall of China. " +
                        "Hike along this ancient wonder, marveling at its architectural grandeur and historical significance. " +
                        "Experience the breathtaking landscapes and immerse yourself in Chinese culture and traditions. " +
                        "This package includes guided tours, accommodation, meals, and transportation."
            ),
            PackageDetailData(
                3,
                "Taj Mahal Expedition",
                dbHelper.drawableToByteArray(this, R.drawable.india),
                "12.11.2024",
                "22.11.2024",
                "India",
                "RM2000 per person",
                "Discover the beauty and romance of the iconic Taj Mahal in India. " +
                        "Explore this UNESCO World Heritage Site, marveling at its stunning architecture and rich history. " +
                        "Immerse yourself in the vibrant culture of India, from bustling markets to ancient temples. " +
                        "This package includes guided tours, accommodation, meals, and transportation."
            ),
            PackageDetailData(
                4,
                "Seoul Exploration",
                dbHelper.drawableToByteArray(this, R.drawable.korea),
                "8.12.2024",
                "18.12.2024",
                "Korea",
                "RM1700 per person",
                "Experience the dynamic city of Seoul in South Korea. " +
                        "Discover its modern skyscrapers alongside ancient palaces and temples. " +
                        "Indulge in Korean cuisine, explore vibrant neighborhoods, and shop in trendy boutiques. " +
                        "This package includes guided tours, accommodation, meals, and transportation."
            ),
            PackageDetailData(
                5,
                "Tokyo Adventure",
                dbHelper.drawableToByteArray(this, R.drawable.japan),
                "15.1.2025",
                "25.1.2025",
                "Japan",
                "RM2200 per person",
                "Embark on an exciting adventure in the bustling metropolis of Tokyo, Japan. " +
                        "Discover the fusion of traditional culture and cutting-edge technology. " +
                        "Visit iconic landmarks, savor delicious cuisine, and experience the energy of this vibrant city. " +
                        "This package includes guided tours, accommodation, meals, and transportation."
            ),
            PackageDetailData(
                6,
                "Bangkok Bliss",
                dbHelper.drawableToByteArray(this, R.drawable.thailand),
                "20.2.2025",
                "1.3.2025",
                "Thailand",
                "RM1600 per person",
                "Explore the vibrant city of Bangkok, Thailand's bustling capital. " +
                        "Discover ancient temples, bustling markets, and vibrant nightlife. " +
                        "Indulge in delicious Thai cuisine and experience the warmth and hospitality of the Thai people. " +
                        "This package includes guided tours, accommodation, meals, and transportation."
            )
        )
        for (packageDetail in packageDetails) {
            dbHelper.addPackage(
                packageDetail.id,
                packageDetail.title,
                packageDetail.image,
                packageDetail.startDate,
                packageDetail.endDate,
                packageDetail.location,
                packageDetail.price,
                packageDetail.desc
            )
        }

        val btn = findViewById<TextView>(R.id.signupbutton)
        btn.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }

        val btn2 = findViewById<TextView>(R.id.forgotbutton)
        btn2.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        email = findViewById(R.id.editTextTextEmailAddress2)
        password = findViewById(R.id.editTextTextPassword2)
        validate = findViewById(R.id.button3)
        dbHelper = DBHelper(this)

        validate.setOnClickListener {
            val emailTEXT = email.text.toString()
            val passwordTEXT = password.text.toString()
            val validateData = dbHelper.validUserData(emailTEXT, passwordTEXT)
            if (emailTEXT == "admin@voyage.com" && passwordTEXT == "Voyage8888"){
                Toast.makeText(this, "Sign in Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminHome::class.java)
                startActivity(intent)
            }
            else if (TextUtils.isEmpty(emailTEXT) || TextUtils.isEmpty(passwordTEXT)) {
                Toast.makeText(this, "Please enter your email and password!", Toast.LENGTH_SHORT)
                    .show()
            } else if (validateData) {
                // Sign in successfully
                Toast.makeText(this, "Sign in Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
            } else {
                // Account not registered
                Toast.makeText(this, "Invalid input. Please Try Again!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
