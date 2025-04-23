package com.example.testing

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

class EditTour : AppCompatActivity() {

    private lateinit var editTourImage: ImageButton
    private lateinit var editTourTitle: EditText
    private lateinit var editStartDate: TextView
    private lateinit var editEndDate: TextView
    private lateinit var editTourLocation: EditText
    private lateinit var editTourPrice: EditText
    private lateinit var editTourDesc: EditText
    private lateinit var submitButton: Button
    private lateinit var dbHelper: DBHelper
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_tour)

        editTourImage = findViewById(R.id.editTourImage)
        editTourTitle = findViewById(R.id.editTourTitle)
        editStartDate = findViewById(R.id.startDate)
        editEndDate = findViewById(R.id.endDate)
        editTourLocation = findViewById(R.id.editTourLocation)
        editTourPrice = findViewById(R.id.editTourPrice)
        editTourDesc = findViewById(R.id.editTourDesc)
        submitButton = findViewById(R.id.submitButton)
        dbHelper = DBHelper(this)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            startActivity(Intent(this, SelectEditTour::class.java))
        }

        // Retrieve the package ID from the intent extras
        val packageId = intent.getIntExtra("package_id", -1)

        // Retrieve the package details from the database
        val packageDetail = dbHelper.getPackageDetail(packageId)

        if (packageDetail != null) {
            // Populate the EditText fields with the current package details
            editTourTitle.setText(packageDetail.title)
            editStartDate.setText(packageDetail.startDate)
            editEndDate.setText(packageDetail.endDate)
            editTourLocation.setText(packageDetail.location)
            editTourPrice.setText(packageDetail.price)
            editTourDesc.setText(packageDetail.desc)
            // Load and display the current image of the tour package in the ImageView
            val bitmap = BitmapFactory.decodeByteArray(packageDetail.image, 0, packageDetail.image.size)
            editTourImage.setImageBitmap(bitmap)
        }

        // Set click listener for the floating action button (Save changes)
        submitButton.setOnClickListener {
            // Retrieve the new values from the EditText fields
            val newTitle = editTourTitle.text.toString()
            val newImage = editTourImage.drawable.toBitmap()
            val newLocation = editTourLocation.text.toString()
            val newStartDate = editStartDate.text.toString()
            val newEndDate = editEndDate.text.toString()
            val newPrice = editTourPrice.text.toString()
            val newDesc = editTourDesc.text.toString()

            val convertImage = bitmapToByteArray(newImage)
            dbHelper.updatePackage(packageId, newTitle, convertImage, newStartDate, newEndDate, newLocation, newPrice, newDesc)
            Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show()

            // After saving changes, navigate back to ViewAllPackage activity
            startActivity(Intent(this, AdminHome::class.java))
        }
        //Hide and Show the Start Date and End Date
        val startDateButton = findViewById<Button>(R.id.startDateButton)
        val endDateButton = findViewById<Button>(R.id.endDateButton)

        startDateButton.setOnClickListener {
            toggleDatePickerVisibility(R.id.startDatePicker, R.id.startDate)
        }

        endDateButton.setOnClickListener {
            toggleDatePickerVisibility(R.id.endDatePicker, R.id.endDate)
        }
    }

    private fun toggleDatePickerVisibility(datePickerId: Int, selectedDateId: Int) {
        val datePicker = findViewById<DatePicker>(datePickerId)

        if (datePicker.visibility == View.GONE) {
            datePicker.visibility = View.VISIBLE
            val params = datePicker.layoutParams
            params.height = 1100
            datePicker.layoutParams = params
        } else {
            datePicker.visibility = View.GONE
            val selectedDate = findViewById<TextView>(selectedDateId)
            selectedDate.text =
                "${datePicker.year}.${datePicker.month + 1}.${datePicker.dayOfMonth}"
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream)
        return stream.toByteArray()
    }

    fun selectImage(view: View){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            editTourImage.setImageURI(selectedImageUri)
        }
    }
}
