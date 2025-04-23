package com.example.testing

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

class DeleteTour : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.delete_tour)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
            startActivity(Intent(this, AdminHome::class.java))
        }

        val dbHelper = DBHelper(this)
        val container: LinearLayout = findViewById(R.id.container)
        val packageDetails = dbHelper.getAllPackageDetails()
        for (packageDetail in packageDetails) {
            val cardView = layoutInflater.inflate(R.layout.package_card, container, false) as MaterialCardView

            val tourImage: ImageView = cardView.findViewById(R.id.tourImage)
            val tourTitle: TextView = cardView.findViewById(R.id.tourTitle)
            val tourStartDate: TextView = cardView.findViewById(R.id.tourStartDate)
            val tourLocation: TextView = cardView.findViewById(R.id.tourLocation)
            val tourPrice: TextView = cardView.findViewById(R.id.tourPrice)

            tourImage.setImageBitmap(BitmapFactory.decodeByteArray(packageDetail.image, 0, packageDetail.image.size))
            tourTitle.text = packageDetail.title
            tourStartDate.text = "Date: ${packageDetail.startDate}"
            tourLocation.text = "Location: ${packageDetail.location}"
            tourPrice.text = "Price: ${packageDetail.price}"

            // Create a LinearLayout to hold the button at the bottom
            val buttonLayout = LinearLayout(this)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            buttonLayout.orientation = LinearLayout.HORIZONTAL
            buttonLayout.layoutParams = params

            // Add delete button
            val deleteButton = Button(this)
            deleteButton.setBackgroundResource(R.drawable.button_style)
            deleteButton.text = "Delete"
            deleteButton.setTextColor(ContextCompat.getColor(this, R.color.white))
            val buttonParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            buttonParams.weight = 1.0f
            deleteButton.layoutParams = buttonParams
            deleteButton.setOnClickListener {
                // Handle delete operation for this tour item
                // For example, you can call a method to delete the tour from the database
                dbHelper.deletePackage(packageDetail.id)
                container.removeView(cardView)
                deleteButton.visibility = View.GONE
                Toast.makeText(this, "Package deleted successfully", Toast.LENGTH_SHORT).show()
            }
            // Add the delete button to the buttonLayout
            buttonLayout.addView(deleteButton)

            // Add the card view and buttonLayout to the container
            container.addView(cardView)
            container.addView(buttonLayout)
        }
    }
}
