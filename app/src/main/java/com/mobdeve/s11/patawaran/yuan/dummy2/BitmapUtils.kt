package com.mobdeve.s11.patawaran.yuan.dummy2

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
    return outputStream.toByteArray()
}
