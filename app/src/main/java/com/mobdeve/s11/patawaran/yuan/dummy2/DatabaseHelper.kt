// DatabaseHelper.kt
package com.mobdeve.s11.patawaran.yuan.dummy2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "your_database"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "posts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CAPTION = "caption"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_SCREENSHOT = "screenshot"
        private const val COLUMN_PHOTO_PREFIX = "photo" // Prefix for photo columns
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_CAPTION TEXT, $COLUMN_LOCATION TEXT, $COLUMN_SCREENSHOT BLOB, ${COLUMN_PHOTO_PREFIX}0 BLOB, ${COLUMN_PHOTO_PREFIX}1 BLOB, ${COLUMN_PHOTO_PREFIX}2 BLOB)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAllPosts(): List<PostDB> {
        val postsList = ArrayList<PostDB>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val captionIndex = cursor.getColumnIndex(COLUMN_CAPTION)
                val locationIndex = cursor.getColumnIndex(COLUMN_LOCATION)
                val screenshotIndex = cursor.getColumnIndex(COLUMN_SCREENSHOT)
                val photoIndices = (0..2).map { cursor.getColumnIndex("${COLUMN_PHOTO_PREFIX}$it") }

                // Check if all required columns exist
                if (captionIndex >= 0 && locationIndex >= 0 && screenshotIndex >= 0 && photoIndices.all { it >= 0 }) {
                    val caption = cursor.getString(captionIndex)
                    val location = cursor.getString(locationIndex)

                    // Check if screenshot is null
                    val screenshot = cursor.getBlob(screenshotIndex)
                    val screenshotBitmap = if (screenshot != null) {
                        BitmapFactory.decodeByteArray(screenshot, 0, screenshot.size)
                    } else {
                        null
                    }

                    // Decode photo bitmaps
                    val photoBitmaps = photoIndices.mapNotNull { index ->
                        val photoBlob = cursor.getBlob(index)
                        if (photoBlob != null) {
                            BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.size)
                        } else {
                            null
                        }
                    }

                    val post = PostDB(caption, location, screenshotBitmap, photoBitmaps)
                    postsList.add(post)
                } else {
                    // Handle missing columns gracefully
                    // For example, log an error or skip this row
                    Log.e("DatabaseHelper", "Missing columns in database table")
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return postsList
    }
}
