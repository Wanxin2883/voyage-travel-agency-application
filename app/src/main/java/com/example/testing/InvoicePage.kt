package com.example.testing
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InvoicePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_page)

        val dbHelper = DBHelper(this)

        val tvPackageTitle = findViewById<TextView>(R.id.tvPackageTitle)
        val tvPaidAmount = findViewById<TextView>(R.id.tvPaidAmount)

        // Retrieve package details from the database
        val packageId = intent.getIntExtra("package_id", -1)
        val packageDetailData = dbHelper.getPackageDetail(packageId) // Assuming you have the package ID
        tvPackageTitle.text = packageDetailData?.title
        tvPaidAmount.text = packageDetailData?.price

        val btnHome = findViewById<Button>(R.id.btnHome)
        btnHome.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }
}
