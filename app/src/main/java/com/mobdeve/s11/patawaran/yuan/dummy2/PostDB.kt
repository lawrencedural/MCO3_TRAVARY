package com.mobdeve.s11.patawaran.yuan.dummy2

import android.graphics.Bitmap

data class PostDB(
    val caption: String,
    val location: String,
    val screenshotBitmap: Bitmap?, // Nullable Bitmap for the screenshot
    val photoBitmaps: List<Bitmap> // Bitmaps for the images
)
