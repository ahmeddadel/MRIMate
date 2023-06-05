package com.dolla.mrimate.pojo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.dolla.mrimate.util.getCurrentDateAsString
import kotlinx.serialization.Serializable

/**
 * @created 05/06/2023 - 2:03 PM
 * @project MRIMate
 * @author adell
 */
@Serializable
data class Scan(
    val classId: Int,
    val classType: String,
    val percentage: Float,
    val scanImage: ByteArray,
    val date: String = getCurrentDateAsString(),
    var report: String = ""
) {
    fun getScanImage(): Bitmap {
        return BitmapFactory.decodeByteArray(scanImage, 0, scanImage.size)
    }
}