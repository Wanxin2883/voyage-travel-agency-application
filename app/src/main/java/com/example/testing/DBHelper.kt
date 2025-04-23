package com.example.testing

import PackageDetailData
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "VoyageDB"
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"

        private const val TABLE_PACKAGE_DETAILS = "package_details"
        private const val COLUMN_PID = "package_id"
        private const val COLUMN_PIMAGE = "package_image"
        private const val COLUMN_PTITLE = "package_title"
        private const val COLUMN_PSTARTDATE = "start_date"
        private const val COLUMN_PENDDATE = "end_date"
        private const val COLUMN_PLOCATION = "package_location"
        private const val COLUMN_PPRICE = "package_price"
        private const val COLUMN_PDESC = "package_desc"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable =
            "CREATE TABLE $TABLE_USERS " +
                    "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_PHONE TEXT, " +
                    "$COLUMN_EMAIL TEXT, " +
                    "$COLUMN_PASSWORD TEXT)"
        db.execSQL(createUserTable)

        val createPackageTable =
            "CREATE TABLE $TABLE_PACKAGE_DETAILS"+
                    "($COLUMN_PID INTEGER PRIMARY KEY, " +
                    "$COLUMN_PTITLE TEXT, " +
                    "$COLUMN_PIMAGE BLOB, " +
                    "$COLUMN_PSTARTDATE TEXT, " +
                    "$COLUMN_PENDDATE TEXT, " +
                    "$COLUMN_PLOCATION TEXT, " +
                    "$COLUMN_PPRICE TEXT, " +
                    "$COLUMN_PDESC TEXT)"
        db.execSQL(createPackageTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS ${TABLE_PACKAGE_DETAILS}")
        onCreate(db)
    }

    // User-related methods
    fun addUser(name: String, phone: String, email: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PHONE, phone)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    fun getUserInfo(): UserData {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        cursor.close()
        return UserData(name, phone, email)
    }

    fun updateUser(name: String, phone: String, email: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PHONE, phone)
            put(COLUMN_EMAIL, email)
        }
        val result = db.update(TABLE_USERS, contentValues, null, null)
        db.close()
        return result != -1
    }

    fun checkEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(email))
        val count = cursor?.count ?: 0
        cursor?.close()
        db.close()
        return count > 0
    }

    fun updatePassword(email: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_PASSWORD, newPassword)
        val result = db.update(TABLE_USERS, contentValues, "$COLUMN_EMAIL=?", arrayOf(email))
        return result != -1
    }

    fun validUserData(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query =
            "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor? =
            db.rawQuery(query, arrayOf(email, password))
        val count = cursor?.count ?: 0
        cursor?.close()
        db.close()
        return count > 0
    }

    fun addPackage(
        id: Int,
        title: String,
        image:ByteArray,
        startDate: String,
        endDate: String,
        location: String,
        price: String,
        desc: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply{
            put(COLUMN_PID, id)
            put(COLUMN_PTITLE, title)
            put(COLUMN_PIMAGE, image)
            put(COLUMN_PSTARTDATE, startDate)
            put(COLUMN_PENDDATE, endDate)
            put(COLUMN_PLOCATION, location)
            put(COLUMN_PPRICE, price)
            put(COLUMN_PDESC, desc)
        }
        val result = db.insert(TABLE_PACKAGE_DETAILS, null, contentValues)
        return result != -1L
    }

    fun updatePackage(
        packageId: Int,
        title: String,
        image: ByteArray,
        startDate: String,
        endDate: String,
        location: String,
        price: String,
        desc: String
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PTITLE, title)
            put(COLUMN_PIMAGE, image)
            put(COLUMN_PSTARTDATE, startDate)
            put(COLUMN_PENDDATE, endDate)
            put(COLUMN_PLOCATION, location)
            put(COLUMN_PPRICE, price)
            put(COLUMN_PDESC, desc)
        }
        val result = db.update(
            TABLE_PACKAGE_DETAILS,
            contentValues,
            "$COLUMN_PID=?",
            arrayOf(packageId.toString())
        )
        db.close()
        return result != -1
    }

    fun getPackageDetail(packageId: Int): PackageDetailData?{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PACKAGE_DETAILS WHERE $COLUMN_PID = ?"
        val cursor = db.rawQuery(query, arrayOf(packageId.toString()))
        var packageData: PackageDetailData? = null
        if (cursor.moveToFirst()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PTITLE))
            val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PIMAGE))
            val startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PSTARTDATE))
            val endDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PENDDATE))
            val location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLOCATION))
            val price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PPRICE))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PDESC))
            packageData = PackageDetailData(id, title, image, startDate, endDate, location, price, desc)
        }
        cursor.close()
        return packageData
    }

    fun getAllPackageDetails(): List<PackageDetailData> {
        val packageList = mutableListOf<PackageDetailData>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PACKAGE_DETAILS"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PTITLE))
            val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PIMAGE))
            val startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PSTARTDATE))
            val endDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PENDDATE))
            val location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLOCATION))
            val price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PPRICE))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PDESC))
            val packageData = PackageDetailData(id, title, image, startDate, endDate, location, price, desc)
            packageList.add(packageData)
        }

        cursor.close()
        return packageList
    }

    fun drawableToByteArray(context: Context, drawableId: Int): ByteArray {
        // Decode the drawable resource into a Bitmap
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)

        // Further reduce compression quality
        val format = Bitmap.CompressFormat.PNG // Use JPEG format
        val quality = 70 // Lower quality value

        // Convert the Bitmap into a ByteArray with further compression
        val stream = ByteArrayOutputStream()
        bitmap.compress(format, quality, stream)
        return stream.toByteArray()
    }

    fun deletePackage(packageId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_PACKAGE_DETAILS, "$COLUMN_PID=?", arrayOf(packageId.toString()))
        return result != -1
    }
}
