package com.example.testing

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class ViewAllPackage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_package)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
            startActivity(Intent(this, HomePage::class.java))
        }

        // Assuming `dbHelper` is an instance of your DBHelper class
        val dbHelper = DBHelper(this)

        // Get a reference to the layout where you want to add the card views
        val container: LinearLayout = findViewById(R.id.container)

        // Retrieve package details from the database
        val packageDetails = dbHelper.getAllPackageDetails()

        // Iterate through each package detail and create a card view
        for (packageDetail in packageDetails) {
            // Inflate the card view layout
            val cardView = layoutInflater.inflate(R.layout.package_card, container, false) as MaterialCardView

            // Find views within the card view layout
            val tourImage: ImageView = cardView.findViewById(R.id.tourImage)
            val tourTitle: TextView = cardView.findViewById(R.id.tourTitle)
            val tourStartDate: TextView = cardView.findViewById(R.id.tourStartDate)
            val tourLocation: TextView = cardView.findViewById(R.id.tourLocation)
            val tourPrice: TextView = cardView.findViewById(R.id.tourPrice)

            // Set package details to the views
            tourImage.setImageBitmap(BitmapFactory.decodeByteArray(packageDetail.image, 0, packageDetail.image.size))
            tourTitle.text = packageDetail.title
            tourStartDate.text = "Date: ${packageDetail.startDate}"
            tourLocation.text = "Location: ${packageDetail.location}"
            tourPrice.text = "Price: ${packageDetail.price}"
            // Add the card view to the container
            container.addView(cardView)

            // Add a click listener to the card view
            cardView.setOnClickListener {
                // Start a new activity to display the details of the selected tour package
                val intent = Intent(this, PackageDetail::class.java)
                intent.putExtra("package_id", packageDetail.id) // Pass the package ID to the next activity
                startActivity(intent)
            }
        }
    }
}
