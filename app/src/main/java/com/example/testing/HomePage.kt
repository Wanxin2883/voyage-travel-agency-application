package com.example.testing

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.bottomnavigation.BottomNavigationView

//Home Page
class HomePage : AppCompatActivity() {

    private val categoryList = arrayOf("Tour", "Attraction", "Ticket", "Hotel", "Transport")
    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbHelper: DBHelper

    //For Bottom Navigation bar
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
        setContentView(R.layout.home_page)  //To display the layout of homepage

        dbHelper = DBHelper(this)

        //Navigation element in Home Page
        val allPackage = findViewById<TextView>(R.id.viewAllPackage1)
        val allPackage2 = findViewById<TextView>(R.id.viewAllPackage2)
        allPackage.setOnClickListener{
            startActivity(Intent(this, ViewAllPackage::class.java))
        }
        allPackage2.setOnClickListener{
            startActivity(Intent(this, ViewAllPackage::class.java))
        }

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Initialize views
        listView = findViewById(R.id.listView)
        searchView = findViewById(R.id.searchView)

        val allPackageDetails = dbHelper.getAllPackageDetails()

        // Populate list view with all package titles
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, allPackageDetails.map { it.title })
        listView.adapter = adapter

        // Set item click listener for list view
        listView.setOnItemClickListener { _, _, position, _ ->
            val packageName = adapter.getItem(position)
            val packageDetail = allPackageDetails.find { it.title == packageName }
            if (packageDetail != null) {
                val intent = Intent(this, PackageDetail::class.java)
                intent.putExtra("package_id", packageDetail.id)
                startActivity(intent)
            }
        }

        // Set up search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        // Hide the ListView initially
        listView.visibility = android.view.View.GONE

        // Set up search view focus change listener
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            // Show or hide the ListView based on search view focus
            listView.visibility = if (hasFocus) {
                // Adjust the height of the ListView when it has focus
                val params = listView.layoutParams
                params.height = 500 // Set the desired height here
                listView.layoutParams = params

                android.view.View.VISIBLE
            } else {
                // Hide the ListView when search view loses focus
                android.view.View.GONE
            }
        }

        //To display different categories of tourism package
        val categoryLayout: LinearLayout = findViewById(R.id.layoutCategories)
        val countryLayout: LinearLayout = findViewById(R.id.layoutRecommended)
        val packageLayoutNew: LinearLayout = findViewById(R.id.layoutNew)

        val categoryIcon = arrayOf(
            R.drawable.baseline_flight_takeoff_24,
            R.drawable.baseline_attractions_24,
            R.drawable.baseline_local_movies_24,
            R.drawable.baseline_hotel_24,
            R.drawable.baseline_directions_transit_24
        )

        for (i in categoryIcon.indices){
            val view = layoutInflater.inflate(R.layout.package_category, categoryLayout, false)
            val cardView: MaterialCardView = view.findViewById(R.id.cardView3)
            val itemImage: ImageView = view.findViewById(R.id.item_image)
            val itemName: TextView = view.findViewById(R.id.item_name)
            itemImage.setImageResource(categoryIcon[i])
            itemName.text = categoryList[i]
            categoryLayout.addView(view)

            // Set click listener for card view
            cardView.setOnClickListener {
                // When card is clicked, create an intent to navigate to PackageDetail activity
                val intent = Intent(this, ViewAllPackage::class.java)
                startActivity(intent)
            }
        }

        val middleIndex = (dbHelper.getAllPackageDetails().size / 2)
        for (packageDetail in dbHelper.getAllPackageDetails().subList(0, middleIndex)) {
            // Use packageDetail.id instead of loop index i
            val view = layoutInflater.inflate(R.layout.country_card, countryLayout, false)
            val cardView: MaterialCardView = view.findViewById(R.id.cardView1)
            val itemImage: ImageView = view.findViewById(R.id.item_recommended_image)
            val itemNames: TextView = view.findViewById(R.id.item_country)
            val getImage: ByteArray? = packageDetail.image

            if (getImage != null) {
                itemImage.setImageBitmap(BitmapFactory.decodeByteArray(getImage, 0, getImage.size))
            }
            itemNames.text = packageDetail.location
            countryLayout.addView(view)

            // Set click listener for card view
            cardView.setOnClickListener {
                // When card is clicked, create an intent to navigate to PackageDetail activity
                val intent = Intent(this, PackageDetail::class.java)
                // Pass the package ID as an extra to the intent
                intent.putExtra("package_id", packageDetail.id)
                startActivity(intent)
            }
        }

        for (packageDetail in dbHelper.getAllPackageDetails().subList(middleIndex, dbHelper.getAllPackageDetails().size)) {
            // Use packageDetail.id instead of loop index i
            val view = layoutInflater.inflate(R.layout.new_package_card, packageLayoutNew, false)
            val packageCard: MaterialCardView = view.findViewById(R.id.cardView2)
            val packageImage: ImageView = view.findViewById(R.id.new_image)
            val packagePrice: TextView = view.findViewById(R.id.product_price)
            val packageName: TextView = view.findViewById(R.id.product_name)
            val getImage: ByteArray? = packageDetail.image

            if (getImage != null) {
                packageImage.setImageBitmap(BitmapFactory.decodeByteArray(getImage, 0, getImage.size))
            }
            packagePrice.text = packageDetail.price
            packageName.text = packageDetail.title
            packageLayoutNew.addView(view)

            // Set click listener for card view
            packageCard.setOnClickListener {
                // When card is clicked, create an intent to navigate to PackageDetail activity
                val intent = Intent(this, PackageDetail::class.java)
                // Pass the package ID as an extra to the intent
                intent.putExtra("package_id", packageDetail.id)
                startActivity(intent)
            }
        }

    }
}
