package com.example.testing

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

class PaymentPage : AppCompatActivity() {

    private lateinit var radioGrabPay: RadioButton
    private lateinit var radioOnlineBanking: RadioButton
    private lateinit var spinnerBanks: Spinner
    private lateinit var radioAddPaymentCard: RadioButton
    private lateinit var etCardNumber: EditText
    private lateinit var etExpiryDate: EditText
    private lateinit var etCVV: EditText
    private lateinit var radioTNGWallet: RadioButton
    private lateinit var btnPay: Button
    private lateinit var packageTitleTextView: TextView
    private lateinit var packagePriceTextView: TextView
    private lateinit var packageLocationTextView: TextView
    private lateinit var dbHelper: DBHelper
    var packageId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_page)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
            startActivity(Intent(this, PackageDetail::class.java))
        }

        // Initialize
        radioGrabPay = findViewById(R.id.radioGrabPay)
        radioOnlineBanking = findViewById(R.id.radioOnlineBanking)
        spinnerBanks = findViewById(R.id.spinnerBanks)
        radioAddPaymentCard = findViewById(R.id.radioAddPaymentCard)
        etCardNumber = findViewById(R.id.etCardNo)
        etExpiryDate = findViewById(R.id.etCardExp)
        etCVV = findViewById(R.id.etCVV)
        radioTNGWallet = findViewById(R.id.radioTNGWallet)
        btnPay = findViewById(R.id.btnPay)
        dbHelper = DBHelper(this)

        packageTitleTextView = findViewById(R.id.packageTitleTextView)
        packagePriceTextView = findViewById(R.id.packagePriceTextView)
        packageLocationTextView = findViewById(R.id.packageLocationTextView)

        // Retrieve package ID from intent
        packageId = intent.getIntExtra("package_id", -1)
        if (packageId != -1) {
            // Fetch package details from DBHelper
            val packageDetailData = dbHelper.getPackageDetail(packageId)

            // Update UI with package details
            packageTitleTextView.text = packageDetailData?.title
            packagePriceTextView.text = packageDetailData?.price
            packageLocationTextView.text = packageDetailData?.location
        } else {
            // Handle case when package ID is not received
            Toast.makeText(this, "Invalid package ID", Toast.LENGTH_SHORT).show()
        }

        // Sample bank names
        val bankNames = listOf("Affin Islam Bank","CIMB Bank", "Bank Rakyat", "BSN", "Maybank", "Public Bank", "RHB Bank")

        // Create an ArrayAdapter using the bank names array and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bankNames)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinnerBanks.adapter = adapter

        // Set click listener for Pay Now button
        btnPay.setOnClickListener {
            // Check which payment option is selected
            when {
                radioGrabPay.isChecked -> payWithGrabPay()
                radioOnlineBanking.isChecked -> payWithOnlineBanking()
                radioAddPaymentCard.isChecked -> payWithPaymentCard()
                radioTNGWallet.isChecked -> payWithTNGWallet()
                else -> Toast.makeText(this, "Please select a payment option", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for payment options to show/hide corresponding input fields
        radioOnlineBanking.setOnCheckedChangeListener { _, isChecked ->
            spinnerBanks.visibility = if (isChecked) android.view.View.VISIBLE else android.view.View.GONE
        }

        radioAddPaymentCard.setOnCheckedChangeListener { _, isChecked ->
            etCardNumber.visibility = if (isChecked) android.view.View.VISIBLE else android.view.View.GONE
            etExpiryDate.visibility = if (isChecked) android.view.View.VISIBLE else android.view.View.GONE
            etCVV.visibility = if (isChecked) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    private fun payWithGrabPay() {
        // Implement payment logic for GrabPay
        // For demonstration purposes, navigate to receipt page
        navigateToReceiptPage()
    }

    private fun payWithOnlineBanking() {
        // Implement payment logic for Online Banking
        // For demonstration purposes, navigate to receipt page
        navigateToReceiptPage()
    }


    private fun payWithPaymentCard() {
        // Implement payment logic for Payment Card
        // For demonstration purposes, navigate to receipt page
        navigateToReceiptPage()
    }

    private fun payWithTNGWallet() {
        // Implement payment logic for TNG e-wallet
        // For demonstration purposes, navigate to receipt page
        navigateToReceiptPage()
    }

    private fun navigateToReceiptPage() {
        val intent = Intent(this, InvoicePage::class.java)
        intent.putExtra("package_id", packageId)
        startActivity(intent)

        // Show successful toast message
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
    }
}
