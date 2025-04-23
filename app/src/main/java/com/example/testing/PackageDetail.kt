package com.example.testing

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class PackageDetail : AppCompatActivity() {

    private lateinit var displayTitle: TextView
    private lateinit var displayImage: ImageView
    private lateinit var displayStartDate: TextView
    private lateinit var displayEndDate: TextView
    private lateinit var displayLocation: TextView
    private lateinit var displayPrice: TextView
    private lateinit var displayDesc: TextView
    private lateinit var bookButton: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.package_detail)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
            startActivity(Intent(this, HomePage::class.java))
        }

        displayTitle = findViewById(R.id.p_title)
        displayImage = findViewById(R.id.p_image)
        displayStartDate = findViewById(R.id.p_startDate)
        displayEndDate = findViewById(R.id.p_endDate)
        displayLocation = findViewById(R.id.p_location)
        displayPrice = findViewById(R.id.p_price)
        displayDesc = findViewById(R.id.p_desc)
        bookButton =findViewById(R.id.bookButton)
        dbHelper = DBHelper(this)

        val packageId = intent.getIntExtra("package_id", -1)
        if (packageId != -1) {
            val packageDetail = dbHelper.getPackageDetail(packageId)
            if (packageDetail != null) {
                displayTitle.text = packageDetail.title
                displayImage.setImageBitmap(BitmapFactory.decodeByteArray(packageDetail.image, 0, packageDetail.image.size))
                displayStartDate.text = packageDetail.startDate
                displayEndDate.text = packageDetail.endDate
                displayLocation.text = packageDetail.location
                displayPrice.text = packageDetail.price
                displayDesc.text = packageDetail.desc
            } else {
                // Handle case when package detail is not found
                Toast.makeText(this, "Package detail not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle case when package ID is not received
            Toast.makeText(this, "Invalid package ID", Toast.LENGTH_SHORT).show()
        }

        bookButton.setOnClickListener {
            val intent = Intent(this, PaymentPage::class.java)
            intent.putExtra("package_id", packageId)
            startActivity(intent)
        }
    }
}
