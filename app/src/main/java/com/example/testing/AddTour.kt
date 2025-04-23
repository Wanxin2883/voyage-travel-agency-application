package com.example.testing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

class AddTour : AppCompatActivity() {

    private lateinit var uploadImageButton: ImageButton
    private lateinit var addPackageId: EditText
    private lateinit var addTourTitle: EditText
    private lateinit var startDate: TextView
    private lateinit var endDate: TextView
    private lateinit var addTourLocation: EditText
    private lateinit var addTourPrice: EditText
    private lateinit var addTourDesc: EditText
    private lateinit var submitButton: Button
    private lateinit var dbHelper: DBHelper
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_tour)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
            startActivity(Intent(this, AdminHome::class.java))
        }

        uploadImageButton = findViewById(R.id.uploadImageButton)
        addPackageId = findViewById(R.id.addPackageId)
        addTourTitle = findViewById(R.id.addTourTitle)
        startDate = findViewById(R.id.startDate)
        endDate = findViewById(R.id.endDate)
        addTourLocation = findViewById(R.id.addTourLocation)
        addTourPrice = findViewById(R.id.addTourPrice)
        addTourDesc = findViewById(R.id.addTourDesc)
        submitButton = findViewById(R.id.submitButton)
        dbHelper = DBHelper(this)

        submitButton.setOnClickListener {
            val id = addPackageId.text.toString().toInt()
            val title = addTourTitle.text.toString().trim()
            val image = uploadImageButton.drawable.toBitmap()
            val startDate = startDate.text.toString().trim()
            val endDate = endDate.text.toString().trim()
            val location = addTourLocation.text.toString().trim()
            val price = addTourPrice.text.toString().trim()
            val desc = addTourDesc.text.toString().trim()
            if (id.toString().isNotEmpty() && title.isNotEmpty() &&
                startDate.isNotEmpty() && endDate.isNotEmpty() &&
                location.isNotEmpty() && price.isNotEmpty() && desc.isNotEmpty()){
                val convertImage = bitmapToByteArray(image)
                if (dbHelper.addPackage(id, title, convertImage, startDate, endDate, location, price, desc)){
                    Toast.makeText(this, "Save successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AdminHome::class.java))
                } else{
                    Toast.makeText(this, "Failed to add new package. Please try again!", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            }
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

        if (datePicker.visibility == android.view.View.GONE) {
            datePicker.visibility = android.view.View.VISIBLE
            val params = datePicker.layoutParams
            params.height = 1100
            datePicker.layoutParams = params
        } else {
            datePicker.visibility = android.view.View.GONE
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
            uploadImageButton.setImageURI(selectedImageUri)
        }
    }
}
